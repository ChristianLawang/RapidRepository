package utilfx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Tanggalan extends TextField{
    
    private static SimpleDateFormat input = new SimpleDateFormat("ddMMyy");
    private static SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
    
    private Date tanggal = null;
    private String tgl = null;
    private String border = null;
    
    private static final String valid = "-fx-focus-color: #2ECC71 ; -fx-background-color: -fx-focus-color, white;";
    private static final String invalid = "-fx-focus-color: #E74C3C; -fx-background-color: -fx-focus-color, white;";
    private static final String onfocus = "-fx-focus-color: #3498DB; -fx-background-color: -fx-focus-color, white;";
    private static final String promptText = "DDMMYY";
    
    public Tanggalan()
    {
        super();
        setPromptText(promptText);
        
        if ("".equals(getText())) {
            tanggal = null;
        }
        
        EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {

                    if (isValid(getText()) && !"".equals(getText())) {
                        formatDateOutput();
                        setStyle(valid);
                        tgl = getText();

                    } else {
                        tanggal = null;
                        setText("");
                        setPromptText("Invalid Date");
                        setStyle(invalid);
                        tgl = getText();
                    }
                }
            }
        };
        setOnKeyPressed(eH);

        ChangeListener<Boolean> cL = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {

                if (newPropertyValue && getText() != null) {
                    if (!"".equals(getText())) {
                        formatDateInput();
                        setPromptText("DDMMYY");
//                        tgl = getText();
                    }
                }
                if (oldPropertyValue && getText() != null) {
                    if (isEditable()) {
                        setStyle(null);
                        setPromptText("DDMMYY");
                        if (isValid(getText()) && !"".equals(getText())) {
                            formatDateOutput();
                            tgl = getText();
                        }
                        if ("".equals(getText())) {
                            tanggal = null;
                            tgl = "";
                            setText(tgl);
                        }
                        if (!isValid(getText()) && !"".equals(getText())) {
                            setText(tgl);
                        }
                    }
                }
            }
        };
        focusedProperty().addListener(cL);
    }
    
    private boolean isValid(String s) {
        return s.length() == 6 && isNumeric(s);
    }

    public void formatDateOutput() {
        try {
            if (isEditable()) {
                tanggal = input.parse(getText());
                setText(output.format(tanggal));
                setText(getText().toUpperCase());
            }
        } catch (ParseException ex) {
            Logger.getLogger(Tanggalan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void formatDateInput() {
        try {
            if (isEditable()) {
                tanggal = output.parse(getText());
                setText(input.format(tanggal));
                setText(getText().toUpperCase());
            }
        } catch (ParseException ex) {
            Logger.getLogger(Tanggalan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public void setTanggalText(Date inputDate)
    {
        if(inputDate!=null)
        {
            this.setText(output.format(inputDate));
        }
    }
    
    public Date getTanggalText() {
        try {
            if (getText() != null && !"".equals(getText())) {
                return output.parse(getText());
            }
        } 
        catch (ParseException ex) {
            Logger.getLogger(Tanggalan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void setEditableTanggalan(Boolean value, Boolean colour) {
        this.setEditable(value);
        if (colour) {
            setStyle("-fx-focus-color: #3498DB;\n"
                    + "    -fx-background-color: -fx-focus-color, white;");
        } else {
            setStyle("-fx-focus-color: #D0D0D0;\n"
                    + "    -fx-background-color: -fx-focus-color, #F0F0F0;");
        }
    }
}
