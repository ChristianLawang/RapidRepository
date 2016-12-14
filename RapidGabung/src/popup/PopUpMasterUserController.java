package popup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entity.TrUser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import service.UserService;
import util.DtoBroadcaster;
import util.ManagedFormHelper;

public class PopUpMasterUserController<T> implements Initializable {
	@FXML
	TableView lb_header;
	
	@FXML
	private TextField textCari;
	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    textCari.requestFocus();
    setListenerEnterTampil();

    }
	
	@FXML
    public void onMouseClicked(MouseEvent evt)
    {
        if (evt.getClickCount() > 1) 
        {
            TrUser dataHeader = (TrUser) lb_header.getSelectionModel().getSelectedItem();
            if(dataHeader!=null)
            {
                DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "loadHeader", dataHeader);
                Stage stage = (Stage) lb_header.getScene().getWindow();
                stage.close();
            }
        }
    }
	
	  public void load()
	    {
		  UserService serviceUser = new UserService();
	        ObservableList<TrUser> olHeader = FXCollections.observableArrayList(serviceUser.getDataUser(textCari.getText()));
	        
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
		public void setListenerEnterTampil() {
			EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.ENTER) {
						onClickTampilkan();
					}
				}
			};
			textCari.setOnKeyPressed(eH);
		}
	  
	  @FXML
		public void onClickTampilkan() {
			load();
		}
}
