package controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import entity.TrPelanggan;
import entity.TrUser;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import service.HeaderService;
import service.PelangganService;
import util.ManagedFormHelper;
import util.MessageBox;
import util.SequenceUtil;
import util.TempUtil;
import util.WindowsHelper;
import utilfx.AutoCompleteComboBoxListener;

public class PilihPelangganSubController implements Initializable{

	@FXML
	private ComboBox cmbPelanggan;
	@FXML
	private TextField txtDialogNoPickup;
	@FXML
	private TextField txtDialogJumlahBarang;
	
	@FXML
	private Button btnSimpan, btnBatal;

	private TrUser usr = LoginController.getUserLogin();
	
	public void initData(String passParam) {
		
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		setListenerEscMenu();
		setListenerEnterSimpan();
		List<TrPelanggan> lstPelanggan = PelangganService.getDataPelanggan();
		for (TrPelanggan trPelanggan : lstPelanggan) {
			cmbPelanggan.getItems().add(trPelanggan.getNamaAkun());
		}
		new AutoCompleteComboBoxListener<TrPelanggan>(cmbPelanggan);
	}
	
	@FXML
	public void onClickOK(Event event){
		TrPelanggan trPelanggan = PelangganService.getPelangganByName( (String) cmbPelanggan.getSelectionModel().getSelectedItem().toString());
		if(trPelanggan!=null){
			Map<String, Object> map = new HashMap<String, Object>();
	        map.put("pelanggan object", PelangganService.getPelangganByName( (String) cmbPelanggan.getSelectionModel().getSelectedItem().toString()));
	        map.put("no pickup", txtDialogNoPickup.getText());
	        map.put("input ke", new Integer(0));
	        map.put("jumlah barang", txtDialogJumlahBarang.getText());
	        TempUtil.saveFotoTimbangLocal(map);
			Button currentNode = (Button) event.getSource();
		    Stage stage = (Stage) currentNode.getScene().getWindow();
		    stage.close();
		    MenuController controller = new MenuController();
		    controller.onFotoTimbang();
		}else{
			MessageBox.alert("Pelanggan "+cmbPelanggan.getSelectionModel().getSelectedItem().toString()+" tidak terdaftar, silahkan hubungi administrasi");
		}
	}

	
	@FXML
	public void onExit(Event evt) {
		backDtoListener();
		Button currentNode = (Button) evt.getSource();
	    Stage stage = (Stage) currentNode.getScene().getWindow();
	    stage.close();
	}
	
	@FXML
	public void setListenerEscMenu() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE) {
					onExit(event);
				}
			}
		};
		
        btnBatal.setOnKeyPressed(eH);
	}
	
	@FXML
	public void setListenerEnterSimpan() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onClickOK(event);
				}
			}
		};
		txtDialogJumlahBarang.setOnKeyPressed(eH);
		btnSimpan.setOnKeyPressed(eH);
	}

	
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

}
