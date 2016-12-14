package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import entity.TrCabang;
import entity.TrHarga;
import entity.TrUser;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.DtoBroadcaster;
import util.DtoListener;
import util.ManagedFormHelper;
import util.WindowsHelper;
import utilfx.Comboboks;
import service.GenericService;
import service.MasterCabangService;
import service.MasterHargaService;
import service.UserService;

@SuppressWarnings("unused")
public class MasterUserController implements Initializable {
	
	@FXML
	private TextField txtIdUser, txtNamaUser, txtEmail, txtToken;
	
	@FXML
	private Comboboks cbStatus;
	
	@FXML
	private TableView lb_header;
	
	@FXML
	private ComboBox cbCabang;
	
	@FXML
	private TextField textCari;
	
	@FXML
	private Button btnCari, btnSimpan, btnBatal;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Objek kelas ini
        ManagedFormHelper.instanceController = this;
        load();
        setListenerEnterTampil();
        cbCabang.setValue("-");
		cbStatus.setValue("-");	

        // memanggil combobox cabang
        ObservableList<TrCabang> listCabang = FXCollections.observableArrayList(MasterCabangService.getDataCabang());
		for (TrCabang i : listCabang) {
			cbCabang.getItems().add(i.getKodeCabang());
		}
		//add Combo Status
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "Aktif",
			        "Non Aktif"
			    );
		cbStatus.setItems(options);
		Platform.runLater(new Runnable() {
		     @Override
		     public void run() {
		         txtIdUser.requestFocus();
		     }
		});
	}
	
	  public void load()
	    {
		  UserService serviceUser = new UserService();
	        ObservableList<TrUser> olHeader = FXCollections.observableArrayList(UserService.getDataUser(textCari.getText()));
	        
	        TableColumn col = new TableColumn("Id User");
	        col.setPrefWidth(195.0);
	        col.setCellValueFactory(
	            new Callback<TableColumn.CellDataFeatures<TrUser,String>,ObservableValue<String>>()
	            {                   
	                @Override
	                public ObservableValue<String> call(TableColumn.CellDataFeatures<TrUser, String> param) 
	                {                                                                                             
	                    return new SimpleStringProperty(param.getValue().getIdUser());                   
	                }          
	            }
	        );
	        lb_header.getColumns().addAll(col);
	        
	        col = new TableColumn("Nama User");
	        col.setPrefWidth(300.0);
	        col.setCellValueFactory(
	            new Callback<TableColumn.CellDataFeatures<TrUser,String>,ObservableValue<String>>()
	            {                   
	                @Override
	                public ObservableValue<String> call(TableColumn.CellDataFeatures<TrUser, String> param) 
	                {                                                                                             
	                    return new SimpleStringProperty(param.getValue().getNamaUser());                   
	                }          
	            }
	        );
	        lb_header.getColumns().addAll(col);
	        
	        col = new TableColumn("Email");
	        col.setPrefWidth(257.0);
	        col.setCellValueFactory(
	            new Callback<TableColumn.CellDataFeatures<TrUser,String>,ObservableValue<String>>()
	            {                   
	                @Override
	                public ObservableValue<String> call(TableColumn.CellDataFeatures<TrUser, String> param) 
	                {                                                                                             
	                    return new SimpleStringProperty(param.getValue().getEmail());                   
	                }          
	            }
	        );
	        lb_header.getColumns().addAll(col);	        
	        lb_header.setItems(olHeader);
	    }
	  
	  
	  @FXML
	    public void onMouseClicked(MouseEvent evt)
	    {
	        if (evt.getClickCount() > 1) 
	        {
	            TrUser dataHeader = (TrUser) lb_header.getSelectionModel().getSelectedItem();
	            System.out.println("---------------Data Header : "+dataHeader.getIdUser());
	            if(dataHeader!=null)
	            {
	                DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "loadHeader", dataHeader);
	                Stage stage = (Stage) lb_header.getScene().getWindow();
//	                stage.close();
	            }
	        }
	    }
	
	 @DtoListener(idDtoListener = "loadHeader")  //Dari search
	    public void loadDtoListener(TrUser userHeader){
		 txtIdUser.setText(userHeader.getIdUser());
		 txtNamaUser.setText(userHeader.getNamaUser());
		 txtEmail.setText(userHeader.getEmail());
		 txtToken.setText(userHeader.getRememberToken());
		 cbCabang.setValue(userHeader.getKodeCabang());
//		 cbStatus.selectKode(userHeader.getStatus());

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
	
//	@FXML
//	public void onClickCari() {
//		try {
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Browse Header");
//            dialogStage.initModality(Modality.APPLICATION_MODAL);               //Supaya selalu ada di depan
//            Parent root = FXMLLoader.load(getClass().getResource("/popup/PopUpMasterUser.fxml"));
//            Scene scene = new Scene(root);
//
//            dialogStage.setScene(scene);
//            dialogStage.show();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//           
//        }
//		
//	}
//	@FXML
//	public void setListenerEnterCari() {
//		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
//			@Override
//			public void handle(KeyEvent event) {
//				if (event.getCode() == KeyCode.ENTER) {
//					onClickCari();
//				}
//			}
//		};
//		txtIdUser.setOnKeyPressed(eH);
//		btnCari.setOnKeyPressed(eH);
//	}
	
	@FXML
	public void setListenerEnterTampil() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					load();
				}
			}
		};
//		txtIdUser.setOnKeyPressed(eH);
		textCari.setOnKeyPressed(eH);
	}
	
	@FXML
    public void onSave(Event evt){
		List<TrUser> test = UserService.getDataUser(txtIdUser.getText());
		if(test.size()>0){
			// UPDATE TABLE
//			UserService.updateDataHarga(txtKdZona.getText(), txtKdAsal.getText()
//					, txtKdPropinsi.getText(), txtKdKabupaten.getText(), txtKdkecamatan.getText()
//					, txtKdPerwakilan.getText(), txtZona.getText() 
//					, Integer.parseInt(txtCGKReg.getText()), txtCGKRegEtd.getText(), Integer.parseInt(txtCGKBest.getText()));
		}else{
			
			//SAVE
			TrUser trusr = new TrUser();
		
			trusr.setIdUser(txtIdUser.getText());
			trusr.setNamaUser(txtNamaUser.getText());
			trusr.setEmail(txtEmail.getText());
			trusr.setKodeCabang(cbCabang.getValue().toString());
//			trusr.setStatus(cbStatus.getValue().toString());
//			trusr.setRememberToken(txtToken.getText());
			
			
			Calendar cal = Calendar.getInstance();
			trusr.setTglCreate(cal.getTime());
			trusr.setTglUpdate(cal.getTime());
			trusr.setFlag(0);
			if (txtIdUser.getText() == null) {
				GenericService.save(TrUser.class, trusr, true); 
		}
			load();
		clearForm();
		btnSimpan.setText("SIMPAN");
		}
}
    
    @FXML
    public void onCancel(Event evt)
    {
        clearForm();
    }
    

	public void clearForm()

	{	
		txtIdUser.clear(); 
		txtNamaUser.clear();
		txtEmail.clear();
		cbCabang.setValue("-");
		cbStatus.setValue("-");	
		txtToken.clear();
		btnSimpan.setText("SIMPAN");

  
	}	
}