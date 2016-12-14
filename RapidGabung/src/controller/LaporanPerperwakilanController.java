package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import VO.EntryDataShowVO;
import VO.ReportVO;
import entity.TrPelanggan;
import entity.TrPerwakilan;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import service.MasterPerwakilanService;
import service.PelangganService;
import service.ReportService;
import util.DateUtil;
import util.DtoListener;
import util.ExportJasper;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import util.formatRupiah;
import utilfx.AutoCompleteComboBoxListener;

public class LaporanPerperwakilanController implements Initializable {

	@FXML
	private Button btnPrintRResi, btnPrintRTagihan;

	@FXML
	private DatePicker dpAwal, dpAkhir;
	
	@FXML
	private TextField txtCari, txtTotalBiaya, txtJumlahPerwakilan, txtJumlahBarang, txtJumlahBerat;

	@FXML
	private TableView<ReportVO> tvPerwakilan;

	@FXML
	private TableColumn<ReportVO, Number> colNo;

	@FXML
	private TableColumn<ReportVO, String> colPerwakilan;
	
	@FXML
	private TableColumn<ReportVO, String> colIdKardusTerakhir;

	@FXML
	private TableColumn<ReportVO, Number> colJmlBarang;

	@FXML
	private TableColumn<ReportVO, Number> colBerat;

	@FXML
	private TableColumn<ReportVO, String> colTotalBiaya;

	private ObservableList<ReportVO> masterData = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox cmbPerwakilan;
	@FXML
	private ComboBox cmbPelanggan;
	@FXML 
	private CheckBox chkAll;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;
		
		dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());

		this.populateCombo();
		cmbPerwakilan.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				cmbPelanggan.getItems().add("All Pelanggan");
				List<TrPelanggan> lstPelanggan = PelangganService.getPelangganByKodePerwakilan(t1);
				for (TrPelanggan trPelanggan : lstPelanggan) {
					cmbPelanggan.getItems().add(trPelanggan.getKodePelanggan());
				}
//				cmbPelanggan.setValue("All Pelanggan");
				
				new AutoCompleteComboBoxListener<>(cmbPelanggan);
				
			}
		});
		chkAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//		        chk2.setSelected(!newValue);
		    	System.out.println(chkAll.isSelected());
		    	if(chkAll.isSelected()){
		    		cmbPelanggan.setValue(null);		    		
		    	}
		    	cmbPelanggan.setDisable(chkAll.isSelected());
		    }
		});
	}

	private void populateCombo() {
		cmbPerwakilan.getItems().add("All Perwakilan");
		List<TrPerwakilan> lstPerwakilan = MasterPerwakilanService.getAllPerwakilanCabangDistinct();
		for (TrPerwakilan trPerwakilan : lstPerwakilan) {
			cmbPerwakilan.getItems().add(trPerwakilan.getKodePerwakilan());
		}
		cmbPerwakilan.setValue("All Perwakilan");
		
//		cmbPelanggan.getItems().add("All Pelanggan");
		List<TrPelanggan> lstPelanggan = PelangganService.getAllPelanggan();
		for (TrPelanggan trPelanggan : lstPelanggan) {
			cmbPelanggan.getItems().add(trPelanggan.getKodePelanggan());
		}
//		cmbPelanggan.setValue("All Pelanggan");
		
		new AutoCompleteComboBoxListener<>(cmbPelanggan);
		
	}
	
	public void SetTable() {
		List<EntryDataShowVO> tt = ReportService.dataPerperwakilan(
				DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
				DateUtil.convertToDatabaseColumn(dpAkhir.getValue()), 
				(String) cmbPerwakilan.getSelectionModel().getSelectedItem().toString(),
				(String) cmbPelanggan.getSelectionModel().getSelectedItem());
		Integer no=1;
		int totalBiaya =0;
		int jumlahBarang = 0;
		int jumlahBerat = 0;
		for (EntryDataShowVO t : tt) {
			totalBiaya  += t.gettBiaya().intValueExact();
			jumlahBarang += t.getCount().intValueExact();
			jumlahBerat += t.getSumBerat().intValue();
			masterData.add(new ReportVO(
					no++, 
					t.getKdPerwakilan(), 
					t.getCount().intValueExact(),
					t.getSumBerat().intValue(), 
					String.valueOf(formatRupiah.formatIndonesia(t.gettBiaya().intValue())),
					t.getIdDataPaket()));
		}		
		txtJumlahBarang.setText(String.valueOf(jumlahBarang));
		txtJumlahBerat.setText(String.valueOf(jumlahBerat));
		txtTotalBiaya.setText(String.valueOf(formatRupiah.formatIndonesia(totalBiaya)));
		txtJumlahPerwakilan.setText(String.valueOf(tt.size()));
		colNo.setCellValueFactory(cellData -> cellData.getValue().noProperty());
		colPerwakilan.setCellValueFactory(cellData -> cellData.getValue().kdPerwakilanProperty());
		colJmlBarang.setCellValueFactory(cellData -> cellData.getValue().jumlahBarangProperty());
		colBerat.setCellValueFactory(cellData -> cellData.getValue().sumBeratProperty());
		colTotalBiaya.setCellValueFactory(cellData -> cellData.getValue().biayaProperty());
		colIdKardusTerakhir.setCellValueFactory(cellData -> cellData.getValue().resiKardusTerakhir());
		
		FilteredList<ReportVO> filteredData = new FilteredList<>(masterData, p -> true);

		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (data.getKdPerwakilan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getBiaya().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});
		
		SortedList<ReportVO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvPerwakilan.comparatorProperty());
		tvPerwakilan.setItems(sortedData);
	}
	
	@FXML
	public void  onClikCari(){
		masterData.clear();
		SetTable();
	}

	public void onClikPdf() {
		try {
			Date dateAwl = DateUtil.convertToDatabaseColumn(dpAwal.getValue());
			Date dateAkh = DateUtil.convertToDatabaseColumn(dpAkhir.getValue());
			
			String dateFile = DateUtil.getDateNotSeparator(dateAwl)+" sd "+DateUtil.getDateNotSeparator(dateAkh).substring(4);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("tgl_awal", DateUtil.convertToDatabaseColumn(dpAwal.getValue()));
			parameters.put("tgl_akhir", DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
			ExportJasper.JasperExportPdf(parameters, "C:/DLL/REPORT/perperwakilan.jrxml", "C:/DLL/REPORT/EXPORT/",
					"PerPerwakilan", dateFile);
			MessageBox.alert("Export Berhasil di Drive C:/DLL/REPORT/EXPORT/"
					+dateFile
					+" "+"PerPerwakilan.pdf");
		} catch (Exception e) {
			MessageBox.alert(e.getMessage());
		}

	}

	@FXML
	public void onClikExcel() {
		try {
			Date dateAwl = DateUtil.convertToDatabaseColumn(dpAwal.getValue());
			Date dateAkh = DateUtil.convertToDatabaseColumn(dpAkhir.getValue());
			
			String dateFile = DateUtil.getDateNotSeparator(dateAwl)+" sd "+DateUtil.getDateNotSeparator(dateAkh).substring(4);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("tgl_awal", DateUtil.convertToDatabaseColumn(dpAwal.getValue()));
			parameters.put("tgl_akhir", DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
			ExportJasper.JasperExportExcell(parameters, "C:/DLL/REPORT/perperwakilan.jrxml", "C:/DLL/REPORT/EXPORT/",
					"PerPerwakilan", dateFile);
			MessageBox.alert("Export Berhasil di Drive C:/DLL/REPORT/EXPORT/"
					+dateFile
					+" "+"PerPerwakilan.xls");
		} catch (Exception e) {
			MessageBox.alert(e.getMessage());
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
