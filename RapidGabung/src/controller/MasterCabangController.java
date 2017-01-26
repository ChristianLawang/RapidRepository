package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import com.sun.prism.impl.Disposer.Record;

import VO.BrowseSemuaDataVO;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import entity.TrCabang;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import service.BrowseSemuaDataService;
import service.GenericService;
import service.MasterCabangService;
import util.DateUtil;
import util.DtoBroadcaster;
import util.DtoListener;
import util.HibernateUtil;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import util.formatRupiah;

public class MasterCabangController implements Initializable {

	@FXML
	TextField txtCari, txtKdCabang, txtKdPropinsi, txtKdPerwakilan, txtEmail, txtNamaCabang;

	@FXML
	AnchorPane anchorPane;

	@FXML
	ScrollPane scrollPane;
	int imageXsp = 0;
	int imageYsp = 0;

	@FXML
	Button btnSimpan, btnClear, btnRotate, btnReset, btnKirim;

	@FXML
	TableView listboxMasterCabang;
	
	@FXML
	private ObservableList<MasterCabangTV> masterData = FXCollections.observableArrayList();

	@FXML
	private TableColumn<MasterCabangTV, String>	colKodeCabang, colKodePropinsi,	colKodePerwakilan, colEmail, colNamaCabang;
	
	@FXML
	private TableColumn colAction;
	public static List<TrCabang> getDataCabangByID(String id) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrCabang.class);
		c.add(Restrictions.eq("kodeCabang", id));

		List<TrCabang> list = c.list();

		s.close();

		return list;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		ManagedFormHelper.instanceController = this;
		listboxMasterCabang.setEditable(true);
		settingListboksMasterCabang();
	}

	@FXML
	public void onClickKirim() {
		// TODO LIST
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/popup/PopUpKirimDataManifest.fxml"));
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Kirim Data Manifest");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(WindowsHelper.primaryStage);
			dialogStage.initStyle(StageStyle.UTILITY);
			dialogStage.setResizable(false);
			Parent root = (Parent) fxmlLoader.load();
			Scene scene = new Scene(root);
			dialogStage.setScene(scene);
			dialogStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class MasterCabangTV{
		public final StringProperty kodeCabang;
		public final StringProperty kodePerwakilan;
		public final StringProperty email;
		public final StringProperty namaCabang;
		public final StringProperty kodePropinsi;
		
		public MasterCabangTV(String kodeCabang, String kodePerwakilan, String email, String namaCabang, String kodePropinsi){
			this.kodeCabang = new SimpleStringProperty(kodeCabang);
			this.kodePerwakilan = new SimpleStringProperty(kodePerwakilan);
			this.email = new SimpleStringProperty(email);
			this.namaCabang = new SimpleStringProperty(namaCabang);
			this.kodePropinsi = new SimpleStringProperty(kodePropinsi);
		}
		
		public String getKodeCabang(){return this.kodeCabang.get();}
		public String getKodePerwakilan(){return this.kodePerwakilan.get();}
		public String getEmail(){return this.kodeCabang.get();}
		public String getNamaCabang(){return this.kodeCabang.get();}
		public String getKodePropinsi(){return this.kodePropinsi.get();}
		
		public void setKodeCabang(String kodeCabang){this.kodeCabang.set(kodeCabang);}
		public void setKodePerwakilan(String kodePerwakilan){this.kodePerwakilan.set(kodePerwakilan);}
		public void setEmail(String email){this.email.set(email);}
		public void setNamaCabang(String namaCabang){this.namaCabang.set(namaCabang);}
		public void setKodePropinsi(String kodePropinsi){this.kodePropinsi.set(kodePropinsi);}
		
		
		public StringProperty kodeCabangProperty(){return kodeCabang;}
		public StringProperty kodePerwakilanProperty(){return kodePerwakilan;}
		public StringProperty emailProperty(){return email;}
		public StringProperty namaCabangProperty(){return namaCabang;}
		public StringProperty kodePropinsiProperty(){return kodePropinsi;}
	}
	
	public void settingListboksMasterCabang() {
		List<TrCabang> tt = FXCollections.observableArrayList(MasterCabangService.getDataCabang_());
		for (TrCabang trCabang : tt) {
			masterData.add(new MasterCabangTV(
					trCabang.getKodeCabang(),
					trCabang.getKodePerwakilan(),
					trCabang.getEmail(),
					trCabang.getNamaCabang(),
					trCabang.getKodePropinsi()
					)
				);
		}
		
		
		colKodeCabang.setCellValueFactory(cellData -> cellData.getValue().kodeCabangProperty());
		colKodePerwakilan.setCellValueFactory(cellData -> cellData.getValue().kodePerwakilanProperty());
		colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
		colNamaCabang.setCellValueFactory(cellData -> cellData.getValue().namaCabangProperty());
		
		addButton();
		FilteredList<MasterCabangTV> filteredData = new FilteredList<>(masterData, p -> true);

		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (data.getKodeCabang().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton();
					return true;
				} else if (data.getKodePerwakilan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton();
					return true;
				} else if (data.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton();
					return true;
				} else if (data.getNamaCabang().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton();
					return true;
				}
				return false;
			});
		});
		
		SortedList<MasterCabangTV> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(listboxMasterCabang.comparatorProperty());
		listboxMasterCabang.setItems(sortedData);

	}
	
	public void addButton() {
		
		colAction.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});

		colAction.setCellFactory(
				new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

					@Override
					public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
						return new ButtonCell(listboxMasterCabang);
					}
				});
	}

	@SuppressWarnings("restriction")
	private class ButtonCell extends TableCell<Record, Boolean> {

		TableView<MasterCabangTV> tab;

		ButtonCell(final TableView<MasterCabangTV> tblView) {
			this.tab = tblView;
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				int selectdIndex = getTableRow().getIndex();
				MasterCabangTV boVo = (MasterCabangTV) tab.getItems().get(selectdIndex);
				final HBox hbox = new HBox(5);
	
				Button buttonDelete = new Button("Delete");
				buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						int selectdIndex = getTableRow().getIndex();
						MasterCabangTV a = (MasterCabangTV) tab.getItems().get(selectdIndex);
						int[] dataButtonMessageBox = new int[2];
						dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
						dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
						int hasilMessageBox = MessageBox.confirm("Apakah Yakin akan Melakukan Delete?",
								dataButtonMessageBox);
						if (hasilMessageBox == 5) { // cancel

						} else if (hasilMessageBox == 6) {
							MasterCabangService.showTableSetelahDelete(a.getKodeCabang());
							masterData.clear();
							settingListboksMasterCabang();
						}
					}
				});
				
				hbox.getChildren().add(buttonDelete);
				setGraphic(hbox);
			}
		}
	}

	@FXML
	public void onMouseClicked(MouseEvent evt) {
		if (evt.getClickCount() > 1) {
			MasterCabangTV dataHeader = (MasterCabangTV) listboxMasterCabang.getSelectionModel().getSelectedItem();
			System.out.println("---------------Data Header : " + dataHeader.getKodeCabang());
			btnSimpan.setText("UPDATE");
			if (dataHeader != null) {
//				DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "loadHeader", dataHeader);
//				Stage stage = (Stage) listboxMasterCabang.getScene().getWindow();
//				// btnSimpan.setDisable(false);
				txtKdCabang.setText(dataHeader.getKodeCabang());
				txtKdPropinsi.setText(dataHeader.getKodePropinsi());
				txtKdPerwakilan.setText(dataHeader.getKodePerwakilan());
				txtEmail.setText(dataHeader.getEmail());
				txtNamaCabang.setText(dataHeader.getNamaCabang());

			}
		}
	}

	@DtoListener(idDtoListener = "loadHeader") // Dari search
	public void loadDtoListener(TrCabang userHeader) {
		txtKdCabang.setText(userHeader.getKodeCabang());
		txtKdPropinsi.setText(userHeader.getKodePropinsi());
		txtKdPerwakilan.setText(userHeader.getKodePerwakilan());
		txtEmail.setText(userHeader.getEmail());
		txtNamaCabang.setText(userHeader.getNamaCabang());

	}

	public void onSave(Event evt) {
		List<TrCabang> test = MasterCabangService.getDataCabangByID(txtKdCabang.getText());
		if (test.size() > 0) {
			// UPDATE TABLE
			MasterCabangService.updateDataCabang(
					txtKdCabang.getText(), 
					txtKdPropinsi.getText(),
					txtKdPerwakilan.getText(), 
					txtEmail.getText(), 
					txtNamaCabang.getText());
		} else {
			TrCabang trcab = new TrCabang();
			trcab.setKodeCabang(txtKdCabang.getText());
			trcab.setKodePropinsi(txtKdPropinsi.getText());
			trcab.setKodePerwakilan(txtKdPerwakilan.getText());
			trcab.setEmail(txtEmail.getText());
			trcab.setNamaCabang(txtNamaCabang.getText());
			
			trcab.setTglCreate(DateUtil.getNow());
			trcab.setFlag(0);
//			trcab.setTglUpdate(cal.getTime());
			
			GenericService.save(TrCabang.class, trcab, true);
		}
		masterData.clear();
		settingListboksMasterCabang();
		clearForm();
		btnSimpan.setText("SIMPAN");

	}

	@FXML
	public void onCancel(Event evt) {
		clearForm();
	}

	public void clearForm()

	{
		txtKdCabang.clear();
		txtKdPropinsi.clear();
		txtKdPerwakilan.clear();
		txtEmail.clear();
		txtNamaCabang.clear();
		btnSimpan.setText("SIMPAN");

	}

	@DtoListener(idDtoListener = "backTop")
	public void backDtoListener() {
		try {
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("/controller/Menu.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			menuPage.prefWidthProperty().bind(WindowsHelper.primaryStage.widthProperty()); // FullScreen,
																							// hilangin
																							// klo
																							// g
																							// mau
																							// fullscreen
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
