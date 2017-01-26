package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sun.prism.impl.Disposer.Record;

import VO.EntryDataShowVO;
import driver.GabungPaketBarcode;
import driver.GabungPaketResi;
import entity.TrUser;
import entity.TtGabungPaket;
import entity.TtGabungSementara;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import service.BrowseSemuaDataService;
import service.ConfigurationService;
import service.GabungPaketService;
import service.GenericService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.PropertiesUtil;
import util.WindowsHelper;

public class GabungPaketController implements Initializable {

	// coba gabung paket
	
	@FXML
	TableView listTableA, listTableB;

	@FXML
	TextField txtIdResiPrintUlang, txt_kd_keranjang, txt_resi_kardus, txt_resi_barang, txt_total_berat, txt_berat_all,
			txt_insert_code;

	@FXML
	Button btn_print;

	@FXML
	Button btn_print_ulang;
	
	@FXML
	Button btnClearSession;

	private String tujuan, finalNoAuto;

	private List<EntryDataShowVO> listEntry = new ArrayList<EntryDataShowVO>();

	public TrUser uLogin = LoginController.getUserLogin();

	public String IdKomputer;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Properties prop = ConfigurationService.getHibernateProperties();
		String perw = (String) prop.get("app.current.perwakilan");
		String kdPerw = (String) prop.get("app.current.kodeperwakilan");
		IdKomputer = perw + kdPerw;

		btn_print.setDisable(true);
		ManagedFormHelper.instanceController = this;
		txt_insert_code.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tujuan = txt_insert_code.getText();
				int LengTujuanInput = tujuan.length();
				if (LengTujuanInput > 3) { // scan no resi
					// chris nambah disini
					Boolean isKodeKardus = this.getIsNomorKardus(tujuan);
					if(isKodeKardus){
						if(!tujuan.substring(0, 3).equals(txt_kd_keranjang.getText())){
							audioShow();
							MessageBox.alert("ID Kardus harus tujuan " + txt_kd_keranjang.getText());
						}else{
							String kodePerwakilan = PropertiesUtil.getPerwakilan();
							String idKardus = tujuan.toUpperCase();
							List<TtGabungPaket> lstGabungPaket = GabungPaketService.unionGabungPaket(idKardus, kodePerwakilan);
							if(lstGabungPaket.size()==0){
								audioShow();
								MessageBox.alert("Nomor kardus tidak ditemukan");
							}else{
								for (TtGabungPaket gp : lstGabungPaket) {
									System.out.println("--> gp.getIdKardus() : " + gp.getIdKardus());
									TtGabungSementara gs = new TtGabungSementara(
											IdKomputer + GabungPaketService.getMaxIdGabungSementara(IdKomputer, txt_resi_kardus.getText()),
											txt_resi_kardus.getText(),
											gp.getIdKardus(),
											gp.getAwb(),
											null,
											null,
											gp.getKodePerwakilan(),
											txt_kd_keranjang.getText(),
											DateUtil.fotoTimbangDateGenerateRule(new Date()),
											null,
											null
											);
									System.out.println();
									GenericService.save(TtGabungSementara.class, gs, true);
									
									GabungPaketService.updateSesudahGabung(2, DateUtil.getNow(),
											gp.getAwb(), txt_resi_kardus.getText());
								}
							}
						}
						sohwTableA();
						sohwTableB();
						
					}else{
						txt_resi_barang.setText(tujuan.toUpperCase());
						String kdKotak = GabungPaketService.validasiGabungPaket(tujuan);
						System.out.println("------------------Kode Kotak  :"+kdKotak);
						if (kdKotak == null || kdKotak.equals("")) {
							String tujuanBarang = GabungPaketService.getTujuanPaket(txt_resi_barang.getText());
							System.out.println("------------------Tujuan Barang  : "+tujuanBarang);
							if (tujuanBarang != null && !tujuanBarang.equals("")) {
								if (txt_kd_keranjang.getText().equalsIgnoreCase(tujuanBarang)
										|| txt_kd_keranjang.getText().equalsIgnoreCase("RPD")) {
									if (txt_kd_keranjang.getText().equalsIgnoreCase("RPD")
											&& !tujuanBarang.equalsIgnoreCase("JNE")
											&& !uLogin.getKodeCabang().substring(0, 3)
													.equalsIgnoreCase(tujuanBarang)
											&& !tujuanBarang.equalsIgnoreCase("XXX")) {
										TtGabungSementara gabSes = new TtGabungSementara();
										String id = GabungPaketService.getMaxIdGabungSementara(IdKomputer, txt_resi_kardus.getText());
										gabSes.setId(IdKomputer + id);
										gabSes.setIdKardus(txt_resi_kardus.getText());
										gabSes.setAwb(txt_resi_barang.getText());
										gabSes.setSession(txt_kd_keranjang.getText());
										gabSes.setTujuan("RPD");
										gabSes.setTglCreate(DateUtil.fotoTimbangDateGenerateRule(new Date()));	
										GenericService.save(TtGabungSementara.class, gabSes, true);
										GabungPaketService.updateSesudahGabung(2, DateUtil.getNow(),
												txt_resi_barang.getText(), txt_resi_kardus.getText());
										sohwTableA();
										sohwTableB();
									} else {
										if (tujuanBarang.equalsIgnoreCase("JNE") && txt_kd_keranjang.getText().equalsIgnoreCase("RPD")) {
											audioShow();
											MessageBox.alert("JNE tidak bisa gabung paket Ke RPD");
										} else if (uLogin.getKodeCabang().substring(0, 3)
												.equalsIgnoreCase(tujuanBarang) && txt_kd_keranjang.getText().equalsIgnoreCase("RPD")) {
											audioShow();
											MessageBox.alert("Gabung paket cabang yang sama tidak bisa");
										}else if(tujuanBarang.equalsIgnoreCase("XXX") && txt_kd_keranjang.getText().equalsIgnoreCase("RPD")){
											audioShow();
											MessageBox.alert("Pending tidak bisa gabung paket ke RPD");
										}
									}
									if (!txt_kd_keranjang.getText().equalsIgnoreCase("RPD")) {
										TtGabungSementara gabSes = new TtGabungSementara();
										String id = GabungPaketService.getMaxIdGabungSementara(IdKomputer, txt_resi_kardus.getText());
										gabSes.setId(IdKomputer + id);
										gabSes.setIdKardus(txt_resi_kardus.getText());
										gabSes.setAwb(txt_resi_barang.getText());
										gabSes.setSession(txt_kd_keranjang.getText());
										gabSes.setTujuan(tujuanBarang);
	//									gabSes.setTglCreate(DateUtil.getNow());
										gabSes.setTglCreate(DateUtil.fotoTimbangDateGenerateRule(new Date()));									
										GenericService.save(TtGabungSementara.class, gabSes, true);
										GabungPaketService.updateSesudahGabung(2, DateUtil.getNow(),
												txt_resi_barang.getText(), txt_resi_kardus.getText());
										sohwTableA();
										sohwTableB();
									}
								} else {
									audioShow();
									MessageBox.alert("Resi " + tujuanBarang + " Coba Periksa Kembali");
								}
							} else {
								audioShow();
								MessageBox.alert("Resi Belum Di Input / Tidak terdaftar");
							}
						} else {
							audioShow();
							MessageBox.alert("Resi Sudah Gabung Paket " + kdKotak.toUpperCase());
						}
					}
				} else { // scan kode tujuan
					txt_kd_keranjang.setText(tujuan.toUpperCase());
					String kdKotak = GabungPaketService.getKdKotakGabungPaketSessionTampilTableB(tujuan);
					if (kdKotak == null || kdKotak.equals("")) {
						String noResiKardus = GabungPaketService.getMaxCode2(txt_kd_keranjang.getText().toUpperCase(), DateUtil.fotoTimbangDateGenerateRule(new Date()));
						String formatD = DateUtil.getDateNotSeparator(DateUtil.fotoTimbangDateGenerateRule(new Date()));
						finalNoAuto = tujuan.toUpperCase() + noResiKardus + "-" + formatD + uLogin.getKodeCabang();
						txt_resi_kardus.setText(finalNoAuto);
						sohwTableA();
						sohwTableB();
					} else {
						txt_resi_kardus.setText(kdKotak);
						sohwTableB();
						sohwTableA();
					}
				}
				txt_insert_code.setText("");
			}

			private Boolean getIsNomorKardus(String tujuan) {
				if(tujuan.length()>6){
					if(tujuan.contains("-")){
						return true;
					}else{
						return false;
					}
				}
				return false;
			}
		});
		btnClearSession.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int[] dataButtonMessageBox = new int[2];
				dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
				dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
				int hasilMessageBox = MessageBox.confirm("Apakah Yakin akan Melakukan Clear Session?",
						dataButtonMessageBox);
				if (hasilMessageBox == 6) {
//					List<TtGabungSementara> sessKemarin = GabungPaketService.loadGabungPaketSementaraKemarin();
//					for (TtGabungSementara data : sessKemarin) {
//						GabungPaketService.restoreNoResiFromSession(data);
//					}
					GabungPaketService.deleteSessionKemarin();
					sohwTableB();
					sohwTableA();
				}
			}
		});
	}

	// -------------------------------------------------------------------TABLE
	// B
	private void sohwTableA() {

		listTableA.getColumns().clear();
		Long id = null;
		ObservableList<EntryDataShowVO> olDokumen = FXCollections.observableArrayList();
		if (!txt_kd_keranjang.getText().equals("RPD")) {
			olDokumen = FXCollections
					.observableArrayList(GabungPaketService.getNonGabungPaket(txt_kd_keranjang.getText()));
		} else {
			olDokumen = FXCollections.observableArrayList(
					GabungPaketService.getNonGabungPaketHO(uLogin.getKodeCabang().substring(0, 3)));
		}
		TableColumn col = new TableColumn("No");
		col.setPrefWidth(30.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new ReadOnlyObjectWrapper(listTableA.getItems().indexOf(param.getValue()) + 1);
					}

				});
		listTableA.getColumns().addAll(col);
		col = new TableColumn("Kode Perwakilan");
		col.setPrefWidth(120.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new SimpleStringProperty(param.getValue().getKdPerwakilan());
					}
				});
		listTableA.getColumns().addAll(col);

		col = new TableColumn("Kode AWB");
		col.setPrefWidth(170.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new SimpleStringProperty(param.getValue().getAwbData());
					}
				});
		listTableA.getColumns().addAll(col);

		col = new TableColumn("Tujuan");
		col.setPrefWidth(160.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new SimpleStringProperty(param.getValue().getTujuan());
					}
				});
		listTableA.getColumns().addAll(col);

		col = new TableColumn("Action");
		col.setPrefWidth(140.0);
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
				return new ButtonCellWaitingPending(listTableA);
			}

		});

		listTableA.getColumns().add(col);

		listTableA.setItems(olDokumen);

	}

	// ------------------------------------------------------------------TABLE B
	private void sohwTableB() {
		int sum = 0;
		double sumBulat = 0.0;
		listTableB.getColumns().clear();
		listEntry.clear();

		listEntry = GabungPaketService.getGabungPaketAfterSave(txt_resi_kardus.getText());
		for (EntryDataShowVO en : listEntry) {
			sum += Integer.parseInt(en.getBpFinal());

			sumBulat += Double.parseDouble(en.getbFinal());

		}
		txt_total_berat.setText(String.valueOf(sum));
		txt_berat_all.setText(String.valueOf(listEntry.size()));
		ObservableList<EntryDataShowVO> olDokumen = FXCollections.observableArrayList(listEntry);
		TableColumn col = new TableColumn("No");
		col.setPrefWidth(30.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new ReadOnlyObjectWrapper(listTableB.getItems().indexOf(param.getValue()) + 1);
					}
				});
		listTableB.getColumns().addAll(col);
		col = new TableColumn("Kode");
		col.setPrefWidth(160.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new SimpleStringProperty(param.getValue().getAwbData());
					}
				});
		listTableB.getColumns().addAll(col);

		col = new TableColumn("Tujuan");
		col.setPrefWidth(210.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new SimpleStringProperty(param.getValue().getTujuan());
					}
				});
		listTableB.getColumns().addAll(col);

		col = new TableColumn("Berat");
		col.setPrefWidth(110.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<EntryDataShowVO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<EntryDataShowVO, String> param) {
						return new SimpleStringProperty(param.getValue().getBpFinal());
					}
				});
		listTableB.getColumns().addAll(col);

		col = new TableColumn("Action");
		col.setPrefWidth(83.0);
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
				return new ButtonCell(listTableB);
			}

		});

		listTableB.getColumns().add(col);

		listTableB.setItems(olDokumen);
		if (listEntry.size() != 0) {
			btn_print.setDisable(false);
		} else {
			btn_print.setDisable(true);
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

	private class ButtonCellWaitingPending extends TableCell<Record, Boolean> {

		TableView tab;

		ButtonCellWaitingPending(final TableView tblView) {
			this.tab = tblView;
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				final HBox hbox = new HBox(5);
				Button button = new Button("Pending");
				// final TableCell<String> c = this;
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						int selectdIndex = getTableRow().getIndex();
						EntryDataShowVO a = (EntryDataShowVO) tab.getItems().get(selectdIndex);
						int[] dataButtonMessageBox = new int[2];
						dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
						dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
						int hasilMessageBox = MessageBox.confirm("Apakah Yakin akan Melakukan Pending?",
								dataButtonMessageBox);
						if (hasilMessageBox == 5) { // cancel

						} else if (hasilMessageBox == 6) {
							GabungPaketService.updatePending(a.getAwbData(), 2);
							sohwTableA();
						}
					}
				});
				hbox.getChildren().add(button);
				Button but = new Button("Waiting");
				but.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						int selectdIndex = getTableRow().getIndex();
						EntryDataShowVO a = (EntryDataShowVO) tab.getItems().get(selectdIndex);
						int[] dataButtonMessageBox = new int[2];
						dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
						dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
						int hasilMessageBox = MessageBox.confirm("Apakah Yakin akan Melakukan Pending?",
								dataButtonMessageBox);
						if (hasilMessageBox == 5) { // cancel
							backDtoListener();
						} else if (hasilMessageBox == 6) {
							GabungPaketService.updatePending(a.getAwbData(), 1);
							sohwTableA();
						}
					}
				});
				hbox.getChildren().add(but);
				setGraphic(hbox);
			}
		}

	}

	private class ButtonCell extends TableCell<Record, Boolean> {

		final Button cellButton = new Button("Delete");

		ButtonCell(final TableView tblView) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					int selectdIndex = getTableRow().getIndex();
					EntryDataShowVO a = (EntryDataShowVO) tblView.getItems().get(selectdIndex);
					GabungPaketService.deleteSesudahGabung(a.getAwbData());
					int totalBerat = Integer.parseInt(txt_total_berat.getText());
					int BpFinalHapus = Integer.parseInt(a.getBpFinal());
					int totalHapus = totalBerat - BpFinalHapus;
					txt_total_berat.setText(String.valueOf(totalHapus));
					sohwTableA();
					sohwTableB();
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

	@FXML
	public void onClickPrint() {
		List<TtGabungSementara> listDataGabungSession = GabungPaketService
				.getDataGabungPaketSession(txt_resi_kardus.getText());
		List<TtGabungPaket> gabungPaketSave = new ArrayList<TtGabungPaket>();
		System.out.println("listDataGabungSession : " + listDataGabungSession.size());
		for (TtGabungSementara dat : listDataGabungSession) {
			dat.print();
			TtGabungPaket gabPaket = new TtGabungPaket();
			gabPaket.setId(dat.getId());
			gabPaket.setIdKardus(dat.getIdKardus());
			gabPaket.setIdKardusSub(dat.getIdKardus());
			gabPaket.setAwb(dat.getAwb());
			gabPaket.setKodePerwakilan(dat.getTujuan());
			gabPaket.setOleh(uLogin.getNamaUser());
//			gabPaket.setTglCreate(DateUtil.getNow());
//			gabPaket.setTglCreate(DateUtil.fotoTimbangDateGenerateRule(new Date()));
			gabPaket.setTglCreate(dat.getTglCreate()); 
			GabungPaketService.updateSesudahGabung(1, DateUtil.getNow(), dat.getAwb(), dat.getIdKardus());
			gabungPaketSave.add(gabPaket);

		}

		GabungPaketService.saveGabungSessToGabungPaket(gabungPaketSave);
		// ---------------------------------------------------PRINT
		GabungPaketResi print = new GabungPaketResi();
		print.printConfig(txt_resi_kardus.getText(), uLogin.getNamaUser());
		int sum = 0;
		double sumBulat = 0.0;
//		GabungPaketService serv = new GabungPaketService();
		for (EntryDataShowVO en : listEntry) {
			sum += Integer.parseInt(en.getBpFinal());
			sumBulat += Double.parseDouble(en.getbFinal());
		}
		int Koli = listEntry.size();
		GabungPaketBarcode gab = new GabungPaketBarcode(txt_resi_kardus.getText(), String.valueOf(sumBulat),
				String.valueOf(sum), uLogin.getNamaUser(), Koli);
		gab.print();
		btn_print.setDisable(true);
		clear();

	}

	@FXML
	public void onClickPrintUlang() {
		GabungPaketResi print = new GabungPaketResi();
		int sum = 0;
		double sumBulat = 0.0;
		if (txtIdResiPrintUlang.getText().isEmpty()) {
			String noResiTerakhir = GabungPaketService.noResiTerakhir();
			print.printConfig(noResiTerakhir, uLogin.getNamaUser());
			List<EntryDataShowVO> listPrintUlang = GabungPaketService.getGabungPaketPrintUlang(noResiTerakhir);
			for (EntryDataShowVO en : listPrintUlang) {
				sum += Integer.parseInt(en.getBpFinal());
				sumBulat += Double.parseDouble(en.getbFinal());
			}
			int Koli = listPrintUlang.size();
			GabungPaketBarcode gab = new GabungPaketBarcode(noResiTerakhir, String.valueOf(sumBulat),
					String.valueOf(sum), uLogin.getNamaUser(), Koli);
			gab.print();
			clear();
		} else {
			List<EntryDataShowVO> listPrintUlang = GabungPaketService
					.getGabungPaketPrintUlang(txtIdResiPrintUlang.getText());
			if (listPrintUlang.size() != 0) {
				print.printConfig(txtIdResiPrintUlang.getText(), uLogin.getNamaUser());
				for (EntryDataShowVO en : listPrintUlang) {
					sum += Integer.parseInt(en.getBpFinal());
					sumBulat += Double.parseDouble(en.getbFinal());
				}
				int Koli = listPrintUlang.size();
				GabungPaketBarcode gab = new GabungPaketBarcode(txtIdResiPrintUlang.getText(), String.valueOf(sumBulat),
						String.valueOf(sum), uLogin.getNamaUser(), Koli);
				gab.print();
				clear();
			} else {
				MessageBox.alert("No Resi yang di masukan tidak terdaftar");
			}
		}
	}

	public void audioShow() {
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(GabungPaketController.class.getResource("salah.wav"));
			Clip clip;
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		}
	}

	public void clear() {
		txt_kd_keranjang.setText("");
		txt_resi_kardus.setText("");
		txt_resi_barang.setText("");
		txt_total_berat.setText("");
		txt_berat_all.setText("");
		listTableA.getColumns().clear();
		listTableB.getColumns().clear();
		txtIdResiPrintUlang.setText("");
	}
}
