package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.sun.prism.impl.Disposer.Record;

import entity.TrKurir;
import entity.TrPelanggan;
import entity.TrPickup;
import entity.TtJadwalPickup;
import entity.TtPickup;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import service.GenericService;
import service.JadwalPickupService;
import service.PelangganService;
import util.DateUtil;
import util.DtoListener;
import util.EmailUtil;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import utilfx.AutoCompleteComboBoxListener;

public class JadwalPickupController implements Initializable {	
	// INI BUAT TEST COMMIT KE GITHUB
	@FXML
	AnchorPane anchorPaneContains;
	@FXML
	Button btnTambahKurir, btnTambahJadwalPickup;
	Double width = 262.0;
	Integer numberPickup = 0;
	Integer numberRow = 0;
	List<Map<String, Object>> pickup = new ArrayList<Map<String, Object>>();
	@FXML
	TableView tbvJadwalPickup;
	@FXML
	TableView tbvJadwalPickupAfter;
	@FXML
	DatePicker dtpickup;
	@FXML
	TextField txtHari;
	private Integer idx;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ManagedFormHelper.instanceController = this;
		dtpickup.setValue(LocalDate.now());
		txtHari.setText(getNamaHariByKode(""+DateUtil.getNomorHariDalamSeminggu(DateUtil.convertToDateColumn(dtpickup.getValue()))));
		loadJadwalPickup();
		loadMasterPickup();
		btnTambahKurir.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Integer index = pickup.size();
				index++;
				if((index & 1) == 0){
					createGridJadwalKurir(index, numberRow, 506.0, 12.0 + ((numberRow)*400.0));
					addMapping(index, numberRow);
					numberRow++;
				}else{
					createGridJadwalKurir(index,  numberRow, 14.0, 12.0 + ((numberRow)*400.0));
					addMapping(index, numberRow);
				}
				loadMasterPickup();
			}
		});
		dtpickup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				txtHari.setText(getNamaHariByKode(""+DateUtil.getNomorHariDalamSeminggu(DateUtil.convertToDateColumn(dtpickup.getValue()))));
				loadJadwalPickup();
				loadMasterPickup();
			}
		});
		btnTambahJadwalPickup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				callDialog();
			}
		});
	}

	private void loadJadwalPickup() {
		anchorPaneContains.getChildren().clear();
		numberRow=0;
		pickup.clear();
		Integer numberPickup = JadwalPickupService.getMaxJadwalPickup(DateUtil.convertToDateColumn(dtpickup.getValue()));
		for(Integer number=1;number<=numberPickup;number++){
			TrKurir kurir = JadwalPickupService.getKurirPerPickup(number, DateUtil.convertToDateColumn(dtpickup.getValue()));
			Integer index = pickup.size();
			index++;
			if((index & 1) == 0){
				createGridJadwalKurirWithParam(index, numberRow, 506.0, 12.0 + ((numberRow)*400.0), kurir);
				addMapping(index, numberRow);
				numberRow++;
			}else{
				createGridJadwalKurirWithParam(index,  numberRow, 14.0, 12.0 + ((numberRow)*400.0), kurir);
				addMapping(index, numberRow);
			}
		}
		
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	private void createGridJadwalKurirWithParam(Integer index, Integer row, Double posX, Double posY, TrKurir kurir) {
		GridPane gridPane = new GridPane();
		gridPane.setLayoutX(posX);
		gridPane.setLayoutY(posY);
		gridPane.setPrefHeight(462.0);
		gridPane.setPrefWidth(465.0);
		
		ColumnConstraints columnConst = new ColumnConstraints();
		columnConst.setHgrow(Priority.SOMETIMES);		
		columnConst.setMinWidth(10.0);
		columnConst.setPrefWidth(100.0);
		
		RowConstraints rowConst1 = new RowConstraints();
		rowConst1.setMaxHeight(51.0);
		rowConst1.setMinHeight(10.0);
		rowConst1.setPrefHeight(10.0);
		rowConst1.setVgrow(Priority.SOMETIMES);
		
		RowConstraints rowConst2 = new RowConstraints();
		rowConst2.setMaxHeight(365.0);
		rowConst2.setMinHeight(10.0);
		rowConst2.setPrefHeight(165.0);
		rowConst2.setVgrow(Priority.SOMETIMES);
		
		RowConstraints rowConst3 = new RowConstraints();
		rowConst3.setMinHeight(10.0);
		rowConst3.setPrefHeight(30.0);
		rowConst3.setVgrow(Priority.SOMETIMES);		
		
		gridPane.getColumnConstraints().addAll(columnConst);
		gridPane.getRowConstraints().addAll(rowConst1, rowConst2, rowConst3);
		
		AnchorPane anchorPaneGridpane1 = new AnchorPane();
		anchorPaneGridpane1.setPrefHeight(200.0);
		anchorPaneGridpane1.setPrefWidth(200.0);		
		
		Label lbAnchorPaneGridPane1 = new Label("Pilih Kurir");
		Label hiddenIndex = new Label(index.toString());
		Label hiddenRow = new Label(row.toString());
		lbAnchorPaneGridPane1.setLayoutX(14.0);
		lbAnchorPaneGridPane1.setLayoutY(11.0);
		hiddenIndex.setVisible(false);
		hiddenRow.setVisible(false);
		@SuppressWarnings("rawtypes")
		ComboBox cmbAnchorPaneGridPane1 = new ComboBox();
		cmbAnchorPaneGridPane1.setLayoutX(77.0);
		cmbAnchorPaneGridPane1.setLayoutY(7.0);
		cmbAnchorPaneGridPane1.setPrefHeight(25.0);
		cmbAnchorPaneGridPane1.setPrefWidth(266.0);
		List<TrKurir> lstKurir = JadwalPickupService.getKurirLokalAsc();
		for (TrKurir kur : lstKurir) {
			cmbAnchorPaneGridPane1.getItems().add(kur.getNama());
		}
		cmbAnchorPaneGridPane1.setValue(kurir.getNama());
		new AutoCompleteComboBoxListener<TrKurir>(cmbAnchorPaneGridPane1);
		
		anchorPaneGridpane1.getChildren().addAll(hiddenIndex, hiddenRow, lbAnchorPaneGridPane1, cmbAnchorPaneGridPane1);
		
		AnchorPane anchorPaneGridpane2 = new AnchorPane();
		anchorPaneGridpane2.setPrefHeight(200.0);
		anchorPaneGridpane2.setPrefWidth(200.0);
		gridPane.setRowIndex(anchorPaneGridpane2, 1);
		@SuppressWarnings("rawtypes")
		TableView tvAnchorPaneGridPane2 = new TableView();
		tvAnchorPaneGridPane2.setPrefHeight(200.0);
		tvAnchorPaneGridPane2.setPrefWidth(343.0);
		anchorPaneGridpane2.setBottomAnchor(tvAnchorPaneGridPane2, 0.0);
		anchorPaneGridpane2.setLeftAnchor(tvAnchorPaneGridPane2, 0.0);
		anchorPaneGridpane2.setRightAnchor(tvAnchorPaneGridPane2, 0.0);
		anchorPaneGridpane2.setTopAnchor(tvAnchorPaneGridPane2, 0.0);
            
		@SuppressWarnings("rawtypes")
		TableColumn tbColumnAnchorPaneGridPane2A = new TableColumn("Pickup");
		tbColumnAnchorPaneGridPane2A.setPrefWidth(75.0);
		TableColumn tbColumnAnchorPaneGridPane2B = new TableColumn("Jam");
		tbColumnAnchorPaneGridPane2B.setPrefWidth(75.0);
		@SuppressWarnings("rawtypes")
		TableColumn tbColumnAnchorPaneGridPane2C = new TableColumn("Action");
		tbColumnAnchorPaneGridPane2C.setPrefWidth(162.0);
		tvAnchorPaneGridPane2.getColumns().addAll(tbColumnAnchorPaneGridPane2A, tbColumnAnchorPaneGridPane2B, tbColumnAnchorPaneGridPane2C);
		buildTableViewPickup(tvAnchorPaneGridPane2, cmbAnchorPaneGridPane1);
		anchorPaneGridpane2.getChildren().addAll(tvAnchorPaneGridPane2);
		AnchorPane anchorPaneGridpane3 = new AnchorPane();
		anchorPaneGridpane3.setPrefHeight(200.0);
		anchorPaneGridpane3.setPrefWidth(200.0);
		gridPane.setRowIndex(anchorPaneGridpane3, 2);
//		Button btnPickup = new Button("Pickup Manual");
//		btnPickup.setLayoutX(189.0);
//		btnPickup.setLayoutY(17.0);
//		btnPickup.setMnemonicParsing(false);	
//		btnPickup.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				callDialog(tvAnchorPaneGridPane2, cmbAnchorPaneGridPane1, index);
//			}
//		});
		Button btnPickup2 = new Button("Pickup Jadwal");
		btnPickup2.setLayoutX(289.0);
		btnPickup2.setLayoutY(17.0);
		btnPickup2.setMnemonicParsing(false);	
		btnPickup2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Set<JadwalTV> selection = new HashSet<JadwalTV>(tbvJadwalPickup.getSelectionModel().getSelectedItems());
				for (JadwalTV jadwal : selection) {
					Integer id = GenericService.getMaxTable(TtJadwalPickup.class, "id");
					JadwalPickupService.insertJadwalPickup(id, jadwal.getIdPickup(), DateUtil.getNomorHariDalamSeminggu(DateUtil.convertToDateColumn(dtpickup.getValue())), jadwal.getPelanggan(), jadwal.getJam(), (String) cmbAnchorPaneGridPane1.getSelectionModel().getSelectedItem(), index, DateUtil.convertToDateColumn(dtpickup.getValue()));
				}
				buildTableViewPickup(tvAnchorPaneGridPane2, cmbAnchorPaneGridPane1);
				loadMasterPickup();
			}
		});		
		Button btnBatal = new Button("Batal");
		btnBatal.setLayoutX(409.0);
		btnBatal.setLayoutY(17.0);
		btnBatal.setMnemonicParsing(false);	
//		anchorPaneGridpane3.getChildren().addAll(btnPickup, btnPickup2,  btnBatal);
		anchorPaneGridpane3.getChildren().addAll(btnPickup2,  btnBatal);
		gridPane.getChildren().addAll(anchorPaneGridpane1, anchorPaneGridpane2, anchorPaneGridpane3);
		anchorPaneContains.getChildren().add(gridPane);
		anchorPaneContains.setPrefHeight(posY+400.0);
	}	
	
	private String revealStatusJadwalPickup(TtJadwalPickup jadwal) {
		String status = "";
		if(jadwal.getSentMail()==1){
			status = "EMAIL";
		}
		if(jadwal.getFlagJalan()==1){
			status = "JALAN";
		}
		return status;
	}
	
	private void buildTableViewPickup(TableView tv, ComboBox cmb) {
		TrKurir kurir = JadwalPickupService.getKurirByName((String) cmb.getSelectionModel().getSelectedItem());
		List<TtJadwalPickup> lstForPickup = JadwalPickupService.getJadwalPickupByKurirByNikKurir(kurir.getNik(), DateUtil.convertToDateColumn(dtpickup.getValue()));
		ObservableList<JadwalTV> pops = FXCollections.observableArrayList();
		for (TtJadwalPickup jadwal : lstForPickup) {
			JadwalTV row = new JadwalTV(
				jadwal.getId(),
				jadwal.getIdPickup(),
				jadwal.getKodePelanggan(),
				jadwal.getJamPickup(),
				jadwal.getSourcePickup(),
				this.revealStatusJadwalPickup(jadwal),
				jadwal.getFlagJalan()==1
				);
			pops.add(row);
		}				
		tv.getColumns().clear();
		tv.getItems().clear();
		
		tv.setItems(pops);
			
		TableColumn<JadwalTV, String> pelangganCol = new TableColumn<JadwalTV, String>("Pelanggan");
		TableColumn<JadwalTV, String> jamCol = new TableColumn<JadwalTV, String>("Jam");
		TableColumn<JadwalTV, String> statusCol = new TableColumn<JadwalTV, String>("Status");
		TableColumn<JadwalTV, String> sendCol = new TableColumn<JadwalTV, String>("Berita");
		TableColumn<JadwalTV, String> jalanCol = new TableColumn<JadwalTV, String>("Kurir");
		TableColumn<JadwalTV, String> deleteCol = new TableColumn<JadwalTV, String>("Action");		
		
		Callback<TableColumn<JadwalTV, String>, TableCell<JadwalTV, String>> cellFactorySend = //
                new Callback<TableColumn<JadwalTV, String>, TableCell<JadwalTV, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<JadwalTV, String> param ){
                        final TableCell<JadwalTV, String> cell = new TableCell<JadwalTV, String>(){

                            final Button btnSend = new Button( "Email" );

                            @Override
                            public void updateItem( String item, boolean empty ){
                                super.updateItem( item, empty );
                                if ( empty ){setGraphic( null );setText( null );
                                
                                }else{
									btnSend.setOnAction( ( ActionEvent event ) -> {
										JadwalTV row = getTableView().getItems().get( getIndex() );
										TrPelanggan pelanggan = PelangganService.getPelangganById(row.getPelanggan());
										String emailBody = 
												"Yth. " + pelanggan.getNamaAkun() + System.lineSeparator() +
												System.lineSeparator() +
												"Permintaan Pick Up anda tanggal " + System.lineSeparator() +
												"kami akan melakukan pick up barang anda ("+DateUtil.getStdDateDisplay(DateUtil.convertToDateColumn(dtpickup.getValue()))+ ")(pukul "+row.getJam()+") telah diproses. Paket anda akan di pick up" + System.lineSeparator() +
												"Oleh : " + System.lineSeparator() +
												kurir.getNama() + " (" + kurir.getNik() + ") " + System.lineSeparator() +
												kurir.getTelp() + System.lineSeparator() +
												"Jika ada perubahaan maka kami akan menghubungi anda kembali. Untuk bantuan informasi dapat " + System.lineSeparator() +
												"menghubungi Yudhi 08980913500 " + System.lineSeparator() +
												System.lineSeparator() +
												System.lineSeparator() +
												"Terima Kasih " + System.lineSeparator() +
												System.lineSeparator() +
												"Rapid Express";
										String subject = "Rapid Pick Up Request " + DateUtil.getStdDateDisplay(new Date());
//										String email = "zechreich2015@gmail.com";
										String email = pelanggan.getEmail();
										
										EmailUtil.sendJadwalPickup(emailBody, subject, email);  										
										JadwalPickupService.updateJadwalPickup(1, 0, row.getId());
										
										buildTableViewPickup(tv, cmb);
	                                });
                                    setGraphic( btnSend );
                                    setText( null );
                                }
                            }
                        };
                        return cell;
                    }
                };
                Callback<TableColumn<JadwalTV, String>, TableCell<JadwalTV, String>> cellFactoryDelete = //
        		        new Callback<TableColumn<JadwalTV, String>, TableCell<JadwalTV, String>>()
        		        {
        		            @Override
        		            public TableCell call( final TableColumn<JadwalTV, String> param ){
        		            	final TableCell<JadwalTV, String> cell = new TableCell<JadwalTV, String>(){
        		            	final Button btnDelete = new Button( "Delete" );

        		                            @Override
        		                            public void updateItem( String item, boolean empty ){
        		                                super.updateItem( item, empty );
        		                                if ( empty ){setGraphic( null );setText( null );
        		                                
        		                                }else{
        		                                	btnDelete.setOnAction( ( ActionEvent event ) -> {
        		                                		JadwalTV row = getTableView().getItems().get( getIndex() );
        		                                		if(!row.getStatus().equals("")){
        		                                			MessageBox.alert("Tidak bisa menghapus jadwal pickup apabila sudah dilakukan pengiriman email, atau kurir pickup sudah berangkat");
        		                                		}else{
        		                    						JadwalPickupService.deleteJadwalPickup(row.getId());
        		                    						pops.remove(row);
        		                    						loadMasterPickup();
        		                                		}
        			                                });
        		                                    setGraphic( btnDelete );
        		                                    setText( null );
        		                                }
        		                            }
        		                        };
        		                        return cell;
        		                    }
        		                };
        Callback<TableColumn<JadwalTV, String>, TableCell<JadwalTV, String>> cellFactoryJalan = //
		        new Callback<TableColumn<JadwalTV, String>, TableCell<JadwalTV, String>>()
		        {
		            @Override
		            public TableCell call( final TableColumn<JadwalTV, String> param ){
		            	final TableCell<JadwalTV, String> cell = new TableCell<JadwalTV, String>(){
		            	final Button btnJalan = new Button( "Berangkat" );
		            	
		                            @Override
		                            public void updateItem( String item, boolean empty ){
		                                super.updateItem( item, empty );
		                                if ( empty ){setGraphic( null );setText( null );
		                                	
		                                }else{
		                                	JadwalTV row = getTableView().getItems().get( getIndex() );
		                                	btnJalan.setText(row.getStatus().equals("JALAN")?"Batal Berangkat":"Berangkat");
		                                	btnJalan.setOnAction( ( ActionEvent event ) -> {
	                                			JadwalPickupService.updateJadwalPickup(1, row.getStatus().equals("JALAN")?0:1, row.getId() );										
	                                			buildTableViewPickup(tv, cmb);
			                                });
		                                    setGraphic( btnJalan );
		                                	setText( null );
		                                }
		                            }
		                        };
		                        return cell;
		                    }
		                };

//        berangkatCol.setEditable(true);
        sendCol.setCellFactory( cellFactorySend );	
        jalanCol.setCellFactory( cellFactoryJalan );
        deleteCol.setCellFactory( cellFactoryDelete );
        tv.getColumns().addAll(pelangganCol, jamCol, statusCol, jalanCol, sendCol, deleteCol);
//		tv.getColumns().addAll(pelangganCol, jamCol, statusCol, berangkatCol, sendCol, deleteCol);
		pelangganCol.setCellValueFactory(new PropertyValueFactory<JadwalTV, String>("pelanggan"));
		jamCol.setCellValueFactory(new PropertyValueFactory<JadwalTV, String>("jam"));
		statusCol.setCellValueFactory(new PropertyValueFactory<JadwalTV, String>("status"));
			
		tv.setEditable(true);
		
		tv.setRowFactory(new Callback<TableView<JadwalTV>, TableRow<JadwalTV>>() {
            @Override
            public TableRow<JadwalTV> call(TableView<JadwalTV> paramP) {
                return new TableRow<JadwalTV>() {
                    @Override
                    protected void updateItem(JadwalTV paramT, boolean paramBoolean) {
                    	if(paramT!=null){
                    		if(paramT.getStatus().equals("EMAIL")){
                    			String style = "-fx-background-color: linear-gradient(#b9fcb3 0%, #FFFFFF 90%, #eaeaea 90%);";
                    			setStyle(style);
                    		}else if(paramT.getStatus().equals("JALAN")){
                    			String style = "-fx-background-color: linear-gradient(#b3c6fc 0%, #FFFFFF 90%, #eaeaea 90%);";
                    			setStyle(style);
                    		}else{
                    			String style = "-fx-background-color: linear-gradient(#b7b7b7 0%, #FFFFFF 90%, #eaeaea 90%);";
                    			setStyle(style);
                    		}
                    	}else{
    	                    setStyle("");
                    	}
	                    super.updateItem(paramT, paramBoolean);
                    }
                };
            }
        });
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

	@SuppressWarnings("unchecked")
	private void loadMasterPickup() {
		// list sebelum
		List<Map> data = JadwalPickupService.getMasterPickupByDay(DateUtil.convertToDateColumn(dtpickup.getValue()));
		
		tbvJadwalPickup.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<JadwalTV> pops = FXCollections.observableArrayList();
		for(Integer ind = 0;ind<data.size();ind++){
			Integer i = 0;
			String idPickup = (String) data.get(ind).get("ID");
			String pelanggan = (String) data.get(ind).get("KODE_PELANGGAN");
			String jam = (String) data.get(ind).get("JAM_PICKUP");
			String source = (String) data.get(ind).get("SOURCE");
			String status = "0";
			Boolean jalan = true;
			JadwalTV row = new JadwalTV(
					0,
					source.subSequence(1, 1)+idPickup,
					pelanggan,
					jam,
					source,
					status,
					jalan
				);
			pops.add(row);
		}
			
		tbvJadwalPickup.getColumns().clear();
		tbvJadwalPickup.getItems().clear();
		
		tbvJadwalPickup.setItems(pops);
			
		TableColumn<JadwalTV, String> pelangganCol = new TableColumn<JadwalTV, String>("Pelanggan");
		TableColumn<JadwalTV, String> jamCol = new TableColumn<JadwalTV, String>("Jam");
		TableColumn<JadwalTV, String> ownerCol = new TableColumn<JadwalTV, String>("Ownner");
		TableColumn<JadwalTV, String> actionCol = new TableColumn<JadwalTV, String>("");
			
		tbvJadwalPickup.getColumns().addAll(pelangganCol, jamCol, ownerCol, actionCol);
			
		pelangganCol.setCellValueFactory(new PropertyValueFactory<JadwalTV, String>("pelanggan"));
		jamCol.setCellValueFactory(new PropertyValueFactory<JadwalTV, String>("jam"));
		ownerCol.setCellValueFactory(new PropertyValueFactory<JadwalTV, String>("source"));
		
		Callback<TableColumn<JadwalTV, String>, TableCell<JadwalTV, String>> cellFactoryDelete = //
                new Callback<TableColumn<JadwalTV, String>, TableCell<JadwalTV, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<JadwalTV, String> param ){
                        final TableCell<JadwalTV, String> cell = new TableCell<JadwalTV, String>(){

                            final Button btnDelete = new Button( "Delete" );

                            @Override
                            public void updateItem( String item, boolean empty ){
                                super.updateItem( item, empty );
                                if ( empty ){setGraphic( null );setText( null );
                                
                                }else{
                                	JadwalTV row = getTableView().getItems().get( getIndex() );
                                	btnDelete.setOnAction( ( ActionEvent event ) -> {
	                                		int[] dataButtonMessageBox = new int[2];
	                						dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
	                						dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
	                						int hasilMessageBox = MessageBox.confirm("Apakah Yakin akan Melakukan Delete?",
	                								dataButtonMessageBox);
                						if (hasilMessageBox == 6) { // cancel
                							JadwalPickupService.deleteJadwalPickupLangsung(row.getIdPickup());
                							loadMasterPickup();
                						}
	                                });  
                                	String source = row.getSource();
                                	if(source.equals("MANUAL")){
                                		setGraphic( btnDelete );
                                        setText( null );
                                	}                                    
                                }
                            }
                        };
                        return cell;
                    }
                };
		
        actionCol.setCellFactory( cellFactoryDelete );
		tbvJadwalPickup.setEditable(true);
		
		// list sesudah
		List<Map> dataAfter = JadwalPickupService.getMasterPickupByDayAfter(DateUtil.convertToDateColumn(dtpickup.getValue()));
		
		tbvJadwalPickupAfter.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<JadwalAfterTV> popsAfter = FXCollections.observableArrayList();
		for(Integer ind = 0;ind<dataAfter.size();ind++){
			JadwalAfterTV row = new JadwalAfterTV(
					(String) dataAfter.get(ind).get("KODE_PELANGGAN"),
					(String) dataAfter.get(ind).get("JAM_PICKUP"),
					(String) dataAfter.get(ind).get("NAMA")
				);
			popsAfter.add(row);
		}
			
		tbvJadwalPickupAfter.getColumns().clear();
		tbvJadwalPickupAfter.getItems().clear();
		
		tbvJadwalPickupAfter.setItems(popsAfter);
			
		TableColumn<JadwalAfterTV, String> pelangganAfterCol = new TableColumn<JadwalAfterTV, String>("Pelanggan");
		TableColumn<JadwalAfterTV, String> jamAfterCol = new TableColumn<JadwalAfterTV, String>("Jam");
		TableColumn<JadwalAfterTV, String> namaAfterCol = new TableColumn<JadwalAfterTV, String>("Pickup Oleh");
			
		tbvJadwalPickupAfter.getColumns().addAll(pelangganAfterCol, jamAfterCol, namaAfterCol);
			
		pelangganAfterCol.setCellValueFactory(new PropertyValueFactory<JadwalAfterTV, String>("pelanggan"));
		jamAfterCol.setCellValueFactory(new PropertyValueFactory<JadwalAfterTV, String>("jam"));
		namaAfterCol.setCellValueFactory(new PropertyValueFactory<JadwalAfterTV, String>("namakurir"));
			
		tbvJadwalPickupAfter.setEditable(true);
	}
	
	public static class JadwalTV{
		private IntegerProperty id;
		private SimpleStringProperty idPickup;
		private StringProperty pelanggan;
		private StringProperty jam;
		private StringProperty source;
		private StringProperty status;
		private BooleanProperty jalan;
		
		public JadwalTV(Integer id, String idPickup, String pelanggan, String jam, String source, String status, Boolean jalan){
			this.id = new SimpleIntegerProperty(id);
			this.idPickup = new SimpleStringProperty(idPickup);
			this.pelanggan = new SimpleStringProperty(pelanggan);
			this.jam = new SimpleStringProperty(jam);
			this.source = new SimpleStringProperty(source);
			this.status = new SimpleStringProperty(status);
			this.jalan = new SimpleBooleanProperty(jalan);
		}
		public String getSource(){
			return this.source.get();
		}
		public void setSource(String source){
			this.source.set(source);
		}
		public Integer getId() {
			return id.get();
		}
		public void setId(Integer id) {
			this.id.set(id);
		}
		public String getIdPickup() {
			return idPickup.get();
		}
		public void setIdPickup(String idPickup) {
			this.idPickup.set(idPickup);
		}
		public String getPelanggan() {
			return pelanggan.get();
		}
		public void setPelanggan(String pelanggan) {
			this.pelanggan.set(pelanggan);
		}
		public String getJam() {
			return jam.get();
		}
		public void setJam(String jam) {
			this.jam.set(jam);
		}
		public String getStatus() {
			return status.get();
		}
		public void setStatus(String status) {
			this.status.set(status);
		}
		public Boolean getJalan() {
			return jalan.get();
		}
		public void setJalan(Boolean jalan) {
			this.jalan.set(jalan);
		}
		public BooleanProperty jalanProperty() {
			return jalan;
		}
		public StringProperty sourceProperty(){
			return source;
		}
	}
	
	public static class JadwalAfterTV{
		private StringProperty pelanggan;
		private StringProperty jam;
		private StringProperty namakurir;
		
		public JadwalAfterTV(String pelanggan, String jam, String namakurir){
			this.pelanggan = new SimpleStringProperty(pelanggan);
			this.jam = new SimpleStringProperty(jam);
			this.namakurir = new SimpleStringProperty(namakurir);
		}
		public String getPelanggan(){
			return this.pelanggan.get();
		}
		public String getJam(){
			return this.jam.get();
		}
		public String getNamaKurir(){
			return this.namakurir.get();
		}
		public StringProperty pelangganProperty(){
			return this.pelanggan;
		}
		public StringProperty jamProperty(){
			return this.jam;
		}
		public StringProperty namakurirProperty(){
			return this.namakurir;
		}		
	}

	private void addMapping(Integer index, Integer row) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("index", index);
		map.put("row", row);
		map.put("flag", 0);
		
		pickup.add(map);
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	private void createGridJadwalKurir(Integer index, Integer row, Double posX, Double posY) {
		GridPane gridPane = new GridPane();
		gridPane.setLayoutX(posX);
		gridPane.setLayoutY(posY);
		gridPane.setPrefHeight(462.0);
		gridPane.setPrefWidth(465.0);
		
		ColumnConstraints columnConst = new ColumnConstraints();
		columnConst.setHgrow(Priority.SOMETIMES);		
		columnConst.setMinWidth(10.0);
		columnConst.setPrefWidth(100.0);
		
		RowConstraints rowConst1 = new RowConstraints();
		rowConst1.setMaxHeight(51.0);
		rowConst1.setMinHeight(10.0);
		rowConst1.setPrefHeight(10.0);
		rowConst1.setVgrow(Priority.SOMETIMES);
		
		RowConstraints rowConst2 = new RowConstraints();
		rowConst2.setMaxHeight(365.0);
		rowConst2.setMinHeight(10.0);
		rowConst2.setPrefHeight(165.0);
		rowConst2.setVgrow(Priority.SOMETIMES);
		
		RowConstraints rowConst3 = new RowConstraints();
		rowConst3.setMinHeight(10.0);
		rowConst3.setPrefHeight(30.0);
		rowConst3.setVgrow(Priority.SOMETIMES);		
		
		gridPane.getColumnConstraints().addAll(columnConst);
		gridPane.getRowConstraints().addAll(rowConst1, rowConst2, rowConst3);
		
		AnchorPane anchorPaneGridpane1 = new AnchorPane();
		anchorPaneGridpane1.setPrefHeight(200.0);
		anchorPaneGridpane1.setPrefWidth(200.0);			
		
		Label lbAnchorPaneGridPane1 = new Label("Pilih Kurir");
		Label hiddenIndex = new Label(index.toString());
		Label hiddenRow = new Label(row.toString());
		lbAnchorPaneGridPane1.setLayoutX(14.0);
		lbAnchorPaneGridPane1.setLayoutY(11.0);
		hiddenIndex.setVisible(false);
		hiddenRow.setVisible(false);
		@SuppressWarnings("rawtypes")
		ComboBox cmbAnchorPaneGridPane1 = new ComboBox();
		cmbAnchorPaneGridPane1.setLayoutX(77.0);
		cmbAnchorPaneGridPane1.setLayoutY(7.0);
		cmbAnchorPaneGridPane1.setPrefHeight(25.0);
		cmbAnchorPaneGridPane1.setPrefWidth(266.0);
		List<TrKurir> lstKurir = JadwalPickupService.getKurirLokalAsc();
		for (TrKurir kur : lstKurir) {
			cmbAnchorPaneGridPane1.getItems().add(kur.getNama());
		}
		new AutoCompleteComboBoxListener<TrKurir>(cmbAnchorPaneGridPane1);
		anchorPaneGridpane1.getChildren().addAll(hiddenIndex, hiddenRow, lbAnchorPaneGridPane1, cmbAnchorPaneGridPane1);
		
		AnchorPane anchorPaneGridpane2 = new AnchorPane();
		anchorPaneGridpane2.setPrefHeight(200.0);
		anchorPaneGridpane2.setPrefWidth(200.0);
		gridPane.setRowIndex(anchorPaneGridpane2, 1);
		@SuppressWarnings("rawtypes")
		TableView tvAnchorPaneGridPane2 = new TableView();
		tvAnchorPaneGridPane2.setPrefHeight(200.0);
		tvAnchorPaneGridPane2.setPrefWidth(343.0);
		anchorPaneGridpane2.setBottomAnchor(tvAnchorPaneGridPane2, 0.0);
		anchorPaneGridpane2.setLeftAnchor(tvAnchorPaneGridPane2, 0.0);
		anchorPaneGridpane2.setRightAnchor(tvAnchorPaneGridPane2, 0.0);
		anchorPaneGridpane2.setTopAnchor(tvAnchorPaneGridPane2, 0.0);
		@SuppressWarnings("rawtypes")
		TableColumn tbColumnAnchorPaneGridPane2A = new TableColumn("Pickup");
		tbColumnAnchorPaneGridPane2A.setPrefWidth(75.0);
		TableColumn tbColumnAnchorPaneGridPane2B = new TableColumn("Jam");
		tbColumnAnchorPaneGridPane2B.setPrefWidth(75.0);
		@SuppressWarnings("rawtypes")
		TableColumn tbColumnAnchorPaneGridPane2C = new TableColumn("Action");
		tbColumnAnchorPaneGridPane2C.setPrefWidth(162.0);
		tvAnchorPaneGridPane2.getColumns().addAll(tbColumnAnchorPaneGridPane2A, tbColumnAnchorPaneGridPane2B, tbColumnAnchorPaneGridPane2C);
		anchorPaneGridpane2.getChildren().addAll(tvAnchorPaneGridPane2);
		AnchorPane anchorPaneGridpane3 = new AnchorPane();
		anchorPaneGridpane3.setPrefHeight(200.0);
		anchorPaneGridpane3.setPrefWidth(200.0);
		gridPane.setRowIndex(anchorPaneGridpane3, 2);
		
		Button btnPickup2 = new Button("Pickup Jadwal");
		btnPickup2.setLayoutX(289.0);
		btnPickup2.setLayoutY(17.0);
		btnPickup2.setMnemonicParsing(false);	
		btnPickup2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Set<JadwalTV> selection = new HashSet<JadwalTV>(tbvJadwalPickup.getSelectionModel().getSelectedItems());
				for (JadwalTV jadwal : selection) {
					Integer id = GenericService.getMaxTable(TtJadwalPickup.class, "id");
					JadwalPickupService.insertJadwalPickup(
							id, 
							jadwal.getIdPickup(), 
							DateUtil.getNomorHariDalamSeminggu(DateUtil.convertToDateColumn(dtpickup.getValue())), 
							jadwal.getPelanggan(), 
							jadwal.getJam(), 
							(String) cmbAnchorPaneGridPane1.getSelectionModel().getSelectedItem(), 
							index, 
							DateUtil.convertToDateColumn(dtpickup.getValue())
							);
				}
				buildTableViewPickup(tvAnchorPaneGridPane2, cmbAnchorPaneGridPane1);
				loadMasterPickup();
			}

		});	
		Button btnBatal = new Button("Batal");
		btnBatal.setLayoutX(409.0);
		btnBatal.setLayoutY(17.0);
		btnBatal.setMnemonicParsing(false);	
//		anchorPaneGridpane3.getChildren().addAll(btnPickup, btnPickup2, btnBatal);
		anchorPaneGridpane3.getChildren().addAll(btnPickup2, btnBatal);
		gridPane.getChildren().addAll(anchorPaneGridpane1, anchorPaneGridpane2, anchorPaneGridpane3);
		anchorPaneContains.getChildren().add(gridPane);
		anchorPaneContains.setPrefHeight(posY+400.0);
	}	
	
	private void callDialog() {
		Stage stg = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			AnchorPane anchorDialogPhotoTerakhir = (AnchorPane) loader
					.load(this.getClass().getResource("DialogPickup.fxml"));
			Scene scnPhotoTerakhir = new Scene(anchorDialogPhotoTerakhir);
			stg.setScene(scnPhotoTerakhir);

			stg.setTitle("Pickup Jadwal Sendiri");
			stg.initModality(Modality.WINDOW_MODAL);
			stg.initOwner(WindowsHelper.primaryStage);
			stg.initStyle(StageStyle.UTILITY);
			stg.show();

			// tarik semua node dari FXML
			ComboBox cmbPelanggan = (ComboBox) scnPhotoTerakhir.lookup("#cmbPelanggan");
			
			TextField txtDialogJam = (TextField) scnPhotoTerakhir.lookup("#txtDialogJam");
			TextField txtDialogMin = (TextField) scnPhotoTerakhir.lookup("#txtDialogMin");
			
			Button btnSimpan = (Button) scnPhotoTerakhir.lookup("#btnSimpan");
			Button btnBatal = (Button) scnPhotoTerakhir.lookup("#btnBatal");
			
			List<TrPelanggan> lstPelanggan = PelangganService.getDataPelanggan();
			for (TrPelanggan trPelanggan : lstPelanggan) {
				cmbPelanggan.getItems().add(trPelanggan.getNamaAkun());
			}
			new AutoCompleteComboBoxListener<TrPelanggan>(cmbPelanggan);
					
			btnSimpan.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					TrPelanggan trPelanggan = PelangganService.getPelangganByName( (String) cmbPelanggan.getValue());
					if(trPelanggan!=null){						
						String lastMaxId = GenericService.getLastVarcharID("tt_pickup", "Id");
						System.out.println("--> lastMaxId : " + lastMaxId);
						Integer intNum = 0;
						if(lastMaxId==null){
							System.out.println("part 1");
							lastMaxId="S00000000";
							intNum = Integer.parseInt(lastMaxId.substring(2));
						}else{
							System.out.println("part 2");
							intNum = Integer.parseInt(lastMaxId.substring(2));
						}		
						String fixedID = String.format("%08d", intNum+1);
						JadwalPickupService.insertJadwalPickupLangsung(
								"S"+fixedID,
								trPelanggan.getKodePelanggan(),
								DateUtil.getNomorHariDalamSeminggu(DateUtil.convertToDateColumn(dtpickup.getValue())),
								txtDialogJam.getText() + ":" +	txtDialogMin.getText(),
								new Date(),
								new Date(),
								"0"								
								);
						loadMasterPickup();
						stg.close();
		        	}else{
		        		MessageBox.alert("Input Pelanggan salah, silahkan di koreksi ulang");
		        	}
				}
			});
			btnBatal.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					stg.close();
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
