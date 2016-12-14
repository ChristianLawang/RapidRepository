package driver;

import java.util.Calendar;
import java.util.Date;

import util.DateUtil;

public class TestTanggal {

	public static void main(String[] args) {
		DateUtil du = new DateUtil();
		System.out.println(du.getDayOnly(new Date()));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 01);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.DAY_OF_MONTH, 28);
		
		System.out.println("Calendar : " + DateUtil.fotoTimbangDateGenerateRule(cal.getTime()));
		
		
	}

}
