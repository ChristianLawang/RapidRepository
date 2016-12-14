package controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.sun.prism.impl.Disposer.Record;

import VO.BankVO;
import VO.DepositVO;
import VO.EntryDataShowVO;
import VO.InvoiceVO;
import VO.PenandaLunasVO2;
import entity.TrKurir;
import entity.TrPelanggan;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import service.BankService;
import service.BrowseSemuaDataService;
import service.DepositService;
import service.GenericService;
import service.InvoiceService;
import service.PelangganService;
import service.PenandaLunasService;
import util.DateUtil;
import util.DtoListener;
import util.EmailUtil;
import util.ManagedFormHelper;
import util.MessageBox;
import util.PDFUtil;
import util.WindowsHelper;
import util.formatRupiah;
import utilfx.AutoCompleteComboBoxListener;

public class PenandaLunasController2 implements Initializable {

	// general
	private List<EntryDataShowVO> listEntry;
	private List<BankVO> listBank;
	private int selectdIndex = 0;
	private int totalPop, sisaDepositPop, grandTotalPop;
	private Boolean Checklist = false;
	private String pelanggan = "";

	// search
	@FXML
	private TextField txtNamaPelanggan;
	@FXML
	private DatePicker dpAwal, dpAkhir;
	@FXML
	private ComboBox cbPelanggan;
	@FXML
	private CheckBox chkAllPelanggan;

	// button
	@FXML
	Button btnInvoice, btnPiutang;

	// table
	@FXML
	private TableView<PenandaLunasVO2> tvPenandaLunas;

	// column
	@FXML
	private TableColumn<PenandaLunasVO2, Boolean> colCheklist;
	@FXML
	private TableColumn<PenandaLunasVO2, String> colTgl;
	@FXML
	private TableColumn<PenandaLunasVO2, String> colNmPelanggan;
	@FXML
	private TableColumn<PenandaLunasVO2, String> colNoPickUp;
	@FXML
	private TableColumn<PenandaLunasVO2, String> colTotalPiutang;
	@FXML
	private TableColumn<PenandaLunasVO2, String> colTglBayar;
	@FXML
	private TableColumn<PenandaLunasVO2, String> colBank;

	private ObservableList<PenandaLunasVO2> DataPickup = FXCollections.observableArrayList();

	private CheckBox cbAll = new CheckBox();

	// popup
	@FXML
	TableView<PenandaLunasVO2> tvPopUpLunas, tvPopUpLunasBank;

	@FXML
	private TextField txtTotal, txtSisaDeposit, txtGrandTotal, txtTotalBayar;

	@FXML
	private DatePicker dbxTglBayar;

	@FXML
	private Button btnInvoicePopUp;

	// column
	@FXML
	private TableColumn<PenandaLunasVO2, String> colTglPopUp;
	@FXML
	private TableColumn<PenandaLunasVO2, String> colNoPickupPopUp;
	@FXML
	private TableColumn<PenandaLunasVO2, String> colNmPelangganPopUp;
	@FXML
	private TableColumn<PenandaLunasVO2, Number> colTotalPiutangPopUp;

	@FXML
	private ComboBox cmbBank;
	
	@FXML
	private Label lblInfo;

	private Stage stgPopUp;
	private List<EntryDataShowVO> listEntryPopUp = new ArrayList<>();
	private ObservableList<PenandaLunasVO2> DataPickupPopUp = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;

		enableDisable(true);

		dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());

		tvPenandaLunas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// memanggil untuk combo referensi
		ObservableList<TrPelanggan> listReferensi = FXCollections
				.observableArrayList(PelangganService.getDataReferensi());
		for (TrPelanggan i : listReferensi) {
			cbPelanggan.getItems().add(i.getNamaAkun());
		}

		new AutoCompleteComboBoxListener<TrKurir>(cbPelanggan);

		cbPelanggan.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				pelanggan = t1;

			}
		});

		chkAllPelanggan.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					cbPelanggan.setDisable(true);
					Checklist = true;
				} else {
					cbPelanggan.setDisable(false);
					Checklist = false;
				}
			}
		});

		cbAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				if (cbAll.isSelected()) {
					DataPickup.clear();
					showData(true);
				} else {
					DataPickup.clear();
					showData(false);
				}
			}
		});

	}

	public void onClikCari() {
		DataPickup.clear();
		showData(false);
	}
	
	public void onClikInvoice() {
		DataPickupPopUp.clear();
		listEntryPopUp = new ArrayList<>();
		final List<PenandaLunasVO2> trv = tvPenandaLunas.getItems();
		boolean cekChecklist = false;
		for (PenandaLunasVO2 penandaL : trv) {
			if (penandaL.getChecked().get() && penandaL.getBank().get().equals("")) {
				EntryDataShowVO data = new EntryDataShowVO();
				data.setTglCreated(DateUtil.Date(penandaL.getTanggal().get(), "yyyy-MM-dd"));
				data.setKdPickup(penandaL.getNoPickUp().get());
				data.setKdPelanggan(penandaL.getNmPelanggan().get());
				data.settBiaya(BigDecimal.valueOf(penandaL.getTotalPiutang().get()));
				listEntryPopUp.add(data);
			}

			if (!penandaL.getBank().get().equals("")) {
				cekChecklist = true;
			}
		}

		if (listEntryPopUp != null && listEntryPopUp.size() > 0) {
			boolean cekNotSame = false;
			for (int i = 0; i < listEntryPopUp.size(); i++) {
				for (int j = 0; j < listEntryPopUp.size(); j++) {
					if (!listEntryPopUp.get(i).getKdPelanggan().equals(listEntryPopUp.get(j).getKdPelanggan())) {
						cekNotSame = true;
						break;
					}
				}
			}

			if (!cekNotSame) {
				showDataPopUp(listEntryPopUp);
			} else {
				MessageBox.alert("Pelanggan Yang Dipilih Harus Sama");
			}
		} else {
			if (!cekChecklist) {
				MessageBox.alert("Pilih Salah Satu Data Terlebih Dahulu");
			} else {
				MessageBox.alert("Data Sudah Lunas");
			}
		}
	}

	public void onClikPiutang() {
		listEntryPopUp = new ArrayList<>();
		final List<PenandaLunasVO2> trv = tvPenandaLunas.getItems();
		final List<String> lstDaftarPelanggan = new ArrayList<String>();
		for (PenandaLunasVO2 penandaL : trv) {
			if (penandaL.getChecked().get()) {
				EntryDataShowVO data = new EntryDataShowVO();
				data.setTglCreated(DateUtil.Date(penandaL.getTanggal().get(), "yyyy-MM-dd"));
				data.setKdPickup(penandaL.getNoPickUp().get());
				data.setKdPelanggan(penandaL.getNmPelanggan().get());
				data.settBiaya(BigDecimal.valueOf(penandaL.getTotalPiutang().get()));
				listEntryPopUp.add(data);
				lstDaftarPelanggan.add(penandaL.getNmPelanggan().get());
			}
		}
		// java distinct
		List<String> eachPelanggan = new ArrayList<String>(new HashSet<String>( lstDaftarPelanggan ));
		int[] dataButtonMessageBox = new int[2];
		dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
		dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
		int hasilMessageBox = MessageBox.confirm("Apakah anda yakin ingin mengirim email tagihan piutang kepada " + eachPelanggan.size() + " pelanggan yang dipilih",
				dataButtonMessageBox);
		if (hasilMessageBox == 6) {
			Map<String, Object> config = GenericService.getConfig();
			
			String username = config.get("email_username")==null?"":(String) config.get("email_username");
			String password = config.get("email_password")==null?"":(String) config.get("email_password");
			
			String pdfFolder = generateOrReplaceFolder(new Date());
			for (String strPelanggan : eachPelanggan) {
				TrPelanggan pel = PelangganService.getPelangganById(strPelanggan);
				List<PenandaLunasVO2> dataPDF = new ArrayList<PenandaLunasVO2>();
				for (PenandaLunasVO2 test : trv) {
					if(test.getChecked().get()){
						if(test.getNmPelanggan().get().equals(pel.getKodePelanggan())){
							dataPDF.add(test);
						}
					}
				}
				String path = 
						pdfFolder+"/"+pel.getKodePelanggan()+"-PIUTANG"+".PDF";
				String subject = "Piutang - " + pel.getKodePelanggan();
				String bodyEmail = "Terlampir, tagihan yang harus dibayarkan";
				// jeneret pede'ef
				PDFUtil.createPiutang(dataPDF, pel, path);
				// sen imel
				EmailUtil.sendEmailPenandaLunas(username, password, subject, bodyEmail, pel, path);
				
			}
		}
	}
	
	private String generateOrReplaceFolder(Date date) {
		
		System.out.println("date : " + date);
		
		String strYear = DateUtil.getYearOnly(new Date());
		String strMonth = DateUtil.getMonthOnly(new Date());
		String strDay = DateUtil.getDayOnly(date);
		
		String path = "C:/DLL/PDF-PENANDA-LUNAS/" + strYear + "/" + strMonth + "/" + strDay;
		
		File dir = new File(path);
		dir.mkdirs();
		
		return path;
	}
	
	private void enableDisable(boolean cek) {
		btnInvoice.setDisable(cek);
		btnPiutang.setDisable(cek);
	}

	private void showData(boolean chkAll) {
		if (Checklist) {
			listEntry = PenandaLunasService.getDataPelunasanAll(DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
					DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
		} else {
			listEntry = PenandaLunasService.getDataPelunasan(pelanggan,
					DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
					DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
		}
		String namaPelanggan = "";
		String sisaDeposit = "";
		String totalPiutang = "";
		String sisaHutang = "";
		
		Integer biaya = 0;
		Integer piutang = 0;
		for (EntryDataShowVO t : listEntry) {
			DataPickup.add(new PenandaLunasVO2(chkAll,
					t.getTglCreated() != null ? String.valueOf(DateUtil.dateToStdDateLiteral(t.getTglCreated())) : "",
					t.getKdPelanggan(), t.getKdPickup(), t.gettBiaya().intValue(),
					t.getTglBayar() != null ? String.valueOf(DateUtil.dateToStdDateLiteral(t.getTglBayar())) : "",
					t.getBank()));
			namaPelanggan = t.getKdPelanggan();
			piutang += t.gettBiaya().intValue();
			if(t.getBank().isEmpty()){
				biaya += t.gettBiaya().intValue();
			}
		}
		
		if(!Checklist){
			List<DepositVO> tr = DepositService.getDataDeposit(namaPelanggan);
			if(tr.size()>0){
				sisaDeposit = util.formatRupiah.formatIndonesia(tr.get(0).getSisaDeposit());
			}else{
				sisaDeposit = util.formatRupiah.formatIndonesia(0);
			}
			totalPiutang = util.formatRupiah.formatIndonesia(piutang);
			sisaHutang = util.formatRupiah.formatIndonesia(biaya);
			lblInfo.setText("Nama Pelanggan : " + namaPelanggan + ", sisa deposit : " + sisaDeposit + ", total piutang : " + totalPiutang + ", sisa hutang : " + sisaHutang);
		}else{
			totalPiutang = util.formatRupiah.formatIndonesia(piutang);
			sisaHutang = util.formatRupiah.formatIndonesia(biaya);
			lblInfo.setText("Total piutang seluruh pelanggan : " + totalPiutang + ", total sisa hutang seluruh pelanggan : " + sisaHutang);
		}
				
		colCheklist.setCellValueFactory(cellData -> cellData.getValue().checked);
		colCheklist.setCellFactory(CheckBoxTableCell.forTableColumn(colCheklist));
		if (listEntry != null && listEntry.size() > 0) {
			colCheklist.setGraphic(cbAll);
			enableDisable(false);
		} else {
			colCheklist.setGraphic(null);
		}
		colCheklist.setEditable(true);

		colTgl.setCellValueFactory(cellData -> cellData.getValue().tanggal);
		colNmPelanggan.setCellValueFactory(cellData -> cellData.getValue().nmPelanggan);
		colNoPickUp.setCellValueFactory(cellData -> cellData.getValue().noPickUp);
		colTotalPiutang.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<PenandaLunasVO2, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<PenandaLunasVO2, String> param) {
						return new SimpleStringProperty(
								util.formatRupiah.formatIndonesia(param.getValue().getTotalPiutang().get()));
					}
				});

		colTglBayar.setCellValueFactory(cellData -> cellData.getValue().tglBayar);
		colBank.setCellValueFactory(cellData -> cellData.getValue().bank);

		FilteredList<PenandaLunasVO2> filteredData = new FilteredList<>(DataPickup, p -> true);
		SortedList<PenandaLunasVO2> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvPenandaLunas.comparatorProperty());
		tvPenandaLunas.setItems(sortedData);

	}

	private void showDataPopUp(List<EntryDataShowVO> listData) {
		try {
			List<DepositVO> listSisa = DepositService
					.getDataDeposit(listData != null && listData.size() > 0 ? listData.get(0).getKdPelanggan() : "0");
			listBank = BankService.getDataBank();

			stgPopUp = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			ScrollPane anchorPopUpLunas = (ScrollPane) loader
					.load(this.getClass().getResource("PopUpChecklistLunas.fxml"));
			Scene scnPhotoTerakhir = new Scene(anchorPopUpLunas);
			stgPopUp.setScene(scnPhotoTerakhir);
			stgPopUp.setTitle("Checklist Lunas");
			stgPopUp.initModality(Modality.WINDOW_MODAL);
			stgPopUp.initOwner(WindowsHelper.primaryStage);
			stgPopUp.initStyle(StageStyle.UTILITY);
			stgPopUp.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent window) {
					Stage innerStage = (Stage) window.getSource();
					tvPopUpLunas = (TableView<PenandaLunasVO2>) innerStage.getScene().lookup("#tvPopUpLunas");
					tvPopUpLunasBank = (TableView<PenandaLunasVO2>) innerStage.getScene().lookup("#tvPopUpLunasBank");

					tvPopUpLunasBank.setPlaceholder(new Label(""));
					txtTotal = (TextField) innerStage.getScene().lookup("#txtTotal");
					txtSisaDeposit = (TextField) innerStage.getScene().lookup("#txtSisaDeposit");
					txtGrandTotal = (TextField) innerStage.getScene().lookup("#txtGrandTotal");
					txtTotalBayar = (TextField) innerStage.getScene().lookup("#txtTotalBayar");
					dbxTglBayar = (DatePicker) innerStage.getScene().lookup("#dbxTglBayar");
					cmbBank = (ComboBox) innerStage.getScene().lookup("#cmbBank");
					btnInvoicePopUp = (Button) innerStage.getScene().lookup("#btnInvoicePopUp");

					for (BankVO dataBank : listBank) {
						cmbBank.getItems().add(dataBank.getNmBank());
					}
					cmbBank.setPromptText("Pilih Bank :");

					btnInvoicePopUp.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							kirmInvoicePopUp(txtTotalBayar, dbxTglBayar);
						}
					});

					BigDecimal countTotal = BigDecimal.ZERO;

					for (EntryDataShowVO t : listData) {
						DataPickupPopUp.add(
								new PenandaLunasVO2(String.valueOf(DateUtil.dateToStdDateLiteral(t.getTglCreated())),
										t.getKdPelanggan(), t.getKdPickup(), t.gettBiaya().intValue()));

						countTotal = countTotal.add(t.gettBiaya());
					}

					txtTotal.setText(util.formatRupiah.formatIndonesia(countTotal.intValue()));
					txtSisaDeposit.setText(listSisa != null && listSisa.size() > 0
							? util.formatRupiah.formatIndonesia(listSisa.get(0).getSisaDeposit()) : "0");

					txtGrandTotal.setText(util.formatRupiah.formatIndonesia(countTotal.intValue()
							- Integer.valueOf(util.formatRupiah.formatIndonesiaTanpaTitik(txtSisaDeposit.getText()))));

					totalPop = Integer.valueOf(util.formatRupiah.formatIndonesiaTanpaTitik(txtTotal.getText()));
					sisaDepositPop = Integer
							.valueOf(util.formatRupiah.formatIndonesiaTanpaTitik(txtSisaDeposit.getText()));
					grandTotalPop = totalPop - sisaDepositPop;

					tvPopUpLunas.getColumns().clear();
					tvPopUpLunas.getItems().clear();

					TableColumn noCol = new TableColumn("No");
					TableColumn tglCol = new TableColumn("Tanggal");
					TableColumn noPikCol = new TableColumn("No Pickup");
					TableColumn nmPelangganCol = new TableColumn("Nama Pelanggan");
					TableColumn totalCol = new TableColumn("Total Piutang");
					TableColumn actionCol = new TableColumn("Action");

					noCol.setCellValueFactory(
							new Callback<TableColumn.CellDataFeatures<PenandaLunasVO2, String>, ObservableValue<String>>() {
								@Override
								public ObservableValue<String> call(
										TableColumn.CellDataFeatures<PenandaLunasVO2, String> param) {
									return new ReadOnlyObjectWrapper(
											tvPopUpLunas.getItems().indexOf(param.getValue()) + 1);
								}
							});

					tglCol.setCellValueFactory(
							new Callback<TableColumn.CellDataFeatures<PenandaLunasVO2, String>, ObservableValue<String>>() {
								@Override
								public ObservableValue<String> call(
										TableColumn.CellDataFeatures<PenandaLunasVO2, String> param) {
									return new SimpleStringProperty(param.getValue().getTanggal().get());
								}
							});

					noPikCol.setCellValueFactory(
							new Callback<TableColumn.CellDataFeatures<PenandaLunasVO2, String>, ObservableValue<String>>() {
								@Override
								public ObservableValue<String> call(
										TableColumn.CellDataFeatures<PenandaLunasVO2, String> param) {
									return new SimpleStringProperty(param.getValue().getNoPickUp().get());
								}
							});
					nmPelangganCol.setCellValueFactory(
							new Callback<TableColumn.CellDataFeatures<PenandaLunasVO2, String>, ObservableValue<String>>() {
								@Override
								public ObservableValue<String> call(
										TableColumn.CellDataFeatures<PenandaLunasVO2, String> param) {
									return new SimpleStringProperty(param.getValue().getNmPelanggan().get());
								}
							});
					totalCol.setCellValueFactory(
							new Callback<TableColumn.CellDataFeatures<PenandaLunasVO2, String>, ObservableValue<String>>() {
								@Override
								public ObservableValue<String> call(CellDataFeatures<PenandaLunasVO2, String> param) {
									return new SimpleStringProperty(
											util.formatRupiah.formatIndonesia(param.getValue().totalPiutang.get()));
								}
							});

					actionCol.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

						@Override
						public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
							return new ButtonCell(tvPopUpLunas);
						}

					});

					tvPopUpLunas.getColumns().addAll(noCol, tglCol, noPikCol, nmPelangganCol, totalCol, actionCol);
					tvPopUpLunas.setItems(DataPickupPopUp);
				}
			});
			stgPopUp.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void kirmInvoicePopUp(TextField totalBayar, DatePicker tglBayars) {
		if (!totalBayar.getText().trim().equals("")) {
			if (cmbBank.getSelectionModel().getSelectedIndex() != -1) {
				// boolean isDigit = totalBayar.getText().matches("\\d{1}");
				// if (isDigit) {
				if (Integer.valueOf(totalBayar.getText()) < grandTotalPop) {
					MessageBox.alert("Total Bayar Minimal Sama Dengan Total Keseluruhan Tagihan");
				} else {
					int deposit = Integer
							.valueOf(util.formatRupiah.formatIndonesiaTanpaTitik(txtSisaDeposit.getText()));

					if (Integer.valueOf(totalBayar.getText()) > grandTotalPop) {
						deposit = Integer.valueOf(totalBayar.getText()) - grandTotalPop;
					} else {
						deposit = 0;
					}

					int bankId = listBank.get(cmbBank.getSelectionModel().getSelectedIndex()).getIdBank();
					for (int i = 0; i < listEntryPopUp.size(); i++) {
						// update flag lunas
						PenandaLunasService.updateLunas(listEntryPopUp.get(i).getKdPickup());
						// insert/update to tt_invoice
						List<InvoiceVO> listDataInvoice = InvoiceService
								.getDataInvoice(listEntryPopUp.get(i).getKdPickup());
						if (listDataInvoice != null && listDataInvoice.size() > 0) {
							// update
							InvoiceService.updateData(listEntryPopUp.get(i).getKdPickup(),
									DateUtil.convertToDatabaseColumn(tglBayars.getValue()), bankId);
						} else {
							// insert
							InvoiceService.insertData(listEntryPopUp.get(i).getKdPickup(),
									DateUtil.convertToDatabaseColumn(tglBayars.getValue()), bankId);
						}
					}

					List<DepositVO> listDataDeposit = DepositService
							.getDataDeposit(listEntryPopUp.get(0).getKdPelanggan());
					if (listDataDeposit != null && listDataDeposit.size() > 0) {
						// update
						DepositService.updateData(listEntryPopUp.get(0).getKdPelanggan(), deposit);
					} else {
						// insert
						DepositService.insertData(listEntryPopUp.get(0).getKdPelanggan(), deposit);
					}
					
					// chris nambah
					Map<String, Object> config = GenericService.getConfig();
					
					String username = config.get("email_username")==null?"":(String) config.get("email_username");
					String password = config.get("email_password")==null?"":(String) config.get("email_password");
					
					String pdfFolder = generateOrReplaceFolder(new Date());
					TrPelanggan pel = PelangganService.getPelangganById(listEntryPopUp.get(0).getKdPelanggan());					
					
					String path = 
							pdfFolder+"/"+pel.getKodePelanggan()+"-INVOICE"+".PDF";
					String subject = "Rapid Express - Konfirmasi Pembayaran ("+pel.getNamaAkun()+") "+ DateUtil.convertToDatabaseColumn(dpAwal.getValue()).toString() + " s/d " + DateUtil.convertToDatabaseColumn(dpAkhir.getValue()).toString();
					String emailBody = 
							"Pelanggan Terhormat, " + System.lineSeparator() +
							System.lineSeparator() +
							"Nama Pelanggan : " + pel.getNamaAkun()+ System.lineSeparator() +
							"Terima kasih atas pembayaran tagihan Rapid Express anda sebesar Rp." + totalBayar.getText() + " yang sudah kami terima pada tanggal " + DateUtil.getStdDateDisplay(new Date()) + System.lineSeparator() +
							"Anda memiliki sisa deposit sebesar Rp. : " + formatRupiah.formatIndonesiaTanpaKoma(new Integer(deposit).toString()) + " yang dapat digunakan untuk penambahan pembayaran berikutnya."+ System.lineSeparator() +
							System.lineSeparator() +
							"Selamat menikmati layanan dari Rapid Express" +  System.lineSeparator() +
							System.lineSeparator() +
							"Salam hangat, " + System.lineSeparator() +
							"Rapid Express Indonesia ";						
					// jeneret pede'ef
					PDFUtil.createInvoice(deposit, listEntryPopUp, pel, path);
					// sen imel
					EmailUtil.sendEmailPenandaLunas(username, password, subject, emailBody, pel, path);
					
					DataPickupPopUp.clear();
					stgPopUp.close();
					DataPickup.clear();
					showData(false);

					
					MessageBox.alert(
							"Berhasil Mengirimkan Invoice ke Pelanggan " + listEntryPopUp.get(0).getKdPelanggan());
				}
				// } else {
				// MessageBox.alert("Hanya Boleh Angka dari [0 s.d 9] tanpa
				// titik [.] dan koma [,]");
				// }
			} else {
				MessageBox.alert("Silahkan Pilih Bank Terlebih Dahulu");
			}
		} else {
			MessageBox.alert("Total Bayar Tidak Boleh Kosong");
		}
	}

	private class ButtonCell extends TableCell<Record, Boolean> {

		final Button cellButton = new Button("Delete");

		ButtonCell(final TableView<PenandaLunasVO2> tblView) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int selectdIndex = getTableRow().getIndex();
					PenandaLunasVO2 a = (PenandaLunasVO2) tblView.getItems().get(selectdIndex);
					int[] dataButtonMessageBox = new int[2];
					dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
					dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
					int hasilMessageBox = MessageBox.confirm("Apakah Yakin akan Melakukan Delete?",
							dataButtonMessageBox);

					if (hasilMessageBox == 5) { // cancel
					} else if (hasilMessageBox == 6) {
						listEntryPopUp.remove(selectdIndex);
						DataPickupPopUp.clear();
						stgPopUp.close();
						showDataPopUp(listEntryPopUp);
					}
				}
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);
			}
		}
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
