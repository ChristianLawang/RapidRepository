package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;


public class TempUtil {

	private static final String TEMP_FOLDER = "C:/DLL/temp";

	// chris
	public static void saveFotoTimbangLocal(Map<String, Object> map) {
		File file = new File(TEMP_FOLDER, "fototimbang.tmp");
		try {
			FileOutputStream f = new FileOutputStream(file);
	        ObjectOutputStream s = new ObjectOutputStream(f);
	        s.writeObject(map);
		    s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	// chris
	public static Map<String, Object> loadFotoTimbangLocal(){
		Map<String, Object> obj = null;
		try {
			File file = new File(TEMP_FOLDER, "fototimbang.tmp");
		    FileInputStream f = new FileInputStream(file);
		    ObjectInputStream s;
			s = new ObjectInputStream(f);
		    try {
		    	obj = (Map<String, Object>) s.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	// chris
	public static void clearFotoTimbangLocal(){
		File file = new File(TEMP_FOLDER, "fototimbang.tmp");
		try {
			FileOutputStream f = new FileOutputStream(file);
	        ObjectOutputStream s = new ObjectOutputStream(f);
	        s.writeObject(null);
		    s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	// chris
	public static void saveFotoTimbangCamera(String strAWB, String strFolder, String strFileName) {
		File file = new File(TEMP_FOLDER, "fototimbangCamera.tmp");
		try {
			FileOutputStream f = new FileOutputStream(file);
	        ObjectOutputStream s = new ObjectOutputStream(f);
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("awb", strAWB);
	        map.put("folder", strFolder);
	        map.put("filename", strFileName);
	        s.writeObject(map);
		    s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	// chris
	public static Map<String, Object> loadFotoTimbangCamera() {
		Map<String, Object> obj = null;
		try {
			File file = new File(TEMP_FOLDER, "fototimbangcamera.tmp");
		    FileInputStream f = new FileInputStream(file);
		    ObjectInputStream s;
			s = new ObjectInputStream(f);
		    try {
		    	obj = (Map<String, Object>) s.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
}
