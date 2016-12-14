package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.Message;

import com.sun.prism.impl.Disposer.Record;

import VO.MapingJneVO;
import VO.ResiJneRapidVO;
import VO.ResiJneVO;
import entity.TtMappingResiJne;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import service.MapingJneService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.UploadUtil;
import util.WindowsHelper;
import javafx.stage.FileChooser;

public class MappingResiController implements Initializable {
	private FileChooser fileChooser = new FileChooser();
	@FXML
	private Hyperlink hyperLink;

	@FXML
	private TableView<ResiJneVO> tableResiJne;
	@FXML
	private TableView<ResiJneRapidVO> tableResiJneRapid;
	@FXML
	private TextField txtSearchTable1, txtSearchTable2;
	@FXML
	private TableColumn<ResiJneVO, String> colResiJne;
	@FXML
	private TableColumn<ResiJneVO, String> colPenerima;
	@FXML
	private TableColumn<ResiJneVO, String> colPengirim;
	@FXML
	private TableColumn<ResiJneVO, String> colTujuan;
	@FXML
	private TableColumn<ResiJneVO, String> colService;
	@FXML
	private TableColumn<ResiJneVO, Number> colHarga;

	@FXML
	private TableColumn<ResiJneRapidVO, String> colResiJne2;
	@FXML
	private TableColumn<ResiJneRapidVO, String> colPenerima2;
	@FXML
	private TableColumn<ResiJneRapidVO, String> colPengirim2;
	@FXML
	private TableColumn<ResiJneRapidVO, String> colTujuan2;
	@FXML
	private TableColumn<ResiJneRapidVO, String> colCreated2;
	@FXML
	private TableColumn<ResiJneRapidVO, Number> colHarga2;
	@FXML
	private TableColumn colAction, colAction2;

	@FXML
	DatePicker dpAwalJne, dpAkhirJne, dpAwalRapid, dpAkhirRapid;

	private ObservableList<ResiJneVO> masterData = FXCollections.observableArrayList();
	ObservableList<ResiJneRapidVO> masterJneRapid = FXCollections.observableArrayList();
	private String awbTableJne="";
	private int selectdIndex1;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ManagedFormHelper.instanceController = this;
		dpAwalJne.setValue(LocalDate.now());
		dpAkhirJne.setValue(LocalDate.now());
		dpAwalRapid.setValue(LocalDate.now());
		dpAkhirRapid.setValue(LocalDate.now());

		dpAkhirJne.setOnAction(event -> {
			masterData.clear();
			addTableJne(DateUtil.convertToDatabaseColumn(dpAwalJne.getValue()),
					DateUtil.convertToDatabaseColumn(dpAkhirJne.getValue()));
		});

		dpAkhirRapid.setOnAction(event -> {
			masterJneRapid.clear();
			MapingJneVO mapJne = new MapingJneVO();
			mapJne.setDpAwalRapid(DateUtil.convertToDatabaseColumn(dpAwalRapid.getValue()));
			mapJne.setDpAkhirRapid(DateUtil.convertToDatabaseColumn(dpAkhirRapid.getValue()));
			mapJne.setData(false);
			addTableJneRapid(mapJne);
		});
	}

	// ------------------------------------------------------------------add
	// Table
	public void addTableJne(java.sql.Date dateAwal, java.sql.Date dateAkhir) {
		List<TtMappingResiJne> tt = MapingJneService.getDataUploadJne(dateAwal, dateAkhir);
		for (TtMappingResiJne t : tt) {
			masterData.add(new ResiJneVO(t.getResiJne(), t.getPenerima(), t.getTujuan(), t.getPengirim(),
					t.getService(), t.getHarga(), ""));
		}
		colResiJne.setCellValueFactory(cellData -> cellData.getValue().kdJNEProperty());
		colPenerima.setCellValueFactory(cellData -> cellData.getValue().penerimaProperty());
		colPengirim.setCellValueFactory(cellData -> cellData.getValue().pengirimProperty());
		colTujuan.setCellValueFactory(cellData -> cellData.getValue().tujuanProperty());
		colService.setCellValueFactory(cellData -> cellData.getValue().serviceProperty());
		colHarga.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
		addButton();
		FilteredList<ResiJneVO> filteredData = new FilteredList<>(masterData, p -> true);

		txtSearchTable1.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (data.getKdJne().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton();
					return true;
				} else if (data.getPenerima().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton();
					return true;
				} else if (data.getPengirim().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton();
					return true;
				} else if (data.getService().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton();
					return true;
				} else if (data.getTujuan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton();
					return true;
				} else if (data.getHarga().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton();
					return true;
				}
				return false;
			});
		});
		SortedList<ResiJneVO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableResiJne.comparatorProperty());
		tableResiJne.setItems(sortedData);
	}

	// Chris
	@FXML
	public void onCLickUpload() {
		File file = fileChooser.showOpenDialog(WindowsHelper.primaryStage);
		if (file != null) {
			System.out.println("--> path : " + file.getAbsolutePath());
			hyperLink.setText(file.getAbsolutePath());
			UploadUtil uu = new UploadUtil(file.getAbsolutePath());
			uu.upload();
			addTableJne(DateUtil.convertToDatabaseColumn(dpAwalJne.getValue()),
					DateUtil.convertToDatabaseColumn(dpAkhirJne.getValue()));
		}
	}

	public void addTableJneRapid(MapingJneVO mapJne) {
		List<TtMappingResiJne> tt = new ArrayList<TtMappingResiJne>();
		if (mapJne.getData()) {
			tt = MapingJneService.getListCariJneRapid(mapJne);
		} else {
			tt = MapingJneService.getListCariJneRapidAll(mapJne);
		}
		for (TtMappingResiJne tu : tt) {
			String dateCreated = DateUtil.dateToStdDateLiteral(tu.getTglCreate());
			masterJneRapid.add(new ResiJneRapidVO(tu.getResiJne(), tu.getPenerima(), tu.getTujuan(), tu.getPengirim(),
					"", tu.getHarga(), dateCreated));
		}
		colResiJne2.setCellValueFactory(cellData -> cellData.getValue().kdJNEProperty());
		colPenerima2.setCellValueFactory(cellData -> cellData.getValue().penerimaProperty());
		colPengirim2.setCellValueFactory(cellData -> cellData.getValue().pengirimProperty());
		colTujuan2.setCellValueFactory(cellData -> cellData.getValue().tujuanProperty());
		colCreated2.setCellValueFactory(cellData -> cellData.getValue().createdProperty());
		colHarga2.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
		addButton2(masterJneRapid);
		FilteredList<ResiJneRapidVO> filteredData2 = new FilteredList<>(masterJneRapid, t -> true);

		txtSearchTable2.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData2.setPredicate(data2 -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (data2.getKdJne().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton2(masterJneRapid);
					return true;
				} else if (data2.getPenerima().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton2(masterJneRapid);
					return true;
				} else if (data2.getPengirim().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton2(masterJneRapid);
					return true;
				} else if (data2.getCreated().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton2(masterJneRapid);
					return true;
				} else if (data2.getTujuan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton2(masterJneRapid);
					return true;
				} else if (data2.getHarga().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					addButton2(masterJneRapid);
					return true;
				}
				return false;
			});
		});
		SortedList<ResiJneRapidVO> sortedData2 = new SortedList<>(filteredData2);
		sortedData2.comparatorProperty().bind(tableResiJneRapid.comparatorProperty());
		tableResiJneRapid.setItems(sortedData2);
	}

	// ------------------------------------------------------------------------->>>Cell
	// Button
	private class ButtonCell extends TableCell<Record, Boolean> {

		final Button cellButton = new Button("Cari");

		ButtonCell(final TableView tblView) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					selectdIndex1 = getTableRow().getIndex();
					ResiJneVO a = (ResiJneVO) tblView.getItems().get(selectdIndex1);
					awbTableJne = a.getKdJne();
					String penerima = a.getPenerima().substring(0, 3);
					String pengirim = a.getPengirim().substring(0, 3);
					String tujuan = a.getTujuan().substring(0, 3);
					MapingJneVO mapJne = new MapingJneVO();
					mapJne.setPenerima(penerima);
					mapJne.setPengirim(pengirim);
					mapJne.setTujuan(tujuan);
					mapJne.setData(true);
					masterJneRapid.clear();
					addTableJneRapid(mapJne);
				}
			});
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);
			}
		}

	}

	private class ButtonCell2 extends TableCell<Record, Boolean> {

		final Button cellButton2 = new Button("Submit");

		ButtonCell2(final TableView tblView, ObservableList<ResiJneRapidVO> masterJneRapid) {
			cellButton2.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					int selectdIndex = getTableRow().getIndex();
					ResiJneRapidVO a = (ResiJneRapidVO) tblView.getItems().get(selectdIndex);
					MapingJneService map = new MapingJneService();
					if (awbTableJne.equals("") || awbTableJne == null) {
						MessageBox.alert("Rasi Jne Belum di pilih");
					} else {
						Boolean update = map.updateResiJne(awbTableJne, a.getKdJne());
						masterData.clear();
						masterJneRapid.clear();
						MapingJneVO mapJne = new MapingJneVO();
						mapJne.setDpAwalRapid(DateUtil.convertToDatabaseColumn(dpAwalRapid.getValue()));
						mapJne.setDpAkhirRapid(DateUtil.convertToDatabaseColumn(dpAkhirRapid.getValue()));
						mapJne.setData(false);
						addTableJneRapid(mapJne);
						addTableJne(DateUtil.convertToDatabaseColumn(dpAwalRapid.getValue()), DateUtil.convertToDatabaseColumn(dpAkhirRapid.getValue()));
						awbTableJne = "";
					}
				}
			});
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton2);
			}
		}

	}

	// -------------------------------------------------------------------------------->>Add
	// Button
	public void addButton() {
		colAction.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});

		colAction.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

			@Override
			public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
				return new ButtonCell(tableResiJne);
			}

		});
	}

	public void addButton2(ObservableList<ResiJneRapidVO> masterJneRapid) {
		colAction2.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});

		colAction2.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

			@Override
			public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
				return new ButtonCell2(tableResiJneRapid, masterJneRapid);
			}

		});
	}

	// ------------------------------------------------------------------------------------>>
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
