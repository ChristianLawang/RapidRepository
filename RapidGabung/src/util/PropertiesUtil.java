package util;

import java.util.Properties;

import com.ochafik.lang.jnaerator.runtime.This;

import service.ConfigurationService;

public class PropertiesUtil {

	private static Properties prop = ConfigurationService.getHibernateProperties();
	
	public static String getPerwakilan() {
		return prop.getProperty("app.current.perwakilan");
	}

	public static String getKodePerwakilan() {
		return prop.getProperty("app.current.kodeperwakilan");
	}

	public static String getImageDriveLocation() {
		return prop.getProperty("app.slr.drive.location");
	}

	public static String getImageFolderLocation() {
		return prop.getProperty("app.slr.folder.location");
	}

	public static String getEmailUser() {
		return prop.getProperty("app.current.email.username");
	}

	public static String getEmailPassword() {
		return prop.getProperty("app.current.email.password");
	}

	public static String getCanonDriverLocation() {
		return prop.getProperty("app.current.canon.sdk.location");
	}
	
}
