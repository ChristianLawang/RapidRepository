package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import java.util.List;

import java.util.ResourceBundle;








import entity.TrPelanggan;
import entity.TrPerwakilan;
import entity.TrPickup;
import javafx.beans.property.IntegerProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import service.GenericService;
import service.MasterPerwakilanService;
import service.MasterPickupService;
import service.PelangganService;

import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import utilfx.AutoCompleteComboBoxListener;

public class MasterPickupController implements Initializable {

	@FXML
	ComboBox cmbHari, cmbPerwakilan;
	@FXML
	Button btnSimpan;
	@FXML
	TextField txtJam, txtMenit;
	@FXML
	TableView<PickupTV> listboxMasterPickup;
	@FXML
	TableView<PickupSubTV> listboxMasterPickupJadwal;
	@FXML
	private TableColumn<PickupTV, String> 
		noCol,
		pelangganCol;
	String kodePelanggan;
	TrPelanggan trPelanggan = new TrPelanggan();
	ObservableList<PickupTV> masterData = FXCollections.observableArrayList();
	@FXML
	TextField txtCari;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;
		
		cmbHari.getItems().add("Minggu");
		cmbHari.getItems().add("Senin");
		cmbHari.getItems().add("Selasa");
		cmbHari.getItems().add("Rabu");
		cmbHari.getItems().add("Kamis");
		cmbHari.getItems().add("Jumat");
		cmbHari.getItems().add("Sabtu");	
		cmbHari.setValue("Minggu");
		listboxMasterPickup.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
	        	PickupTV selection = listboxMasterPickup.getSelectionModel().getSelectedItem();
				trPelanggan = PelangganService.getPelangganByName( (String) selection.getNamaPelanggan());                   
				refreshSubTable();
		    }
		});
		btnSimpan.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(trPelanggan==null){
					MessageBox.alert("Nama Pelanggan salah input");
				}else{
					TrPickup trPickup = new TrPickup();
					String lastMaxId = GenericService.getMaxTableStringRaw(TrPickup.class, "id");
					Integer intNum = 0;
					if(lastMaxId==null){
						lastMaxId="M0";
						intNum = Integer.parseInt(lastMaxId.substring(1));
					}else{
						intNum = Integer.parseInt(lastMaxId.substring(1));
					}
					trPickup.setId("M"+(intNum+1));
					trPickup.setKodePelanggan(trPelanggan.getKodePelanggan());
					trPickup.setKodeHari(getKodeHariByName((String) cmbHari.getSelectionModel().getSelectedItem().toString()));
					trPickup.setJamPickup(txtJam.getText() + ":" + txtMenit.getText());
					trPickup.setTglCreate(new Date());
					trPickup.setTglUpdate(new Date());
					trPickup.setFlag("0");
					GenericService.save(TrPickup.class, trPickup, true);
					
					refreshSubTable();
				}
			}
		});
		
		cmbPerwakilan.getItems().add("All Perwakilan");
		List<TrPerwakilan> lstPerwakilan = MasterPerwakilanService.getAllPerwakilanCabangDistinct();
		for (TrPerwakilan trPerwakilan : lstPerwakilan) {
			cmbPerwakilan.getItems().add(trPerwakilan.getKodePerwakilan());
		}
		cmbPerwakilan.setValue("All Perwakilan");
		
		cmbPerwakilan.valueProperty().addListener(new ChangeListener<String>() {
	        @Override public void changed(ObservableValue ov, String t, String t1) {
		    	refreshTable();
		    }
		});
		refreshTable();
	}
	public void refreshTable(){
		List<TrPelanggan> pelanggan = PelangganService.getDataPelanggan((String) cmbPerwakilan.getSelectionModel().getSelectedItem().toString());

//		listboxMasterPickup.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		masterData.clear();
		for(Integer ind = 0;ind<pelanggan.size();ind++){
			PickupTV row = new PickupTV(
					new Integer(ind+1).toString(), 
					pelanggan.get(ind).getNamaAkun()	
					);
			masterData.add(row);
		}
		
		noCol.setCellValueFactory(cellData -> cellData.getValue().getNoProperty());
		pelangganCol.setCellValueFactory(cellData -> cellData.getValue().getPelangganProperty());
		
		FilteredList<PickupTV> filteredData = new FilteredList<>(masterData, p -> true);
		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {			
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
		
				String lowerCaseFilter = newValue.toLowerCase();
		
				if (data.getNamaPelanggan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});
		
		SortedList<PickupTV> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(listboxMasterPickup.comparatorProperty());
		listboxMasterPickup.setItems(sortedData);
		
		
//		listboxMasterPickup.getColumns().clear();
//		listboxMasterPickup.getItems().clear();
//		
//		listboxMasterPickup.setItems(pops);
//		
//		TableColumn<PickupTV, Integer> noCol = new TableColumn<PickupTV, Integer>("No");
//		TableColumn<PickupTV, String> namaPelangganCol = new TableColumn<PickupTV, String>("Nama Pelanggan");
//		
//		listboxMasterPickup.getColumns().addAll(noCol, namaPelangganCol);
//		
//		noCol.setCellValueFactory(new PropertyValueFactory<PickupTV, Integer>("no"));
//		namaPelangganCol.setCellValueFactory(new PropertyValueFactory<PickupTV, String>("namaPelanggan"));
//		
//		listboxMasterPickup.setEditable(true);
	}
	public void refreshSubTable(){
		List<TrPickup> pickup = MasterPickupService.getJadwalPickupByKodePelanggan(trPelanggan.getKodePelanggan());

		listboxMasterPickupJadwal.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<PickupSubTV> pops = FXCollections.observableArrayList();
		for(Integer ind = 0;ind<pickup.size();ind++){
			PickupSubTV row = new PickupSubTV(
					(ind+1)+"", 
					pickup.get(ind).getId(),
					getNamaHariByKode(pickup.get(ind).getKodeHari()),
					pickup.get(ind).getJamPickup()
					);
			pops.add(row);
		}
		
		listboxMasterPickupJadwal.getColumns().clear();
		listboxMasterPickupJadwal.getItems().clear();
		
		listboxMasterPickupJadwal.setItems(pops);
		
		TableColumn<PickupSubTV, String> noCol = new TableColumn<PickupSubTV, String>("No");
		TableColumn<PickupSubTV, String> hariCol = new TableColumn<PickupSubTV, String>("Hari");
		TableColumn<PickupSubTV, String> jamCol = new TableColumn<PickupSubTV, String>("Jam");
		TableColumn<PickupSubTV, String> actionCol = new TableColumn<PickupSubTV, String>("Action");
		
		Callback<TableColumn<PickupSubTV, String>, TableCell<PickupSubTV, String>> cellFactory = //
                new Callback<TableColumn<PickupSubTV, String>, TableCell<PickupSubTV, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<PickupSubTV, String> param ){
                        final TableCell<PickupSubTV, String> cell = new TableCell<PickupSubTV, String>(){

                            final Button btn = new Button( "Delete" );

                            @Override
                            public void updateItem( String item, boolean empty ){
                                super.updateItem( item, empty );
                                if ( empty ){setGraphic( null );setText( null );
                                }else{
                                    btn.setOnAction( ( ActionEvent event ) -> {
                                    	int[] dataButtonMessageBox = new int[2];
                    					dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
                    					dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
                                    	int hasilMessageBox = MessageBox.confirm("Apakah Yakin akan Melakukan Delete?",
                    							dataButtonMessageBox);
                    					if (hasilMessageBox == 6) { 
                    						PickupSubTV row = getTableView().getItems().get( getIndex() );
                    						MasterPickupService.setFlag(row.getId(), 1);
                    						refreshSubTable();
                    					}
	                                });
                                    setGraphic( btn );
                                    setText( null );
                                }
                            }
                        };
                        return cell;
                    }
                };

        actionCol.setCellFactory( cellFactory );
        
		listboxMasterPickupJadwal.getColumns().addAll(noCol, hariCol, jamCol, actionCol);
		
		noCol.setCellValueFactory(new PropertyValueFactory<PickupSubTV, String>("no"));
		hariCol.setCellValueFactory(new PropertyValueFactory<PickupSubTV, String>("hari"));
		jamCol.setCellValueFactory(new PropertyValueFactory<PickupSubTV, String>("jam"));
		
		listboxMasterPickupJadwal.setEditable(true);
	}
		
	public static class PickupTV{
		private StringProperty no;
		private StringProperty namaPelanggan;
		
		public PickupTV(String no, String namaPelanggan){
			this.no = new SimpleStringProperty(no);
			this.namaPelanggan = new SimpleStringProperty(namaPelanggan);
		}
		public String getNo() {
			return no.get();
		}
		public void setNo(String no) {
			this.no.set(no);
		}
		public String getNamaPelanggan() {
			return namaPelanggan.get();
		}
		public void setNamaPelanggan(String namaPelanggan) {
			this.namaPelanggan.set(namaPelanggan);
		}
		public StringProperty getNoProperty(){
			return no;
		}
		public StringProperty getPelangganProperty(){
			return namaPelanggan;
		}
	}
	
	public static class PickupSubTV{
		private StringProperty no;
		private StringProperty id;
		private StringProperty hari;
		private StringProperty jam;
		
		public PickupSubTV(String no, String id, String hari, String jam){
			this.no = new SimpleStringProperty(no);
			this.id = new SimpleStringProperty(id);
			this.hari = new SimpleStringProperty(hari);
			this.jam = new SimpleStringProperty(jam);
		}
		public String getNo() {
			return no.get();
		}
		public void setNo(String no) {
			this.no.set(no);
		}
		public String getHari() {
			return hari.get();
		}
		public void setHari(String hari) {
			this.hari.set(hari);
		}
		public String getJam() {
			return jam.get();
		}
		public void setJam(String jam) {
			this.jam.set(jam);
		}
		public void setId(String id){
			this.id.set(id);
		}
		public String getId(){
			return this.id.get();
		}
	}
	
	private String getKodeHariByName(String nama) {
		switch(nama){
			case "Minggu": return "1";
			case "Senin": return "2";
			case "Selasa": return "3";
			case "Rabu": return "4";
			case "Kamis": return "5";
			case "Jumat": return "6";
			case "Sabtu": return "7";
		}
		return null;
	}
	private String getNamaHariByKode(String kodeHari) {
		switch(kodeHari){
			case "1": return "Minggu";
			case "2": return "Senin";
			case "3": return "Selasa";
			case "4": return "Rabu";
			case "5": return "Kamis";
			case "6": return "Jumat";
			case "7": return "Sabtu";
		}
	return null;
	}
	@DtoListener(idDtoListener = "backTop")
	public void backDtoListener() {
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
