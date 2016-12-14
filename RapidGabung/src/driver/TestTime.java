package driver;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import VO.TagihanVO;
import service.PelangganService;

public class TestTime {

	public static void main(String[] args) {
		PelangganService testS = new PelangganService();
		
		Calendar calStart=Calendar.getInstance();
		calStart.setTime(new Date());
		calStart.set(Calendar.DAY_OF_MONTH, 14);
		calStart.set(Calendar.HOUR_OF_DAY, 0);
		calStart.set(Calendar.MINUTE, 0);
		calStart.set(Calendar.SECOND, 0);
		calStart.set(Calendar.MILLISECOND, 0);
		
		Calendar calEnd=Calendar.getInstance();
		calEnd.setTime(new Date());
		calEnd.set(Calendar.DAY_OF_MONTH, 14);
		calEnd.set(Calendar.HOUR_OF_DAY, 23);
		calEnd.set(Calendar.MINUTE, 59);
		calEnd.set(Calendar.SECOND, 59);
		calEnd.set(Calendar.MILLISECOND, 59);
		
		List<TagihanVO> res = testS.getNotifikationEmailPelanggan("syengalery", calStart.getTime(), calEnd.getTime());
		System.out.println("--> res : " + res.size());
		
	}

}
