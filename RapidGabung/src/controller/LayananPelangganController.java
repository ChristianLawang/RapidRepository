package controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import VO.EntryDataShowVO;
import VO.LayananPelangganVO;
import VO.StatusLaporanVO;
import entity.TrKurir;
import entity.TrUser;
import entity.TtDataEntry;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import service.LayananPelangganService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import utilfx.AutoCompleteComboBoxListener;

public class LayananPelangganController implements Initializable {

	// general
	private String noResi = "";
	// main
	// search
	@FXML
	private TextField txtCari;
	@FXML
	private DatePicker dpAwal, dpAkhir;

	// button
	@FXML
	Button btnInputLayanan;

	// table
	@FXML
	private TableView<LayananPelangganVO> tvBrowseLayanan;

	// column
	@FXML
	private TableColumn<LayananPelangganVO, String> colCreated;
	@FXML
	private TableColumn<LayananPelangganVO, String> colNoAwb;
	@FXML
	private TableColumn<LayananPelangganVO, String> colPerwakilan;
	@FXML
	private TableColumn<LayananPelangganVO, String> colStatus;
	@FXML
	private TableColumn<LayananPelangganVO, String> colNamaKurir;
	@FXML
	private TableColumn<LayananPelangganVO, String> colPelanggaranKurir;
	@FXML
	private TableColumn<LayananPelangganVO, String> colTglSelesai;
	@FXML
	private TableColumn<LayananPelangganVO, String> colNoLaporan;
	@FXML
	private TableColumn<LayananPelangganVO, String> colAction;

	private ObservableList<LayananPelangganVO> dataLayanan = FXCollections.observableArrayList();

	// popup
	@FXML
	private TextField txtNoHpPenerima, cmbNoResi;
	@FXML
	private TextArea txtMasalah, txtNotes;
	@FXML
	private Text txtNoLaporan, txtTglLaporan, txtCS, txtPerwakilan, txtKurir, txtStatusWeb;
	@FXML
	private ComboBox cmbStatusLaporan;
	@FXML
	private Button btnProses;
	@FXML
	private CheckBox ckbxType1, ckbxType2, ckbxType3;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;

		dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());

		tvBrowseLayanan.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void onClikCari() {
		showData();
	}

	public void onClickInputLayanan() {
		showDataPopUp(false, "");
	}

	private void showData() {
		dataLayanan.clear();
		List<EntryDataShowVO> listEntry = LayananPelangganService.getDataLayananPelanggan(
				DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
				DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));

		for (EntryDataShowVO t : listEntry) {
			dataLayanan.add(new LayananPelangganVO(
					t.getTglCreated() != null ? String.valueOf(DateUtil.dateToStdDateLiteral(t.getTglCreated())) : "",
					t.getAwbData(), t.getKdPerwakilan(), t.getStatusLaporan(), t.getNmKurir(), t.getPelanggaranKurir(),
					t.getTglSelesai() != null ? String.valueOf(DateUtil.dateToStdDateLiteral(t.getTglSelesai())) : "",
					t.getNoLaporan2()));
		}

		colCreated.setCellValueFactory(cellData -> cellData.getValue().tanggal);
		colNoAwb.setCellValueFactory(cellData -> cellData.getValue().noAwb);
		colPerwakilan.setCellValueFactory(cellData -> cellData.getValue().perwakilan);
		colStatus.setCellValueFactory(cellData -> cellData.getValue().statusLaporan);
		colNamaKurir.setCellValueFactory(cellData -> cellData.getValue().namaKurir);
		colPelanggaranKurir.setCellValueFactory(cellData -> cellData.getValue().pelanggaranKurir);
		colTglSelesai.setCellValueFactory(cellData -> cellData.getValue().tglSelesai);
		colNoLaporan.setCellValueFactory(cellData -> cellData.getValue().noLaporan);
		colAction.setCellFactory(
				new Callback<TableColumn<LayananPelangganVO, String>, TableCell<LayananPelangganVO, String>>() {

					@Override
					public TableCell<LayananPelangganVO, String> call(TableColumn<LayananPelangganVO, String> p) {
						return new ButtonCell(tvBrowseLayanan);
					}

				});

		FilteredList<LayananPelangganVO> filteredData = new FilteredList<>(dataLayanan, p -> true);

		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (data.getNoAwb().get().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getPerwakilan().get().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getStatusLaporan().get().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getNamaKurir().get().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getPelangganKurir().get().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}

				return false;
			});
		});

		SortedList<LayananPelangganVO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvBrowseLayanan.comparatorProperty());
		tvBrowseLayanan.setItems(sortedData);
	}

	private class ButtonCell extends TableCell<LayananPelangganVO, String> {

		final Button cellButton = new Button("EDIT");

		ButtonCell(final TableView<LayananPelangganVO> tblView) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int selectdIndex = getTableRow().getIndex();
					LayananPelangganVO a = (LayananPelangganVO) tblView.getItems().get(selectdIndex);
					showDataPopUp(true, a.getNoAwb().get());
				}
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(String t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);
			}
		}
	}

	private void showDataPopUp(boolean isDisabled, String noResis) {
		try {
			// init data
			TrUser uLogin = LoginController.getUserLogin();

			Stage stgPopUp = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			ScrollPane anchorPopUpLayanan = (ScrollPane) loader
					.load(this.getClass().getResource("PopUpInputLayananPelanggan.fxml"));
			Scene scnPhotoTerakhir = new Scene(anchorPopUpLayanan);
			stgPopUp.setScene(scnPhotoTerakhir);
			stgPopUp.setTitle("Layanan Pelanggan");
			stgPopUp.initModality(Modality.WINDOW_MODAL);
			stgPopUp.initOwner(WindowsHelper.primaryStage);
			stgPopUp.initStyle(StageStyle.UTILITY);
			stgPopUp.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent window) {
					Stage innerStage = (Stage) window.getSource();
					txtNoLaporan = (Text) innerStage.getScene().lookup("#txtNoLaporan");
					// cmbNoResi = (ComboBox)
					// innerStage.getScene().lookup("#cmbNoResi");
					cmbNoResi = (TextField) innerStage.getScene().lookup("#cmbNoResi");
					txtMasalah = (TextArea) innerStage.getScene().lookup("#txtMasalah");
					txtNoHpPenerima = (TextField) innerStage.getScene().lookup("#txtNoHpPenerima");
					txtCS = (Text) innerStage.getScene().lookup("#txtCS");
					txtPerwakilan = (Text) innerStage.getScene().lookup("#txtPerwakilan");
					txtKurir = (Text) innerStage.getScene().lookup("#txtKurir");
					txtStatusWeb = (Text) innerStage.getScene().lookup("#txtStatusWeb");
					txtNotes = (TextArea) innerStage.getScene().lookup("#txtNotes");
					txtTglLaporan = (Text) innerStage.getScene().lookup("#txtTglLaporan");
					cmbStatusLaporan = (ComboBox) innerStage.getScene().lookup("#cmbStatusLaporan");
					btnProses = (Button) innerStage.getScene().lookup("#btnProses");
					ckbxType1 = (CheckBox) innerStage.getScene().lookup("#ckbxType1");
					ckbxType2 = (CheckBox) innerStage.getScene().lookup("#ckbxType2");
					ckbxType3 = (CheckBox) innerStage.getScene().lookup("#ckbxType3");

					if (isDisabled) {
						cmbNoResi.setDisable(true);
						List<EntryDataShowVO> listDataMain = LayananPelangganService
								.getDataLayananPelangganByNoResi(noResis);
						if (listDataMain != null && listDataMain.size() > 0) {
							txtNoLaporan.setText(listDataMain.get(0).getNoLaporan2());
							// cmbNoResi.setText(listDataMain.get(0).getAwbData());
							txtMasalah.setText(listDataMain.get(0).getMasalah());
							txtNoHpPenerima.setText(listDataMain.get(0).getNoTlpn());
							cmbNoResi.setText(noResis);
							cmbStatusLaporan.setValue(listDataMain.get(0).getStatusLaporan());
							txtPerwakilan.setText(listDataMain.get(0).getKdPerwakilan());
							txtKurir.setText(listDataMain.get(0).getNmKurir());
							txtTglLaporan.setText(DateUtil.getStdDateTimeDisplay(
									listDataMain.get(0).getTglUpdated() != null ? listDataMain.get(0).getTglUpdated()
											: listDataMain.get(0).getTglCreated()));

							parsingCheckListValue(listDataMain.get(0).getPelanggaranKurir());
							txtStatusWeb.setText(listDataMain.get(0).getStatus());
							txtNotes.setText(listDataMain.get(0).getNotes());
						} else {
							List<EntryDataShowVO> listData = LayananPelangganService.getDataOtomatis(noResis);
							if (listData != null && listData.size() > 0) {
								txtPerwakilan.setText(listData.get(0).getKdPerwakilan());
								txtKurir.setText(listData.get(0).getNmKurir());
								txtStatusWeb.setText(listData.get(0).getStatusLaporan());
							} else {
								setKosong();
							}
						}
					} else {
						List<EntryDataShowVO> listDataCount = LayananPelangganService.getDataLayananPelangganAll();
						SimpleDateFormat formater = new SimpleDateFormat("yyMMdd");
						String a = String.valueOf(listDataCount.size() + 1);
						String c = formater.format(new Date());
						String b = "";
						if (a.length() != 4) {
							for (int i = 0; i < (4 - a.length()); i++) {
								b += "0";
							}
							b += a;
						}
						txtNoLaporan.setText(util.PropertiesUtil.getKodePerwakilan() + "-" + c + "-" + b);
						cmbNoResi.textProperty().addListener(new ChangeListener<String>() {
							@Override
							public void changed(final ObservableValue<? extends String> observable,
									final String oldValue, final String newValue) {
								if (newValue != null && !newValue.toString().trim().equals("")) {
									List<EntryDataShowVO> listDataMain = LayananPelangganService
											.getDataLayananPelangganByNoResi(newValue.toString());
									if (listDataMain != null && listDataMain.size() > 0) {
										txtNoLaporan.setText(String.valueOf(listDataMain.get(0).getId()));
										cmbNoResi.setText(listDataMain.get(0).getAwbData());
										txtMasalah.setText(listDataMain.get(0).getMasalah());
										txtNoHpPenerima.setText(listDataMain.get(0).getNoTlpn());
										cmbStatusLaporan.setValue(listDataMain.get(0).getStatusLaporan());
										txtPerwakilan.setText(listDataMain.get(0).getKdPerwakilan());
										txtKurir.setText(listDataMain.get(0).getNmKurir());
										txtTglLaporan.setText(DateUtil
												.getStdDateTimeDisplay(listDataMain.get(0).getTglUpdated() != null
														? listDataMain.get(0).getTglUpdated()
														: listDataMain.get(0).getTglCreated()));

										parsingCheckListValue(listDataMain.get(0).getPelanggaranKurir());
										txtStatusWeb.setText(listDataMain.get(0).getStatus());
										txtNotes.setText(listDataMain.get(0).getNotes());
									} else {
										List<EntryDataShowVO> listData = LayananPelangganService
												.getDataOtomatis(newValue.toString());
										if (listData != null && listData.size() > 0) {
											txtPerwakilan.setText(listData.get(0).getKdPerwakilan());
											txtKurir.setText(listData.get(0).getNmKurir());
											txtStatusWeb.setText(listData.get(0).getStatusLaporan());
										} else {
											setKosong();
										}
									}
								}
							}
						});
						// combo resi
						/*
						 * di ganti dengan textfield krn lambat load pake combo
						 * ObservableList<TtDataEntry> listResi = FXCollections
						 * .observableArrayList(LayananPelangganService.
						 * getDataAwb()); for (TtDataEntry i : listResi) {
						 * cmbNoResi.getItems().add(i.getAwbDataEntry()); }
						 * 
						 * new AutoCompleteComboBoxListener<TrKurir>(cmbNoResi);
						 * 
						 * cmbNoResi.valueProperty().addListener(new
						 * ChangeListener<String>() {
						 * 
						 * @Override public void changed(ObservableValue ov,
						 * String t, String t1) { noResi = t1; } });
						 * 
						 * cmbNoResi.getSelectionModel().selectedItemProperty().
						 * addListener(new ChangeListener() {
						 * 
						 * @Override public void changed(ObservableValue arg0,
						 * Object oldValue, Object newValue) { if (newValue !=
						 * null && !newValue.toString().trim().equals("")) {
						 * List<EntryDataShowVO> listDataMain =
						 * LayananPelangganService
						 * .getDataLayananPelangganByNoResi(newValue.toString())
						 * ; if (listDataMain != null && listDataMain.size() >
						 * 0) {
						 * txtNoLaporan.setText(String.valueOf(listDataMain.get(
						 * 0).getId()));
						 * cmbNoResi.setValue(listDataMain.get(0).getAwbData());
						 * txtMasalah.setText(listDataMain.get(0).getMasalah());
						 * txtNoHpPenerima.setText(listDataMain.get(0).getNoTlpn
						 * ()); cmbStatusLaporan.setValue(listDataMain.get(0).
						 * getStatusLaporan());
						 * txtPerwakilan.setText(listDataMain.get(0).
						 * getKdPerwakilan());
						 * txtKurir.setText(listDataMain.get(0).getNmKurir());
						 * txtTglLaporan.setText(DateUtil
						 * .getStdDateTimeDisplay(listDataMain.get(0).
						 * getTglUpdated() != null ?
						 * listDataMain.get(0).getTglUpdated() :
						 * listDataMain.get(0).getTglCreated()));
						 * 
						 * parsingCheckListValue(listDataMain.get(0).
						 * getPelanggaranKurir());
						 * txtStatusWeb.setText(listDataMain.get(0).getStatus())
						 * ; txtNotes.setText(listDataMain.get(0).getNotes()); }
						 * else { List<EntryDataShowVO> listData =
						 * LayananPelangganService
						 * .getDataOtomatis(newValue.toString()); if (listData
						 * != null && listData.size() > 0) {
						 * txtPerwakilan.setText(listData.get(0).getKdPerwakilan
						 * ()); txtKurir.setText(listData.get(0).getNmKurir());
						 * txtStatusWeb.setText(listData.get(0).getStatusLaporan
						 * ()); } else { setKosong(); } } } } });
						 */
					}

					// combo status laporan
					List<StatusLaporanVO> listStatus = LayananPelangganService.getDataStatusLaporan();
					for (StatusLaporanVO dataStatus : listStatus) {
						cmbStatusLaporan.getItems().add(dataStatus.getKeterangan());
					}
					cmbStatusLaporan.setPromptText("Pilih Status :");

					txtTglLaporan.setText(DateUtil.getStdDateTimeDisplay(new Date()));
					txtCS.setText(uLogin.getNamaUser());

					btnProses.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							if (isDisabled) {
								if (validateProses()) {
									LayananPelangganService.updateData(noResis,
											getCheckListValue(ckbxType1, ckbxType2, ckbxType3),
											listStatus.get(cmbStatusLaporan.getSelectionModel().getSelectedIndex())
													.getId(),
											new Date(), txtNoHpPenerima.getText(), txtNotes.getText(),
											txtMasalah.getText());

									if (cmbStatusLaporan.getValue().toString().equals("Done")) {
										LayananPelangganService.updateTglSelesai(new Date(), cmbNoResi.getText());
									}
									stgPopUp.close();
									MessageBox.alert("Input Data Layanan Pelanggan Berhasil");
									showData();
								}
							} else {
								if (validateProses()) {
									LayananPelangganService.insertData(txtNoLaporan.getText(), cmbNoResi.getText(),
											getCheckListValue(ckbxType1, ckbxType2, ckbxType3),
											listStatus.get(cmbStatusLaporan.getSelectionModel().getSelectedIndex())
													.getId(),
											new Date(), txtNoHpPenerima.getText(), txtNotes.getText(),
											txtMasalah.getText());

									if (cmbStatusLaporan.getValue().toString().equals("Done")) {
										LayananPelangganService.updateTglSelesai(new Date(), cmbNoResi.getText());
									}
									stgPopUp.close();
									MessageBox.alert("Input Data Layanan Pelanggan Berhasil");
									showData();
								}
							}
						}
					});

				}
			});
			stgPopUp.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Boolean validateProses() {
		List<TtDataEntry> listAwb = LayananPelangganService.getDataAwb(cmbNoResi.getText());
		if (listAwb != null && listAwb.size() > 0) {
			if (cmbStatusLaporan.getSelectionModel().getSelectedIndex() != -1) {
				return true;
			} else {
				MessageBox.alert("Status Laporan Harap Dipilih Terlebih Dahulu");
			}
		} else {
			MessageBox.alert("Invalid NO RESI");
		}
		return false;
	}

	private String getCheckListValue(CheckBox ck1, CheckBox ck2, CheckBox ck3) {
		String hasil = "";
		if (ck1.isSelected()) {
			hasil += ck1.getText().toString() + ",";
		}
		if (ck2.isSelected()) {
			hasil += ck2.getText().toString() + ",";
		}
		if (ck3.isSelected()) {
			hasil += ck3.getText().toString() + ",";
		}
		return !hasil.equals("") ? hasil.substring(0, hasil.length() - 1) : "";
	}

	private void parsingCheckListValue(String hasil) {
		if (hasil != null && !hasil.equals("")) {
			String[] h1 = hasil.trim().split(",");
			ckbxType1.setSelected(false);
			ckbxType2.setSelected(false);
			ckbxType3.setSelected(false);
			for (int i = 0; i < h1.length; i++) {
				if (h1[i].equals(ckbxType1.getText().toString())) {
					ckbxType1.setSelected(true);
				} else if (h1[i].equals(ckbxType2.getText().toString())) {
					ckbxType2.setSelected(true);
				} else if (h1[i].equals(ckbxType3.getText().toString())) {
					ckbxType3.setSelected(true);
				}
			}
		}
	}

	private void setKosong() {
		// txtNoLaporan.setText("");
		txtMasalah.setText("");
		txtNoHpPenerima.setText("");
		txtNotes.setText("");
		txtPerwakilan.setText("");
		txtKurir.setText("");
		txtStatusWeb.setText("");
		txtTglLaporan.setText(DateUtil.getStdDateTimeDisplay(new Date()));
		cmbStatusLaporan.getSelectionModel().clearSelection();
		ckbxType1.setSelected(false);
		ckbxType2.setSelected(false);
		ckbxType3.setSelected(false);
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