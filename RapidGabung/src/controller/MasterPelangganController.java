package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.hibernate.transaction.BTMTransactionManagerLookup;

import com.sun.prism.impl.Disposer.Record;

import VO.SMSExportVO;
import VO.TagihanVO;
import controller.MasterPickupController.PickupSubTV;
import controller.MasterPickupController.PickupTV;
import entity.TrCabang;
import entity.TrDiskon;
import entity.TrPelanggan;
import entity.TrSales;
import utilfx.Tanggalan;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import service.GenericService;
import service.MasterCabangService;
import service.MasterPickupService;
import service.PelangganService;
import util.DateUtil;
import util.DtoBroadcaster;
import util.DtoListener;
import util.EmailUtil;
import util.ManagedFormHelper;
import util.MessageBox;
import util.PDFUtil;
import util.UploadUtil;
import util.WindowsHelper;

public class MasterPelangganController implements Initializable {

	@FXML
	TableView listBoxPelanggan;

	@FXML
	javafx.scene.control.TextField txtKodePelanggan, txtNamaAkun, txtNamaPemilik, txtEmailPelanggan, txtLineID,
			txtTelpPelanggan, txtInstagram, txtDiskonRapid, txtDiskonJNE,  txtAlamatPelanggan, txtKeterangan ;

	@FXML
	Button btnSimpan, btnBatal;

	@FXML
	CheckBox chkSMS;

	@FXML
	Tanggalan dpMulaigabung, dpMulaiDiskon ;
	
	@FXML
	ComboBox cbJabatan1, txtNamaSales, cbJabatan2, txtReferensi;

	Task emailWorker;
	
	@FXML
	TableView<PelangganTV> tvTagihanPelanggan;
	
	
	// non FXML
	private CheckBox chkAll = new CheckBox();
	private DatePicker dateAwl = new DatePicker();
	private DatePicker dateAkhir = new DatePicker();
	private ComboBox cmbCabang = new ComboBox();
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ManagedFormHelper.instanceController = this;
		cbJabatan1.getItems().addAll("Sales", "Agen", "Referensi");
//		cbJabatan2.getItems().addAll("Sales", "Agen", "Referensi");
		settingListboksMasterPelanggan();
		setListenerEnterSimpan();
		
		cbJabatan1.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				cbJabatan2.getItems().removeAll(cbJabatan2.getItems());
				txtNamaSales.getItems().removeAll(txtNamaSales.getItems());
				txtReferensi.getItems().removeAll(txtReferensi.getItems());
				cbJabatan2.setDisable(false);
				txtReferensi.setDisable(false);
				if(t1.equals("Sales")){
					ObservableList<TrSales> listSales = FXCollections.observableArrayList();
					listSales.clear();
					listSales = FXCollections.observableArrayList(PelangganService.getDataSales2());
					for (TrSales i : listSales) {
						txtNamaSales.getItems().add(i.getNamaSales());
					}
					cbJabatan2.getItems().addAll("Referensi");
					ObservableList<TrPelanggan> listReferensi = FXCollections.observableArrayList();
					listReferensi.clear();
					listReferensi = FXCollections.observableArrayList(PelangganService.getDataReferensi());
					for (TrPelanggan i : listReferensi) {
						txtReferensi.getItems().add(i.getNamaAkun());
					}
					cbJabatan2.setValue("Referensi");
				}else if(t1.equals("Agen")){
					ObservableList<TrSales> listSales = FXCollections.observableArrayList();
					listSales.clear();
					listSales = FXCollections.observableArrayList(PelangganService.getDataAgen());
					for (TrSales i : listSales) {
						txtNamaSales.getItems().add(i.getNamaSales());
					}
					
					ObservableList<TrSales> listReferensi = FXCollections.observableArrayList();
					listReferensi.clear();
					listReferensi = FXCollections.observableArrayList(PelangganService.getDataSales2());
					for (TrSales i : listReferensi) {
						txtReferensi.getItems().add(i.getNamaSales());
					}
					cbJabatan2.getItems().addAll("Sales");
				}else if(t1.equals("Referensi")){
					ObservableList<TrPelanggan> listReferensi = FXCollections.observableArrayList();
					listReferensi.clear();
					listReferensi = FXCollections.observableArrayList(PelangganService.getDataReferensi());
					for (TrPelanggan i : listReferensi) {
						txtNamaSales.getItems().add(i.getNamaAkun());
					}
					cbJabatan2.setDisable(true);
					txtReferensi.setDisable(true);
				}
				
			}
		});
		chkAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
	           public void changed(ObservableValue<? extends Boolean> ov,
	             Boolean old_val, Boolean new_val) {
	        	   List<TrPelanggan> tr = PelangganService.getDataPelangganForTagihan(
							DateUtil.convertToDatabaseColumn(dateAwl.getValue()), 
							DateUtil.convertToDatabaseColumn(dateAkhir.getValue()), (String) cmbCabang.getSelectionModel().getSelectedItem().toString());
				   tvTagihanPelanggan = generateTableViewTagihanPelanggan(tvTagihanPelanggan, tr);
	          }
	        });

	}
	public static class DiskonTV {
		private StringProperty id;
		private StringProperty idDiskon;
		private StringProperty kode;
		private IntegerProperty diskonRapid;
		private IntegerProperty diskonJNE;
		private StringProperty tglMulaiDiskon;
		private IntegerProperty flag;
		
		public DiskonTV(
				String id, 
				String idDiskon,
				String kode, 
				Integer diskonRapid, 
				Integer diskonJNE, 
				String tglMulaiDiskon, 
				Integer flag){
			this.id = new SimpleStringProperty(id);
			this.idDiskon = new SimpleStringProperty(idDiskon);
			this.kode = new SimpleStringProperty(kode);
			this.diskonRapid = new SimpleIntegerProperty(diskonRapid);
			this.diskonJNE = new SimpleIntegerProperty(diskonJNE);
			this.tglMulaiDiskon = new SimpleStringProperty(tglMulaiDiskon);
			this.flag = new SimpleIntegerProperty(flag);					
		}

		public String getId() {
			return id.get();
		}

		public void setId(String id) {
			this.id.set(id);
		}
		
		public String getIdDiskon() {
			return idDiskon.get();
		}

		public void setIdDiskon(String idDiskon) {
			this.idDiskon.set(idDiskon);
		}

		public String getKode() {
			return kode.get();
		}

		public void setKode(String kode) {
			this.kode.set(kode);
		}

		public Integer getDiskonRapid() {
			return diskonRapid.get();
		}

		public void setDiskonRapid(Integer diskonRapid) {
			this.diskonRapid.set(diskonRapid);
		}

		public Integer getDiskonJNE() {
			return diskonJNE.get();
		}

		public void setDiskonJNE(Integer diskonJNE) {
			this.diskonJNE.set(diskonJNE);
		}

		public String getTglMulaiDiskon() {
			return tglMulaiDiskon.get();
		}

		public void setTglMulaiDiskon(String tglMulaiDiskon) {
			this.tglMulaiDiskon.set(tglMulaiDiskon);
		}

		public Integer getFlag() {
			return flag.get();
		}

		public void setFlag(Integer flag) {
			this.flag.set(flag);
		}
		
	}
	@FXML
	public void onClickDiskonSchedule(){
		Stage stgKirimTagihan = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			AnchorPane anchorDialogPhotoTerakhir;
			
				anchorDialogPhotoTerakhir = (AnchorPane) FXMLLoader
						.load(this.getClass().getResource("DialogGantiDiskon.fxml"));
			
			Scene scnKirimTagihan = new Scene(anchorDialogPhotoTerakhir);
			stgKirimTagihan.setScene(scnKirimTagihan);
			stgKirimTagihan.setTitle("Pengaturan Diskon");
			stgKirimTagihan.initModality(Modality.WINDOW_MODAL);
			stgKirimTagihan.initOwner(WindowsHelper.primaryStage);
			stgKirimTagihan.initStyle(StageStyle.UTILITY);
			stgKirimTagihan.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent window) {
					Stage innerStage = (Stage) window.getSource();
					final TableView<PelangganTV> tblPelanggan = populateThisPelanggan((TableView<PelangganTV>) innerStage.getScene().lookup("#tblPelanggan"));
					TableView<DiskonTV> tblDiskon = (TableView<DiskonTV>) innerStage.getScene().lookup("#tblDiskon");
					
					TextField txtRapidPercentage = (TextField) innerStage.getScene().lookup("#txtRapidPercentage");
					TextField txtJNEPercentage = (TextField) innerStage.getScene().lookup("#txtJNEPercentage");
					DatePicker dtAwal = (DatePicker) innerStage.getScene().lookup("#dtAwal");
					
					Button btnTambahkan = (Button) innerStage.getScene().lookup("#btnTambahkan");
					tblPelanggan.setOnMousePressed(new EventHandler<MouseEvent>() {
					    @Override 
					    public void handle(MouseEvent event) {
					    	PelangganTV selection = tblPelanggan.getSelectionModel().getSelectedItem();
					    	loadRightList(tblDiskon, selection.getKode());
					    }
					});
					btnTambahkan.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							PelangganTV selection = tblPelanggan.getSelectionModel().getSelectedItem();
							TrDiskon diskon = new TrDiskon();
							diskon.setIdDiskon(GenericService.getMaxTableString(TrDiskon.class, "idDiskon"));
							diskon.setKodePelanggan(selection.getKode());
							diskon.setDiskonRapid(Integer.parseInt(txtRapidPercentage.getText()));
							diskon.setDiskonJne(Integer.parseInt(txtJNEPercentage.getText()));
							diskon.setTglMulaiDiskon(DateUtil.convertToDateColumn(dtAwal.getValue()));
							diskon.setTglCreate(new Date());
							diskon.setTglUpdate(new Date());
							diskon.setFlag(0);
							GenericService.save(TrDiskon.class, diskon, true);
							loadRightList(tblDiskon, selection.getKode());
						}
					});
					
					
				}

				private TableView<PelangganTV> populateThisPelanggan(TableView<PelangganTV> tblPelanggan) {
					List<TrPelanggan> tr = PelangganService.getAllPelanggan();
					tblPelanggan.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					ObservableList<PelangganTV> pops = FXCollections.observableArrayList();
					for(Integer ind = 0;ind<tr.size();ind++){
						PelangganTV row = new PelangganTV(
								true,
								tr.get(ind).getKodePelanggan(), 
								tr.get(ind).getNamaAkun(), 
								tr.get(ind).getEmail()				
								);
						pops.add(row);
					}
					
					tblPelanggan.getColumns().clear();
					tblPelanggan.getItems().clear();
					
					tblPelanggan.setItems(pops);
					
					TableColumn<PelangganTV, String> namaCol = new TableColumn<PelangganTV, String>("Nama Pelanggan");
					namaCol.setPrefWidth(240.0);
					tblPelanggan.getColumns().addAll(namaCol);
					
					namaCol.setCellValueFactory(new PropertyValueFactory<PelangganTV, String>("nama"));
					return tblPelanggan;
				}
			});
			stgKirimTagihan.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadRightList(TableView<DiskonTV> tbl, String kode) {
		List<TrDiskon> lstDiskon = PelangganService.getDataDiskonByPelangganID(kode);
		
		tbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<DiskonTV> pops = FXCollections.observableArrayList();
		for(Integer ind = 0;ind<lstDiskon.size();ind++){
			DiskonTV row = new DiskonTV(
					new Integer(ind+1).toString(), 
					lstDiskon.get(ind).getIdDiskon(),
					lstDiskon.get(ind).getKodePelanggan(), 
					lstDiskon.get(ind).getDiskonRapid(), 
					lstDiskon.get(ind).getDiskonJne(), 
					DateUtil.getStdDateDisplay(lstDiskon.get(ind).getTglMulaiDiskon()), 
					lstDiskon.get(ind).getFlag());
			pops.add(row);
		}
		
		tbl.getColumns().clear();
		tbl.getItems().clear();
		
		tbl.setItems(pops);
		
		TableColumn<DiskonTV, String> noCol = new TableColumn<DiskonTV, String>("No.");
		noCol.setPrefWidth(38.0);
		TableColumn<DiskonTV, String> tglEfektifCol = new TableColumn<DiskonTV, String>("Tanggal Efektif");
		tglEfektifCol.setPrefWidth(139.0);
		TableColumn<DiskonTV, String> diskonRapidCol = new TableColumn<DiskonTV, String>("Diskon Rapid");
		diskonRapidCol.setPrefWidth(83.0);
		TableColumn<DiskonTV, String> diskonJNECol = new TableColumn<DiskonTV, String>("Diskon JNE");
		diskonJNECol.setPrefWidth(94.0);
		TableColumn<DiskonTV, String> actionCol = new TableColumn<DiskonTV, String>("Action");
		actionCol.setPrefWidth(144.0);
		Callback<TableColumn<DiskonTV, String>, TableCell<DiskonTV, String>> cellFactory = //
                new Callback<TableColumn<DiskonTV, String>, TableCell<DiskonTV, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<DiskonTV, String> param ){
                        final TableCell<DiskonTV, String> cell = new TableCell<DiskonTV, String>(){

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
                    						DiskonTV row = getTableView().getItems().get( getIndex() );
                    						PelangganService.setDiskonFlag(row.getIdDiskon(),1);
                    						loadRightList(tbl, kode);
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
        tbl.getColumns().addAll(noCol, tglEfektifCol, diskonRapidCol, diskonJNECol, actionCol);
		noCol.setCellValueFactory(new PropertyValueFactory<DiskonTV, String>("id"));
		tglEfektifCol.setCellValueFactory(new PropertyValueFactory<DiskonTV, String>("tglMulaiDiskon"));
		diskonRapidCol.setCellValueFactory(new PropertyValueFactory<DiskonTV, String>("diskonRapid"));
		diskonJNECol.setCellValueFactory(new PropertyValueFactory<DiskonTV, String>("diskonJNE"));
	}
	
	public void settingListboksMasterPelanggan() {
		listBoxPelanggan.getColumns().clear();
		PelangganService serviceDokumen = new PelangganService();
		Long id = null;

		ObservableList<TrPelanggan> olDokumen = FXCollections.observableArrayList(PelangganService.getDataPelanggan());
		TableColumn col = new TableColumn("Kode");
		col.setPrefWidth(120.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrPelanggan, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrPelanggan, String> param) {
						return new SimpleStringProperty(param.getValue().getKodePelanggan());
					}
				});
		listBoxPelanggan.getColumns().addAll(col);

		col = new TableColumn("Nama Pelanggan");
		col.setPrefWidth(150.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrPelanggan, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrPelanggan, String> param) {
						// return null;
						return new SimpleStringProperty(param.getValue().getNamaPemilik());
					}
				});
		listBoxPelanggan.getColumns().addAll(col);

		col = new TableColumn("Email");
		col.setPrefWidth(180.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrPelanggan, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrPelanggan, String> param) {
						return new SimpleStringProperty(param.getValue().getEmail());
					}
				});
		listBoxPelanggan.getColumns().addAll(col);

		col = new TableColumn("Action");
		col.setPrefWidth(65.0);
		col.setStyle("-fx-alignment: CENTER;");
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});

		col.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

			@Override
			public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
				// ini button nya(dia kan panggil function clik)
				return new ButtonCell(listBoxPelanggan);
			}

		});
		listBoxPelanggan.getColumns().add(col);

		listBoxPelanggan.setItems(olDokumen);
	}

	@FXML
	public void onMouseClicked(MouseEvent evt) {
		if (evt.getClickCount() > 1) {
			TrPelanggan dataHeader = (TrPelanggan) listBoxPelanggan.getSelectionModel().getSelectedItem();
			System.out.println("---------------Data Header : " + dataHeader.getKodePelanggan());
			txtKodePelanggan.setDisable(true);
			txtNamaAkun.requestFocus();
			if (dataHeader != null) {
				DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "loadHeader", dataHeader);
				Stage stage = (Stage) listBoxPelanggan.getScene().getWindow();
				
			}
		}
	}

	@DtoListener(idDtoListener = "loadHeader") // Dari search
	public void loadDtoListener(TrPelanggan userHeader) {
		txtKodePelanggan.setText(userHeader.getKodePelanggan());
		txtNamaAkun.setText(userHeader.getNamaAkun());
		txtNamaPemilik.setText(userHeader.getNamaPemilik());
		txtEmailPelanggan.setText(userHeader.getEmail());
		txtTelpPelanggan.setText(userHeader.getTelp());
		txtAlamatPelanggan.setText(userHeader.getAlamat());
		txtLineID.setText(userHeader.getLine());
		txtInstagram.setText(userHeader.getInstagram());
		txtKeterangan.setText(userHeader.getKeterangan());
		txtDiskonRapid.setText(userHeader.getDiskonRapid().toString());
		txtDiskonJNE.setText(userHeader.getDiskonJne().toString());
		dpMulaiDiskon.setTanggalText(userHeader.getTglMulaiDiskon());
		txtNamaSales.setValue(userHeader.getNamaSales());
		txtReferensi.setValue(userHeader.getReferensi());
		dpMulaigabung.setTanggalText(userHeader.getTglGabung());
		cbJabatan1.setValue(userHeader.getJabatan1());
		cbJabatan2.setValue(userHeader.getJabatan2());
	}

	@FXML
	public void onSave(Event evt) {
		List<TrPelanggan> test = PelangganService.getDataPelangganByID(txtKodePelanggan.getText());
		if(test.size()>0){
			// UPDATE TABLE
			PelangganService.updateDataPelanggan(txtKodePelanggan.getText()
					, txtNamaAkun.getText(), txtNamaPemilik.getText()
					, txtEmailPelanggan.getText(), txtTelpPelanggan.getText()
					, txtAlamatPelanggan.getText()
					, txtLineID.getText(), txtInstagram.getText(), txtKeterangan.getText()
					, Integer.parseInt(txtDiskonRapid.getText().toString())
					, Integer.parseInt(txtDiskonJNE.getText().toString()), txtNamaSales.getValue(), txtReferensi.getValue()
					, dpMulaiDiskon.getTanggalText(), dpMulaigabung.getTanggalText(), cbJabatan1.getValue(), cbJabatan2.getValue());
		}else{
			
			//SAVE
			TrPelanggan trpel = new TrPelanggan();
			trpel.setKodePelanggan(txtNamaAkun.getText());
			trpel.setNamaAkun(txtNamaAkun.getText());
			trpel.setNamaPemilik(txtNamaPemilik.getText());
			trpel.setEmail(txtEmailPelanggan.getText());
			trpel.setTelp(txtTelpPelanggan.getText());
			trpel.setAlamat(txtAlamatPelanggan.getText());
			trpel.setLine(txtLineID.getText());
			trpel.setInstagram(txtInstagram.getText());
			trpel.setKeterangan(txtKeterangan.getText());
			trpel.setDiskonRapid(Integer.parseInt(txtDiskonRapid.getText()));
			trpel.setDiskonJne(Integer.parseInt(txtDiskonJNE.getText()));
			trpel.setTglMulaiDiskon(dpMulaiDiskon.getTanggalText());
			trpel.setNamaSales(txtNamaSales.getValue().toString());
			trpel.setTglCreate(DateUtil.getNow());
		    trpel.setReferensi(txtReferensi.getValue()==null?"":txtReferensi.getValue().toString());
			trpel.setTglGabung(dpMulaigabung.getTanggalText());
			trpel.setFlag(0);
			trpel.setJabatan1(cbJabatan1.getValue().toString());
			trpel.setJabatan2(cbJabatan2.getValue()==null?"":cbJabatan2.getValue().toString());
			System.out.println("------------------->>>> Pelanggan Kode : "+trpel.getKodePelanggan());
			GenericService.save(TrPelanggan.class, trpel, true); 
			settingListboksMasterPelanggan();
		}
		settingListboksMasterPelanggan();
	clearForm();
	txtKodePelanggan.setDisable(false);
	btnSimpan.setText("SUBMIT");	
}

	@FXML
	public void onCancel(Event evt) {
		clearForm();
	}

	public void clearForm()

	{
		txtKodePelanggan.clear();
		txtNamaAkun.clear();
		txtNamaPemilik.clear();
		txtEmailPelanggan.clear();
		txtTelpPelanggan.clear();
		txtAlamatPelanggan.clear();
		txtLineID.clear();
		txtInstagram.clear();
		txtKeterangan.clear();
		txtDiskonRapid.setText("0");
		txtDiskonJNE.setText("0");
		dpMulaiDiskon.clear();
		txtNamaSales.setValue("");
		txtReferensi.setValue("");
		dpMulaigabung.clear();
		btnSimpan.setText("SIMPAN");
		txtKodePelanggan.setDisable(false);
		cbJabatan2.setValue("");
		cbJabatan1.getItems().removeAll(cbJabatan1.getItems());
		cbJabatan1.getItems().addAll("Sales", "Agen", "Referensi");
		}
    
	@FXML
	public void onExit(Event evt) {
		backDtoListener();
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
					onSave(event);
				}
			}
		};
		dpMulaigabung.setOnKeyPressed(eH);
		btnSimpan.setOnKeyPressed(eH);
	}

	private class ButtonCell extends TableCell<Record, Boolean> {

		final Button cellButton = new Button("Delete");

		ButtonCell(final TableView tblView) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					int selectdIndex = getTableRow().getIndex();
					TrPelanggan a = (TrPelanggan) tblView.getItems().get(selectdIndex);
					int[] dataButtonMessageBox = new int[2];
					dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
					dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
					int hasilMessageBox = MessageBox.confirm("Apakah Yakin akan Melakukan Delete?",
							dataButtonMessageBox);
					if (hasilMessageBox == 5) { // cancel

					} else if (hasilMessageBox == 6) {
						PelangganService.showTableSetelahDelete(a.getKodePelanggan());
						settingListboksMasterPelanggan();
					}
					
				}
			});
		}


		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {

				setGraphic(cellButton);
			}
		}

	}

	@DtoListener(idDtoListener = "backTop")
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

	// chris
	@FXML
	public void onClickDownloadSMS(Event evt){
		UploadUtil uu = new UploadUtil(null); // khusus buat export excel
		
		PelangganService ps = new PelangganService();
		
		List<SMSExportVO> dataRapid = PelangganService.getSMSRapid();
		List<SMSExportVO> dataJNE = PelangganService.getSMSJNE();
		
		try {
			UploadUtil.generateExcel(dataRapid, "C:/DLL/REPORT/SMSRapid.XLSX"); // for rapid
			UploadUtil.generateExcel(dataJNE, "C:/DLL/REPORT/SMSJNE.XLSX"); // for JNE
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// chris
	@FXML
	public void onClickKirimTagihan(Event evt) {
		Stage stgKirimTagihan = new Stage();
		try {			
			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			AnchorPane anchorDialogPhotoTerakhir = (AnchorPane) FXMLLoader
					.load(this.getClass().getResource("DialogKirimTagihan.fxml"));
			Scene scnKirimTagihan = new Scene(anchorDialogPhotoTerakhir);
			stgKirimTagihan.setScene(scnKirimTagihan);
				stgKirimTagihan.setTitle("Kirim Tagihan");
			stgKirimTagihan.initModality(Modality.WINDOW_MODAL);
			stgKirimTagihan.initOwner(WindowsHelper.primaryStage);
			stgKirimTagihan.initStyle(StageStyle.UTILITY);
				stgKirimTagihan.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent window) {
					Stage innerStage = (Stage) window.getSource();
					tvTagihanPelanggan = (TableView<PelangganTV>) innerStage.getScene()
							.lookup("#tvTagihanPelanggan");
					dateAwl = (DatePicker) innerStage.getScene().lookup("#dateAwal");
					dateAkhir = (DatePicker) innerStage.getScene().lookup("#dateAkhir");
					cmbCabang = (ComboBox) innerStage.getScene().lookup("#cmbCabang");
					ProgressBar emailProgress = (ProgressBar) innerStage.getScene().lookup("#emailProgress");
					Label lblEmailProgress = (Label) innerStage.getScene().lookup("#lblEmailProgress");
					dateAwl.setValue(LocalDate.now());
					dateAkhir.setValue(LocalDate.now());

					cmbCabang.getItems().add("All Cabang");
					List<TrCabang> lstCabang = MasterCabangService.getAllPerwakilanCabangDistinct();
					for (TrCabang trCabang : lstCabang) {
						cmbCabang.getItems().add(trCabang.getKodePerwakilan());
					}
					cmbCabang.setValue("All Cabang");

					Date a = DateUtil.convertToDatabaseColumn(dateAwl.getValue());
					Date b = DateUtil.convertToDatabaseColumn(dateAkhir.getValue());
					System.out.println("-----------------------"+a +"/"+b);
					
					List<TrPelanggan> tr = PelangganService.getDataPelangganForTagihan(
							DateUtil.convertToDatabaseColumn(dateAwl.getValue()), 
							DateUtil.convertToDatabaseColumn(dateAkhir.getValue()), (String) cmbCabang.getSelectionModel().getSelectedItem().toString());
					tvTagihanPelanggan = generateTableViewTagihanPelanggan(tvTagihanPelanggan, tr);

					lblEmailProgress.setText(tr.size() + " tagihan siap kirim");
					Button btnCari = (Button) innerStage.getScene().lookup("#btnCari");
					btnCari.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							
							List<TrPelanggan> tr = PelangganService.getDataPelangganForTagihan(
									DateUtil.convertToDatabaseColumn(dateAwl.getValue()), 
									DateUtil.convertToDatabaseColumn(dateAkhir.getValue()), (String) cmbCabang.getSelectionModel().getSelectedItem().toString());
							tvTagihanPelanggan = generateTableViewTagihanPelanggan(tvTagihanPelanggan, tr);
							lblEmailProgress.setText("total ada " + tr.size() + " tagihan");
						}
					});
					
					Button btnClear = (Button) innerStage.getScene().lookup("#btnClear");
					btnClear.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							dateAwl.setValue(LocalDate.now());
							dateAkhir.setValue(LocalDate.now());
							tvTagihanPelanggan.getItems().clear();
						}
					});
					
					Button btnKeluar = (Button) innerStage.getScene().lookup("#btnKeluar");
					btnKeluar.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							stgKirimTagihan.close();
						}
					});
					
					Button btnSentEmail = (Button) innerStage.getScene().lookup("#btnSentEmail");
					btnSentEmail.setOnAction(new EventHandler<ActionEvent>() {
							@Override
						public void handle(ActionEvent event) {
							final List<PelangganTV> trv = tvTagihanPelanggan.getItems();
							final Integer sizeCheckOnly = getNumberOfCheck(trv); 

//							String username = "rapidexpress.id@gmail.com";
//							String password = "rapid150515";	
//							String username = "rapid.jakarta@gmail.com";
//							String password = "jktjktjkt";
							
							Map<String, Object> config = GenericService.getConfig();
							
							String username = config.get("email_username")==null?"":(String) config.get("email_username");
							String password = config.get("email_password")==null?"":(String) config.get("email_password");
							
							String pdfFolder = generateOrReplaceFolder(DateUtil.convertToDateColumn(dateAwl.getValue()));
							
							emailProgress.setProgress(0);

								emailWorker = 

						                new Task() {
											@Override
						                    protected Object call() throws Exception {
						                    	Integer intProgresVal = 0;
						                    	for (PelangganTV trPelanggan : trv) {
						                    		if(trPelanggan.getCheck()){
						                    			intProgresVal++;
							                    		final Integer intVal = intProgresVal;
							                    		Thread.sleep(100);
							        					PDFUtil pdf = new PDFUtil();
							        					EmailUtil email = new EmailUtil();
							        					
							        					String path = 
							        							pdfFolder+"/"+trPelanggan.getKode()+"-"+
							        							DateUtil.convertToDatabaseColumn(dateAwl.getValue())+"-"+
							        							DateUtil.convertToDatabaseColumn(dateAkhir.getValue())+".PDF";
							        					List<TagihanVO> vo = PelangganService
							        							.getNotifikationEmailPelanggan(
							        									trPelanggan.getKode(), 
							        									DateUtil.convertToDatabaseColumn(dateAwl.getValue()), 
							        									DateUtil.convertToDatabaseColumn(dateAkhir.getValue()) 
							        								);
							        					
							        					for (TagihanVO test : vo) {
							        						test.print();
														}
							        					String created = DateUtil.getDateRipTime(vo.get(0).getCreated());
//							        					String created = DateUtil.getDateRipTime(new Date());
							        					
							        					pdf.generateAndCreateFilePDFForEmailKurirInNew(
							        							vo, 
							        							path, 
							        							trPelanggan, 
							        							DateUtil.convertToDatabaseColumn(dateAwl.getValue()).toString(),
							        							DateUtil.convertToDatabaseColumn(dateAkhir.getValue()).toString(),
							        							""		        							
							        							);
							        					
							        					String text = getBodyEmail(vo);
//							        					String toUser = "zechreich2015@gmail.com";
							        					String toUser = trPelanggan.getEmail();
							        					
							        					email.setEmail(
							        							username, 
							        							password,
							        							toUser,
							        							text,
							        							path, 
							        							trPelanggan.getNama(),
							        							created
							        						);
							        					
							        					Platform.runLater(new Runnable() {
							        			            @Override public void run() {
							        			            	lblEmailProgress.setText("("+intVal + "/" + sizeCheckOnly + "), sent to : " + toUser + "");
							        			            }
							        			        });
							        					updateMessage("2000 milliseconds");
							        					updateProgress(intProgresVal, sizeCheckOnly);
							        				}
						                    	}
						                    		
						                        return true;
											}
						                };

						                emailProgress.progressProperty().unbind();
						                emailProgress.progressProperty().bind(emailWorker.progressProperty());
						               
						                emailWorker.messageProperty().addListener(new ChangeListener<String>() {
						                	@Override
											public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

						                	}
						                });
						                
						                new Thread(emailWorker).start();
									}
								});
					        }

				
					});
				System.out.println("--> end");
				stgKirimTagihan.show();

			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

	private Integer getNumberOfCheck(List<PelangganTV> trv) {
		Integer num = 0;
		for (PelangganTV pelangganTV : trv) {
			if(pelangganTV.getCheck()){
				num++;
			}
		}
		
		return num;
	}
	
	private TableView<PelangganTV> generateTableViewTagihanPelanggan(
			TableView<PelangganTV> tv, 
			List<TrPelanggan> tr) {
		tv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<PelangganTV> pops = FXCollections.observableArrayList();
		for(Integer ind = 0;ind<tr.size();ind++){
			PelangganTV row = new PelangganTV(
					chkAll.isSelected(), 
					tr.get(ind).getKodePelanggan(), 
					tr.get(ind).getNamaAkun(), 
					tr.get(ind).getEmail()				
					);
			pops.add(row);
		}
		
		tv.getColumns().clear();
		tv.getItems().clear();
		
		tv.setItems(pops);
		
		TableColumn<PelangganTV, Boolean> checkCol = new TableColumn<PelangganTV, Boolean>("Kirim?");
		TableColumn<PelangganTV, String> kodeCol = new TableColumn<PelangganTV, String>("Kode Pelanggan");
		TableColumn<PelangganTV, String> namaCol = new TableColumn<PelangganTV, String>("Nama Pelanggan");
		TableColumn<PelangganTV, String> emailCol = new TableColumn<PelangganTV, String>("Email");
		
		tv.getColumns().addAll(checkCol, kodeCol, namaCol, emailCol);
		
		checkCol.setCellValueFactory(new PropertyValueFactory<PelangganTV, Boolean>("check"));
		checkCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkCol));
		checkCol.setGraphic(chkAll);
		checkCol.setEditable(true);
		
		kodeCol.setCellValueFactory(new PropertyValueFactory<PelangganTV, String>("kode"));
		namaCol.setCellValueFactory(new PropertyValueFactory<PelangganTV, String>("nama"));
		emailCol.setCellValueFactory(new PropertyValueFactory<PelangganTV, String>("email"));
		
		tv.setEditable(true);
		
		return tv;
	}
	
	public static class PelangganTV {
		private BooleanProperty check;
		private StringProperty kode;
		private StringProperty nama;
		private StringProperty email;
		
		public PelangganTV(boolean check, String kode, String nama, String email){
			this.check = new SimpleBooleanProperty(check);
			this.kode = new SimpleStringProperty(kode);
			this.nama = new SimpleStringProperty(nama);
			this.email = new SimpleStringProperty(email);
		}

		public boolean getCheck() {
			return check.get();
		}

		public void setCheck(boolean check) {
			this.check.set(check);
		}

		public String getKode() {
			return kode.get();
		}

		public void setKode(String kode) {
			this.kode.set(kode);
		}

		public String getNama() {
			return nama.get();
		}

		public void setNama(String nama) {
			this.nama.set(nama);
		}

		public String getEmail() {
			return email.get();
		}

		public void setEmail(String email) {
			this.email.set(email);
		}
		
		public BooleanProperty checkProperty() {
			return check;
		}
		
		public StringProperty kodeProperty() {
			return kode;
		}
		
		public StringProperty namaProperty() {
			return nama;
		}
		
		public StringProperty emailProperty() {
			return email;
		}
	}
	
	private String generateOrReplaceFolder(Date date) {
		
		System.out.println("date : " + date);
		
		String strYear = DateUtil.getYearOnly(new Date());
		String strMonth = DateUtil.getMonthOnly(new Date());
//		String strDay = DateUtil.getDayOnlyTommorow(new Date());
		String strDay = DateUtil.getDayOnly(date);
		
		String path = "C:/DLL/PDF/"+strYear + "/" + strMonth+"/"+strDay;
		
		File dir = new File(path);
		dir.mkdirs();
		
		return path;
	}
	
	// chris
	private String getBodyEmail(List<TagihanVO> vo) {
		
		String text = "";
		String isList = "[!LIST]";
		try {
			BufferedReader reader = new BufferedReader(new FileReader("c:/dll/bodyemail.txt"));
			
			String line;

			while((line = reader.readLine()) != null){ 
				if(line.equals(isList)){
//					Collections.sort(vo, new Comparator<TagihanVO>(){
//						@Override
//						public int compare(TagihanVO vo1, TagihanVO vo2) {
//							return vo1.getPenerima().compareToIgnoreCase(vo2.getPenerima());
//						}
//					});
					for (TagihanVO tagihanVO : vo) {
						text+="\r\n";
						text+=tagihanVO.getAwb() + " - " + tagihanVO.getPenerima();
					}
				}else{
					text+="\r\n";
					text+=line;
				}
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return text;
	}
	
	// chris
	public void onClickExportToCSV(Event evt){
		UploadUtil uu = new UploadUtil(null); 
		try {
			UploadUtil.generateExcel("C:/DLL/REPORT/ExportRapid.XLSX");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// dipanggil dari LoginController
	public static void autoSchedulerDiskon() {
		
	}	
}
