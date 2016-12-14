package driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.TtHeader;

public class CheckSync {

	public static void main(String[] args) {
		Map<String, Object> imp = new HashMap<String, Object>();
		try {
			File file = new File("D:/Rapid/20160715.hash");
		    FileInputStream f = new FileInputStream(file);
		    ObjectInputStream s;
			s = new ObjectInputStream(f);
		    try {
		    	imp = (Map<String, Object>) s.readObject();
		    	
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<TtHeader> header = (List<TtHeader>) imp.get("header");
		System.out.println(header.size());
		
		for (TtHeader tt : header) {
			System.out.println(tt.getAwbHeader());
			
		}
	}

}
