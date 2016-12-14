package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import com.sun.prism.impl.Disposer.Record;

import VO.KurirInVO;
import driver.KurirInPrint;
import entity.TrKurir;
import entity.TrMasalah;
import entity.TrUser;
import entity.TtStatusKurirIn;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import service.ConfigurationService;
import service.KurirInService;
import service.MasterKurirService;
import service.MasterMasalahService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import util.formatInteger;
import utilfx.AutoCompleteComboBoxListener;

public class ScanKurirInController implements Initializable {

	@FXML
	ComboBox cbNamaMasalah, cbNamaKurir;

	@FXML
	TableView listResiKurir, listHasilScan;

	@FXML
	Button btnOK, btnPrint;

	@FXML
	RadioButton rdbtnHariIni, rdbtnKemarin, rdbtnKemarinLusa;

	@FXML
	TextField txtNamaPenerima, txtAWB, txtJumlahResiKurir, txtJamKurirIn, txtMenitKurirIn, txtJumlahHasilSacn;

	@FXML
	DatePicker txtTglLapor;
	private static String idKurir;
	private Boolean flagMasalah = true;
	private String cmbMasalahValidasi = null;
	private String noAutoKurirIn;
	private ObservableList<KurirInVO> olDokumen = FXCollections.observableArrayList();
	public TrUser uLogin = LoginController.getUserLogin();
	
	public String IdKomputer;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		Properties prop = ConfigurationService.getHibernateProperties();
		String perw =(String) prop.get("app.current.perwakilan");
		String kdPerw =(String) prop.get("app.current.kodeperwakilan");
		IdKomputer = perw+kdPerw;
		
		
		StringConverter<Integer> minSecConverter = new formatInteger.IntRangeStringConverter(0, 59);
		txtJamKurirIn.setTextFormatter(new TextFormatter<>(new formatInteger.IntRangeStringConverter(0, 23), 0));
		txtMenitKurirIn.setTextFormatter(new TextFormatter<>(minSecConverter, 0));
		formatInteger.prepareTextField(txtJamKurirIn);
		formatInteger.prepareTextField(txtMenitKurirIn);
		
		// Set Objek kelas ini
		btnPrint.setDisable(true);
		ManagedFormHelper.instanceController = this;
		LocalDate localDate = txtTglLapor.getValue();

		// set radio button
		ToggleGroup myToggleGroup = new ToggleGroup();
		rdbtnHariIni.setToggleGroup(myToggleGroup);
		rdbtnHariIni.setUserData("Tgl Hari Ini");

		rdbtnKemarin.setToggleGroup(myToggleGroup);
		rdbtnKemarin.setUserData("Tgl Kemarin");
		rdbtnKemarinLusa.setToggleGroup(myToggleGroup);
		rdbtnKemarinLusa.setUserData("Tgl Kemarin Lusa");

		// untuk menampilkan tanggalan berdasarkan radio button yang dipilih
		myToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

				if (myToggleGroup.getSelectedToggle() != null) {
					System.out.println(myToggleGroup.getSelectedToggle().getUserData().toString());
					if (rdbtnHariIni.isSelected()) {
						txtTglLapor.setValue(localDate.now());
					} else if (rdbtnKemarin.isSelected()) {
						txtTglLapor.setValue(localDate.now().minusDays(1));
					} else {
						txtTglLapor.setValue(localDate.now().minusDays(2));
					}
				}
			}
		});

		MasterMasalahService referensiMasalah = new MasterMasalahService();
		ObservableList<TrMasalah> listNamaMasalah = FXCollections
				.observableArrayList(referensiMasalah.getDataMasalah());
		for (TrMasalah i : listNamaMasalah) {
			cbNamaMasalah.getItems().add(i.getIdMasalah() + " - " + i.getNamaMasalah());
		}

		MasterKurirService referensiKurir = new MasterKurirService();
		ObservableList<TrKurir> listNamaKurir = FXCollections.observableArrayList(referensiKurir.getDataKurir());
		for (TrKurir i : listNamaKurir) {
			cbNamaKurir.getItems().add(
//					i.getNik() + " - " + i.getNama() + " ( " + i.getKodePerwakilan() + " - " + i.getIdJabatan() + " ) ");
					i.getNama());
		}

		new AutoCompleteComboBoxListener<TrKurir>(cbNamaKurir);

		cbNamaKurir.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
//				idKurir = t1.substring(0, 6);
				TrKurir kur = MasterKurirService.getKurirByNama(t1);
				if(kur!=null){
					idKurir = kur.getNik();
					settingListboksResiDibawaKurir();
					noAutoKurirIn = NoAutoKurirIn();
				}
			}
		});

		cbNamaMasalah.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				cmbMasalahValidasi = "ada";
				if (!t1.equals("-1 - Diterima")) {
					txtNamaPenerima.setDisable(true);
					flagMasalah = false;
				} else {
					txtNamaPenerima.setDisable(false);
					flagMasalah = true;
				}
				txtJamKurirIn.requestFocus();
			}
		});

		settingListboksResiDibawaKurir();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				rdbtnHariIni.requestFocus();
				rdbtnHariIni.setSelected(true);
			}
		});

		txtAWB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (isValidFormEntry()) {
					List<KurirInVO> data = KurirInService.getListAfterScan(txtAWB.getText(), idKurir, "ONPROCESS");
					if (!data.isEmpty()) {
						TtStatusKurirIn ts = new TtStatusKurirIn();
						if (!cbNamaMasalah.getValue().toString().equals("-1 - Diterima")) {
							ts.setPenerima(cbNamaMasalah.getValue().toString());
						} else {
							ts.setPenerima(txtNamaPenerima.getText());
						}
						LocalDate localDate = txtTglLapor.getValue();
						Date u = DateUtil.convertToDatabaseColumn(localDate);
						
						String id = KurirInService.getMaxIdKurirIn(IdKomputer);
						ts.setId(IdKomputer+id);
						ts.setNoStrukKurirIn(noAutoKurirIn);
						ts.setIdKurir(idKurir);
						ts.setStatus("SEND");
						ts.setMasalah(cbNamaMasalah.getValue().toString());
						ts.setWaktuJam(txtJamKurirIn.getText());
						ts.setWaktuMenit(txtMenitKurirIn.getText());
						ts.setIdBarang(txtAWB.getText());
						ts.setTanggal(u);
						ts.setTglCreate(DateUtil.getNow());
						ts.setFlag(0);
						KurirInService.updateStatusResiKurir(ts, flagMasalah);

						settingListboksResiDibawaKurir();
						settingLisboxHasilScan(txtAWB.getText(), txtNamaPenerima.getText(),
								cbNamaMasalah.getValue().toString());
						txtNamaPenerima.requestFocus();
						
					} else {
						MessageBox.alert("Resi yang dimasukan salah");
						txtAWB.setText("");
					}
				}
			}
		});
		// chris nambah
		txtNamaPenerima = this.initTextFieldNamaPenerima(txtNamaPenerima);
		// chris nambah
		txtJamKurirIn = this.initTextFieldJamKurirIn(txtJamKurirIn);
		// chris nambah
		txtMenitKurirIn = this.initTextFieldMenitKurirIn(txtMenitKurirIn);
	}
	
	// chris nambah
	private TextField initTextFieldMenitKurirIn(TextField txtField) {
		txtField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 1) { // pindah ke menit
				txtAWB.requestFocus();
			}
		});
		return txtField;
	}

	// chris nambah
	private TextField initTextFieldJamKurirIn(TextField txtField) {
		txtField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 1) { // pindah ke menit
				System.out.println("pindah ke menit");
				txtMenitKurirIn.requestFocus();
			}
		});
		return txtField;
	}

	// chris nambah
	private TextField initTextFieldNamaPenerima(TextField txtField) {
		txtField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 0) { // bila ada isi
				cbNamaMasalah.getSelectionModel().select(0);
				cbNamaMasalah.setDisable(true);
			} else {
				cbNamaMasalah.setDisable(false);
			}
		});

		return txtField;
	}

	@FXML
	public void onClickPrint() {
		KurirInPrint print = new KurirInPrint();
		print.printConfig(noAutoKurirIn, uLogin.getNamaUser());
		btnPrint.setDisable(true); 
		clear();
		listHasilScan.getColumns().clear();
		listResiKurir.getColumns().clear();
		olDokumen.clear();
		noAutoKurirIn = NoAutoKurirIn();
		txtJumlahHasilSacn.setText("0");
		txtJumlahResiKurir.setText("0");
		cbNamaKurir.setValue(null);
	}

	public void settingLisboxHasilScan(String awb, String penerima, String masalah) {
		listHasilScan.getColumns().clear();
		if (masalah.equals("-1 - Diterima")) {
			olDokumen.add(new KurirInVO(awb, penerima));
		} else {
			olDokumen.add(new KurirInVO(awb, masalah));
		}
		TableColumn col = new TableColumn("No");
		col.setPrefWidth(50.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KurirInVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<KurirInVO, String> param) {
						return new ReadOnlyObjectWrapper(listHasilScan.getItems().indexOf(param.getValue()) + 1);
					}
				});
		listHasilScan.getColumns().addAll(col);

		col = new TableColumn("No Resi");
		col.setPrefWidth(230.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KurirInVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<KurirInVO, String> param) {
						return new SimpleStringProperty(param.getValue().getAwbData());
					}
				});
		listHasilScan.getColumns().addAll(col);

		col = new TableColumn("Penerima");
		col.setPrefWidth(240.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KurirInVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<KurirInVO, String> param) {
						// return null;
						return new SimpleStringProperty(param.getValue().getPenerima());
					}
				});
		listHasilScan.getColumns().addAll(col);

		listHasilScan.setItems(olDokumen);
		txtJumlahHasilSacn.setText(String.valueOf(olDokumen.size()));
		btnPrint.setDisable(false);
		clear();
	}

	public void settingListboksResiDibawaKurir() {
		listResiKurir.getColumns().clear();
		List<KurirInVO> listEntry = KurirInService.getResiByKurir(idKurir);
		txtJumlahResiKurir.setText(String.valueOf(listEntry.size()));
		ObservableList<KurirInVO> olDokumen = FXCollections.observableArrayList(KurirInService.getResiByKurir(idKurir));
		TableColumn col = new TableColumn("No");
		col.setPrefWidth(50.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KurirInVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<KurirInVO, String> param) {
						return new ReadOnlyObjectWrapper(listResiKurir.getItems().indexOf(param.getValue()) + 1);
					}

				});
		listResiKurir.getColumns().addAll(col);

		col = new TableColumn("No Resi");
		col.setPrefWidth(204.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KurirInVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<KurirInVO, String> param) {
						return new SimpleStringProperty(param.getValue().getIdBarang());
					}
				});
		listResiKurir.getColumns().addAll(col);

		col = new TableColumn("Penerima");
		col.setPrefWidth(222.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KurirInVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<KurirInVO, String> param) {
						return new SimpleStringProperty(param.getValue().getPenerima());
					}
				});
		listResiKurir.getColumns().addAll(col);

		listResiKurir.setItems(olDokumen);

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

	public void clear() {
		txtNamaPenerima.setText("");
		txtAWB.setText("");
		txtJamKurirIn.setText("");
		txtMenitKurirIn.setText("");
	}

	public Boolean isValidFormEntry() {
		Boolean res = true;
		String strMessage = "";
		if (txtJamKurirIn.getText().isEmpty()) {
			strMessage += "Jam harus diisi.\n";
		}
		if (txtMenitKurirIn.getText().isEmpty()) {
			strMessage += "Menit harus diisi.\n";
		}
		if (cmbMasalahValidasi == null) {
			strMessage += "Masalah belum di pilih.\n";
		}
//		try{
//			if (t && cbNamaMasalah.isDisable()) {
//				strMessage += "Masalah / Penerima masih kosong.\n";
//			}
//		}catch(Exception e){
//			strMessage += "Masalah / Penerima masih kosong.\n";
//		}
		if (!strMessage.isEmpty()) {
			MessageBox.alert(strMessage);
			res = false;
		}
		return res;
	}

	public static String NoAutoKurirIn() {
		String noAutoKurirIn = KurirInService.getMaxCodeKurirIN(idKurir);
		String formatD = DateUtil.getDateNotSeparator(DateUtil.getNow());
		String finalNoAutoKurirIn = idKurir.toUpperCase() + "-" + formatD + "-" + noAutoKurirIn;
		return finalNoAutoKurirIn;
	}
}