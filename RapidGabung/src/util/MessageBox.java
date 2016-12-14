package util;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.WindowsHelper;

/**
 *
 * @author HP
 */
public class MessageBox extends Stage{
    
    private final int WIDTH_DEFAULT = 300;

    public static final int ICON_INFO = 0;
    public static final int ICON_ERROR = 1;
    public static final int ICON_CONFIRM = 2;
    protected static int selectedButton = 0;
    public static final int BUTTON_YES = 3;
    public static final int BUTTON_NO = 4;
    public static final int BUTTON_CANCEL = 5;
    public static final int BUTTON_OK = 6;
    
    static class ConfirmButton extends Button {
        private String text;
        private MessageBox messageBox;
        private int tipe;

        ConfirmButton(MessageBox messageBox, int tipe) {
            this.messageBox = messageBox;
            this.tipe = tipe;
        }

        ConfirmButton(String s, MessageBox mBox, int mtipe) {
            super(s);
            messageBox = mBox;
            this.tipe = mtipe;
        }

        ConfirmButton(String s, Node node, MessageBox messageBox, int tipe) {
            super(s, node);
            this.messageBox = messageBox;
            this.tipe = tipe;
        }
    }
    
    public static void alert(String message) {
        Stage owner = WindowsHelper.primaryStage;
        MessageBox a = new MessageBox(owner, message, ICON_INFO);
        
        a.showAndWait();
    }
    
    public static void alertBesar(Integer intHeight, String message) {
        Stage owner = WindowsHelper.primaryStage;
        MessageBox a = new MessageBox(intHeight, owner, message, ICON_INFO);
        
        a.showAndWait();
    }
    
    public static int confirm(String message, int... buttons) {
        Stage owner = WindowsHelper.primaryStage;
        MessageBox a = new MessageBox(owner, message, ICON_CONFIRM);
        a.hbox2.getChildren().clear();
        for (int i = 0; i < buttons.length; i++) {
            int button = buttons[i];
            String text = "OKE";
            switch (button) {
                case BUTTON_YES:
                    text = "Yes";
                    break;
                case BUTTON_NO:
                    text = "No";
                    break;
                case BUTTON_CANCEL:
                    text = "Cancel";
                    break;
                case BUTTON_OK:
                    text = "Ok";
                    break;
            }
            ConfirmButton btn = new ConfirmButton(text, a, button);
            
            EventHandler<ActionEvent> eH = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (actionEvent.getSource() instanceof ConfirmButton) {
                        ConfirmButton b = (ConfirmButton) actionEvent.getSource();
                        b.messageBox.selectedButton = b.tipe;
                        b.messageBox.close();
                    }
                }
            };
            
            btn.setOnAction(eH);
            a.hbox2.getChildren().add(btn);
        }
        a.showAndWait();
        return a.selectedButton;
    }

    HBox hbox2;

    public MessageBox(Stage owner, String msg, int type) {
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.TRANSPARENT);

        Label label = new Label(msg);
        label.setWrapText(true);
        label.setGraphicTextGap(20);
        //ini jangan di comment
        label.setGraphic(new ImageView(getImage(type)));

        Button button = new Button("OK");
        EventHandler<ActionEvent> eH = new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent arg0) {
                MessageBox.this.close();
            }
        };
        button.setOnAction(eH);

        BorderPane borderPane = new BorderPane();
        //Ini jangan dicomment v
        borderPane.getStylesheets().add(getClass().getResource("/css/alert.css").toExternalForm());
        borderPane.setTop(label);

        hbox2 = new HBox();
        hbox2.setAlignment(Pos.CENTER);
        hbox2.setSpacing(3);
        hbox2.getChildren().add(button);
        borderPane.setBottom(hbox2);

        // calculate width of string
        final Text text = new Text(msg);
        text.snapshot(null, null);
        // + 20 because there is padding 10 left and right
        int width = (int) text.getLayoutBounds().getWidth() + 40;

        if (width < WIDTH_DEFAULT)
            width = WIDTH_DEFAULT;

        int height = 100;

        final Scene scene = new Scene(borderPane, width, height);
        scene.setFill(Color.TRANSPARENT);//sebenernya transparan
        setScene(scene);

        // make sure this stage is centered on top of its owner
        setX(owner.getX() + (owner.getWidth() / 2 - width / 2));
        setY(owner.getY() + (owner.getHeight() / 2 - height / 2));
    }
    
    public MessageBox(Integer tinggiParam, Stage owner, String msg, int type) {
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.TRANSPARENT);

        Label label = new Label(msg);
        label.setWrapText(true);
        label.setGraphicTextGap(20);
        //ini jangan di comment
        label.setGraphic(new ImageView(getImage(type)));

        Button button = new Button("OK");
        EventHandler<ActionEvent> eH = new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent arg0) {
                MessageBox.this.close();
            }
        };
        button.setOnAction(eH);

        BorderPane borderPane = new BorderPane();
        //Ini jangan dicomment v
        borderPane.getStylesheets().add(getClass().getResource("/css/alert.css").toExternalForm());
        borderPane.setTop(label);

        hbox2 = new HBox();
        hbox2.setAlignment(Pos.CENTER);
        hbox2.setSpacing(3);
        hbox2.getChildren().add(button);
        borderPane.setBottom(hbox2);

        // calculate width of string
        final Text text = new Text(msg);
        text.snapshot(null, null);
        // + 20 because there is padding 10 left and right
        int width = (int) text.getLayoutBounds().getWidth() + 40;

        if (width < WIDTH_DEFAULT)
            width = WIDTH_DEFAULT;

        int height = tinggiParam;

        final Scene scene = new Scene(borderPane, width, height);
        scene.setFill(Color.TRANSPARENT);//sebenernya transparan
        setScene(scene);

        // make sure this stage is centered on top of its owner
        setX(owner.getX() + (owner.getWidth() / 2 - width / 2));
        setY(owner.getY() + (owner.getHeight() / 2 - height / 2));
    }

    private Image getImage(int type) {
        if (type == ICON_ERROR)
            return new Image(getClass().getResourceAsStream("/css/images/error.png"));
        else if (type == ICON_CONFIRM)
            return new Image(getClass().getResourceAsStream("/css/images/tanya.png"));
        else
            return new Image(getClass().getResourceAsStream("/css/images/info.png"));
    }
}
