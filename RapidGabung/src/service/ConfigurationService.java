package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationService {
	private static final String PROG_HOME_KEY="sismon.home";
	

	private static Properties hibernateProperties=null;
	
	
	static{
		hibernateProperties=loadFile("config", "hibernate.properties");
	}

	
	private static Properties loadFile(String folder, String filename){
		Properties prop=new Properties();
		//File file=new File("C:/Users/vuryfaink/workspace_svn/RapidGabung",filename);
		File file=new File("C:/DLL/",filename);
		
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				fis.close();
				file=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return prop;
	}
	
	

	public static Properties getHibernateProperties() {
		
		return hibernateProperties;
	}

	public static String getAppHome() {
		return System.getProperty(PROG_HOME_KEY);
	}	

	


}
