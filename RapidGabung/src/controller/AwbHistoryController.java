package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import controller.MasterPelangganController.PelangganTV;
import entity.TtDataEntry;
import entity.TtGabungPaket;
import entity.TtHeader;
import entity.TtPotoTimbang;
import entity.TtStatusKurirIn;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import service.AWBHistoryService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.WindowsHelper;

public class AwbHistoryController implements Initializable {
	@FXML
	TableView lstView;
	@FXML
	Button btnCari;
	@FXML
	Label lblTglAwb, lblTglFoto, lblTglInput, lblTglManifestGudang, lblTglTerimaGudang, lblTglGabungPaket, lblTglPerwakilan;
	@FXML
	ImageView imgView;
	@FXML
	TextField txtAwb;

	AWBHistoryService myService = new AWBHistoryService();
	
	String awb = null;
	
	final int MIN_PIXELS = 10;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        ManagedFormHelper.instanceController = this;
        clearForm();
        
        btnCari.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearForm();
				show();
			}
		});
        
        System.out.println(awb);
        if(awb!=null){
        	txtAwb.setText(awb);
        	show();
        }
	}		
		
	private void show() {
		TtHeader header = myService.getHeaderByAwb(txtAwb.getText());
		TtPotoTimbang potoTimbang = myService.getPotoTimbangByAwb(txtAwb.getText());
		TtDataEntry dataEntry = myService.getDataEntryByAwb(txtAwb.getText());
		TtGabungPaket gabungPaket = myService.getGabungPaketByAwb(txtAwb.getText());
		
		lblTglAwb.setText(header==null?"":DateUtil.getStdDateTimeDisplay(header.getTglCreate()) + " ("+header.getUserCreate()+") ");
		lblTglFoto.setText(potoTimbang==null?"":DateUtil.getStdDateTimeDisplay(potoTimbang.getTglGambar()) + " ("+potoTimbang.getUser()+") ");
		lblTglInput.setText(dataEntry==null?"":DateUtil.getStdDateTimeDisplay(dataEntry.getTglUpdate()) + " ("+dataEntry.getUser()+") ");
		lblTglManifestGudang.setText(null);
		lblTglTerimaGudang.setText(null);
		lblTglGabungPaket.setText(gabungPaket==null?"":DateUtil.getStdDateTimeDisplay(gabungPaket.getTglCreate()) + " ("+gabungPaket.getOleh()+") ");
		lblTglPerwakilan.setText(gabungPaket==null?"":gabungPaket.getKodePerwakilan());

		File file = new File(potoTimbang==null?null:potoTimbang.getGambar());
		Image img = new Image(file.toURI().toString());
		
		imgView.setImage(img);
		imgView.setRotate(180);
		
		populateTable();
	}		
	
	private void populateTable() {
		List<Map<String, Object>> data = myService.getStatusKurirByAWB(txtAwb.getText());
		lstView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<KurirTV> pops = FXCollections.observableArrayList();
		for(Integer ind = 0;ind<data.size();ind++){
			KurirTV row = new KurirTV(
					ind+1, 
					DateUtil.getStdDateTimeDisplay((Date) data.get(ind).get("TANGGAL")), 
					(String) data.get(ind).get("NAMA_KURIR"),
					(String) data.get(ind).get("PENERIMA"),
					(String) data.get(ind).get("MASALAH")				
					);
			pops.add(row);
		}
		
		lstView.getColumns().clear();
		lstView.getItems().clear();
		
		lstView.setItems(pops);
		
		TableColumn<KurirTV, Integer> noCol = new TableColumn<KurirTV, Integer>("No");
		TableColumn<KurirTV, String> tanggalCol = new TableColumn<KurirTV, String>("Tanggal");
		TableColumn<KurirTV, String> namaCol = new TableColumn<KurirTV, String>("Nama Kurir");
		TableColumn<KurirTV, String> penerimaCol = new TableColumn<KurirTV, String>("Nama Penerima");
		TableColumn<KurirTV, String> statusCol = new TableColumn<KurirTV, String>("Status Kirim");
		
		lstView.getColumns().addAll(noCol, tanggalCol, namaCol, statusCol, penerimaCol);
		
		noCol.setCellValueFactory(new PropertyValueFactory<KurirTV, Integer>("no"));
		tanggalCol.setCellValueFactory(new PropertyValueFactory<KurirTV, String>("tanggal"));
		namaCol.setCellValueFactory(new PropertyValueFactory<KurirTV, String>("nama"));
		penerimaCol.setCellValueFactory(new PropertyValueFactory<KurirTV, String>("penerima"));
		statusCol.setCellValueFactory(new PropertyValueFactory<KurirTV, String>("status"));
		
		lstView.setEditable(true);
	}
	
	public static class KurirTV{
		private IntegerProperty no;
		private StringProperty tanggal;
		private StringProperty nama;
		private StringProperty penerima;
		private StringProperty status;
		
		public KurirTV(Integer no, String tanggal, String nama, String penerima, String status){
			this.no = new SimpleIntegerProperty(no);
			this.tanggal = new SimpleStringProperty(tanggal);
			this.nama = new SimpleStringProperty(nama);
			this.penerima = new SimpleStringProperty(penerima);
			this.status = new SimpleStringProperty(status);
		}
		public Integer getNo() {
			return no.get();
		}
		public void setNo(Integer no) {
			this.no.set(no);
		}
		public String getTanggal() {
			return tanggal.get();
		}
		public void setTanggal(String tanggal) {
			this.tanggal.set(tanggal);
		}
		public String getNama() {
			return nama.get();
		}
		public void setNama(String nama) {
			this.nama.set(nama);
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
	
	private void clearForm() {
		Image img = null;
		imgView.setImage(img);
		
		lblTglAwb.setText("");
		lblTglFoto.setText("");
		lblTglInput.setText("");
		lblTglManifestGudang.setText("");
		lblTglTerimaGudang.setText("");
		lblTglGabungPaket.setText("");
		lblTglPerwakilan.setText("");	
	
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

	public void set(String awbData) {
		txtAwb.setText(awbData);
    	show();
		
	}
}
