package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import controller.MasterPelangganController.PelangganTV;
import entity.TrCabang;
import entity.TrPelanggan;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import service.GenericService;
import service.MasterCabangService;
import service.PelangganService;
import util.DtoListener;
import util.EmailUtil;
import util.ManagedFormHelper;
import util.WindowsHelper;

public class NewsLetterController implements Initializable {

	// general
	private Task emailWorker;
	private String pathFile = "";

	@FXML
	private TextField txtJudul;
	@FXML
	private Text txtFileName;
	@FXML
	private HTMLEditor txtBody;
	@FXML
	private Button btnBrowseGambar;

	// popup
	@FXML
	TableView<PelangganTV> tvTagihanPelanggan;
	private CheckBox chkAll = new CheckBox();
	private ComboBox cmbCabang = new ComboBox();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;	
	}

	public void onClikKirimMain() {
		showDataPopUp();
	}

	public void onBrowseGambar() {
		FileChooser fileChooser = new FileChooser();
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
          
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        txtFileName.setText(file.getName());
        pathFile = file.getPath();
	}

	private void showDataPopUp() {
		try {
			Stage stgPopUp = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			AnchorPane anchorPopUpLayanan = (AnchorPane) loader
					.load(this.getClass().getResource("PopUpNewsLetter.fxml"));
			Scene scnPhotoTerakhir = new Scene(anchorPopUpLayanan);
			stgPopUp.setScene(scnPhotoTerakhir);
			stgPopUp.setTitle("News Letter");
			stgPopUp.initModality(Modality.WINDOW_MODAL);
			stgPopUp.initOwner(WindowsHelper.primaryStage);
			stgPopUp.initStyle(StageStyle.UTILITY);
			stgPopUp.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent window) {
					chkAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
						public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
							List<TrPelanggan> tr = PelangganService.getDataPelangganForNewsLetter(
									(String) cmbCabang.getSelectionModel().getSelectedItem().toString());
							tvTagihanPelanggan = generateTableViewTagihanPelanggan(tvTagihanPelanggan, tr);
						}
					});

					Stage innerStage = (Stage) window.getSource();
					tvTagihanPelanggan = (TableView<PelangganTV>) innerStage.getScene().lookup("#tvTagihanPelanggan");
					cmbCabang = (ComboBox) innerStage.getScene().lookup("#cmbCabang");
					ProgressBar emailProgress = (ProgressBar) innerStage.getScene().lookup("#emailProgress");
					Label lblEmailProgress = (Label) innerStage.getScene().lookup("#lblEmailProgress");

					cmbCabang.getItems().add("All Cabang");
					List<TrCabang> lstCabang = MasterCabangService.getAllPerwakilanCabangDistinct();
					for (TrCabang trCabang : lstCabang) {
						cmbCabang.getItems().add(trCabang.getKodePerwakilan());
					}
					cmbCabang.setValue("All Cabang");

					List<TrPelanggan> tr = PelangganService.getDataPelangganForNewsLetter(
							(String) cmbCabang.getSelectionModel().getSelectedItem().toString());
					tvTagihanPelanggan = generateTableViewTagihanPelanggan(tvTagihanPelanggan, tr);

					lblEmailProgress.setText(tr.size() + " newsletter siap kirim");
					Button btnCari = (Button) innerStage.getScene().lookup("#btnCari");
					btnCari.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							List<TrPelanggan> tr = PelangganService.getDataPelangganForNewsLetter(
									(String) cmbCabang.getSelectionModel().getSelectedItem().toString());
							tvTagihanPelanggan = generateTableViewTagihanPelanggan(tvTagihanPelanggan, tr);
							lblEmailProgress.setText("total ada " + tr.size() + " newsletter");
						}
					});

					Button btnClear = (Button) innerStage.getScene().lookup("#btnClear");
					btnClear.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							tvTagihanPelanggan.getItems().clear();
						}
					});

					Button btnKeluar = (Button) innerStage.getScene().lookup("#btnKeluar");
					btnKeluar.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							stgPopUp.close();
						}
					});

					Button btnSentEmail = (Button) innerStage.getScene().lookup("#btnSentEmail");
					btnSentEmail.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							final List<PelangganTV> trv = tvTagihanPelanggan.getItems();
							final Integer sizeCheckOnly = getNumberOfCheck(trv);
							Map<String, Object> config = GenericService.getConfig();
							emailProgress.setProgress(0);
							emailWorker = new Task() {

								@Override
								protected Object call() throws Exception {
									Integer intProgresVal = 0;
									for (PelangganTV trPelanggan : trv) {
										if (trPelanggan.getCheck()) {
											intProgresVal++;
											final Integer intVal = intProgresVal;
											Thread.sleep(100);
											EmailUtil email = new EmailUtil();
											String toUser = trPelanggan.getEmail();
											//String toUser = "imamfarisi@gmail.com";
											email.sendNewsLetter(txtBody.getHtmlText(), txtJudul.getText().toString(),
													toUser, pathFile);

											Platform.runLater(new Runnable() {
												@Override
												public void run() {
													lblEmailProgress.setText("(" + intVal + "/" + sizeCheckOnly
															+ "), sent to : " + toUser + "");
												}
											});
											updateMessage("2000 milliseconds");
											updateProgress(intProgresVal, sizeCheckOnly);
										}
									}

									return true;
								}
							};

							emailProgress.progressProperty().unbind();
							emailProgress.progressProperty().bind(emailWorker.progressProperty());

							emailWorker.messageProperty().addListener(new ChangeListener<String>() {
								@Override
								public void changed(ObservableValue<? extends String> observable, String oldValue,
										String newValue) {

								}
							});

							new Thread(emailWorker).start();
						}
					});
				}
			});
			stgPopUp.show();
		} catch (Exception e) {
		}
	}

	private Integer getNumberOfCheck(List<PelangganTV> trv) {
		Integer num = 0;
		for (PelangganTV pelangganTV : trv) {
			if (pelangganTV.getCheck()) {
				num++;
			}
		}
		return num;
	}

	private TableView<PelangganTV> generateTableViewTagihanPelanggan(TableView<PelangganTV> tv, List<TrPelanggan> tr) {
		tv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<PelangganTV> pops = FXCollections.observableArrayList();
		for (Integer ind = 0; ind < tr.size(); ind++) {
			PelangganTV row = new PelangganTV(chkAll.isSelected(), tr.get(ind).getKodePelanggan(),
					tr.get(ind).getNamaAkun(), tr.get(ind).getEmail());
			pops.add(row);
		}

		tv.getColumns().clear();
		tv.getItems().clear();

		tv.setItems(pops);

		TableColumn<PelangganTV, Boolean> checkCol = new TableColumn<PelangganTV, Boolean>("Kirim?");
		TableColumn<PelangganTV, String> kodeCol = new TableColumn<PelangganTV, String>("Kode Pelanggan");
		TableColumn<PelangganTV, String> namaCol = new TableColumn<PelangganTV, String>("Nama Pelanggan");
		TableColumn<PelangganTV, String> emailCol = new TableColumn<PelangganTV, String>("Email");

		tv.getColumns().addAll(checkCol, kodeCol, namaCol, emailCol);

		checkCol.setCellValueFactory(new PropertyValueFactory<PelangganTV, Boolean>("check"));
		checkCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkCol));
		checkCol.setGraphic(chkAll);
		checkCol.setEditable(true);

		kodeCol.setCellValueFactory(new PropertyValueFactory<PelangganTV, String>("kode"));
		namaCol.setCellValueFactory(new PropertyValueFactory<PelangganTV, String>("nama"));
		emailCol.setCellValueFactory(new PropertyValueFactory<PelangganTV, String>("email"));

		tv.setEditable(true);

		return tv;
	}

	private void setKosong() {
		txtJudul.setText("");
		txtFileName.setText("");
		// txtBody.set("");
	}

	@DtoListener(idDtoListener = "backTop")
	public void backDtoListener() {
		try {
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("/controller/Menu.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			menuPage.prefWidthProperty().bind(WindowsHelper.primaryStage.widthProperty());
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
