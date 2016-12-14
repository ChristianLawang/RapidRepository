package util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.util.StringConverter;

public class formatInteger {
	public static int toIntegerVersiNullJadiNol(String data)
    {
        Integer hasil = new Integer(0);
        if(data == null || data.equalsIgnoreCase(""))
        {
            return hasil;
        }
        else
        {
            try
            {
                hasil = new Integer(data);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return hasil;
        }
    }
	
	public static void prepareTextField(TextField tf) {//TODO LIST
	    tf.setAlignment(Pos.CENTER);
	    tf.setBackground(Background.EMPTY);
	    tf.setBorder(Border.EMPTY);
	    tf.setPadding(Insets.EMPTY);
	    tf.setPrefColumnCount(2);
	}
	public static class IntRangeStringConverter extends StringConverter<Integer> {

	    private final int min;
	    private final int max;

	    public IntRangeStringConverter(int min, int max) {
	        this.min = min;
	        this.max = max;
	    }

	    @Override
	    public String toString(Integer object) {
	        return "";
	    }

	    @Override
	    public Integer fromString(String string) {
	        int integer = Integer.parseInt(string);
	        if (integer > max || integer < min) {
	            throw new IllegalArgumentException();
	        }

	        return 0;
	    }

	}

}
