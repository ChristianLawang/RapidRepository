package driver;

import java.util.Calendar;
import java.util.Date;

import util.DateUtil;

public class DateRule {

	public static void main(String[] args) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        System.out.println("Normal : " + cal.getTime());
		Date dt = DateUtil.fotoTimbangDateGenerateRule(cal.getTime());
		
		System.out.println("Rapid ; " + dt);
	}
}
