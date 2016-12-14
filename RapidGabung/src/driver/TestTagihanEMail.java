package driver;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import VO.TagihanVO;
import service.PelangganService;
import util.DateUtil;

public class TestTagihanEMail {

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        cal.add(Calendar.DATE, -1);      
        System.out.println(cal.getTime());
        
		List<TagihanVO> vo = PelangganService
				.getNotifikationEmailPelanggan(
						"syengalery", 
						cal.getTime(), 
						cal.getTime() 
					);
	}

}
