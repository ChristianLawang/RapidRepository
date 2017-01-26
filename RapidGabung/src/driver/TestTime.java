package driver;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import VO.TagihanVO;
import service.PelangganService;
import util.DateUtil;

public class TestTime {

	public static void main(String[] args) {
		PelangganService testS = new PelangganService();
		
		Calendar calStart=Calendar.getInstance();
		calStart.setTime(new Date());
		calStart.set(Calendar.DAY_OF_MONTH, 14);
		calStart.set(Calendar.HOUR_OF_DAY, 12);
		calStart.set(Calendar.MINUTE, 0);
		calStart.set(Calendar.SECOND, 0);
		calStart.set(Calendar.MILLISECOND, 0);
		
		System.out.println("--> waktu asli : " + calStart.getTime());
		System.out.println("--> waktu foto timbang : " + DateUtil.fotoTimbangDateGenerateRule(calStart.getTime()));
	}

}
