package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import VO.EntryDataShowVO;
import VO.TerimaPerwakilanVO;
import entity.TrUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import service.TerimaPerwakilanService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import utilfx.DateTimePicker;

public class TerimaPerwakilanController implements Initializable {

	@FXML
	private TextField txtIdKardus, txtAwb;

	@FXML
	private Button btnSave;

	@FXML
	private TableColumn<TerimaPerwakilanVO, Number> colNo;

	@FXML
	private TableColumn<TerimaPerwakilanVO, String> colKdPerwakilan;
	
	@FXML
	private TableColumn<TerimaPerwakilanVO, String> colKdKardusSub;

	@FXML
	private TableColumn<TerimaPerwakilanVO, String> colAwb;

	@FXML
	private TableColumn<TerimaPerwakilanVO, String> colTujuan;

	@FXML
	private TableColumn<TerimaPerwakilanVO, Boolean> colAction;

	private CheckBox cbAll = new CheckBox();

	@FXML
	private TableView<TerimaPerwakilanVO> tvAllAwb;
	
	@FXML
	private DateTimePicker dtpTglTerima;

	private ObservableList<TerimaPerwakilanVO> TrmPerwakilan = FXCollections.observableArrayList();

	List<EntryDataShowVO> listPaket = new ArrayList<EntryDataShowVO>();

	public TrUser uLogin = LoginController.getUserLogin();

	@FXML
	public TreeView<String> treeView;

	public TreeItem<String> root = new TreeItem<>(DateUtil.getStdDateDisplay(new Date()));

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ManagedFormHelper.instanceController = this;
		btnSave.setDisable(true);
		dtpTglTerima.setDateTimeValue(LocalDateTime.now());
		dtpTglTerima.setOnAction(event -> {
			treeView.setRoot(null);
			TreeItem<String> root = new TreeItem<>(DateUtil.getStdDateDisplay(DateUtil.convertToDatabaseColumn(dtpTglTerima.getValue())));
		    List<String> lstTreeOnDemand = TerimaPerwakilanService.getTreeViewParent(dtpTglTerima.getDateTimeValue());
		    for (String str : lstTreeOnDemand) {
		    	TreeItem idParent = new TreeItem<>(str);
		    	List<String> lstTreeSub = TerimaPerwakilanService.getTreeViewSub(str);
		    	for (String sub : lstTreeSub) {
					TreeItem idSub = new TreeItem<>(sub);
					List<String> awb = TerimaPerwakilanService.getTreeViewDetail(sub);
					for (String strAwb : awb) {
						TreeItem idAwb = new TreeItem<>(strAwb);
						idSub.getChildren().add(idAwb);
					}
					idParent.getChildren().add(idSub);
				}
		    	root.getChildren().add(idParent);
			}
		    treeView.setRoot(root);    
		});
//		treeView.setCellFactory(p -> new TreeCellFactory());

        root.setExpanded(true);
		txtIdKardus.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TrmPerwakilan.clear();
				setTable();
			}
		});
		
		txtAwb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Boolean data = true;
				for (EntryDataShowVO t : listPaket) {
					if (t.getAwbData().equals(txtAwb.getText())) {
						data = false;
					}
				}
				if (data) {
					MessageBox.alert("Awb tidak terdaftar kardus " + txtIdKardus.getText() + " tidak ada");
					txtAwb.clear();
				} else {
					for (TerimaPerwakilanVO p : tvAllAwb.getItems()) {
						if (p.getAwb().equals(txtAwb.getText())) {
							p.setKdPerwakilan(p.getKdPerwakilan());
							p.setAwb(p.getAwb());
							p.setTujuan(p.getTujuan());
							p.setNo(p.getNo());
							p.setChecked(true);
						}
					}
				}
			}
		});

		cbAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				if (cbAll.isSelected()) {
					TrmPerwakilan.clear();
					setTable();
					txtAwb.setDisable(true);
					txtAwb.clear();

				} else {
					TrmPerwakilan.clear();
					setTable();
					txtAwb.setDisable(false);
				}
			}
		});
	}

	public void setTable() {
		listPaket.clear();
		listPaket = TerimaPerwakilanService.getListDatPaket(txtIdKardus.getText());
		Integer no = 1;
		Boolean Checked = true;
		for (EntryDataShowVO t : listPaket) {
			if (cbAll.isSelected()) {
				TrmPerwakilan
						.add(new TerimaPerwakilanVO(no++, t.getKdPerwakilan(), t.getIdKardusSub(), t.getAwbData(), t.getTujuan(), true));
			} else {
				if (t.getInboundFlag() != 1) {
					Checked = false;
				}else{
					Checked = true;
				}
				TrmPerwakilan
						.add(new TerimaPerwakilanVO(no++, t.getKdPerwakilan(), t.getIdKardusSub(), t.getAwbData(), t.getTujuan(), Checked));
			}
			System.out.println();
			if(t.getInboundFlag()==0){
				btnSave.setDisable(false);
			}
		}
		colNo.setCellValueFactory(cellData -> cellData.getValue().noProperty());
		colKdPerwakilan.setCellValueFactory(cellData -> cellData.getValue().kdPerwakilanProperty());
		colKdKardusSub.setCellValueFactory(cellData -> cellData.getValue().kdKardusSubProperty());
		colAwb.setCellValueFactory(cellData -> cellData.getValue().awbProperty());
		colTujuan.setCellValueFactory(cellData -> cellData.getValue().tujuanProperty());
		colAction.setCellValueFactory(cellData -> cellData.getValue().checkedProperty());

		colAction.setCellFactory(CheckBoxTableCell.forTableColumn(colAction));
		colAction.setGraphic(cbAll);
		colAction.setEditable(true);

		// colAction.setCellFactory(CheckBoxTableCell.forTableColumn(new
		// Callback<Integer, ObservableValue<Boolean>>() {
		//
		// @Override
		// public ObservableValue<Boolean> call(Integer param) {
		// System.out.println("Cours "+TrmPerwakilan.get(param).getAwb());
		// return TrmPerwakilan.get(param).checkedProperty();
		// }
		// }));

		FilteredList<TerimaPerwakilanVO> filteredData = new FilteredList<>(TrmPerwakilan, p -> true);

		SortedList<TerimaPerwakilanVO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvAllAwb.comparatorProperty());
		tvAllAwb.setItems(sortedData);
	}

	@FXML
	public void onClickSave(Event evt) {
//		for (TerimaPerwakilanVO trm : tvAllAwb.getItems()) {
//			int inboundFleg = TerimaPerwakilanService.cekFlag(trm.getAwb());
//			if (trm.getChecked() && inboundFleg==0) {
//				TerimaPerwakilanService.updateTerimaPerwakilan(dtpTglTerima.getDateTimeValue(), uLogin.getNamaUser(), trm.getAwb());
//				txtIdKardus.setText("");
//				txtAwb.setText("");
//			}else if(!trm.getChecked() && inboundFleg==0){
//				TerimaPerwakilanService.updateTerimaPerwakilanBermasalah(dtpTglTerima.getDateTimeValue(), uLogin.getNamaUser(), trm.getAwb());
//			}
//		}
//		TrmPerwakilan.clear();
		
		for (TerimaPerwakilanVO trm : tvAllAwb.getItems()) {
			TerimaPerwakilanService.updateTerimaPerwakilan(trm.getChecked(), dtpTglTerima.getDateTimeValue(), uLogin.getNamaUser(), trm.getAwb());
		}
		TrmPerwakilan.clear();
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
