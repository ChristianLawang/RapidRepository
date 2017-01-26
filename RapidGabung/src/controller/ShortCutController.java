package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import VO.TagihanVO;
import controller.MasterPelangganController.PelangganTV;
import entity.TrCabang;
import entity.TrPelanggan;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import service.GenericService;
import service.MasterCabangService;
import service.PelangganService;
import util.DateUtil;
import util.DtoListener;
import util.EmailUtil;
import util.ExportToExcell;
import util.ManagedFormHelper;
import util.PDFUtil;
import util.WindowsHelper;

public class ShortCutController implements Initializable{

	@FXML
	Button btnKirimManifest, btnKirimTagihan;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ManagedFormHelper.instanceController = this;
		btnKirimManifest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/popup/PopUpKirimDataManifest.fxml"));
					Stage dialogStage = new Stage();
					dialogStage.setTitle("Kirim Data Manifest");
					dialogStage.initModality(Modality.WINDOW_MODAL);
					dialogStage.initOwner(WindowsHelper.primaryStage);
					dialogStage.initStyle(StageStyle.UTILITY);
					dialogStage.setResizable(false);
					Parent root = (Parent) fxmlLoader.load();
					Scene scene = new Scene(root);
					dialogStage.setScene(scene);
					dialogStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnKirimTagihan.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MasterPelangganController pl = new MasterPelangganController();
				pl.onClickKirimTagihan(null);
			}
		});
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
