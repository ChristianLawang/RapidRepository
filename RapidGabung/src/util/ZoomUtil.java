package util;

import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ZoomUtil {

	private static final int MIN_PIXELS = 10;
	
	public Pane createImageViewWithZoomByChrist(
			String pathParam,
			Integer widthParam,
			Integer heightParam
			){
		
    	File file = new File(pathParam);
        Image image = new Image(file.toURI().toString());
        
        double width = image.getWidth();
        double height = image.getHeight();

        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        reset(imageView, width / 2, height / 2);

        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        imageView.setOnMousePressed(e -> {
            
            Point2D mousePress = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        imageView.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
            shift(imageView, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(imageView, new Point2D(e.getX(), e.getY())));
        });

        imageView.setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = imageView.getViewport();

            double scale = clamp(Math.pow(1.01, delta),

                // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                // don't scale so that we're bigger than image dimensions:
                Math.max(width / viewport.getWidth(), height / viewport.getHeight())

            );
            
            Point2D mouse = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            System.out.println("--> newWidth : " + newWidth + ", newHeight : " + newHeight);
            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image

            // solving this for newViewportMinX gives

            // newViewportMinX = x - (x - currentViewportMinX) * scale 

            // we then clamp this value so the image never scrolls out
            // of the imageview:

            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 
                    0, width - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 
                    0, height - newHeight);

            imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

            imageView.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                reset(imageView, width, height);
            }
        });

        HBox buttons = createButtons(width, height, imageView);
        Tooltip tooltip = new Tooltip("Scroll to zoom, drag to pan");
        Tooltip.install(buttons, tooltip);

        Pane container = new Pane(imageView, buttons);
        container.setPrefSize(widthParam, heightParam);

        imageView.fitWidthProperty().bind(container.widthProperty());
        imageView.fitHeightProperty().bind(container.heightProperty());

		return container;
	}
	
    // reset to the top left:
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }
    
    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(), 
                viewport.getMinY() + yProportion * viewport.getHeight());
    }
    
    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth() ;
        double height = imageView.getImage().getHeight() ;

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
    
    private HBox createButtons(double width, double height, ImageView imageView) {
        Button reset = new Button("Reset");
        reset.setOnAction(e -> reset(imageView, width / 2, height / 2));
        Button full = new Button("Full");
        full.setOnAction(e -> reset(imageView, width, height));
        Button zoomIn = new Button("Zoom In");
        zoomIn.setOnAction(e -> zoom(imageView, 10));
        Button zoomOut = new Button("Zoom In");
        zoomOut.setOnAction(e -> zoom(imageView, -10));
        HBox buttons = new HBox(10, reset, full, zoomIn, zoomOut);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10));
        return buttons;
    }

	private void zoom(ImageView imageView, Integer percent) {
//        double delta = e.getDeltaY();
        Rectangle2D viewport = imageView.getViewport();

//        double scale = clamp(Mat        
        Point2D mouse = imageViewToImage(imageView, new Point2D(viewport.getWidth()/2, viewport.getHeight()/2));

        double newWidth = viewport.getWidth() + (viewport.getWidth()*percent/100);
        double newHeight = viewport.getHeight() + (viewport.getWidth()*percent/100);

        System.out.println("--> newWidth : " + newWidth + ", newHeight : " + newHeight);
        // To keep the visual point under the mouse from moving, we need
        // (x - newViewportMinX) / (x - currentViewportMinX) = scale
        // where x is the mouse X coordinate in the image

        // solving this for newViewportMinX gives

        // newViewportMinX = x - (x - currentViewportMinX) * scale 

        // we then clamp this value so the image never scrolls out
        // of the imageview:

        double scale = clamp(Math.pow(1.01, 100),

                // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                // don't scale so that we're bigger than image dimensions:
                Math.max(640 / viewport.getWidth(), 480 / viewport.getHeight())

            );

        
        double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 
                0, 640 - newWidth);
        double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 
                0, 480 - newHeight);

        
        imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
	}

}
