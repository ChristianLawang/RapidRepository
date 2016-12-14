package popup;

import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import VO.EntryDataShowVO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.GenericService;
import service.MasterCabangService;
import util.DateUtil;
import util.EmailUtil;
import util.ExportToExcell;
import util.MessageBox;

public class PopUpKirimDataManifestController<T> implements Initializable {
	@FXML
	TableView<CabangTV> tvTagihanPelanggan;

	@FXML
	private DatePicker dateAwal, dateAkhir;

	@FXML
	private Button btnCari, btnClear, btnSentEmail, btnKeluar;
	
	@FXML
	private ProgressBar emailProgress;
	
	@FXML
	private Label lblEmailProgress;

	public String kdTujuan;

	private ObservableList<EntryDataShowVO> olDokumen = FXCollections.observableArrayList();
	
//	private ObservableList<CabangTV> cabangTV = FXCollections.observableArrayList();
	
	//FA
	private CheckBox chkAll = new CheckBox();
	
	Task emailWorker;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		dateAwal.setValue(LocalDate.now());
		dateAkhir.setValue(LocalDate.now());
		
//		lblEmailProgress.setText(cabangTV.size() + " data Manifest siap kirim");

		dateAkhir.setOnAction(event -> {
			settingListboksMasterCabang();
		});
		//FA
		chkAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
	           public void changed(ObservableValue<? extends Boolean> ov,
	             Boolean old_val, Boolean new_val) {
	        	   List<EntryDataShowVO> vo = MasterCabangService.getDataCabangTagihan(DateUtil.convertToDatabaseColumn(dateAwal.getValue()), DateUtil.convertToDatabaseColumn(dateAkhir.getValue()));
	        	   tvTagihanPelanggan = generateTableViewTagihan(tvTagihanPelanggan, vo);
//	        	   lblEmailProgress.setText(tvTagihanPelanggan.getItems().size() + " Manifest siap kirim");
	          }
	    });
	}

	//FA
	@FXML
	public void onClickSent() {
		int total = getNumberOfCheck(tvTagihanPelanggan.getItems());
//		System.out.println("cabangTV : " + cabangTV.size());
		if(total == 0){
			MessageBox.alert("Harap Pilih Perwakilan terlebih dahulu!");
		}else{
			final List<CabangTV> cabangTV = tvTagihanPelanggan.getItems();
			final Integer sizeCheckOnly = getNumberOfCheck(cabangTV);
			
			Map<String, Object> config = GenericService.getConfig();
			
			String username = config.get("email_username")==null?"":(String) config.get("email_username");
			String password = config.get("email_password")==null?"":(String) config.get("email_password");
			
			emailProgress.setProgress(0);
			
			emailWorker = new Task() {

				@Override
				protected Object call() throws Exception {
					// TODO Auto-generated method stub
					Integer intProgresVal = 0;
					for (CabangTV tv : cabangTV) {
						if (tv.getCheck()) {
							intProgresVal++;
                    		final Integer intVal = intProgresVal;
                    		Thread.sleep(100);
                    		
                    		List<EntryDataShowVO> en = MasterCabangService.getDataCabangExportExcell(
            						DateUtil.convertToDatabaseColumn(dateAwal.getValue()),
            						DateUtil.convertToDatabaseColumn(dateAkhir.getValue()), tv.getKodePerwakilan());
                    		
                    		ExportToExcell.exportToExcell3(en, tv.getKodePerwakilan(), "Data Manifest Master Cabang", DateUtil.dateToStdDateLiteral(DateUtil.getNow()));
                    		
                    		String toEmail = tv.getEmail();
                    		EmailUtil.kirimManifestWithEmail(
            						username, 
            						password, 
            						toEmail, 
            						"Data Manifest Master Cabang" + "-" + tv.getKodePerwakilan() + "-" + DateUtil.dateToStdDateLiteral(DateUtil.getNow()) + ".xls", 
            						"C:/DLL/REPORT/EXPORTEXCEL/" + "Data Manifest Master Cabang" + "-" + tv.getKodePerwakilan() + "-" + DateUtil.dateToStdDateLiteral(DateUtil.getNow()) + ".xls");
                    		
                    		Platform.runLater(new Runnable() {
        			            @Override public void run() {
        			            	lblEmailProgress.setText("("+intVal + "/" + sizeCheckOnly + "), sent to : " + toEmail + "");
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
//		MessageBox.alert("Export Berhasil Di Drive C:/DLL/REPORT/EXPORTEXCEL/");
	}

	@FXML
	public void onClickCari() {
		olDokumen.clear();
		settingListboksMasterCabang();
	}

	@FXML
	public void onClickClear() {
		dateAwal.setValue(LocalDate.now());
		dateAkhir.setValue(LocalDate.now());
	}
	
	//FA
	@FXML
	public void onClickKeluar() {
		Stage stage = (Stage) btnKeluar.getScene().getWindow();
		stage.close();
	}

	//FA
	public void settingListboksMasterCabang() {
		tvTagihanPelanggan.getColumns().clear();
		olDokumen = FXCollections.observableArrayList(MasterCabangService.getDataCabangTagihan(DateUtil.convertToDatabaseColumn(dateAwal.getValue()), DateUtil.convertToDatabaseColumn(dateAkhir.getValue())));
		generateTableViewTagihan(tvTagihanPelanggan, olDokumen);
		lblEmailProgress.setText("total ada " + olDokumen.size() + " data");
	}
	
	//FA
	public static class CabangTV {
		private BooleanProperty check;
		private StringProperty kodePerwakilan;
		private IntegerProperty jumlahAWB;
		private StringProperty email;
		
		public CabangTV(boolean check, String kodePerwakilan, int jumlahAWB, String email) {
			this.check = new SimpleBooleanProperty(check);
			this.kodePerwakilan = new SimpleStringProperty(kodePerwakilan);
			this.jumlahAWB = new SimpleIntegerProperty(jumlahAWB);
			this.email = new SimpleStringProperty(email);
		}

		public boolean getCheck() {
			return check.get();
		}

		public void setCheck(boolean check) {
			this.check.set(check);
		}

		public String getKodePerwakilan() {
			return kodePerwakilan.get();
		}

		public void setKodePerwakilan(String kodePerwakilan) {
			this.kodePerwakilan.set(kodePerwakilan);
		}

		public Integer getJumlahAWB() {
			return jumlahAWB.get();
		}

		public void setJumlahAWB(Integer jumlahAWB) {
			this.jumlahAWB.set(jumlahAWB);
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
		
		public StringProperty kodePerwakilanProperty() {
			return kodePerwakilan;
		}
		
		public IntegerProperty jumlahAWBProperty() {
			return jumlahAWB;
		}
		
		public StringProperty emailProperty() {
			return email;
		}
			
	}
	
	//FA
	private TableView<CabangTV> generateTableViewTagihan(TableView<CabangTV> tv, List<EntryDataShowVO> vo) {
		tv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<CabangTV> obs = FXCollections.observableArrayList();
		for (int i = 0; i < vo.size(); i++) {
			CabangTV row = new CabangTV(chkAll.isSelected(), vo.get(i).getKdPerwakilan(), vo.get(i).getCount().intValue(), vo.get(i).getEmail());
			obs.add(row);
		}
		
		tv.getColumns().clear();
		tv.getItems().clear();
		
		tv.setItems(obs);
		
		TableColumn<CabangTV, Boolean> checkCol = new TableColumn<CabangTV, Boolean> ("Kirim?");
		TableColumn<CabangTV, String> kodeCol = new TableColumn<CabangTV, String> ("Kode Perwakilan");
		TableColumn<CabangTV, BigInteger> jumlahCol = new TableColumn<CabangTV, BigInteger> ("Jumlah AWB");
		TableColumn<CabangTV, String> emailCol = new TableColumn<CabangTV, String> ("Email");
		
		tv.getColumns().addAll(checkCol, kodeCol, jumlahCol, emailCol);
		
		checkCol.setCellValueFactory(new PropertyValueFactory<CabangTV, Boolean>("check"));
		checkCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkCol));
		checkCol.setGraphic(chkAll);
		checkCol.setEditable(true);
		
		kodeCol.setCellValueFactory(new PropertyValueFactory<CabangTV, String>("kodePerwakilan"));
		jumlahCol.setCellValueFactory(new PropertyValueFactory<CabangTV, BigInteger>("jumlahAWB"));
		emailCol.setCellValueFactory(new PropertyValueFactory<CabangTV, String>("email"));
		
		tv.setEditable(true);
		
		return tv;
	}
	
	private Integer getNumberOfCheck(List<CabangTV> trv) {
//		cabangTV.clear();
		Integer num = 0;
		for (CabangTV tv : trv) {
			if(tv.getCheck()){
//				cabangTV.add(tv);
				num++;
			}
		}
		
		return num;
	}

}
