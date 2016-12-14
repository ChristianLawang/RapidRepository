	package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import VO.EntryDataShowVO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import service.ReportService;
import util.DateUtil;
import util.DtoListener;
import util.ExportToExcell;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;

public class LaporanResiController implements Initializable {

	@FXML
	private Button btnPrintRResi, btnPrintRTagihan;

	@FXML
	private DatePicker dpDate;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;

		dpDate.setValue(LocalDate.now());
	}

	@FXML
	public void onTagihan() {
		try {
			List<EntryDataShowVO> en = ReportService
					.getDataTagihan(DateUtil.convertToDatabaseColumn(dpDate.getValue()));
			ExportToExcell.exportToExcellReportTagihan(en, "ReportDataTagihanCustomer",
					DateUtil.getDateNotSeparator(DateUtil.convertToDatabaseColumn(dpDate.getValue())));
			MessageBox.alert("Export Berhasil Di Drive C:/DLL/REPORT/EXPORT/"
					+DateUtil.getDateNotSeparator(DateUtil.convertToDatabaseColumn(dpDate.getValue()))
					+" ReportDataTagihanCustomer.xls");
		} catch (Exception e) {
			MessageBox.alert(e.getMessage());
		}
	}

	@FXML
	public void onPrintResi() {
		try {
			List<EntryDataShowVO> en = ReportService.getDataResi(DateUtil.convertToDatabaseColumn(dpDate.getValue()));
			ExportToExcell.exportToExcellReportResi(en, "ReportDataSMSCustomer",
					DateUtil.getDateNotSeparator(DateUtil.convertToDatabaseColumn(dpDate.getValue())));
			MessageBox.alert("Export Berhasil Di Drive C:/DLL/REPORT/EXPORT/"
					+DateUtil.getDateNotSeparator(DateUtil.convertToDatabaseColumn(dpDate.getValue()))
					+" ReportDataSMSCustomer.xls");
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
