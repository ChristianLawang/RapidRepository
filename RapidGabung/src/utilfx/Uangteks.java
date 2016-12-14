package utilfx;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author HP
 */
public class Uangteks extends TextField{
    
    
    private static DecimalFormat input = new DecimalFormat("#,###");
    private static DecimalFormat output = new DecimalFormat("#,###");
    private static DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###,##0");
    private static final String invalid = "-fx-focus-color: #E74C3C; -fx-background-color: -fx-focus-color, white;";
    
    Number uang = null;
    String uangFormat = "";
    
    public Uangteks()
    {
        super();
        
        
        this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                String teks = event.getCharacter();
                if (isNumeric(getText()) || "".equals(getText())) {
                    if (!teks.matches("[0-9\\.]")) {
                        event.consume();
                    }
                } else {
                    event.consume();
                }
            }
        });

        setAlignment(Pos.CENTER_RIGHT);
        EventHandler<KeyEvent> eH = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    if (isNumeric(getText()) && !"".equals(getText())) {
                        try {
                            formatNumberOutput();
                            uangFormat = getText();
                        } catch (ParseException ex) {
                            Logger.getLogger(Uangteks.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        uang = null;
                        setText("");
                        uangFormat = getText();
                        setStyle(invalid);
                        //uangFormat = getText();
                    }
                }
            }
        };
        setOnKeyPressed(eH);

        ChangeListener<Boolean> cL = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                //System.out.println("Prama =1 "+getUangTeks());
                if (newPropertyValue && getText() != null) {    //Fokus
                    //System.out.println("Masuk 1");
                    try {
                        if (!"".equals(getText())) {
                            formatNumberInput();
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(Uangteks.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (oldPropertyValue) { //outFokus
                    if (getText() != null) {
                        if (isNumeric(getUangteks()) && !"".equals(getText())) {
                            try {
                                formatNumberOutput();
                                uangFormat = getText();
                            } catch (ParseException ex) {
                                Logger.getLogger(Uangteks.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    if ("".equals(getText())) {
                        //System.out.println("Masuk 3");
                        uang = null;
                        uangFormat = "";
                        setText(uangFormat);
                    }
                    if (!isNumeric(getUangteks()) && !"".equals(getText())) {
                        setText(uangFormat);
                    }
                }
            }
        };
        focusedProperty().addListener(cL);
    }
    
    public void formatNumberOutput() throws ParseException {
        if (isEditable()) {
            uang = input.parse(getText());
            setText(output.format(uang));
        }
    }

    public void formatNumberInput() throws ParseException {
        if (isEditable()) {
            uang = output.parse(getText());
            String u = getText().replaceAll(",", "");
//        setText(input.format(uang));
            setText(u);
        }
    }

    public boolean isNumeric(String str) {
        try {
            if (getText() != null) {
                double d = Double.parseDouble(str);
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public String getUangteks() {
        if (getText() != null) {
            return getText().replaceAll(",", "");
        } else {
            return null;
        }
    }

    public void setUangTeks(Integer data) {
        if (data != null) {
            setText(NUMBER_FORMAT.format(data));
        } else {
            setText(null);
        }
    }
    
    public void setEditableUangteks(Boolean value, Boolean colour) {
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