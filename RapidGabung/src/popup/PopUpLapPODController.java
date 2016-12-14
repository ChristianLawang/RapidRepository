package popup;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import VO.BrowseSemuaDataVO;
import VO.EntryDataShowVO;
import VO.LapPODVO;
import controller.BrowseSemuaDataController;
import controller.DataEntryBrowseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.LaporanPODService;
import util.ManagedFormHelper;
import util.WindowsHelper;

public class PopUpLapPODController implements Initializable {
	@FXML
	private TableView tvLapPod;
	
	@FXML
	private TextField txtCari;
	
	@FXML
	private TableColumn<LapPODVO, Number> colNo;
	
	@FXML
	private TableColumn<LapPODVO, String> colPerwakilan;
	
	@FXML
	private TableColumn<LapPODVO, String> colAwb;
	@FXML
	private Label lblTitle;

	private ObservableList<LapPODVO> olDokumen = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ManagedFormHelper.instanceController = this;
		
		tvLapPod.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() > 1) {
					LapPODVO dataHeader = (LapPODVO) tvLapPod.getSelectionModel().getSelectedItem();
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(
								getClass().getResource("/controller/DataEntryBrowse.fxml"));
						Stage dialogStage = new Stage();
						dialogStage.setTitle("Edit Entry Data");
						dialogStage.initModality(Modality.WINDOW_MODAL);
						dialogStage.initOwner(WindowsHelper.primaryStage);
						dialogStage.initStyle(StageStyle.UTILITY);
						dialogStage.setResizable(false);
						Parent root = (Parent) fxmlLoader.load();
						DataEntryBrowseController controller = fxmlLoader
								.<DataEntryBrowseController> getController();
						controller.setFromPopUpLaporanPOD(dataHeader.getAwbData());
						Scene scene = new Scene(root);
						dialogStage.setScene(scene);
						dialogStage.show();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}

	
	public void settingListboksMasterCabang(String kdPerwakilan, Date tglAwl, Date tglAkhir, String menu) {
		List<EntryDataShowVO> tt = LaporanPODService.getReportPOD(kdPerwakilan, tglAwl, tglAkhir, menu);
		Integer no = 1;
		for (EntryDataShowVO t : tt) {
			olDokumen.add(new LapPODVO(no++, t.getKdPerwakilan(), t.getAwbData()));
		}
		
		colNo.setCellValueFactory(cellData -> cellData.getValue().noProperty());
		colPerwakilan.setCellValueFactory(cellData -> cellData.getValue().perwakilanProperty());
		colAwb.setCellValueFactory(cellData -> cellData.getValue().awbDataProperty());

		FilteredList<LapPODVO> filteredData = new FilteredList<>(olDokumen, p -> true);

		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (data.getPerwakilan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}if(data.getAwbData().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});

		SortedList<LapPODVO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvLapPod.comparatorProperty());
		tvLapPod.setItems(sortedData);
	}
	
	public void setFromParent(String kodePerwakilan, String menu, Date tglAwl, Date tglAkhir){
		if(menu.equals("mnJmlhPaket")){
			lblTitle.setText("LAPORAN POD JUMLAH PAKET");
		}else if(menu.equals("mnSudahReport")){
			lblTitle.setText("LAPORAN POD SUDAH REPORT");
		}else if(menu.equals("mnBelumReport")){
			lblTitle.setText("LAPORAN POD BELUM REPORT");
		}else if(menu.equals("mnMasalah")){
			lblTitle.setText("LAPORAN POD BERMASALAH");
		}			
		settingListboksMasterCabang(kodePerwakilan, tglAwl, tglAkhir, menu);
	}
	
	

}
