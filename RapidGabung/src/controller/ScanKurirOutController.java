package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import com.sun.prism.impl.Disposer.Record;

import VO.EntryDataShowVO;
import driver.KurirOutPrint;
import driver.KurirOutPrint2;
import entity.TrKurir;
import entity.TrUser;
import entity.TtStatusKurirOut;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import service.ConfigurationService;
import service.GenericService;
import service.KurirOutService;
import service.MasterKurirService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import utilfx.AutoCompleteComboBoxListener;

public class ScanKurirOutController implements Initializable {

	@FXML
	ComboBox cbNamaKurir;

	@FXML
	private TextField txtScanResi, txtIdScanKurir;
	
	@FXML
	private TextField txtJumlahHasilSacn;

	@FXML
	Button btnPrint;

	@FXML
	TableView tblKurir;

	private String idKurir;
	public TrUser uLogin = LoginController.getUserLogin();
	private List<EntryDataShowVO> listEntry = new ArrayList<EntryDataShowVO>();
	public String IdKomputer;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Properties prop = ConfigurationService.getHibernateProperties();
		String perw =(String) prop.get("app.current.perwakilan");
		String kdPerw =(String) prop.get("app.current.kodeperwakilan");
		IdKomputer = perw+kdPerw;
		
		
		btnPrint.setDisable(true);
		ManagedFormHelper.instanceController = this;

		MasterKurirService referensiKurir = new MasterKurirService();
		ObservableList<TrKurir> listNamaKurir = FXCollections.observableArrayList(referensiKurir.getDataKurir());
		for (TrKurir i : listNamaKurir) {
			cbNamaKurir.getItems().add(
//					i.getNik() + " - " + i.getNama() + " ( " + i.getKodePerwakilan() + " - " + i.getIdJabatan() + " ) ");
					i.getNama());
		}

		new AutoCompleteComboBoxListener<TrKurir>(cbNamaKurir);

		txtScanResi.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String data = KurirOutService.cekAwbForKurir(txtScanResi.getText());
				if (txtIdScanKurir.getText().isEmpty()) {
					MessageBox.alert("Kurir belum di pilih");
				} else {
					if (data != null) {
						TtStatusKurirOut ts = new TtStatusKurirOut();
						String id = KurirOutService.getMaxIdKurirOut(IdKomputer);
						ts.setId(IdKomputer+id);
						ts.setNoStrukKurirOut(txtIdScanKurir.getText());
						ts.setIdKurir(idKurir);
						ts.setStatus("ONPROCESS");
						ts.setIdBarang(txtScanResi.getText());
						ts.setTanggal(DateUtil.getNow());
						ts.setTglCreate(DateUtil.getNow());
						ts.setFlag(0);
						GenericService.save(TtStatusKurirOut.class, ts, true);
						Boolean update = KurirOutService.updateStaturKurir(idKurir, txtScanResi.getText());
						sohwTableA();
						txtScanResi.setText("");
					} else {
						MessageBox.alert("Resi Belum di Entry / Sudah di ambil kurir lain");
						txtScanResi.setText("");
					}
				}
			}
		});

		cbNamaKurir.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
//				idKurir = t1.substring(0, 6);
				System.out.println("--> t1 : " + t1);
				TrKurir kur = MasterKurirService.getKurirByNama(t1);
				if(kur!=null){
					idKurir = kur.getNik();
					String kodeAuto = KurirOutService.getMaxCodeKurir(idKurir.trim());
					String formatD = DateUtil.getDateNotSeparator(DateUtil.getNow());
					txtIdScanKurir.setText(idKurir.toUpperCase() + "-" + formatD + "-" + kodeAuto);
					KurirOutService.deleteStaturKurirAll(listEntry, idKurir, txtIdScanKurir.getText());
					listEntry.clear();
					tblKurir.getColumns().clear();
				}
			}
		});

	}

	private void sohwTableA() {

		tblKurir.getColumns().clear();
		listEntry.clear();
		Long id = null;
		listEntry = KurirOutService.getListAfterScan(txtIdScanKurir.getText());
		txtJumlahHasilSacn.setText(listEntry.size()+"");
		ObservableList<EntryDataShowVO> olDokumen = FXCollections.observableArrayList(listEntry);
		TableColumn col = new TableColumn("No");
		col.setPrefWidth(38.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new ReadOnlyObjectWrapper(tblKurir.getItems().indexOf(param.getValue()) + 1);
					}
				});
		tblKurir.getColumns().addAll(col);

		col = new TableColumn("No Resi");
		col.setPrefWidth(264.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new SimpleStringProperty(param.getValue().getAwbData());
					}
				});
		tblKurir.getColumns().addAll(col);

		col = new TableColumn("Berat");
		col.setPrefWidth(118.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new SimpleStringProperty(param.getValue().getbFinal());
					}
				});
		tblKurir.getColumns().addAll(col);

		col = new TableColumn("Penerima");
		col.setPrefWidth(228.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new SimpleStringProperty(param.getValue().getPenerima());
					}
				});
		tblKurir.getColumns().addAll(col);

		col = new TableColumn("Action");
		col.setPrefWidth(84.0);
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
				return new ButtonCell(tblKurir);
			}

		});
		tblKurir.getColumns().add(col);

		tblKurir.setItems(olDokumen);
		btnPrint.setDisable(false);
	}

	private class ButtonCell extends TableCell<Record, Boolean> {

		final Button cellButton = new Button("Delete");

		ButtonCell(final TableView tblView) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					int selectdIndex = getTableRow().getIndex();
					EntryDataShowVO a = (EntryDataShowVO) tblView.getItems().get(selectdIndex);
					KurirOutService.deleteStatusKurir(a.getAwbData(), txtIdScanKurir.getText());
					sohwTableA();
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

	@FXML
	public void onClickPrint(Event evt) {
		KurirOutPrint print = new KurirOutPrint();
		print.printConfig(txtIdScanKurir.getText(), uLogin.getNamaUser());
		KurirOutPrint2 print2 = new KurirOutPrint2();
		for (EntryDataShowVO en : listEntry) {
			print2.printConfig(en.getAwbData(), en.getBpFinal(), en.getbFinal());
		}
		tblKurir.getColumns().clear();
		listEntry.clear();
		txtScanResi.setText(""); 
		txtIdScanKurir.setText("");
		btnPrint.setDisable(true);
		cbNamaKurir.setValue(null);
	}

	@DtoListener(idDtoListener = "backTop")
	public void backDtoListener() {
		try {
			KurirOutService.deleteStaturKurirAll(listEntry, idKurir, txtIdScanKurir.getText());
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
