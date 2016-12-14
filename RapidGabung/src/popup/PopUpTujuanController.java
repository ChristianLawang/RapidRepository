package popup;

import java.net.URL;
import java.util.ResourceBundle;

import VO.ServicePerwakilanVO;
import controller.DataEntryController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import service.TujuanService;

public class PopUpTujuanController<T> implements Initializable {
	@FXML
	TableView lb_header;

	public String kdTujuan;

	@FXML
	private TextField textCari;

	private DataEntryController dataEntryController;

	private String awb;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		setListener();
		setListenerTabel();
	}

	public void set(DataEntryController dataEntryController, String kdTujuan, String awb) {
		this.dataEntryController = dataEntryController;
		this.kdTujuan = kdTujuan;
		this.awb = awb;
		textCari.setText(kdTujuan);
		load();
	}

	public void setListener() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					load();
				}
			}
		};
		textCari.setOnKeyPressed(eH);
	}

	public void setListenerTabel() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					ServicePerwakilanVO dataHeader = (ServicePerwakilanVO) lb_header.getSelectionModel().getSelectedItem();
					if (dataHeader != null) {
						Stage stage = (Stage) lb_header.getScene().getWindow();
						stage.close();
						dataEntryController.setDataTujuan(dataHeader);
					}
				}
			}
		};
		lb_header.setOnKeyPressed(eH);
	}

	@FXML
	public void onMouseClicked(MouseEvent evt) {
		if (evt.getClickCount() > 1) {
			ServicePerwakilanVO dataHeader = (ServicePerwakilanVO) lb_header.getSelectionModel().getSelectedItem();
			if (dataHeader != null) {
				Stage stage = (Stage) lb_header.getScene().getWindow();
				stage.close();
				dataEntryController.setDataTujuan(dataHeader);
			}
		}
	}

	public void load() {
		lb_header.getColumns().clear();
		ObservableList<ServicePerwakilanVO> olHeader = FXCollections.observableArrayList(TujuanService.getTujuanPaket(textCari.getText(), this.awb));

		TableColumn col = new TableColumn("Kode Tujuan");
		col.setPrefWidth(95.0);
		col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ServicePerwakilanVO, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<ServicePerwakilanVO, String> param) {
				return new SimpleStringProperty(param.getValue().getKode_zona());
			}
		});
		lb_header.getColumns().addAll(col);

		col = new TableColumn("Nama Tujuan");
		col.setPrefWidth(340.0);
		col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ServicePerwakilanVO, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<ServicePerwakilanVO, String> param) {
				String Alamat = param.getValue().getPropinsi() + " " + param.getValue().getKecamatan() + " "
						+ param.getValue().getKabupaten();
				return new SimpleStringProperty(Alamat);
			}
		});
		lb_header.getColumns().addAll(col);

		lb_header.setItems(olHeader);
		 
	}

	@FXML
	public void onClickTampilkan() {
		load();
	}

}
