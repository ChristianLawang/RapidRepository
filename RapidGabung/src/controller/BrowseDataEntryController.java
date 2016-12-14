package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.prism.impl.Disposer.Record;

import entity.TrUser;
import entity.TtDataEntry;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import service.DataPaketService;
import util.DtoListener;
import util.ManagedFormHelper;
import util.WindowsHelper;

@SuppressWarnings("unused")
public class BrowseDataEntryController implements Initializable {

	@FXML
	TextField txt_jml_records;

	@FXML
	TableView listboks_data_entry;

	public void initialize(URL url, ResourceBundle rb) {
		// Set Objek kelas ini

		ManagedFormHelper.instanceController = this;
		txt_jml_records.setEditable(false);
		settingListboksDataEntry();
	}

	public void settingListboksDataEntry() {
		listboks_data_entry.getColumns().clear();
		Long id = null;
		ObservableList<TtDataEntry> olDokumen = FXCollections
				.observableArrayList(DataPaketService.getBrowseDataEntry());
		txt_jml_records.setText(String.valueOf(olDokumen.size()));
		TableColumn col = new TableColumn("No");
		col.setPrefWidth(40.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TtDataEntry, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TtDataEntry, String> param) {
						return new ReadOnlyObjectWrapper(listboks_data_entry.getItems().indexOf(param.getValue()) + 1);
					}

				});
		listboks_data_entry.getColumns().addAll(col);

		col = new TableColumn("Pelanggan");
		col.setPrefWidth(300.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TtDataEntry, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TtDataEntry, String> param) {
						return new SimpleStringProperty(param.getValue().getPengirim());
					}
				});
		listboks_data_entry.getColumns().addAll(col);

		col = new TableColumn("Jumlah Pelanggan");
		col.setPrefWidth(200.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TtDataEntry, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TtDataEntry, String> param) {
						return new SimpleStringProperty(param.getValue().getJumlahPelanggan().toString());
					}
				});
		listboks_data_entry.getColumns().addAll(col);

		col = new TableColumn("Tanggal");
		col.setPrefWidth(200.0);
		col.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TtDataEntry, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TtDataEntry, String> param) {
						return new SimpleStringProperty(param.getValue().getTglCreate().toString());
					}
				});
		listboks_data_entry.getColumns().addAll(col);

		col = new TableColumn("Action");
		col.setPrefWidth(83.0);
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
				return new ButtonCell(listboks_data_entry);
			}

		});
		listboks_data_entry.getColumns().add(col);

		listboks_data_entry.setItems(olDokumen);
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

	private class ButtonCell extends TableCell<Record, Boolean> {
		final Button cellButton = new Button("Entry");

		ButtonCell(final TableView tblView) {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					System.out.println("Tahap 1");
					int selectdIndex = getTableRow().getIndex();
					TtDataEntry a = (TtDataEntry) tblView.getItems().get(selectdIndex);
					try {
						System.out.println("Tahap 2");
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controller/DataEntry.fxml"));
						Stage dialogStage = new Stage();
						dialogStage.setTitle("Entry Data");
						dialogStage.initModality(Modality.WINDOW_MODAL);
						dialogStage.initOwner(WindowsHelper.primaryStage);
						dialogStage.initStyle(StageStyle.UTILITY);
						dialogStage.setResizable(false);
						Parent root = (Parent) fxmlLoader.load();
						DataEntryController controller = fxmlLoader.<DataEntryController> getController();
						controller.set(a, BrowseDataEntryController.this);
						Scene scene = new Scene(root);
						dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

							@Override
							public void handle(WindowEvent event) {
								controller.onClickCancel();
							}

						});
						dialogStage.setScene(scene);
						dialogStage.show();

					} catch (IOException ex) {
						ex.printStackTrace();
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
}
