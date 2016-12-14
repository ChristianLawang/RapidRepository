package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import service.ConfigurationService;

public class MysqlConnect {

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		return MysqlConnect.getMySQLConnection();
	}

	public static Connection getMySQLConnection() throws ClassNotFoundException, SQLException {
		Properties prop = ConfigurationService.getHibernateProperties();
		String driver = (String) prop.get("hibernate.connection.driver_class");
		String hostName = (String) prop.get("hibernate.connection.url");
		String userName = (String) prop.get("hibernate.connection.username");
		String password = (String) prop.get("hibernate.connection.password");
		return getMySQLConnection(driver, hostName, userName, password);
	}

	public static Connection getMySQLConnection(String driver, String hostName, String userName, String password)
			throws SQLException, ClassNotFoundException {
		Class.forName(driver);

		String connectionURL = hostName;

		Connection conn = DriverManager.getConnection(connectionURL, userName, password);
		return conn;
	}
	
//	public static void main(String [] args){
//		try {
//			MysqlConnect.getConnection();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
