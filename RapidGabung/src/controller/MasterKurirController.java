package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.prism.impl.Disposer.Record;

import entity.TrCabang;
import entity.TrKurir;
import entity.TrJabatan;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import service.GenericService;
import service.MasterKurirService;
import service.MasterPosisiService;
import service.MasterWilayahService;
import util.UtilityDateXML;
import util.DtoBroadcaster;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import utilfx.Tanggalan;

public class MasterKurirController implements Initializable {
	
	@FXML
	TextField txtNIK, txtNama, txtNoHP, txtNoKendaraan, txtKdPerwakilan, txtKeterangan;
	
	@FXML
	ComboBox cbJabatan, cbWilayah;
	
	@FXML
	Tanggalan txtTglMasuk;
	
	@FXML
	Button btnSimpan, btnReset;
	
	@FXML
	TableView listBoxMasterKurir;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Setting buttonTop
		// cont.setDisableButton(true, true, true, true, true, true, true, true,
		// true, true, true, true, true, true, true);
		// Set Objek kelas ini
		ManagedFormHelper.instanceController = this;
		settingListboksMasterKurir();
		cbJabatan.setValue("-");
		cbWilayah.setValue("-");
		
		Platform.runLater(new Runnable() {
		     @Override
		     public void run() {
		         txtNIK.requestFocus();
		     }
		});
		
		// init memanggil data jabatan pada combobox jabatan
		MasterPosisiService referensiJabatan = new MasterPosisiService();
        ObservableList<TrJabatan> listJabatan = FXCollections.observableArrayList(MasterPosisiService.getDataPosisi());
		for (TrJabatan i : listJabatan) {
			cbJabatan.getItems().add(i.getJabatan());
		
		}
		// init memanggil data jabatan pada combobox wilayah
		MasterWilayahService referensiWilayah = new MasterWilayahService();
        ObservableList<TrCabang> listWilayah = FXCollections.observableArrayList(MasterWilayahService.getDataCabang());
		for (TrCabang i : listWilayah) {
			cbWilayah.getItems().add(i.getNamaCabang());
		
		}
//        
	}

	public void settingListboksMasterKurir() {
		listBoxMasterKurir.getColumns().clear();
		MasterPosisiService serviceDokumen = new MasterPosisiService();
		// ObservableList<TpbDokumen> olDokumen =
		// FXCollections.observableArrayList(serviceDokumen.getDokumenByIdHeader(globalDataHeader.getId()));
		Long id = null;

		ObservableList<TrKurir> olDokumen = FXCollections.observableArrayList(MasterKurirService.getDataKurir());
		
		TableColumn col = new TableColumn("No");
		col.setPrefWidth(40.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrKurir, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrKurir, String> param) {
						// ReferensiDokumenService rDS = new
						// ReferensiDokumenService();
						 return new ReadOnlyObjectWrapper(listBoxMasterKurir.getItems().indexOf(param.getValue())+1);
					}
					
					
				}); 
		listBoxMasterKurir.getColumns().addAll(col);
		
		col = new TableColumn("NIK");
		col.setPrefWidth(120.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrKurir, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrKurir, String> param) {
						// ReferensiDokumenService rDS = new
						// ReferensiDokumenService();
						return new SimpleStringProperty(param.getValue().getNik());
					}
				});
		listBoxMasterKurir.getColumns().addAll(col);

		col = new TableColumn("Nama ");
		col.setPrefWidth(120.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrKurir, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrKurir, String> param) {
						// return null;
						return new SimpleStringProperty(param.getValue().getNama());
					}
				});
		listBoxMasterKurir.getColumns().addAll(col);
		//
		col = new TableColumn("Jabatan");
		col.setPrefWidth(150.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrKurir, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrKurir, String> param) {
						// return null;
						return new SimpleStringProperty(param.getValue().getIdJabatan());
					}
				});
		listBoxMasterKurir.getColumns().addAll(col);

		col = new TableColumn("Cabang");
		col.setPrefWidth(150.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrKurir, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrKurir, String> param) {
						// return null;
						return new SimpleStringProperty(param.getValue().getKodeCabang());
					}
				});
		listBoxMasterKurir.getColumns().addAll(col);

		col = new TableColumn("No. Kendaraan");
		col.setPrefWidth(100.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrKurir, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrKurir, String> param) {
						// return null;
						return new SimpleStringProperty(param.getValue().getNoKendaraan());
					}
				});
		listBoxMasterKurir.getColumns().addAll(col);

		col = new TableColumn("Tgl Masuk");
		col.setPrefWidth(120.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TrKurir, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TrKurir, String> param) {
						// return null;
						return new SimpleStringProperty(UtilityDateXML.toString(param.getValue().getTglMasuk()));
					}
				});
		listBoxMasterKurir.getColumns().addAll(col);
		
		col = new TableColumn("Action");
		col.setPrefWidth(110.0);
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
				return new ButtonCell(listBoxMasterKurir);
			}

		});
		listBoxMasterKurir.getColumns().add(col);
		listBoxMasterKurir.setItems(olDokumen);
	}

	
	@FXML
    public void onMouseClicked(MouseEvent evt)
    {
        if (evt.getClickCount() > 1) 
        {
            TrKurir dataHeader = (TrKurir) listBoxMasterKurir.getSelectionModel().getSelectedItem();
            System.out.println("---------------Data Header : "+dataHeader.getNik());
            btnSimpan.setText("UPDATE");
            if(dataHeader!=null)
            {
                DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "loadHeader", dataHeader);
                Stage stage = (Stage) listBoxMasterKurir.getScene().getWindow();
         //       stage.close();
            }
        }
    }
	
	@DtoListener(idDtoListener = "loadHeader")  //Dari search
    public void loadDtoListener(TrKurir userHeader){
	 txtNIK.setText(userHeader.getNik());
	 txtNama.setText(userHeader.getNama());
	 txtNoHP.setText(userHeader.getTelp());
	 cbJabatan.setValue(userHeader.getIdJabatan());
	 txtNoKendaraan.setText(userHeader.getNoKendaraan());
	 txtKdPerwakilan.setText(userHeader.getKodePerwakilan());
	 cbWilayah.setValue(userHeader.getKodeCabang());
	 txtTglMasuk.setTanggalText(userHeader.getTglMasuk());
	 txtKeterangan.setText(userHeader.getKeterangan());
	
	// System.out.println("-------------------------Kode Cabang : " +userHeader.getKodeCabang());
	 
    }

	
	@FXML
	public void onSave(Event evt){
		List<TrKurir> test = MasterKurirService.getDataKurirByID(txtNIK.getText());
		if(test.size()>0){
			// UPDATE TABLE
			MasterKurirService.updateDataKurir(txtNIK.getText(), txtNama.getText()
					, txtNoHP.getText(), cbJabatan.getValue(), txtNoKendaraan.getText()
					, txtKdPerwakilan.getText(), cbWilayah.getValue() 
					, txtTglMasuk.getTanggalText(), txtKeterangan.getText());
		}else{
			
			//SAVE
	
		TrKurir trkur = new TrKurir();
		trkur.setNik(txtNIK.getText());
		trkur.setNama(txtNama.getText());
		trkur.setTelp(txtNoHP.getText());
		trkur.setIdJabatan(cbJabatan.getValue().toString());
		trkur.setNoKendaraan(txtNoKendaraan.getText());
		trkur.setKodePerwakilan(txtKdPerwakilan.getText());
		trkur.setKodeCabang(cbWilayah.getValue().toString());
		trkur.setTglMasuk(txtTglMasuk.getTanggalText());
		trkur.setKeterangan(txtKeterangan.getText());
		
		Calendar cal = Calendar.getInstance();
		
		trkur.setTglCreate(cal.getTime());
		trkur.setTglUpdate(cal.getTime());
		trkur.setFlag(0);
		GenericService.save(TrKurir.class, trkur, true);
		}
		settingListboksMasterKurir();
		clearForm();
		btnSimpan.setText("SIMPAN");
//            showDataListboks();
    }
    
    @FXML
    public void onCancel(Event evt)
    {
        clearForm();
    }
    

	public void clearForm()

	{
		txtNIK.clear();
		txtNama.clear();
		txtNoHP.clear();
		cbJabatan.setValue("-");
//		cbJabatan.getSelectionModel().clearSelection();
		txtNoKendaraan.clear();
		txtKdPerwakilan.clear();
		cbWilayah.setValue("-");
//		cbWilayah.getSelectionModel().clearSelection();
		txtTglMasuk.clear();
		txtKeterangan.clear();
		btnSimpan.setDisable(false);

	}


	@DtoListener(idDtoListener = "backTop")
	public void backDtoListener() {
		try {
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("/controller/Menu.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			menuPage.prefWidthProperty().bind(WindowsHelper.primaryStage.widthProperty()); // FullScreen,
																							// hilangin
																							// klo
																							// g
																							// mau
																							// fullscreen
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private class ButtonCell extends TableCell<Record, Boolean> {

		final Button cellButton = new Button("Delete");

		ButtonCell(final TableView tblView) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					int selectdIndex = getTableRow().getIndex();
					TrKurir a = (TrKurir) tblView.getItems().get(selectdIndex);
					int[] dataButtonMessageBox = new int[2];
					dataButtonMessageBox[0] = MessageBox.BUTTON_OK;
					dataButtonMessageBox[1] = MessageBox.BUTTON_CANCEL;
					int hasilMessageBox = MessageBox.confirm("Apakah Yakin akan Melakukan Delete?",
							dataButtonMessageBox);
					if (hasilMessageBox == 5) { // cancel

					} else if (hasilMessageBox == 6) {
						MasterKurirService.showTableSetelahDelete(a.getNik());
						settingListboksMasterKurir();
						clearForm();
						;
					}
					
				}
			});
		}

	
//	private class ButtonCell extends TableCell<Record, Boolean> {
//
//		final Button cellButton = new Button("Edit");
//
//		ButtonCell(final TableView tblView) {
//			// ke sini
//			cellButton.setOnAction(new EventHandler<ActionEvent>() {
//				@Override
//				public void handle(ActionEvent t) {
//					int selectdIndex = getTableRow().getIndex();
//					// ini buat dapetin paramnya dari colum tersebut
//
//					TrKurir a = (TrKurir) tblView.getItems().get(selectdIndex);
//					// contohnya begitu
//					txtNIK.setText(a.getNik());
//					txtNama.setText(a.getNama());
//					txtNoHP.setText(a.getTelp());
//					cbJabatan.getSelectionModel().getSelectedItem();
//					txtNoKendaraan.setText(a.getNoKendaraan());
//					txtKdPerwakilan.setText(a.getKodePerwakilan());
//					cbWilayah.getSelectionModel().getSelectedItem();
////					String a = util.DateUtil.Date(getText(), dateFormat)
//					txtKeterangan.setText(a.getKeterangan());
//					
//					// kalau mau ambil param yang lain :
//					System.out.println("ambil param----------------->>" + a.getNik());
//					// kalau mas anggi sampe sini doank.
//				}
//			});
//		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {

				setGraphic(cellButton);
			}
		}

	}
}