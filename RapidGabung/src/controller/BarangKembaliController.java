package controller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import entity.TtStatusKurirIn;
import entity.TtStatusResiBermasalah;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import service.BarangKembaliService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.WindowsHelper;

public class BarangKembaliController implements Initializable {
	
	@FXML
	DatePicker dtAwal, dtAkhir;
	@FXML
	TableView lstView;
	@FXML
	Button btnCari;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		 //Set Objek kelas ini
        ManagedFormHelper.instanceController = this;
        dtAwal.setValue(LocalDate.now());
        dtAkhir.setValue(LocalDate.now());
        
        btnCari.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				refreshListview();
			}
        });
	}
	
	private void refreshListview() {
		lstView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<MasalahTV> pops = FXCollections.observableArrayList();
		
		BarangKembaliService myService = new BarangKembaliService();
		List<Object[]> result = myService.getResiBermasalah(
					DateUtil.convertToDatabaseColumn(dtAwal.getValue()).toString(),
					DateUtil.convertToDatabaseColumn(dtAkhir.getValue()).toString());
		for (Object[] res : result) {
			MasalahTV row = new MasalahTV(
					(String) res[0],
					(String) res[1],
					(String) res[2]
					);
			pops.add(row);
		}
				
		lstView.getColumns().clear();
		lstView.getItems().clear();
		
		lstView.setItems(pops);
		
		TableColumn<MasalahTV, String> resiCol = new TableColumn<MasalahTV, String>("No. Resi");
		TableColumn<MasalahTV, String> penerimaCol = new TableColumn<MasalahTV, String>("Nama Penerima");
		TableColumn<MasalahTV, String> statusCol = new TableColumn<MasalahTV, String>("Status");
		
		resiCol.prefWidthProperty().bind(lstView.widthProperty().divide(4)); // w * 1/4
		penerimaCol.prefWidthProperty().bind(lstView.widthProperty().divide(4)); // w * 2/4
		statusCol.prefWidthProperty().bind(lstView.widthProperty().divide(2)); // w * 1/4
		
		lstView.getColumns().addAll(resiCol, penerimaCol, statusCol);
		
		resiCol.setCellValueFactory(new PropertyValueFactory<MasalahTV, String>("resi"));
		penerimaCol.setCellValueFactory(new PropertyValueFactory<MasalahTV, String>("penerima"));
		statusCol.setCellValueFactory(new PropertyValueFactory<MasalahTV, String>("status"));
		
		lstView.setEditable(true);
	}
	
	public static class MasalahTV{
		private StringProperty resi = new SimpleStringProperty();
		private StringProperty penerima = new SimpleStringProperty();
		private StringProperty status = new SimpleStringProperty();
		
		public MasalahTV(String resi, String penerima, String status){
			this.resi.set(resi);
			this.penerima.set(penerima);
			this.status.set(status);
		}

		public String getResi() {
			return resi.get();
		}

		public void setResi(String resi) {
			this.resi.set(resi);
		}

		public String getPenerima() {
			return penerima.get();
		}

		public void setPenerima(String penerima) {
			this.penerima.set(penerima);
		}

		public String getStatus() {
			return status.get();
		}

		public void setStatus(String status) {
			this.status.set(status);
		}
		
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
}