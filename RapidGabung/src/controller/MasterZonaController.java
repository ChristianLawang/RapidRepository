package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import entity.TrPerwakilan;
import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import service.GenericService;
import service.MasterPerwakilanService;

@SuppressWarnings("unused")
public class MasterZonaController implements Initializable {
	
	@FXML
	private TextField txtKdZona, txtKdPropinsi, txtKdKabupaten, txtKdKecamatan,
					txtKdPerwakilan, txtZona, txtREG, txtONE;
	@FXML
	private Button btnCari, btnSimpan, btnReset;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        ManagedFormHelper.instanceController = this;
        setListenerEnterCari();
		List<TrPerwakilan> lsPerwakilan = MasterPerwakilanService.getDataPerwakilan();
		Platform.runLater(new Runnable() {
		     @Override
		     public void run() {
		         txtKdZona.requestFocus();
		     }
		});
	}
	
	 @DtoListener(idDtoListener = "loadHeader") 
	    public void loadDtoListener(TrPerwakilan perwakilanHeader){
		 txtKdZona.setText(perwakilanHeader.getKodeZona());
		 txtKdPropinsi.setText(perwakilanHeader.getPropinsi());
		 txtKdKabupaten.setText(perwakilanHeader.getKabupaten());
		 txtKdKecamatan.setText(perwakilanHeader.getKecamatan());
		 txtKdPerwakilan.setText(perwakilanHeader.getKodePerwakilan());
		 txtZona.setText(perwakilanHeader.getZona());
		 txtREG.setText(perwakilanHeader.getRegperwakilan());
		 txtONE.setText(perwakilanHeader.getOneperwakilan());
	    }
	
	@DtoListener(idDtoListener = "backTop")
    public void backDtoListener()
    {
        try {
            HBox bodyHBox = new HBox();
            FXMLLoader menu = new FXMLLoader(getClass().getResource("/controller/Menu.fxml"));
            ScrollPane menuPage = (ScrollPane) menu.load();
            menuPage.prefWidthProperty().bind(WindowsHelper.primaryStage.widthProperty());       //FullScreen, hilangin klo g mau fullscreen
            bodyHBox.getChildren().add(menuPage);
            bodyHBox.setAlignment(Pos.CENTER);
            
            if(WindowsHelper.rootLayout!=null)
            {
                WindowsHelper.rootLayout.setCenter(bodyHBox);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	
	@FXML
	public void onClickCari() {
		try {
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Browse Header");
            dialogStage.initModality(Modality.APPLICATION_MODAL);         
            Parent root = FXMLLoader.load(getClass().getResource("/popup/PopUpMasterZona.fxml"));
            Scene scene = new Scene(root);

            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	@FXML
	public void setListenerEnterCari() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onClickCari();
				}
			}
		};
		btnCari.setOnKeyPressed(eH);
	}
	
	@FXML
	public void onClickReset(Event evt) {
		clearForm();
	}
	
	public void clearForm()

	{
		txtKdZona.clear();
		txtKdPropinsi.clear();
		txtKdKabupaten.clear();
		txtKdKecamatan.clear();
		txtKdPerwakilan.clear();
		txtZona.clear();
		txtREG.clear();
		txtONE.clear();

	}

	
	@FXML
	public void onClickSimpan(Event evt) {

		TrPerwakilan trcab = new TrPerwakilan();
		trcab.setKodeZona(txtKdZona.getText());
		trcab.setPropinsi(txtKdPropinsi.getText());
		trcab.setKabupaten(txtKdKabupaten.getText());
		trcab.setKecamatan(txtKdKecamatan.getText());
		trcab.setKodePerwakilan(txtKdPerwakilan.getText());
		trcab.setZona(txtZona.getText());
		trcab.setRegperwakilan(txtREG.getText());
		trcab.setOneperwakilan(txtONE.getText());

		Calendar cal = Calendar.getInstance();
		trcab.setTglCreate(cal.getTime());
		trcab.setTglUpdate(cal.getTime());

		if (trcab.getKodeZona() != null) {
			try {
				GenericService.save(TrPerwakilan.class, trcab, true);
				clearForm();
			} catch (Exception e) {
				MessageBox.alert("Kode Cabang Harus diisi !!!");
			}

		}
	}

}