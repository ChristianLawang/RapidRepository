package controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import VO.TimbangVO;
import edsdk.utils.CanonCamera;
import entity.TrPelanggan;
import entity.TrUser;
import entity.TtDataEntry;
import entity.TtHeader;
import entity.TtPotoTimbang;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.util.MessageUtil;
import service.DataEntryService;
import service.HeaderService;
import service.PelangganService;
import service.PotoTimbangService;
import util.BigDecimalUtil;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.PerhitunganBiaya;
import util.PrintUtilNew;
import util.PropertiesUtil;
import util.SequenceUtil;
import util.TempUtil;
import util.WindowsHelper;

public class FotoTimbangController implements Initializable {

	@FXML
	private SplitPane splitPaneFotoTimbang;
	@FXML
	private TextField txtAWB, txtNoPickup, txtKodeCustomer, txtJumlahBarang, txtPhotoPaket, txtBerat;
	@FXML
	private ImageView imgViewMainView, imgReview, imgLayanan;
	@FXML
	private ScrollPane scrollPaneImgPoints;
	@FXML
	private Button btnPhotoTerakhir, btnResiTerakhir, btnEditTerakhir, btnEditData, btnPrintResiTerakhir;
	@FXML
	private TextField txtPelangganPT, txtAWBPT, txtBeratPT, txtBerat2PT, txtPPT, txtLPT, txtTPT, txtVolumBeratPT;
	@FXML
	private Button btnFotoUlang, btnPrintResi, btnSave, btnBreak;
	@FXML
	private TextField txtPanjang, txtLebar, txtTinggi;
	@FXML
	private TextField txtVolume;
	@FXML
	private Button btnInputVolume;
	@FXML
	private Label lblLayanan;
	
	private Boolean isOne = false;

	CanonCamera slr = new CanonCamera();
	// Parent root;
	Map<String, Object> mapPelanggan = null;
	Integer numPaket = 0;
	String beratForm;
	private DoubleProperty zoom = new SimpleDoubleProperty(200);
	private int INT_THOUSAND = 6000;

	Map<String, Object> mapFormInfo = TempUtil.loadFotoTimbangLocal();
	private TrUser usr = LoginController.getUserLogin();

	public void initialize(URL url, ResourceBundle rb) {
		// Set Objek kelas ini
		ManagedFormHelper.instanceController = this;
		
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	txtAWB.requestFocus();
	        }
	    });
		
		sessionCamera();

		this.mapPelanggan = this.isFinnishPrevJob();
		if (mapPelanggan == null) {

			this.tampilkanPilihPelanggan();

		}

		splitPaneFotoTimbang.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					isOne=false;
					Image img = null;
					imgLayanan.setImage(img);
					lblLayanan.setText("Regular");
					captureCoy();
				}
				if (ke.getCode().equals(KeyCode.ESCAPE)){
					isOne=false;
					Image img = null;
					imgLayanan.setImage(img);
					lblLayanan.setText("Regular");
					breakCoy();		
				}
				if (ke.getCode().equals(KeyCode.F1)){
					if(!txtAWB.getText().equals("")){
						if(isOne){
							isOne=true;
							lblLayanan.setText("One Days Services");
							Image img = new Image(getClass().getClassLoader().getResourceAsStream("css/images/1dayservice.png"));
							imgLayanan.setImage(img);				
					        PotoTimbangService.updateLayanan(txtAWB.getText(), "ONE");
						}else{
							isOne=true;
							lblLayanan.setText("Regular");
							Image img = new Image("");
							imgLayanan.setImage(img);				
					        PotoTimbangService.updateLayanan(txtAWB.getText(), "REG");
						}
					}else{
						MessageBox.alert("Sebelum menentukan layanan, silahkan lakukan fototimbang terlebih dahulu");
					}
				}
			}
		});
		
		btnBreak = this.initBtnBreak(btnBreak);
		btnInputVolume = this.initBtnInputVolume(btnInputVolume);
		
		btnPhotoTerakhir = this.initBtnPhotoTerakhir(btnPhotoTerakhir);
		btnResiTerakhir = this.initBtnPhotoTerakhir(btnResiTerakhir);
		btnEditTerakhir = this.initBtnPhotoTerakhir(btnEditTerakhir);

		btnEditData = this.initBtnEditData(btnEditData);
		
		txtPanjang = this.initTextFieldHitungVolume(txtPanjang);
		txtLebar = this.initTextFieldHitungVolume(txtLebar);
		txtTinggi = this.initTextFieldHitungVolume(txtTinggi);
	}

	private Button initBtnInputVolume(Button btn) {
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!txtVolume.getText().equals("")){
					isOne=false;
					Image img = null;
					imgLayanan.setImage(img);
					lblLayanan.setText("Regular");
					
					captureVolume();
					TtPotoTimbang _pp = PotoTimbangService.getPotoPaketByAWB(txtAWB.getText());
					
					BigDecimal bgBerat = new BigDecimal(txtBerat.getText());
					BigDecimal bgVolume = new BigDecimal(txtVolume.getText());
	
					if (bgVolume.compareTo(bgBerat) > 0) {
						_pp.setVol("1");
						_pp.setBclose(bgVolume.toString());
						BigDecimalUtil bdu = new BigDecimalUtil();
						String bgRound = bdu.getBigDecimalToStringRound(bgVolume);
						_pp.setBpclose(bgRound);
					
						PrintUtilNew PU = new PrintUtilNew(
								txtAWB.getText(), bgVolume.toString(), 
								Integer.parseInt(bgRound), 
								usr.getIdUser());
						PU.print();
					} else {
						_pp.setVol("0");
						_pp.setBclose(bgBerat.toString());
						BigDecimalUtil bdu = new BigDecimalUtil();
						String bgRound = bdu.getBigDecimalToStringRound(bgBerat);
						_pp.setBpclose(bgRound);
						
						PrintUtilNew PU = new PrintUtilNew(
								txtAWB.getText(), bgBerat.toString(), 
								Integer.parseInt(bgRound), 
								usr.getIdUser());
						PU.print();
					}
					_pp.setTglUpdate(new Date());
					_pp.setBvol(bgVolume.toString());
					BigDecimalUtil bdu = new BigDecimalUtil();
					String bgRound = bdu.getBigDecimalToStringRound(bgVolume);
					_pp.setBpvol(bgRound.toString());
					TtDataEntry dPaket = DataEntryService.getDataPaketByIdDataPaket(_pp.getAwbData());
					dPaket.setBclose(_pp.getBclose());
					dPaket.setPbclose(_pp.getBpclose());
					PotoTimbangService.update(_pp);
					DataEntryService.update(dPaket);
				}else{
					MessageBox.alert("Silahkan input volume terlebih dahulu");
				}

			}
		});
		return btn;
	}	
	
	private void captureVolume() {
		Integer inputKe = (Integer) mapPelanggan.get("input ke");
		inputKe++;
		Integer jumBarang = Integer.parseInt((String) mapPelanggan.get("jumlah barang"));
		if (inputKe > jumBarang) {
			TempUtil.clearFotoTimbangLocal();
			int[] dataButtonMessageBox = new int[2];
			dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
			dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
			int hasilMessageBox = MessageBox.confirm("Foto Timbang Selesai, Apakah akan melakukan foto kembali?",
					dataButtonMessageBox);
			if (hasilMessageBox == 5) { // cancel
				TempUtil.clearFotoTimbangLocal();
//				tampilkanPilihPelanggan();
//				backDtoListener();				
				MenuController controller = new MenuController();
			    controller.onFotoTimbang();
			} else if (hasilMessageBox == 6) {
				mapPelanggan.put("input ke", inputKe+1);
				
				TimbangVO timbang = new TimbangVO();
				System.out.println("--> timbang : " + timbang.getGross());
				if(timbang.getGross().toString().equals("0.00")){
					MessageBox.alert("Tidak ada value pada timbangan, atau timbangan tidak terhubung pada komputer");
				}else{
					// untuk di lempar ke session kamera
					String strFolder = generateOrReplaceFolder();
					String strAWB = generateAWB();
					String strFileName = strAWB + ".png";
					TempUtil.saveFotoTimbangCamera(strAWB, strFolder, strFileName);
					
					// capture
					slr.shoot();
					
					Map<String, Object> mapCam = TempUtil.loadFotoTimbangCamera();				
					TtPotoTimbang potoPaket = transactionFotoTimbang(strAWB, mapPelanggan, mapCam, timbang, inputKe);
	
					PrintUtilNew PU = new PrintUtilNew(potoPaket.getAwbData(), timbang.getGross().toString(),
							Integer.parseInt(timbang.getGrossRoundUp().toString()), usr.getIdUser());
					PU.print();
					this.populateForm(mapPelanggan, mapCam);
				}
			}
		} else {
			TimbangVO timbang = new TimbangVO();
			
			System.out.println("--> timbang : " + timbang.getGross());
			if(timbang.getGross().toString().equals("0.00")){
				MessageBox.alert("Tidak ada value pada timbangan, atau timbangan tidak terhubung pada komputer");
			}else{
				// untuk di lempar ke session kamera
				String strFolder = generateOrReplaceFolder();
				String strAWB = generateAWB();
				String strFileName = strAWB + ".png";
				TempUtil.saveFotoTimbangCamera(strAWB, strFolder, strFileName);
				
				// capture
				slr.shoot();
				
				Map<String, Object> mapCam = TempUtil.loadFotoTimbangCamera();				
				TtPotoTimbang potoPaket = transactionFotoTimbang(strAWB, mapPelanggan, mapCam, timbang, inputKe);
	
//				PrintUtilNew PU = new PrintUtilNew(potoPaket.getAwbData(), timbang.getGross().toString(),
//						Integer.parseInt(timbang.getGrossRoundUp().toString()), usr.getIdUser());
//				PU.print();
				this.populateForm(mapPelanggan, mapCam);		}
			}
	}
	
	private TextField initTextFieldHitungVolume(TextField txt) {
		txt.textProperty().addListener((observable, oldValue, newValue) -> {
			BigDecimal p = new BigDecimal(txtPanjang.getText() + ".00");
			BigDecimal l = new BigDecimal(txtLebar.getText() + ".00");
			BigDecimal t = new BigDecimal(txtTinggi.getText() + ".00");

			BigDecimal result = p.multiply(l).multiply(t).divide(new BigDecimal(this.INT_THOUSAND), MathContext.DECIMAL128);
							
			txtVolume.setText(PerhitunganBiaya.getFixDecimal2Digit(result.toString()));
		});
		return txt;
	}
	
	private Button initBtnBreak(Button btn) {
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				breakCoy();
			}
		});
		return null;
	}
	
	public void sessionCamera() {
		try {
			slr.openSession();
		} catch (Exception e) {
			System.out.println("----------------------------Mwssage error : "+e.getMessage());
			CanonCamera slr = new CanonCamera();
			slr.openSession();
		}
	}

	private Button initBtnEditData(Button btn) {
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mapFormInfo = new HashMap<String, Object>();

				mapFormInfo.put("pp", null);
				mapFormInfo.put("tp", null);
				mapFormInfo.put("type", 1); // 1 artinya, edit terakhir

				handleButtonForShowPopupVolume();
			}
		});
		return btn;
	}

	private Button initBtnPhotoTerakhir(Button btn) {
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(txtAWB.getText().equals("")){
					MessageBox.alert("Belum ada paket yang dilakukan foto dan timbang");
				}else{
					mapFormInfo = new HashMap<String, Object>();
					
					mapFormInfo.put("awb", txtAWB.getText());
					mapFormInfo.put("pp", PotoTimbangService.getPotoPaketByAWB(txtAWB.getText()));
					mapFormInfo.put("tp", (TrPelanggan) mapPelanggan.get("pelanggan object"));
					mapFormInfo.put("berat", txtBerat.getText());
					mapFormInfo.put("type", 0); // 0 artinya, edit terakhir
					handleButtonForShowPopupVolume();
				}
			}
		});
		return btn;
	}

	protected void handleButtonForShowPopupVolume() {

		Stage stgPhotoTerakhir = new Stage();
		TtPotoTimbang pp = (TtPotoTimbang) mapFormInfo.get("pp");
		TrPelanggan tp = (TrPelanggan) mapFormInfo.get("tp");

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			AnchorPane anchorDialogPhotoTerakhir = (AnchorPane) loader
					.load(this.getClass().getResource("DialogPhotoTerakhir.fxml"));
			Scene scnPhotoTerakhir = new Scene(anchorDialogPhotoTerakhir);
			stgPhotoTerakhir.setScene(scnPhotoTerakhir);

			stgPhotoTerakhir.setTitle("Photo Terakhir");
			stgPhotoTerakhir.initModality(Modality.WINDOW_MODAL);
			stgPhotoTerakhir.initOwner(WindowsHelper.primaryStage);
			stgPhotoTerakhir.initStyle(StageStyle.UTILITY);
			stgPhotoTerakhir.show();

			// tarik semua node dari FXML
			txtPelangganPT = (TextField) scnPhotoTerakhir.lookup("#txtPelangganPT");
			txtAWBPT = (TextField) scnPhotoTerakhir.lookup("#txtAWBPT");
			txtBeratPT = (TextField) scnPhotoTerakhir.lookup("#txtBeratPT");
			txtBerat2PT = (TextField) scnPhotoTerakhir.lookup("#txtBerat2PT");
			txtPPT = (TextField) scnPhotoTerakhir.lookup("#txtPPT");
			txtLPT = (TextField) scnPhotoTerakhir.lookup("#txtLPT");
			txtTPT = (TextField) scnPhotoTerakhir.lookup("#txtTPT");
			txtVolumBeratPT = (TextField) scnPhotoTerakhir.lookup("#txtVolumBeratPT");
			imgReview = (ImageView) scnPhotoTerakhir.lookup("#imgReview");
			
			Integer type = (Integer) mapFormInfo.get("type");
			txtAWBPT.setEditable(true);
			if (type == 1) {
				
				txtAWBPT.textProperty().addListener((observable, oldValue, newValue) -> {
					txtPelangganPT.setText("");
					txtBeratPT.setText("");
					txtBerat2PT.setText("");

					txtPPT.setText("0");
					txtLPT.setText("0");
					txtTPT.setText("0");

					txtVolumBeratPT.setText("0.00");
					String strAWB = "";
					if (newValue != null) {
						if (newValue.length() == 16) {
							strAWB = newValue;
							TtPotoTimbang _pp = PotoTimbangService.getPotoPaketByAWB(strAWB);
							TtHeader _dPoto = HeaderService.getDataPotoByPotoPaketID(_pp.getAwbPotoTimbang());
							TrPelanggan _tp = PelangganService.getPelangganById(_pp.getKodePelanggan());

							txtPelangganPT.setText(_tp.getNamaAkun());
							txtBeratPT.setText(_pp.getBclose());
							txtBerat2PT.setText(_pp.getBpclose());
							
							String path = _pp.getGambar();
							
							System.out.println("-> path : " + path);
							File file = new File(path);
							Image image = new Image(file.toURI().toString());
							imgReview.setImage(image);
							imgReview.setRotate(180);

							txtPPT.setText("0");
							txtLPT.setText("0");
							txtTPT.setText("0");

							txtVolumBeratPT.setText("0.00");
							txtAWBPT.requestFocus();
							txtAWBPT.selectAll();
							
//							txtAWBPT.setEditable(false);
						}else if(newValue.length() == 32){
							txtAWBPT.setText(newValue.substring(16, 32));
							
							strAWB = newValue.substring(16, 32);
							
							strAWB = newValue;
							TtPotoTimbang _pp = PotoTimbangService.getPotoPaketByAWB(strAWB);
							TtHeader _dPoto = HeaderService.getDataPotoByPotoPaketID(_pp.getAwbPotoTimbang());
							TrPelanggan _tp = PelangganService.getPelangganById(_pp.getKodePelanggan());

							txtPelangganPT.setText(_tp.getNamaAkun());
							txtBeratPT.setText(_pp.getBclose());
							txtBerat2PT.setText(_pp.getBpclose());
							
							String path = _pp.getGambar();
							
							System.out.println("-> path : " + path);
							File file = new File(path);
							Image image = new Image(file.toURI().toString());
							imgReview.setImage(image);
							imgReview.setRotate(180);

							txtPPT.setText("0");
							txtLPT.setText("0");
							txtTPT.setText("0");

							txtVolumBeratPT.setText("0.00");
							txtAWBPT.requestFocus();
							txtAWBPT.selectAll();
														
						}
					}
				});
			}

			btnFotoUlang = (Button) scnPhotoTerakhir.lookup("#btnFotoUlang");
			btnPrintResi = (Button) scnPhotoTerakhir.lookup("#btnPrintResi");
			btnSave = (Button) scnPhotoTerakhir.lookup("#btnSave");

			btnFotoUlang.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					
					// untuk di lempar ke session kamera
					String strFolder = generateOrReplaceFolder();
					String strAWB = txtAWBPT.getText();
					String strFileName = strAWB + ".png";
					TempUtil.saveFotoTimbangCamera(strAWB, strFolder, strFileName);
					slr.shoot();
					
					String path = strFolder+"/"+strFileName;
					
					System.out.println("-> path : " + path);
					
					File file = new File(path);
					Image image = new Image(file.toURI().toString());
					imgReview.setImage(image);
					imgReview.setRotate(180);
				}

			});

			btnPrintResi.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					String strAWB = txtAWBPT.getText();
					String strGross = txtBeratPT.getText();
					String strGrossRoundUp = txtBerat2PT.getText();
					
					PrintUtilNew PU = new PrintUtilNew(
							strAWB, strGross, 
							Integer.parseInt(strGrossRoundUp), 
							usr.getIdUser());
					PU.print();

				}
			});

			btnSave.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					TtPotoTimbang _pp = PotoTimbangService.getPotoPaketByAWB(txtAWBPT.getText());

					BigDecimal bgBerat = new BigDecimal(txtBeratPT.getText());
					BigDecimal bgVolume = new BigDecimal(txtVolumBeratPT.getText());

					if (bgVolume.compareTo(bgBerat) > 0) {
//						_pp.setTimbang("0");
						_pp.setVol("1");
						_pp.setBclose(bgVolume.toString());
						BigDecimalUtil bdu = new BigDecimalUtil();
						String bgRound = bdu.getBigDecimalToStringRound(bgVolume);
						_pp.setBpclose(bgRound);
					} else {
//						_pp.setTimbang("1");
						_pp.setVol("0");
						_pp.setBclose(bgBerat.toString());
						BigDecimalUtil bdu = new BigDecimalUtil();
						String bgRound = bdu.getBigDecimalToStringRound(bgBerat);
						_pp.setBpclose(bgRound);
					}
					_pp.setTglUpdate(new Date());
					_pp.setBvol(bgVolume.toString());					
//					BigDecimalUtil bdu = new BigDecimalUtil();
//					String bgRound = bdu.getBigDecimalToStringRound(bgVolume);
//					_pp.setBpvolume(bgRound.toString());
					TtDataEntry dPaket = DataEntryService.getDataPaketByIdDataPaket(_pp.getAwbPotoTimbang());
					dPaket.setBclose(_pp.getBclose());
					dPaket.setPbclose(_pp.getBpclose());
					PotoTimbangService.update(_pp);
					DataEntryService.update(dPaket);
					stgPhotoTerakhir.close();
				}

			});

			txtPelangganPT.setText(tp == null ? "" : tp.getNamaPemilik());
			txtAWBPT.setText((String) mapFormInfo.get("awb"));
			System.out.println(mapFormInfo.get("berat"));
			txtBeratPT.setText(mapFormInfo.get("berat") == null ? "0" : (String) mapFormInfo.get("berat"));
			BigDecimalUtil bu = new BigDecimalUtil();
//			BigDecimal beratRoundUp = bu.getStringToBigDecimalRound(
//					mapFormInfo.get("berat") == null ? "0" : (String) mapFormInfo.get("berat"));
			String raw = mapFormInfo.get("berat") == null ? "0" : (String) mapFormInfo.get("berat");
			BigDecimal beratRoundUp = new BigDecimal(bu.getBigDecimalToStringRound(new BigDecimal(raw)));
			txtBerat2PT.setText(beratRoundUp.toString());
			
//			String path = (String) mapFormInfo.get("filename");
			
//			System.out.println("-> path : " + path);
			
//			File file = new File(path);
//			Image image = new Image(file.toURI().toString());
			
			Image img = imgViewMainView.getImage();
			if(img!=null){
				imgReview.setImage(img);
				imgReview.setRotate(180);
			}
			txtPPT.textProperty().addListener((observable, oldValue, newValue) -> {
				BigDecimal p = new BigDecimal(newValue + ".00");
				BigDecimal l = new BigDecimal(txtLPT.getText() + ".00");
				BigDecimal t = new BigDecimal(txtTPT.getText() + ".00");
				
				BigDecimal result = p.multiply(l).multiply(t).divide(new BigDecimal(this.INT_THOUSAND), MathContext.DECIMAL128);
				
				txtVolumBeratPT.setText(PerhitunganBiaya.getFixDecimal2Digit(result.toString()));
			});
			txtLPT.textProperty().addListener((observable, oldValue, newValue) -> {
				BigDecimal p = new BigDecimal(txtPPT.getText() + ".00");
				BigDecimal l = new BigDecimal(newValue + ".00");
				BigDecimal t = new BigDecimal(txtTPT.getText() + ".00");

				BigDecimal result = p.multiply(l).multiply(t).divide(new BigDecimal(this.INT_THOUSAND), MathContext.DECIMAL128);
								
				txtVolumBeratPT.setText(PerhitunganBiaya.getFixDecimal2Digit(result.toString()));
			});
			txtTPT.textProperty().addListener((observable, oldValue, newValue) -> {
				BigDecimal p = new BigDecimal(txtPPT.getText() + ".00");
				BigDecimal l = new BigDecimal(txtLPT.getText() + ".00");
				BigDecimal t = new BigDecimal(newValue + ".00");

				BigDecimal result = p.multiply(l).multiply(t).divide(new BigDecimal(this.INT_THOUSAND), MathContext.DECIMAL128);
				
				txtVolumBeratPT.setText(PerhitunganBiaya.getFixDecimal2Digit(result.toString()));
			});
			
			txtAWBPT.requestFocus();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void populateForm(Map<String, Object> map, Map<String, Object> mapCam) {
		TrPelanggan customer = (TrPelanggan) map.get("pelanggan object");
		txtAWB.setText((String) map.get("awb"));
		txtNoPickup.setText((String) map.get("no pickup"));
		txtKodeCustomer.setText(customer.getKodePelanggan());
		txtJumlahBarang.setText((String) map.get("jumlah barang"));
		numPaket = (Integer) map.get("input ke");
		txtPhotoPaket.setText(numPaket.toString());
		txtBerat.setText(beratForm);

		// this.paneImage = this.addSomething();

		String path = (String) mapCam.get("folder") + "/" + (String) mapCam.get("filename");
		System.out.println("--> path : " + path);
		File file = new File(path);
		Image image = new Image(file.toURI().toString());
		imgViewMainView.setImage(image);
		imgViewMainView.setRotate(180);
	}

	private Map<String, Object> isFinnishPrevJob() {
		return TempUtil.loadFotoTimbangLocal();
	}

	private void tampilkanPilihPelanggan() {
		Stage modalWindow = new Stage();
		Parent rootPilihPelanggan;
		try {
			rootPilihPelanggan = FXMLLoader
					.load(PilihPelangganSubController.class.getResource("dialogpilihpelanggan.fxml"));
			modalWindow.setScene(new Scene(rootPilihPelanggan));
			modalWindow.setTitle("Pilih Pelanggan");
			modalWindow.initModality(Modality.WINDOW_MODAL);
			modalWindow.initOwner(WindowsHelper.primaryStage);
			modalWindow.initStyle(StageStyle.UTILITY);
			modalWindow.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void manageSplitPaneFotoTimbang() {
		
	}

	private void breakCoy() {
		int[] dataButtonMessageBox = new int[2];
		dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
		dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
		
		int hasilMessageBox = MessageBox.confirm("Apakah anda yakin ingin menghentikan proses?",
				dataButtonMessageBox);
		if (hasilMessageBox == 6) { // cancel
			TempUtil.clearFotoTimbangLocal();
			
			MenuController controller = new MenuController();
		    controller.onFotoTimbang();
		} 		
	}
	
	public void captureCoy() {
		Integer inputKe = (Integer) mapPelanggan.get("input ke");
		inputKe++;
		Integer jumBarang = Integer.parseInt((String) mapPelanggan.get("jumlah barang"));
		if (inputKe > jumBarang) {
			TempUtil.clearFotoTimbangLocal();
			int[] dataButtonMessageBox = new int[2];
			dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
			dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
			int hasilMessageBox = MessageBox.confirm("Foto Timbang Selesai, Apakah akan melakukan foto kembali?",
					dataButtonMessageBox);
			if (hasilMessageBox == 5) { // cancel
				TempUtil.clearFotoTimbangLocal();
//				tampilkanPilihPelanggan();
//				backDtoListener();				
				MenuController controller = new MenuController();
			    controller.onFotoTimbang();
			} else if (hasilMessageBox == 6) {
				mapPelanggan.put("input ke", inputKe+1);
				
				TimbangVO timbang = new TimbangVO();
				System.out.println("--> timbang : " + timbang.getGross());
				if(timbang.getGross().toString().equals("0.00")){
					MessageBox.alert("Tidak ada value pada timbangan, atau timbangan tidak terhubung pada komputer");
				}else{
					// untuk di lempar ke session kamera
					String strFolder = generateOrReplaceFolder();
					String strAWB = generateAWB();
					String strFileName = strAWB + ".png";
					TempUtil.saveFotoTimbangCamera(strAWB, strFolder, strFileName);
					
					// capture
					slr.shoot();
					
					Map<String, Object> mapCam = TempUtil.loadFotoTimbangCamera();				
					TtPotoTimbang potoPaket = transactionFotoTimbang(strAWB, mapPelanggan, mapCam, timbang, inputKe);
	
					PrintUtilNew PU = new PrintUtilNew(potoPaket.getAwbData(), timbang.getGross().toString(),
							Integer.parseInt(timbang.getGrossRoundUp().toString()), usr.getIdUser());
					PU.print();
					this.populateForm(mapPelanggan, mapCam);
				}
			}
		} else {
			TimbangVO timbang = new TimbangVO();
			
			System.out.println("--> timbang : " + timbang.getGross());
			if(timbang.getGross().toString().equals("0.00")){
				MessageBox.alert("Tidak ada value pada timbangan, atau timbangan tidak terhubung pada komputer");
			}else{
				// untuk di lempar ke session kamera
				String strFolder = generateOrReplaceFolder();
				String strAWB = generateAWB();
				String strFileName = strAWB + ".png";
				TempUtil.saveFotoTimbangCamera(strAWB, strFolder, strFileName);
				
				// capture
				slr.shoot();
				
				Map<String, Object> mapCam = TempUtil.loadFotoTimbangCamera();				
				TtPotoTimbang potoPaket = transactionFotoTimbang(strAWB, mapPelanggan, mapCam, timbang, inputKe);
	
				PrintUtilNew PU = new PrintUtilNew(potoPaket.getAwbData(), timbang.getGross().toString(),
						Integer.parseInt(timbang.getGrossRoundUp().toString()), usr.getIdUser());
				PU.print();
				this.populateForm(mapPelanggan, mapCam);		}
			}
		}

	private TtPotoTimbang transactionFotoTimbang(
			String awb,
			Map<String, Object> mapPelanggan, 
			Map<String, Object> mapCam, 
			TimbangVO timbang, 
			Integer inputKe) {

		TrPelanggan trPelanggan = (TrPelanggan) mapPelanggan.get("pelanggan object");
		
		beratForm = timbang.getGross().toString();

//		String strPerwakilan = usr.getKodeCabang().substring(0, 3);		
//		String strKodePerwakilan = usr.getKodeCabang().substring(0, 6);
		
		String strPerwakilan = PropertiesUtil.getPerwakilan();
		String strKodePerwakilan = PropertiesUtil.getKodePerwakilan();
		
		String strAsalPaket = strPerwakilan + strKodePerwakilan;
		@SuppressWarnings("unused")
		TtHeader dPoto = insertDataPoto(awb);
		TtPotoTimbang potoPaket = insertPotoPaket(strAsalPaket, trPelanggan, mapCam, timbang);
		@SuppressWarnings("unused")
		TtDataEntry dataPaket = insertDataPaket(awb, strPerwakilan, strAsalPaket, trPelanggan, timbang);
	
		PotoTimbangService.insertFotoTimbangTransaction(dataPaket, potoPaket, dPoto);
			
		mapPelanggan.put("awb", potoPaket.getAwbData());
		mapPelanggan.put("input ke", inputKe);

		TempUtil.saveFotoTimbangLocal(mapPelanggan);
		return potoPaket; 
	}

	private static String generateOrReplaceFolder() {
//		String strDrive = "Z:";
//		String strFolder = "SLRImages";
		
		String strDrive = PropertiesUtil.getImageDriveLocation();
		String strFolder = PropertiesUtil.getImageFolderLocation();
		
		String strYear = DateUtil.getYearOnly(new Date());
		String strMonth = DateUtil.getMonthOnly(new Date());
		String strDay = DateUtil.getDayOnly(new Date());
		
		String path = strDrive + "/" + strFolder + "/" + strYear + "/" + strMonth + "/" + strDay;
		
		System.out.println("--> folder : " + path);
		File dir = new File(path);
		dir.mkdirs();
		
		return path;
	}
	
	private static String generateAWB() {
//		String strFileName = SequenceUtil.getNewAWBID(LoginController.getUserLogin().getKodeCabang().substring(0, 3), LoginController.getUserLogin().getKodeCabang().substring(3, 6));
		SequenceUtil su = new SequenceUtil();
		
		return su.getNewAWBID();
	}
	
	private TtDataEntry insertDataPaket(String awb, String strPerwakilan, String strAsalPaket, TrPelanggan trPelanggan,
			TimbangVO timbang) {
		TtDataEntry dataPaket = new TtDataEntry();
		 dataPaket.setAwbDataEntry(awb);
		dataPaket.setTglCreate(DateUtil.fotoTimbangDateGenerateRule(new Date()));
		dataPaket.setFlagEntry(0);
		// ToDo
		dataPaket.setPengirim(trPelanggan.getNamaAkun());
		dataPaket.setJneFlag(0);
		System.out.println("---------------Timbangan asli  : " + timbang.getGross().toString());
		System.out.println("---------------Timbangan Bulat  : " + timbang.getGrossRoundUp().toString());
		dataPaket.setBclose(timbang.getGross().toString());
		dataPaket.setPbclose(String.valueOf(timbang.getGrossRoundUp().toString()));
//		dataPaket.setKodePerwakilan(strPerwakilan);		
		dataPaket.setAsalPaket(strAsalPaket);

//		PotoPaketService.save(dataPaket);

		return dataPaket;
	}
	
	private TtPotoTimbang insertPotoPaket(String strAsalPaket, TrPelanggan trPelanggan, Map<String, Object> mapCam,
			TimbangVO timbang) {
		String idAWB = (String) mapCam.get("awb");

		System.out.println(idAWB);
		TtPotoTimbang potoPaket = new TtPotoTimbang();
		potoPaket.setAwbPotoTimbang(idAWB.toUpperCase());
		potoPaket.setAwbData(idAWB.toUpperCase());
		String pathImage = (String) mapCam.get("folder") + "/" + (String) mapCam.get("filename");
		potoPaket.setGambar(pathImage);
//		potoPaket.setAwbPotoTimbang((String) mapPelanggan.get("poto paket"));
		potoPaket.setAsalPaket(strAsalPaket);
		potoPaket.setKodePickup((String) mapPelanggan.get("no pickup"));
		potoPaket.setKodePelanggan(trPelanggan.getNamaAkun());
		// nyusul
		potoPaket.setVol("0");
		potoPaket.setBvol("0");
		potoPaket.setBpvol("0");
		
		potoPaket.setTimb(timbang.getGross().toString());
		potoPaket.setBerattimb(timbang.getGrossRoundUp().toString());
		potoPaket.setBclose(timbang.getGross().toString());
		potoPaket.setBpclose(timbang.getGrossRoundUp().toString());
//		potoPaket.setLayanan("REG");
//		potoPaket.setKodePerwakilan(usr.getKodeCabang().substring(0, 3));
		potoPaket.setJneFlag(0);
		potoPaket.setTglGambar(new Date());
		potoPaket.setKoli(0);
		potoPaket.setTglCreate(DateUtil.fotoTimbangDateGenerateRule(new Date()));
		potoPaket.setFlag(0);
//		DataPaketService.save(potoPaket);

		return potoPaket;
	}

	private TtHeader insertDataPoto(String awb) {
		TtHeader dPoto = new TtHeader();
		dPoto.setAwbHeader(awb);
//		dPoto.setIdDataPaket(SequenceUtil.getNewDataPaketID(strPerwakilan, strKodePerwakilan));
//		dPoto.setIdPotoPaket(SequenceUtil.getNewDataPaketID(strPerwakilan, strKodePerwakilan));
		dPoto.setTglCreate(DateUtil.fotoTimbangDateGenerateRule(new Date()));
		dPoto.setWaitingPendingFlag(0);
		dPoto.setUserCreate(usr.getIdUser());
		dPoto.setFlag(0);
		dPoto.setGabungPaketFlag(0);

		//DataPotoService.save(dPoto);

		return dPoto;
	}

	@DtoListener(idDtoListener = "backTop")
	public void backDtoListener() {
		slr.closeSession();
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
