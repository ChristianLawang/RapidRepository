package util;

import Decoder.BASE64Encoder;

public class HashPassword {
	public static String hashPassword(String strYourPassword) {
        java.security.MessageDigest d = null;
        try {
            d = java.security.MessageDigest.getInstance("SHA-1");
            d.reset();
            d.update(strYourPassword.getBytes("UTF-16"));
        } catch (Exception e) {
                e.printStackTrace();
        }        
        byte rawByte[] =d.digest();
        return ((new BASE64Encoder()).encode(rawByte));
   }
	
	public static void main (String [] args){
		HashPassword has = new HashPassword();
		
		String a = has.hashPassword("123");
		System.out.println(a);
	}
}
