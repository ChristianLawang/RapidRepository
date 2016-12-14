package util;

import java.util.Map;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;

import service.*;

public class HibernateUtil {

    private static SessionFactory sessionFactory = null;
    private static SessionFactory sessionFactoryHO = null;
    private static Configuration configuration = null;
    private static Configuration configurationHO = null;

    private static SessionFactory buildSessionFactory() {

        try {
        	configuration=new Configuration();
        	configuration.configure().addProperties(ConfigurationService.getHibernateProperties());
        	sessionFactory=configuration.buildSessionFactory();
        	
        	return sessionFactory;
        }

        catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }

    }
    
    private static SessionFactory buildSessionFactoryHO() {
    	
    	try {
    		Properties HOMySQLMapping = ConfigurationService.getHibernateProperties();
    		
    		Map<String, Object> config = GenericService.getConfig();
    		
    		HOMySQLMapping.setProperty("hibernate.connection.url", config.get("cloud_ip_address")==null?"":"jdbc:"+config.get("cloud_ip_address"));
    		HOMySQLMapping.setProperty("hibernate.connection.username", config.get("cloud_username")==null?"":(String) config.get("cloud_username"));
    		HOMySQLMapping.setProperty("hibernate.connection.password", config.get("cloud_password")==null?"":(String) config.get("cloud_password"));

    		System.out.println(HOMySQLMapping);
    		configurationHO=new Configuration();
    		configurationHO.configure().addProperties(HOMySQLMapping);
        	sessionFactoryHO=configurationHO.buildSessionFactory();
        	
        	return sessionFactoryHO;
        }

        catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
		
	}


    public static SessionFactory getSessionFactory() {
    	
    	if(sessionFactory==null){
    		buildSessionFactory();
    	}

        return sessionFactory;

    }

	private static SessionFactory getSessionFactoryHO() {
		if(sessionFactoryHO==null){
    		buildSessionFactoryHO();
    	}

        return sessionFactoryHO;
	}

	public static void reinit() {
		sessionFactory=null;
		buildSessionFactory();
		
	}
	
	public static Session openSession(){
		Session sess = getSessionFactory().getCurrentSession();
		sess.beginTransaction();
		return sess;
	}


	public static Session beginSession() {
		return getSessionFactory().openSession();
	}


	public static Session openSessionHO() {
		Session sess = getSessionFactoryHO().getCurrentSession();
		sess.beginTransaction();
		return sess;
	}
}
