package Rapid;
	
import java.util.Locale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.MessageBox;
import util.WindowsHelper;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {

    @Override
    public void start(Stage stage){    

        
        Locale.setDefault(Locale.US);
        Stage dialogStage = new Stage();
        WindowsHelper.primaryStage = dialogStage;
        dialogStage.setTitle("");
        
        //Connection with database
//        Persistence.generateSchema("TPBPU", null);
        try{
            //Database
//            EntityManagerFactory emf = Persistence.createEntityManagerFactory("TPBPU");
//            JpaDatabaseHelper.em = emf.createEntityManager();
   
                dialogStage.setTitle("Login");
                dialogStage.initModality(Modality.APPLICATION_MODAL);   
                dialogStage.initStyle(StageStyle.UNDECORATED);//Supaya selalu ada di depan
                Parent root = FXMLLoader.load(getClass().getResource("/controller/LoginForm.fxml"));
                Scene scene = new Scene(root);

                dialogStage.setResizable(false);
                dialogStage.setScene(scene);
                dialogStage.show();
            
        }
        catch(Exception e){       
            MessageBox.alert("Database masih tertutup");
            e.printStackTrace();
        }
    }
    
 
    public static void main(String[] args) {
        launch(args);
    }
}
