package driver;

import util.EmailUtil;

public class TestKirimEmail {

	public static void main(String[] args) {
		String username = "rapidexpress.id@gmail.com";
		String password = "rapid150515";	
		EmailUtil email = new EmailUtil();
		
		email.setEmail(
				username, 
				password,
				"zechreich2015@gmail.com",
				"test body email",
				"C:/DLL/PDF/2016/06/30/3ce_jakarta.pdf", 
				"ayah_dipta@yahoo.co.id",
				"chris"
			);
		
	}

}
