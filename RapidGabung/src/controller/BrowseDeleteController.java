package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import com.sun.prism.impl.Disposer.Record;

import VO.BrowseSemuaDataVO;
import entity.TrPelanggan;
import javafx.application.Platform;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import service.BrowseSemuaDataService;
import service.PelangganService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.PerhitunganBiaya;
import util.WindowsHelper;
import utilfx.AutoCompleteComboBoxListener;

public class BrowseDeleteController implements Initializable {

	@FXML
	DatePicker txtTglMulai, txtTglAkhir;

	@FXML
	TableView<BrowseSemuaDataVO> listBrowse;

	@FXML
	private TextField txtCari;

	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colAwbData;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colCreated;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colLayanan;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colPengirim;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colTelp;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colAsalPaket;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colKdPerwakilan;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colTujuan;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colZona;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colPenerima;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colBFinal;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colBpFinal;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colBVolume;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colHarga;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colTotalBiaya;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colResiJNE;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colReseller;
	@FXML
	private TableColumn<BrowseSemuaDataVO, String> colIdKardus;
	@FXML
	private Button btnRestore;
	
	private ObservableList<BrowseSemuaDataVO> masterData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ManagedFormHelper.instanceController = this;

		txtTglMulai.setValue(LocalDate.now());
		txtTglAkhir.setValue(LocalDate.now());
		
		masterData.clear();
		settingListboksBrowseSemuaData(DateUtil.convertToDatabaseColumn(txtTglMulai.getValue()),
				DateUtil.convertToDatabaseColumn(txtTglAkhir.getValue()));
		btnRestore.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Set<BrowseSemuaDataVO> selection = new HashSet<BrowseSemuaDataVO>(listBrowse.getSelectionModel().getSelectedItems());
				if(selection.size()==0){
					MessageBox.alert("Pilih minimal 1 nomor resi yang akan ingin di restore sekaligus");
				}else{
					for (BrowseSemuaDataVO vo : selection) {
						BrowseSemuaDataService.deleteAwb(vo.getAwbData(),true);
					}
					masterData.clear();
					settingListboksBrowseSemuaData(DateUtil.convertToDatabaseColumn(txtTglMulai.getValue()),
							DateUtil.convertToDatabaseColumn(txtTglAkhir.getValue()));
				}
			}
		});
	}

	@FXML
	public void onClikCari() {
		masterData.clear();
		settingListboksBrowseSemuaData(DateUtil.convertToDatabaseColumn(txtTglMulai.getValue()),
				DateUtil.convertToDatabaseColumn(txtTglAkhir.getValue()));
	}

	public void settingListboksBrowseSemuaData(java.sql.Date dateAwal, java.sql.Date dateAkhir) {

		List<BrowseSemuaDataVO> tt = BrowseSemuaDataService.getBrowseSemuaData(dateAwal, dateAkhir, false);
		for (BrowseSemuaDataVO t : tt) {
			masterData.add(new BrowseSemuaDataVO(t.getAwbData(), t.getCreated(), t.getLayanan(), t.getPengirim(),
					t.getTelp(), t.getAsalPaket(), t.getKodePerwakilan(), t.getTujuan(), t.getZona(), t.getPenerima(),
					t.getBFinal(), t.getBpFinal(), t.getBVolume(), t.getHarga(), t.getTotalBiaya(), t.getResiJNE(),
					t.getReseller(), t.getIdKardus()));
		}
		
		colResiJNE.setCellFactory(TextFieldTableCell.forTableColumn());
		colResiJNE.setOnEditCommit(new EventHandler<CellEditEvent<BrowseSemuaDataVO, String>>() {
			public void handle(CellEditEvent<BrowseSemuaDataVO, String> t) {
				BrowseSemuaDataVO bro = (t.getTableView().getItems().get(t.getTablePosition().getRow()));
				BrowseSemuaDataService.updateResiJne(bro.getAwbData(), t.getNewValue());
			}
		});

		colCreated.setCellValueFactory(cellData -> cellData.getValue().createdProperty());
		colAwbData.setCellValueFactory(cellData -> cellData.getValue().awbDataProperty());
		colLayanan.setCellValueFactory(cellData -> cellData.getValue().layananProperty());
		colPengirim.setCellValueFactory(cellData -> cellData.getValue().pengirimProperty());
		colTelp.setCellValueFactory(cellData -> cellData.getValue().telpProperty());
		colAsalPaket.setCellValueFactory(cellData -> cellData.getValue().asalPaketProperty());
		colKdPerwakilan.setCellValueFactory(cellData -> cellData.getValue().kdPerwakilanProperty());
		colTujuan.setCellValueFactory(cellData -> cellData.getValue().tujuanProperty());
		colZona.setCellValueFactory(cellData -> cellData.getValue().zonaProperty());
		colBFinal.setCellValueFactory(cellData -> cellData.getValue().bFinalProperty());
		colBpFinal.setCellValueFactory(cellData -> cellData.getValue().bpFinalProperty());
		colBVolume.setCellValueFactory(cellData -> cellData.getValue().bVolumeProperty());
		colHarga.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
		colPenerima.setCellValueFactory(cellData -> cellData.getValue().penerimaProperty());
		colTotalBiaya.setCellValueFactory(cellData -> cellData.getValue().totalBiayaProperty());
		colResiJNE.setCellValueFactory(cellData -> cellData.getValue().resiJNEProperty());
		colReseller.setCellValueFactory(cellData -> cellData.getValue().resellerProperty());
		colIdKardus.setCellValueFactory(cellData -> cellData.getValue().idKardusProperty());
		
		FilteredList<BrowseSemuaDataVO> filteredData = new FilteredList<>(masterData, p -> true);

		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (data.getAwbData().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getLayanan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getPengirim().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getTujuan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getTelp().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getAsalPaket().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getKodePerwakilan().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getTujuan().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getZona().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getBFinal().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getBpFinal().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getBVolume().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getHarga().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getTotalBiaya().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getResiJNE().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getReseller().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getPenerima().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getIdKardus().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}

				return false;
			});
		});
		SortedList<BrowseSemuaDataVO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(listBrowse.comparatorProperty());
		listBrowse.setItems(sortedData);
		listBrowse.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
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
	
	

}