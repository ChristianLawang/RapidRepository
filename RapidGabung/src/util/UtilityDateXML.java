/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author kemenkeu
 */
public class UtilityDateXML extends XmlAdapter<String, Date>{
 
    //private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        return dateFormat.parse(v);
    }
    
    public static String toString(Date data)
    {
        String hasil = "";
        if(data!=null)
        {
            hasil = DATE_FORMAT.format(data);
        }
        return hasil;
    }
    
    public static Date toDate(String data)
    {
        Date hasil = null;
        if(data!=null)
        {
            if(!data.equalsIgnoreCase(""))
            {
                try {
                    hasil = DATE_FORMAT.parse(data);
                } catch (ParseException ex) {
                    Logger.getLogger(UtilityDateXML.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return hasil;
    }
    
    public static String convertTimeToString(Date data)
    {
        String hasil = "";
        if(data!=null)
        {
            hasil = TIME_FORMAT.format(data);
        }
        return hasil;
    }
    
    public static Date convertToTime(String data)
    {
        Date hasil = null;
        if(data!=null)
        {
            if(!data.equalsIgnoreCase(""))
            {
                try {
                    hasil = TIME_FORMAT.parse(data);
                } catch (ParseException ex) {
                    Logger.getLogger(UtilityDateXML.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return hasil;
    }
}
