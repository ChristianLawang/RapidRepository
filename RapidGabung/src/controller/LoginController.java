package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import org.hibernate.Session;

import entity.TrUser;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import service.LoginUserService;
import util.HashPassword;
import util.WindowsHelper;

public class LoginController implements Initializable {

	private BorderPane rootLayoutTPB;

	@FXML
	TextField txt_username;

	@FXML
	PasswordField txt_password;

	@FXML
	Label lblLoad;

	@FXML
	Button btnLogin, btnCancel;

	public static TrUser listUser;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		setListener();
		txt_username.setText("superuser");
		txt_password.setText("123");
	}

	public static TrUser getUserLogin() {
		return listUser;
	}

	@FXML
	public void onLoginClick() {
		if (txt_username.getText().equals("") || txt_password.getText().equals("")) {
			lblLoad.setText("Username atau Password tidak tidak boleh kosong");
			lblLoad.setTextFill(Color.RED);
		} else {
			if (txt_username.getText() != null || txt_password.getText() != null || txt_username.getText() != ""
					|| txt_password.getText() != "") {
				String passwordHash = HashPassword.hashPassword(txt_password.getText());
				listUser = LoginUserService.getPelangganUserLogin(txt_username.getText(), passwordHash);
				if (listUser != null) {
					try {
						showMain();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Stage stage = (Stage) txt_username.getScene().getWindow();
					stage.close();
				} else {
					lblLoad.setText("Username dan Password tidak cocok");
					lblLoad.setTextFill(Color.RED);
				}
			}
		}
	}

	@FXML
	public void onCancelClick() {
		Stage stage = (Stage) lblLoad.getScene().getWindow();
		stage.close();
	}

	public void setListener() {
		EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onLoginClick();
				}
			}
		};
		txt_username.setOnKeyPressed(eH);
		txt_password.setOnKeyPressed(eH);
	}

	public void showMain() throws IOException {
		MasterPelangganController.autoSchedulerDiskon();
		// Setting Stage
		WindowsHelper.primaryStage = new Stage();
		WindowsHelper.rootLayout = rootLayoutTPB;
		WindowsHelper.primaryStage.setTitle("RapidExpedition");
		FXMLLoader root = new FXMLLoader(getClass().getResource("/controller/Body.fxml"));
		WindowsHelper.rootLayout = (BorderPane) root.load();

		FXMLLoader top = new FXMLLoader(getClass().getResource("/controller/Top.fxml"));
		AnchorPane topPage = (AnchorPane) top.load();
		WindowsHelper.rootLayout.setTop(topPage);

		HBox bodyHBox = new HBox();
		FXMLLoader menu = new FXMLLoader(getClass().getResource("/controller/Menu.fxml"));
		ScrollPane menuPage = (ScrollPane) menu.load();
		menuPage.prefWidthProperty().bind(WindowsHelper.primaryStage.widthProperty());
		bodyHBox.getChildren().add(menuPage);
		bodyHBox.setAlignment(Pos.CENTER);
		WindowsHelper.rootLayout.setCenter(bodyHBox);

		Scene scene = new Scene(WindowsHelper.rootLayout);

		WindowsHelper.primaryStage.setScene(scene);
		WindowsHelper.primaryStage.setMaximized(true);
		WindowsHelper.primaryStage.show();
	}
}
