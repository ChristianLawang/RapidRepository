package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.WordUtils;

import VO.EntryDataShowVO;
import VO.ServicePerwakilanVO;
import entity.TrUser;
import entity.TtDataEntry;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import popup.PopUpTujuanController;
import service.DataPaketService;
import service.UserService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.PerhitunganBiaya;
import util.WindowsHelper;
import utilfx.Tanggalan;
import utilfx.Uangteks;

public class DataEntryController implements Initializable {

	@FXML
	private AnchorPane anchor_pane_gambar;
	@FXML
	private ImageView imgViewMainView, imgOne, imgJne, imgPending, imgFree;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private TextField txt_awb, txt_kode_pickup, txt_pengirim, txt_reseller, txt_penerima, txt_no_hp, txt_kode_tujuan,
			txt_kode_perwakilan, txt_berat, txt_berat_bulat, txt_admin_input, txt_asal_paket;

	@FXML
	private Uangteks txt_harga, txt_harga_per_kg, txt_asuransi, txt_total_biaya;

	@FXML
	Tanggalan dp_tanggal_transaksi;

	@FXML
	private TextField txt_Ket;

	@FXML
	Label lbl_kettujuan;

	@FXML
	Pane pane;
	Image image;
	
	@FXML
	private Button btn_simpan, btn_batal, btn_cari, btn_Skip;
	final int MIN_PIXELS = 10;

	private int no;
	private int noLoop = 1;
	private String idDataPaket;
	private List<EntryDataShowVO> listEntryAll = new ArrayList<EntryDataShowVO>();
	public BrowseDataEntryController browseCntrl;
	

	// -----------------------------------------------------------------------------
	private Boolean one = false;
	private Boolean jne = false;
	private Boolean pending = false;
	private Boolean free = false;

	private int hargaOne = 0;
	private int hargaReg = 0;
	private String kdPerwakilanReg;
	private String kdPerwakilanOne;
	// -----------------------------------------------------------------------------

	public TrUser uLogin = LoginController.getUserLogin();
	final DoubleProperty zoomProperty = new SimpleDoubleProperty(800);
	double width;
	double height;
	
	//FA
	private int countUp = 0;

	public void initialize(URL url, ResourceBundle rb) {
		ManagedFormHelper.instanceController = this;
		setKodeTujuanListener();
		setListenerEnterSave();
		setListenerLayanan();

		txt_harga = (Uangteks) this.initTextFieldTxtHarga(txt_harga);

		txt_no_hp.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-?#%&*()@$^-_+!.,"
						.contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				}
				if (keyEvent.getCode() == KeyCode.SPACE) {
					keyEvent.consume();
				}

				hendleImage(keyEvent);
			}
		});
		txt_harga.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					setTextAsuransi();
					setTotalBiaya();
				}
			}
		});
		
		// ----------------------------------------------------------------------
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txt_reseller.requestFocus();
			}
		});
		
		txt_reseller.textProperty().addListener((ov, oldValue, newValue) -> {//TODO LIST
			txt_reseller.setText(WordUtils.capitalize(newValue));
			
		});
		
		txt_penerima.textProperty().addListener((ov, oldValue, newValue) -> {//TODO LIST
			txt_penerima.setText(WordUtils.capitalize(newValue));
			
		});

		imgViewMainView.setPreserveRatio(true);
		// reset(imageTest, width / 2, height / 2);
		ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

		imgViewMainView.setOnMousePressed(e -> {

			Point2D mousePress = imageViewToImage(imgViewMainView, new Point2D(e.getX(), e.getY()));
			mouseDown.set(mousePress);
		});

		imgViewMainView.setOnMouseDragged(e -> {
			Point2D dragPoint = imageViewToImage(imgViewMainView, new Point2D(e.getX(), e.getY()));
			shift(imgViewMainView, dragPoint.subtract(mouseDown.get()));
			mouseDown.set(imageViewToImage(imgViewMainView, new Point2D(e.getX(), e.getY())));
		});

		imgViewMainView.setOnScroll(e -> {
			double delta = e.getDeltaY();
			Rectangle2D viewport = imgViewMainView.getViewport();

			double scale = clamp(Math.pow(1.01, delta),

			Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

			// don't scale so that we're bigger than image dimensions:
					Math.max(width / viewport.getWidth(), height / viewport.getHeight())

			);

			Point2D mouse = imageViewToImage(imgViewMainView, new Point2D(e.getX(), e.getY()));

			double newWidth = viewport.getWidth() * scale;
			double newHeight = viewport.getHeight() * scale;

			double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 0, width - newWidth);
			double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 0, height - newHeight);

			imgViewMainView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
		});

		imgViewMainView.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) {
				reset(imgViewMainView, width, height);
			}
		});
		imgViewMainView.fitWidthProperty().bind(pane.widthProperty());
		imgViewMainView.fitHeightProperty().bind(pane.heightProperty());
		imgViewMainView.setRotate(180);
		// -------------------------------------------------------------------------------------------------------
	}

	private TextField initTextFieldTxtHarga(TextField txtField) {
		txtField.textProperty().addListener((observable, oldValue, newValue) -> {
			setTextAsuransi();
			setTotalBiaya();
		});

		return txtField;
	}

	public void setKodeTujuanListener() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onClickTujuan();
				}
				hendleImage(event);
			}
		};
		txt_kode_tujuan.setOnKeyPressed(eH);
	}

	public void setListenerEnterSave() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					txt_Ket.requestFocus();
					onClickSimpan();
				}
				hendleImage(event);
			}
		};
		txt_harga.setOnKeyPressed(eH);
	}
	
	public void setListenerLayanan() { 
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				hendleImage(event);
			}
		};
		txt_reseller.setOnKeyPressed(eH);
		txt_penerima.setOnKeyPressed(eH);
		txt_no_hp.setOnKeyPressed(eH);
		txt_Ket.setOnKeyPressed(eH);
	}

	public void hendleImage(KeyEvent event) {
		if (txt_kode_tujuan.getText().isEmpty()) {
		} else {
			if (!pending) {
				if (event.getCode() == KeyCode.F1) {
					if (imgOne.isVisible()) {
						imgOne.setVisible(false);
						one = false;
						if (jne) {
							txt_kode_perwakilan.setText("JNE");
						} else {
							txt_kode_perwakilan.setText(kdPerwakilanReg);
						}
						txt_harga_per_kg.setText(String.valueOf(hargaReg));
					} else {
						imgOne.setVisible(true);
						one = true;
						txt_harga_per_kg.setText(String.valueOf(hargaOne));
						txt_kode_perwakilan.setText(kdPerwakilanOne);
					}
				}
				if (event.getCode() == KeyCode.F2) {
					if (imgJne.isVisible()) {
						imgJne.setVisible(false);
						jne = false;
						if (one) {
							txt_harga_per_kg.setText(String.valueOf(hargaOne));
							txt_kode_perwakilan.setText(kdPerwakilanOne);
						} else {
							txt_harga_per_kg.setText(String.valueOf(hargaReg));
							txt_kode_perwakilan.setText(kdPerwakilanReg);
						}
					} else {
						imgJne.setVisible(true);
						jne = true;
						txt_harga_per_kg.setText(String.valueOf(hargaReg));
						txt_kode_perwakilan.setText("JNE");
					}
				}
				if (event.getCode() == KeyCode.F4) {
					if (imgFree.isVisible()) {
						imgFree.setVisible(false);
						free = false;
						txt_harga_per_kg.setText(String.valueOf(hargaReg));
						txt_kode_perwakilan.setText(kdPerwakilanReg);
						txt_harga.setDisable(false);
					} else {
						imgFree.setVisible(true);
						free = true;
						imgPending.setVisible(false);
						pending = false;
						txt_harga_per_kg.setText("0");
						txt_harga.setText("0");
						txt_harga.setDisable(true);
						txt_asuransi.setUangTeks(0);
						txt_total_biaya.setUangTeks(0);
						txt_Ket.requestFocus();
					}
				}
			}
			if (event.getCode() == KeyCode.F3) {
				if (imgPending.isVisible()) {
					imgPending.setVisible(false);
					pending = false;
					txt_harga_per_kg.setText(String.valueOf(hargaReg));
					txt_kode_perwakilan.setText(kdPerwakilanReg);
					txt_harga.setDisable(false);
				} else {
					imgPending.setVisible(true);
					pending = true;
					imgJne.setVisible(false);
					jne = false;
					imgOne.setVisible(false);
					one = false;
					imgFree.setVisible(false);
					free = false;
					txt_harga_per_kg.setText("0");
					txt_kode_perwakilan.setText("XXX");
					txt_harga.setText("0");
					txt_harga.setDisable(true);
					txt_asuransi.setUangTeks(0);
					txt_total_biaya.setUangTeks(0);
					txt_Ket.requestFocus();
				}
			}
			
			if (!pending) {
				if (one && jne) {
					txt_harga_per_kg.setText(String.valueOf(hargaOne));
					txt_kode_perwakilan.setText("JNE");
				}
			}
			
			if(free){
				txt_harga_per_kg.setText("0");
				txt_harga.setText("0");
				txt_harga.setDisable(true);
				txt_asuransi.setUangTeks(0);
				txt_total_biaya.setUangTeks(0);
			}
		}
		if (event.getCode() == KeyCode.LEFT) {
			imgViewMainView.setRotate(360);
			imgViewMainView.getOnRotate();
		}
		if (event.getCode() == KeyCode.RIGHT) {
			imgViewMainView.setRotate(180);
			imgViewMainView.getOnRotate();
		}
		
		//FA
		if (event.getCode() == KeyCode.DOWN) {
			imgViewMainView.setFocusTraversable(true);
					
			if (countUp == 0) {
				reset(imgViewMainView, width, height);
			} 
					
			double delta = 40.0;
			double x = 107.0;
			double y = 181.0;
			Rectangle2D viewport = imgViewMainView.getViewport();

			double scale = clamp(Math.pow(1.01, delta),

			Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

			// don't scale so that we're bigger than image dimensions:
				Math.max(width / viewport.getWidth(), height / viewport.getHeight())

				);
					
			Point2D mouse = imageViewToImage(imgViewMainView, new Point2D(x, y));

			double newWidth = viewport.getWidth() * scale;
			double newHeight = viewport.getHeight() * scale;

			double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 0, width - newWidth);
			double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 0, height - newHeight);

			imgViewMainView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));

//			Rectangle2D viewportRect = new Rectangle2D(40, 35, 110, 110);
//			imgViewMainView.setViewport(viewportRect);
		}
				
		//FA
		if (event.getCode() == KeyCode.UP) {
			imgViewMainView.setFocusTraversable(true);
					
			if (countUp == 0) {
				reset(imgViewMainView, width, height);
			}
					
			double delta = -40.0;
			double x = 107.0;
			double y = 181.0;
			Rectangle2D viewport = imgViewMainView.getViewport();

			double scale = clamp(Math.pow(1.01, delta),

			Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

				// don't scale so that we're bigger than image dimensions:
					Math.max(width / viewport.getWidth(), height / viewport.getHeight())

					);

			Point2D mouse = imageViewToImage(imgViewMainView, new Point2D(x, y));

			double newWidth = viewport.getWidth() * scale;
			double newHeight = viewport.getHeight() * scale;

			double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 0, width - newWidth);
			double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 0, height - newHeight);

			imgViewMainView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
			countUp++;
		}
		setTextAsuransi();
		setTotalBiaya();
	}

	//TODO LIST
	public void set(TtDataEntry dp, BrowseDataEntryController browseDataEntryController) {
		listEntryAll = DataPaketService.getDataEntryFromPopup(dp.getPengirim());
		boolean update = DataPaketService.updateClass(listEntryAll, uLogin.getNamaUser());
		this.browseCntrl = browseDataEntryController;
		loopData();
	}

	@FXML
	public void onClickSkip() {
		noLoop++;
		DataPaketService.updateSkip(idDataPaket);
		loopData();
		txt_reseller.requestFocus();
	}

	public void loopData() {
		no = 0;
		if (noLoop > listEntryAll.size()) {
			System.out.println("--> noLoop : " + noLoop);
			System.out.println();
			kembali();
		} else {
			for (EntryDataShowVO dat : listEntryAll) {
				EntryDataShowVO e = new EntryDataShowVO();
				no++;
				if (no == noLoop) {
					btn_simpan.setText("Simpan " + "(" + noLoop + ")");
					ClearData();
					txt_awb.setText(dat.getAwbData());
					txt_kode_pickup.setText(dat.getKdPickup());
					txt_pengirim.setText(dat.getPengirim());
					txt_berat.setText(dat.getbFinal());
					txt_berat_bulat.setText(dat.getBpFinal());
					txt_admin_input.setText(uLogin.getNamaUser());
					// txt_kode_perwakilan.setText(dat.getKdPerwakilan());
					txt_asal_paket.setText(dat.getAsalPaket());
					idDataPaket = dat.getIdDataPaket();
					dp_tanggal_transaksi.setTanggalText(dat.getCreated());
					File file = new File(dat.getGambar());
					image = new Image(file.toURI().toString());
					imgViewMainView.setImage(image);
					imgViewMainView.setPreserveRatio(true);
					width = image.getWidth();
					height = image.getHeight();
					reset(imgViewMainView, 0, 0);
				}
			}
		}
	}

	@FXML
	public void onClickSimpan() {
		if (isValidFormEntry()) {
			noLoop++;
			TtDataEntry saveDataPaket = new TtDataEntry();
			txt_Ket.requestFocus();
			saveDataPaket.setPenerima(txt_penerima.getText().trim());
			saveDataPaket.setTujuan(txt_kode_tujuan.getText().trim());
			saveDataPaket.setTelpPenerima(txt_no_hp.getText().trim());
			saveDataPaket.setReseller(txt_reseller.getText().trim());
			saveDataPaket.setKeterangan(txt_Ket.getText().trim());
			// get harga
			saveDataPaket.setHarga(getHarga());
			// perhitungan biaya 
			saveDataPaket.setBiaya(Integer.parseInt(txt_harga_per_kg.getUangteks().trim()));
			// Perhitungan Asuransi
			saveDataPaket.setAsuransi(getAsuransi());
			// Perhitungan Total Biaya
			saveDataPaket.setTotalBiaya(getTotalBiaya());
			saveDataPaket.setTglUpdate(DateUtil.getNow());
			saveDataPaket.setAwbDataEntry(idDataPaket);
			saveDataPaket.setUser(uLogin.getNamaUser());
			saveDataPaket.setKodePerwakilan(txt_kode_perwakilan.getText());
			boolean save = DataPaketService.updateSaveEntry(saveDataPaket);
			boolean updateLayanan = DataPaketService.updateLayanan(txt_awb.getText(), one, jne, pending,
					txt_kode_perwakilan.getText());
			loopData();
			txt_reseller.requestFocus();
		}
	}

	@FXML
	public void onClickCancel() {
		no = 0;
		for (EntryDataShowVO dat : listEntryAll) {
			TtDataEntry e = new TtDataEntry();
			no++;
			if (no >= noLoop) {
				e.setAwbDataEntry(dat.getIdDataPaket());
				boolean cancel = DataPaketService.updateCancel(e);
			}
		}
		kembali();
	}

	// -------------------------------------------------------SET SAAT TUJUAN DI
	// PILIH
	public void setDataTujuan(ServicePerwakilanVO dataHeader) {
		txt_kode_tujuan.setText(dataHeader.getKode_zona());
		lbl_kettujuan
				.setText(dataHeader.getPropinsi() + " " + dataHeader.getKabupaten() + " " + dataHeader.getKecamatan());
		hargaOne = dataHeader.getOneHarga() != null ? dataHeader.getOneHarga() : 0;
		hargaReg = dataHeader.getRegHarga() != null ? dataHeader.getRegHarga() : 0;
		kdPerwakilanReg = dataHeader.getRegPerwakilan() != null ? dataHeader.getRegPerwakilan() : "";
		kdPerwakilanOne = dataHeader.getOnePerwakilan() != null ? dataHeader.getOnePerwakilan() : "";
		if (pending) {
			txt_harga_per_kg.setText("0");
			txt_kode_perwakilan.setText("XXX");
		} else if (one) {
			txt_harga_per_kg.setText(String.valueOf(hargaOne));
			txt_kode_perwakilan.setText(kdPerwakilanOne);
		} else if (jne) {
			txt_harga_per_kg.setText(String.valueOf(hargaReg));
			txt_kode_perwakilan.setText("JNE");
		} else {
			txt_harga_per_kg.setText(String.valueOf(hargaReg));
			txt_kode_perwakilan.setText(kdPerwakilanReg);
		}
		if (one && jne) {
			txt_harga_per_kg.setText(String.valueOf(hargaOne));
			txt_kode_perwakilan.setText("JNE");
		}
		if(free){
			txt_harga_per_kg.setText("0");
			txt_harga.setText("0");
			txt_harga.setDisable(true);
			txt_asuransi.setUangTeks(0);
			txt_total_biaya.setUangTeks(0);
		}

		txt_total_biaya.setUangTeks(Integer.parseInt(txt_harga_per_kg.getText()));
		setTextAsuransi();
		setTotalBiaya();
		txt_harga.requestFocus();
	}

	// -----------------------------------------------------------------------Validasi
	// Entry
	public Boolean isValidFormEntry() {
		Boolean res = true;
		String strMessage = "";
		if (txt_reseller.getText().isEmpty()) {
			strMessage += "Reseller harus diisi.\n";
		}
		if (txt_penerima.getText().isEmpty()) {
			strMessage += "Penerima harus diisi.\n";
		}
		if (txt_kode_tujuan.getText().isEmpty()) {
			strMessage += "Tujuan asal harus diisi.\n";
		}
		if (!pending && !free && txt_harga_per_kg.getUangteks().equals("0")) {
			strMessage += "Harga Per Kg tidak boleh kosong.\n";
		}
		if (!strMessage.isEmpty()) {
			MessageBox.alert(strMessage);
			res = false;
		}
		return res;
	}

	// --------------------------------------------------------------------------Image
	private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
		double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
		double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

		Rectangle2D viewport = imageView.getViewport();
		return new Point2D(viewport.getMinX() + xProportion * viewport.getWidth(),
				viewport.getMinY() + yProportion * viewport.getHeight());
	}

	private void shift(ImageView imageView, Point2D delta) {
		Rectangle2D viewport = imageView.getViewport();

		double width = imageView.getImage().getWidth();
		double height = imageView.getImage().getHeight();

		double maxX = width - viewport.getWidth();
		double maxY = height - viewport.getHeight();

		double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
		double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

		imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
	}

	private double clamp(double value, double min, double max) {

		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	private void reset(ImageView imageView, double width, double height) {
		imageView.setViewport(new Rectangle2D(0, 0, width, height));
	}

	@FXML
	public void onClickTujuan() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/popup/PopUpTujuan.fxml"));
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cari Tujuan");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(WindowsHelper.primaryStage);
			dialogStage.initStyle(StageStyle.UTILITY);
			dialogStage.setX(450);
			dialogStage.setY(420);
			dialogStage.setResizable(false);
			
			Parent root = (Parent) fxmlLoader.load();
			PopUpTujuanController controller = fxmlLoader.<PopUpTujuanController> getController();
			
			controller.set(DataEntryController.this, txt_kode_tujuan.getText(), txt_awb.getText());
			
			Scene scene = new Scene(root);
			
			dialogStage.setScene(scene);
			dialogStage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void ClearData() {
		txt_awb.setText("");
		txt_kode_pickup.setText("");
		txt_pengirim.setText("");
		txt_reseller.setText("");
		txt_penerima.setText("");
		txt_no_hp.setText("");
		txt_kode_tujuan.setText("");
		txt_kode_perwakilan.setText("");
		txt_berat.setText("");
		txt_berat_bulat.setText("");
		txt_harga_per_kg.setUangTeks(0);
		txt_harga.setUangTeks(0);
		txt_asuransi.setUangTeks(0);
		txt_total_biaya.setUangTeks(0);
		lbl_kettujuan.setText("");
		txt_admin_input.setText("");
		txt_asal_paket.setText("");
		dp_tanggal_transaksi.setTanggalText(null);

		txt_Ket.setText("");
		one = false;
		jne = false;
		pending = false;
		hargaOne = 0;
		hargaReg = 0;
		free=false;
		kdPerwakilanOne = "";
		kdPerwakilanReg = "";
		imgJne.setVisible(false);
		imgOne.setVisible(false);
		imgPending.setVisible(false);
		imgFree.setVisible(false);
		txt_harga.setDisable(false);
	}

	private void kembali() {
		browseCntrl.settingListboksDataEntry();
		Stage stage = (Stage) btn_simpan.getScene().getWindow();
		stage.close();
	}

	@DtoListener(idDtoListener = "backTop")
	public void backDtoListener() {
		try {
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("/controller/Menu.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public int getAsuransi() {
		int jmhAsuransi = PerhitunganBiaya.getTotalAsuransi(txt_harga.getUangteks());
		return jmhAsuransi;
	}

	public int getTotalBiaya() {
		int totalBiaya = PerhitunganBiaya.getTotalBiaya(txt_asuransi.getUangteks(), txt_harga_per_kg.getUangteks(),
				txt_berat_bulat.getText());
		return totalBiaya;
	}

	public void setTextAsuransi() {
		int jmhAsuransi = PerhitunganBiaya.getTotalAsuransi(txt_harga.getUangteks());
		txt_asuransi.setUangTeks(jmhAsuransi);
	}

	public void setTotalBiaya() {
		int totalBiaya = PerhitunganBiaya.getTotalBiaya(txt_asuransi.getUangteks(), txt_harga_per_kg.getUangteks(),
				txt_berat_bulat.getText());
		txt_total_biaya.setUangTeks(totalBiaya);
	}

	public int getHarga() {
		int totalHarga = PerhitunganBiaya.getHarga(txt_berat_bulat.getText(), txt_harga_per_kg.getText());
		return totalHarga;
	}

}
