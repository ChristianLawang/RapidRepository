package controller;

import java.net.URL;
import java.util.ResourceBundle;

import entity.TrUser;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import util.DtoBroadcaster;
import util.ManagedFormHelper;
import util.WindowsHelper;
import util.MessageBox;

public class TopController implements Initializable {
	
	@FXML
	private Label lblUserId;
	
	@FXML
	private ToolBar toolbarTop;

	@FXML
	private static Button btnFirstTop, prevTop, nextTop, lastTop, searchTop, newTop, editTop, deleteTop, saveTop,
			cancelTop, reportTop, copyTop, kirimTop, ambilResponTop, backTop;

	// Toolbar
	@FXML
	public void onFirtsData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "firstTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onPrevData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "prevTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onNextData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "nextTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onLastData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "lastTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onSearchData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "searchTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onNewData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "newTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onEditData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "editTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onDeleteData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "deleteTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onSaveData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "saveTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onCancelData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "cancelTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onReportData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "reportTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onCopyData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "copyTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onKirimData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "kirimTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onAmbilResponData(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "ambilResponTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	@FXML
	public void onBack(Event evt) {
		if (ManagedFormHelper.instanceController != null) {
			DtoBroadcaster.broadcast(ManagedFormHelper.instanceController, "backTop", null);
		} else {
			MessageBox.alert("Silahkan masuk ke pengisian form terlebih dahulu");
		}
	}

	public static void setDisableButton(boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6,
			boolean b7, boolean b8, boolean b9, boolean b10, boolean b11, boolean b12, boolean b13, boolean b14,
			boolean b15) {
		btnFirstTop.setDisable(b1);
		prevTop.setDisable(b2);
		nextTop.setDisable(b3);
		lastTop.setDisable(b4);
		searchTop.setDisable(b5);
		newTop.setDisable(b6);
		editTop.setDisable(b7);
		deleteTop.setDisable(b8);
		saveTop.setDisable(b9);
		cancelTop.setDisable(b10);
		reportTop.setDisable(b11);
		copyTop.setDisable(b12);
		kirimTop.setDisable(b3);
		ambilResponTop.setDisable(b14);
		backTop.setDisable(b15);
	}

	@Override
    public void initialize(URL url, ResourceBundle rb) {
        toolbarTop.prefWidthProperty().bind(WindowsHelper.primaryStage.widthProperty());
        TrUser tu = new LoginController().getUserLogin();
        lblUserId.setText(tu.getNamaUser());;
    }        
}
