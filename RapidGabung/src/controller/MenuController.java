package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.TrUser;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import util.DtoListener;
import util.ManagedFormHelper;
import util.WindowsHelper;

public class MenuController implements Initializable {

	@FXML
	AnchorPane mnMasterUser, mnMasterCabang, mnMasterPelanggan, mnMasterHarga, mnMasterKurir, mnMasterZona,
			mnFotoTimbang, mnBrowseDataEntry, mnScanKuirIn, mnScanKuirOut, mnGabungPaket, mnMapingResi,
			mnBrowseSemuaData, mnBarangKembali, mnKpiCabangKurir, mnLapPelanggan,
			mnLapPerperwakilan, mnLapResi, mnSync, mnPenandaLunas, mnBrowseDelete, mnHistoryAwb, mnTerimaDiperwakilan,
			mnLaporanPod, mnLaporanPenerima,mnLayananPelanggan,mnLaporanPerKecamatan, mnMasterPickup,mnNewsLetter,mnJadwalPickup;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		setViewMenu();
		setListenerEnterUser();
		setListenerEnterCabang();
		setListenerEnterPelanggan();
		setListenerEnterHarga();
		setListenerEnterKurir();
		setListenerEnterZona();
		setListenerEnterFotoTimbang();
		ManagedFormHelper.instanceController = this;
	}

	private void setViewMenu() {
		mnMasterUser.setDisable(true);
		mnMasterCabang.setDisable(true);
		mnMasterPelanggan.setDisable(true);
		mnMasterHarga.setDisable(true);
		mnMasterKurir.setDisable(true);
		mnMasterZona.setDisable(true);
		mnFotoTimbang.setDisable(true);
		mnBrowseDataEntry.setDisable(true);
		mnScanKuirIn.setDisable(true);
		mnScanKuirOut.setDisable(true);
		mnGabungPaket.setDisable(true);
		mnMapingResi.setDisable(true);
		mnBrowseSemuaData.setDisable(true);
		mnBarangKembali.setDisable(true);
		mnKpiCabangKurir.setDisable(true);
		mnLapPelanggan.setDisable(true);
		mnLapPerperwakilan.setDisable(true);
		mnLapResi.setDisable(true);		

		TrUser uLogin = LoginController.getUserLogin();
		if (uLogin.getIdRole() == 4) {
			mnMasterUser.setDisable(false);
			mnMasterCabang.setDisable(false);
			mnMasterPelanggan.setDisable(false);
			mnMasterHarga.setDisable(false);
			mnMasterKurir.setDisable(false);
			mnMasterZona.setDisable(false);
			mnLapPelanggan.setDisable(false);
			mnLapPerperwakilan.setDisable(false);
			mnLapResi.setDisable(false);

		} else if (uLogin.getIdRole() == 1) {
			mnFotoTimbang.setDisable(false);
			mnBrowseSemuaData.setDisable(false);

		} else if (uLogin.getIdRole() == 2) {
			mnBrowseSemuaData.setDisable(false);
			mnBrowseDataEntry.setDisable(false);

		} else if (uLogin.getIdRole() == 3) {
			mnScanKuirIn.setDisable(false);
			mnScanKuirOut.setDisable(false);
			mnGabungPaket.setDisable(false);
		} else if (uLogin.getIdRole() == 5) {
			mnMasterPelanggan.setDisable(false);
			mnLapPelanggan.setDisable(false);
			mnLapResi.setDisable(false);
		} else if (uLogin.getIdRole() == 6) {
			mnMasterHarga.setDisable(false);
			mnLapPelanggan.setDisable(false);
			mnLapPerperwakilan.setDisable(false);
			mnLapResi.setDisable(false);
			mnKpiCabangKurir.setDisable(false);
		} else if (uLogin.getIdRole() == 7) {
			mnMasterCabang.setDisable(false);
			mnMasterPelanggan.setDisable(false);
			mnMasterHarga.setDisable(false);
			mnMasterKurir.setDisable(false);
			mnMasterZona.setDisable(false);
			mnLapPelanggan.setDisable(false);
			mnLapPerperwakilan.setDisable(false);
			mnLapResi.setDisable(false);
			mnKpiCabangKurir.setDisable(false);
		} else if (uLogin.getIdRole() == 0) {
			mnMasterUser.setDisable(false);
			mnMasterCabang.setDisable(false);
			mnMasterPelanggan.setDisable(false);
			mnMasterHarga.setDisable(false);
			mnMasterKurir.setDisable(false);
			mnMasterZona.setDisable(false);
			mnFotoTimbang.setDisable(false);
			mnBrowseDataEntry.setDisable(false);
			mnScanKuirIn.setDisable(false);
			mnScanKuirOut.setDisable(false);
			mnGabungPaket.setDisable(false);
			mnMapingResi.setDisable(false);
			mnBrowseSemuaData.setDisable(false);
			mnBarangKembali.setDisable(false);
			mnKpiCabangKurir.setDisable(false);
			mnLapPelanggan.setDisable(false);
			mnLapPerperwakilan.setDisable(false);
			mnLapResi.setDisable(false);
		}

		if (!uLogin.getKodeCabang().equalsIgnoreCase("CGK001")) {
			mnMasterUser.setDisable(true);
			mnMasterCabang.setDisable(false);
			mnMasterPelanggan.setDisable(false);
			mnMasterHarga.setDisable(true);
			mnMasterKurir.setDisable(false);
			mnMasterZona.setDisable(true);
			mnPenandaLunas.setDisable(true);
		}
	}

	@FXML
	public void onMasterUser() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "masterUser";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("MasterUser.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	@FXML
	public void onMnMasterPickup() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "masterPickup";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("MasterPickup.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	@FXML
	public void onJadwalPickup() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "jadwalPickup";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("JadwalPickup.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	@FXML
	public void onMnNewsLetter() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "newsLetter";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("NewsLetter.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onMasterCabang() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "masterCabang";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("MasterCabang.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onMasterPelanggan() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "masterPelanggan";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("MasterPelanggan.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onMnSync() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "mnSync";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("SyncData.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onMasterHarga() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "masterHarga";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("MasterHarga.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onMasteKurir() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "masterKurir";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("MasterKurir.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onMasterZona() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "masterZona";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("MasterZona.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// #onFoto&Timbang
	@FXML
	public void onFotoTimbang() {
		try {
			ManagedFormHelper.kodeDokumenPabean = "FotoTimbang";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("FotoTimbang.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onBrowseDataEntry(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "browseDataEntry";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("BrowseDataEntry.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onScanKurirIn(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "ScanKurirIn";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("ScanKurirIn.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onScanKurirOut(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "ScanKurirOut";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("ScanKurirOut.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onGabungPaket(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "GabPaket";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("GabungPaket.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onMappingResi(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "mappingResi";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("MappingResiJNE.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onBrowseSemuaData(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "browseSemuaData";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("BrowseSemuaData.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onBarangKembali(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "barangKembali";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("BarangKembali.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onKomplainPelanggan(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "komplainPelanggan";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("KomplainPelanggan.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onKpiCabangKurir(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "kpiCabangKurir";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("KpiCabangKurir.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onLapPelanggan(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "LapPelanggan";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("LapPerpelanggan.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onLapSales(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "LapSales";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("LapSales.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void setListenerEnterUser() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onMasterUser();
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnMasterUser.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterCabang() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onMasterCabang();
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnMasterCabang.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterPelanggan() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onMasterPelanggan();
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnMasterPelanggan.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterHarga() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onMasterHarga();
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnMasterHarga.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterKurir() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onMasteKurir();
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnMasterKurir.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterZona() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onMasterZona();
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnMasterZona.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterFotoTimbang() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onFotoTimbang();
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnFotoTimbang.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterBrowseDataEntry() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onBrowseDataEntry(event);
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnMasterZona.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterScanKurirIn() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onScanKurirIn(event);
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnScanKuirIn.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterScanKurirOUt() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onScanKurirOut(event);
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnScanKuirOut.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterGabungPaket() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onGabungPaket(event);
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnGabungPaket.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerEnterMapping() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onMappingResi(event);
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnMapingResi.setOnKeyPressed(eH);
	}

	@FXML
	public void setListenerBrowseDataSemua() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onBrowseSemuaData(event);
				}
			}
		};
		// txtIdUser.setOnKeyPressed(eH);
		mnBrowseSemuaData.setOnKeyPressed(eH);
	}

	@FXML
	public void onLapPerperwakilan(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "LapPerperwakilan";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("LapPerperwakilan.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@DtoListener(idDtoListener = "backTop")
	public void backDtoListener() throws IOException {
		Stage stage = (Stage) mnLapPerperwakilan.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void onLapResi(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "LapResi";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("LapResi.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onPenandaLunas(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "PenandaLunas";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("PenandaLunas2.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onBrwoseDelete(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "BrowseDelete";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("BrowseDelete.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onHistoryAwb(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "History AWB";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("AwbHistory.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	@FXML
	public void onLayananPelanggan(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "Layanan Pelanggan";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("BrowseLayananPelanggan.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	@FXML
	public void onGabungPaketHO(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "Gabung Paket HO";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("GabungPaketHO.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void onTerimaDiPerwakilan(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "Terima Di Perwakilan";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("TerimaPerwakilan.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	@FXML
	public void onLaporanPOD(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "Laporan POD";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("LapPOD.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

@FXML
	public void onLaporanPenerima(Event evt) {
		try {
			ManagedFormHelper.kodeDokumenPabean = "Laporan Penerima";
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("LapPenerima.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);
			// WindowsHelper.rootLayout.setCenter(null);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}

		} catch (IOException ex) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

@FXML
public void onLaporanPerKecamatan(Event evt) {
	try {
		ManagedFormHelper.kodeDokumenPabean = "Laporan Penerima";
		HBox bodyHBox = new HBox();
		FXMLLoader menu = new FXMLLoader(getClass().getResource("LapPerKecamatan.fxml"));
		ScrollPane menuPage = (ScrollPane) menu.load();
		bodyHBox.getChildren().add(menuPage);
		bodyHBox.setAlignment(Pos.CENTER);
		// WindowsHelper.rootLayout.setCenter(null);

		if (WindowsHelper.rootLayout != null) {
			WindowsHelper.rootLayout.setCenter(bodyHBox);
		}

	} catch (IOException ex) {
		Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
	}
}

}