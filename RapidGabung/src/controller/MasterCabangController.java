package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import com.sun.prism.impl.Disposer.Record;

import javafx.scene.control.TextField;
import entity.TrCabang;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import service.GenericService;
import service.MasterCabangService;
import util.DateUtil;
import util.DtoBroadcaster;
import util.DtoListener;
import util.HibernateUtil;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;

public class MasterCabangController implements Initializable {

	@FXML
	TextField txtKdCabang, txtKdPropinsi, txtKdPerwakilan, txtEmail, txtNamaCabang;

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

	public void settingListboksMasterCabang() {
		listboxMasterCabang.getColumns().clear();
		MasterCabangService serviceDokumen = new MasterCabangService();

		Long id = null;

		ObservableList<TrCabang> olDokumen = FXCollections.observableArrayList(MasterCabangService.getDataCabang_());
		TableColumn col = new TableColumn("Kode Cabang");
		col.setPrefWidth(120.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrCabang, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrCabang, String> param) {
						// ReferensiDokumenService rDS = new
						// ReferensiDokumenService();
						return new SimpleStringProperty(param.getValue().getKodeCabang());
					}
				});
		listboxMasterCabang.getColumns().addAll(col);

		col = new TableColumn("Kode Propinsi");
		col.setPrefWidth(120.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrCabang, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrCabang, String> param) {
						// return null;
						return new SimpleStringProperty(param.getValue().getKodePropinsi());
					}
				});
		listboxMasterCabang.getColumns().addAll(col);
		//
		col = new TableColumn("Kode Perwakilan");
		col.setPrefWidth(150.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrCabang, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrCabang, String> param) {
						// return null;
						return new SimpleStringProperty(param.getValue().getKodePerwakilan());
					}
				});
		listboxMasterCabang.getColumns().addAll(col);

		col = new TableColumn("Email");
		col.setPrefWidth(200.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrCabang, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrCabang, String> param) {
						// return null;
						return new SimpleStringProperty(param.getValue().getEmail());
					}
				});
		listboxMasterCabang.getColumns().addAll(col);

		col = new TableColumn("Nama Cabang");
		col.setPrefWidth(150.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrCabang, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrCabang, String> param) {
						// return null;
						return new SimpleStringProperty(param.getValue().getNamaCabang());
					}
				});
		listboxMasterCabang.getColumns().addAll(col);

		col = new TableColumn("Action");
		col.setPrefWidth(160.0);
		col.setStyle("-fx-alignment: CENTER;");
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});

		col.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

			@Override
			public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
				// ini button nya(dia kan panggil function clik)
				return new ButtonCell(listboxMasterCabang);
			}

		});
		listboxMasterCabang.getColumns().add(col);

		listboxMasterCabang.setItems(olDokumen);
	}

	@FXML
	public void onMouseClicked(MouseEvent evt) {
		if (evt.getClickCount() > 1) {
			TrCabang dataHeader = (TrCabang) listboxMasterCabang.getSelectionModel().getSelectedItem();
			System.out.println("---------------Data Header : " + dataHeader.getKodeCabang());
			btnSimpan.setText("UPDATE");
			if (dataHeader != null) {
				DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "loadHeader", dataHeader);
				Stage stage = (Stage) listboxMasterCabang.getScene().getWindow();
				// btnSimpan.setDisable(false);
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
			MasterCabangService.updateDataCabang(txtKdCabang.getText(), txtKdPropinsi.getText(),
					txtKdPerwakilan.getText(), txtEmail.getText(), txtNamaCabang.getText());
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

	private class ButtonCell extends TableCell<Record, Boolean> {

		final Button cellButton = new Button("Delete");

		ButtonCell(final TableView tblView) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					int selectdIndex = getTableRow().getIndex();
					TrCabang a = (TrCabang) tblView.getItems().get(selectdIndex);
					int[] dataButtonMessageBox = new int[2];
					dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
					dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
					int hasilMessageBox = MessageBox.confirm("Apakah Yakin akan Melakukan Delete?",
							dataButtonMessageBox);
					if (hasilMessageBox == 5) { // cancel

					} else if (hasilMessageBox == 6) {
						MasterCabangService.showTableSetelahDelete(a.getKodeCabang());
						settingListboksMasterCabang();
						
					}

				}
			});
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {

				setGraphic(cellButton);
			}
		}
	}
}
