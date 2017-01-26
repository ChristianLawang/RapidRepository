package controller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controller.MasterPickupController.PickupTV;
import entity.TtStatusKurirIn;
import entity.TtStatusResiBermasalah;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import service.BarangKembaliService;
import service.PelangganService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.WindowsHelper;

public class BarangKembaliController implements Initializable {
	@FXML
	Pane pane;
	
	@FXML
	DatePicker dtAwal, dtAkhir;
	@FXML
	TableView<MasalahTV> lstView;
	@FXML
	Button btnCari;
	@FXML
	ImageView imgViewMainView;
	
	final int MIN_PIXELS = 10;
	
	double width;
	double height;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		 //Set Objek kelas ini
        ManagedFormHelper.instanceController = this;
        dtAwal.setValue(LocalDate.now());
        dtAkhir.setValue(LocalDate.now());
        
        btnCari.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				refreshListview();
			}
        });
        
        lstView.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		    	MasalahTV selection = lstView.getSelectionModel().getSelectedItem();
				String alamatGambar = BarangKembaliService.getFotoImageByAWB(selection.getResi());
				
				System.out.println("--> path : " + alamatGambar);
//				File file = new File("d:/compile rapid/CGK0011600058053.jpg");
				File file = new File(alamatGambar);
				Image img = new Image(file.toURI().toString());
				imgViewMainView.setImage(img);
				imgViewMainView.setRotate(180);
				
				width = img.getWidth();
				height = img.getHeight();
				reset(imgViewMainView, 0, 0);
		    }
		});
        setImageZoom();
	}
	
	// --------------------------------------------------------------------------Image
		private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
			double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
			double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

			Rectangle2D viewport = imageView.getViewport();
			return new Point2D(viewport.getMinX() + xProportion * viewport.getWidth(),
					viewport.getMinY() + yProportion * viewport.getHeight());
		}

		private void shift(ImageView imageView, Point2D delta) {
			Rectangle2D viewport = imageView.getViewport();

			double width = imageView.getImage().getWidth();
			double height = imageView.getImage().getHeight();

			double maxX = width - viewport.getWidth();
			double maxY = height - viewport.getHeight();

			double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
			double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

			imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
		}

		private double clamp(double value, double min, double max) {

			if (value < min)
				return min;
			if (value > max)
				return max;
			return value;
		}

		private void reset(ImageView imageView, double width, double height) {
			imageView.setViewport(new Rectangle2D(0, 0, width, height));
		}
		
	private void refreshListview() {
		lstView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<MasalahTV> pops = FXCollections.observableArrayList();
		
		BarangKembaliService myService = new BarangKembaliService();
		List<Object[]> result = myService.getResiBermasalah(
					DateUtil.convertToDatabaseColumn(dtAwal.getValue()).toString(),
					DateUtil.convertToDatabaseColumn(dtAkhir.getValue()).toString());
		for (Object[] res : result) {
			MasalahTV row = new MasalahTV(
					(String) res[0],
					(String) res[1],
					(String) res[2]
					);
			pops.add(row);
		}
				
		lstView.getColumns().clear();
		lstView.getItems().clear();
		
		lstView.setItems(pops);
		
		TableColumn<MasalahTV, String> resiCol = new TableColumn<MasalahTV, String>("No. Resi");
		TableColumn<MasalahTV, String> penerimaCol = new TableColumn<MasalahTV, String>("Nama Penerima");
		TableColumn<MasalahTV, String> statusCol = new TableColumn<MasalahTV, String>("Status");
		
		resiCol.prefWidthProperty().bind(lstView.widthProperty().divide(4)); // w * 1/4
		penerimaCol.prefWidthProperty().bind(lstView.widthProperty().divide(4)); // w * 2/4
		statusCol.prefWidthProperty().bind(lstView.widthProperty().divide(2)); // w * 1/4
		
		lstView.getColumns().addAll(resiCol, penerimaCol, statusCol);
		
		resiCol.setCellValueFactory(new PropertyValueFactory<MasalahTV, String>("resi"));
		penerimaCol.setCellValueFactory(new PropertyValueFactory<MasalahTV, String>("penerima"));
		statusCol.setCellValueFactory(new PropertyValueFactory<MasalahTV, String>("status"));
		
		lstView.setEditable(true);
	}
	
	public static class MasalahTV{
		private StringProperty resi = new SimpleStringProperty();
		private StringProperty penerima = new SimpleStringProperty();
		private StringProperty status = new SimpleStringProperty();
		
		public MasalahTV(String resi, String penerima, String status){
			this.resi.set(resi);
			this.penerima.set(penerima);
			this.status.set(status);
		}

		public String getResi() {
			return resi.get();
		}

		public void setResi(String resi) {
			this.resi.set(resi);
		}

		public String getPenerima() {
			return penerima.get();
		}

		public void setPenerima(String penerima) {
			this.penerima.set(penerima);
		}

		public String getStatus() {
			return status.get();
		}

		public void setStatus(String status) {
			this.status.set(status);
		}
		
	}

	@DtoListener(idDtoListener = "backTop")
    public void backDtoListener()
    {
        try {
            HBox bodyHBox = new HBox();
            FXMLLoader menu = new FXMLLoader(getClass().getResource("/controller/Menu.fxml"));
            ScrollPane menuPage = (ScrollPane) menu.load();
            menuPage.prefWidthProperty().bind(WindowsHelper.primaryStage.widthProperty());       //FullScreen, hilangin klo g mau fullscreen
            bodyHBox.getChildren().add(menuPage);
            bodyHBox.setAlignment(Pos.CENTER);
            
            if(WindowsHelper.rootLayout!=null)
            {
                WindowsHelper.rootLayout.setCenter(bodyHBox);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	
	private void setImageZoom() {
		imgViewMainView.setPreserveRatio(true);
		// reset(imageTest, width / 2, height / 2);
		ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

		imgViewMainView.setOnMousePressed(e -> {

			Point2D mousePress = imageViewToImage(imgViewMainView, new Point2D(e.getX(), e.getY()));
			mouseDown.set(mousePress);
		});

		imgViewMainView.setOnMouseDragged(e -> {
			Point2D dragPoint = imageViewToImage(imgViewMainView, new Point2D(e.getX(), e.getY()));
			shift(imgViewMainView, dragPoint.subtract(mouseDown.get()));
			mouseDown.set(imageViewToImage(imgViewMainView, new Point2D(e.getX(), e.getY())));
		});

		imgViewMainView.setOnScroll(e -> {
			double delta = e.getDeltaY();
			Rectangle2D viewport = imgViewMainView.getViewport();

			double scale = clamp(Math.pow(1.01, delta),

			Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

			// don't scale so that we're bigger than image dimensions:
					Math.max(width / viewport.getWidth(), height / viewport.getHeight())

			);

			Point2D mouse = imageViewToImage(imgViewMainView, new Point2D(e.getX(), e.getY()));

			double newWidth = viewport.getWidth() * scale;
			double newHeight = viewport.getHeight() * scale;

			double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 0, width - newWidth);
			double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 0, height - newHeight);

			imgViewMainView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
		});

		imgViewMainView.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) {
				reset(imgViewMainView, width, height);
			}
		});
		
		imgViewMainView.fitWidthProperty().bind(pane.widthProperty());
		imgViewMainView.fitHeightProperty().bind(pane.heightProperty());
		imgViewMainView.setRotate(180);
	}
}