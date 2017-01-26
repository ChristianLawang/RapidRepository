package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import controller.MasterPickupController.PickupTV;
import entity.TrCabang;
import entity.TrHarga;
import entity.TrJabatan;
import entity.TrUser;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import util.HashPassword;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import utilfx.Comboboks;
import service.GenericService;
import service.MasterCabangService;
import service.MasterHargaService;
import service.PelangganService;
import service.UserService;

@SuppressWarnings("unused")
public class MasterUserController implements Initializable {
	
	@FXML
	private TextField txtIdUser, txtNamaUser, txtEmail, txtToken;
	
	@FXML
	private TableView<UserTV> lb_header;
	
	@FXML
	private ComboBox cbCabang, cbStatus, cbJabatan;
	
	@FXML
	private TextField textCari;
	
	@FXML
	private Button btnCari, btnSimpan, btnBatal;
	
	ObservableList<UserTV> masterData = FXCollections.observableArrayList();
	
	@FXML
	private TableColumn<UserTV, String> 
		idUserCol,
		namaUserCol,
		emailCol,
		jabatanCol,
		cabangCol,
		statusCol;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Objek kelas ini
        ManagedFormHelper.instanceController = this;
        load();
        setListenerEnterTampil();
        cbCabang.setValue("-");
		cbStatus.setValue("-");	
		cbJabatan.setValue("-");

        // memanggil combobox cabang
        ObservableList<TrCabang> listCabang = FXCollections.observableArrayList(MasterCabangService.getDataCabang());
		for (TrCabang i : listCabang) {
			cbCabang.getItems().add(i.getKodeCabang());
		}
		ObservableList<TrJabatan> listJabatan = FXCollections.observableArrayList(UserService.getDataJabatanUser());
		for (TrJabatan i : listJabatan) {
			cbJabatan.getItems().add(i.getIdJabatan());
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
		
		lb_header.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		    	UserTV selection = lb_header.getSelectionModel().getSelectedItem();
		    	System.out.println("--> selection : " + selection.getIdUser());
				TrUser trUser = UserService.getDataUserById(selection.getIdUser());                 
				txtIdUser.setText(trUser.getIdUser());
				txtNamaUser.setText(trUser.getNamaUser());
				txtEmail.setText(trUser.getEmail());
				txtToken.setText(trUser.getRememberToken());
				cbCabang.setValue(trUser.getKodeCabang());
				cbStatus.setValue(trUser.getStatus()==0?"Aktif":"Non Aktif");
				cbJabatan.setValue(trUser.getIdRole());
		    }
		});
	}
	
	public static class UserTV{
		private StringProperty idUser;
		private StringProperty namaUser;
		private StringProperty email;
		private StringProperty jabatan;
		private StringProperty cabang;
		private StringProperty status;
		
		public UserTV(String idUser, String namaUser, String email, String jabatan, String cabang, String status){
			this.idUser = new SimpleStringProperty(idUser);
			this.namaUser = new SimpleStringProperty(namaUser);
			this.email = new SimpleStringProperty(email);
			this.jabatan = new SimpleStringProperty(jabatan);
			this.cabang = new SimpleStringProperty(cabang);
			this.status = new SimpleStringProperty(status);
		}
		// get value
		public String getIdUser(){
			return idUser.get();
		}
		public String getNamaUser(){
			return namaUser.get();
		}
		public String getEmail(){
			return email.get();
		}
		public String getJabatan(){
			return jabatan.get();
		}
		public String getCabang(){
			return cabang.get();
		}
		public String getStatus(){
			return status.get();
		}
		// get property
		public StringProperty getIdUserProperty(){
			return idUser;
		}
		public StringProperty getNamaUserProperty(){
			return namaUser;
		}
		public StringProperty getEmailProperty(){
			return email;
		}
		public StringProperty getJabatanProperty(){
			return jabatan;
		}
		public StringProperty getCabangProperty(){
			return cabang;
		}
		public StringProperty getStatusProperty(){
			return status;
		}
		// set value
		public void setIdUser(String idUser){
			this.idUser.set(idUser);
		}
		public void setNamaUser(String namaUser){
			this.namaUser.set(namaUser);
		}
		public void setEmail(String email){
			this.email.set(email);
		}
		public void setJabatan(String jabatan){
			this.jabatan.set(jabatan);
		}
		public void setCabang(String cabang){
			this.cabang.set(cabang);
		}
		public void setStatus(String status){
			this.status.set(status);
		}
	}
	
	public void load(){
		UserService serviceUser = new UserService();
	    ObservableList<TrUser> olHeader = FXCollections.observableArrayList(UserService.getDataUser(textCari.getText()));
	       
	    masterData.clear();
		for(Integer ind = 0;ind<olHeader.size();ind++){
			UserTV row = new UserTV(
					olHeader.get(ind).getIdUser(),
					olHeader.get(ind).getNamaUser(),
					olHeader.get(ind).getEmail(),
					olHeader.get(ind).getIdRole(),
					olHeader.get(ind).getKodeCabang(),
					olHeader.get(ind).getStatus()==0?"Aktif":"Non Aktif"
					);
			masterData.add(row);
		}
		
		idUserCol.setCellValueFactory(cellData -> cellData.getValue().getIdUserProperty());
		namaUserCol.setCellValueFactory(cellData -> cellData.getValue().getNamaUserProperty());
		emailCol.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
		jabatanCol.setCellValueFactory(cellData -> cellData.getValue().getJabatanProperty());
		cabangCol.setCellValueFactory(cellData -> cellData.getValue().getCabangProperty());
		statusCol.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
		
		
		
		FilteredList<UserTV> filteredData = new FilteredList<>(masterData, p -> true);
		textCari.textProperty().addListener((observable, oldValue, newValue) -> {			
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
		
				String lowerCaseFilter = newValue.toLowerCase();
		
				if (data.getIdUser().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				if (data.getNamaUser().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});
		
		SortedList<UserTV> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(lb_header.comparatorProperty());
		lb_header.setItems(sortedData);
	}
	
//	 @DtoListener(idDtoListener = "loadHeader")  //Dari search
//	    public void loadDtoListener(TrUser userHeader){
//		 txtIdUser.setText(userHeader.getIdUser());
//		 txtNamaUser.setText(userHeader.getNamaUser());
//		 txtEmail.setText(userHeader.getEmail());
//		 txtToken.setText(userHeader.getRememberToken());
//		 cbCabang.setValue(userHeader.getKodeCabang());
////		 cbStatus.selectKode(userHeader.getStatus());
//
//	    }
	
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

		System.out.println("--> test : " + test.size());
		TrUser trusr = new TrUser();
		
		trusr.setIdUser(txtIdUser.getText());
		trusr.setNamaUser(txtNamaUser.getText());
		String passToken = txtToken.getText()==null?"":txtToken.getText();
		System.out.println("--> passToken : " + passToken);
		trusr.setEmail(txtEmail.getText());
		trusr.setKodeCabang(cbCabang.getValue().toString());
		trusr.setIdRole(cbJabatan.getValue().toString());
		trusr.setStatus(cbStatus.getValue().toString().equals("Aktif")?0:1);	
		Calendar cal = Calendar.getInstance();
		trusr.setTglCreate(cal.getTime());
		trusr.setTglUpdate(cal.getTime());
		trusr.setFlag(0);
		if(test.size()>0){
			if(!passToken.equals("")){
				trusr.setPassword(HashPassword.hashPassword(passToken));
			}else{
				trusr.setPassword(test.get(0).getPassword());
			}
			GenericService.saveOrUpdate(TrUser.class, trusr); 
			
			load();
			clearForm();
		}else{
			if(passToken.equals("")){
				MessageBox.alert("Password harus di isi pada saat buat user baru");
			}else{
				trusr.setPassword(HashPassword.hashPassword(passToken));
				GenericService.saveOrUpdate(TrUser.class, trusr); 
				
				load();
				clearForm();
			}
		}
		
		btnSimpan.setText("SIMPAN");
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
		cbJabatan.setValue("-");
		txtToken.clear();
		btnSimpan.setText("SIMPAN");

  
	}	
}