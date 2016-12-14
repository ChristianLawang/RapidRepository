package util;

import java.util.Date;

import service.DataPaketService;
import service.GenericService;
import service.PotoPaketService;

public class SequenceUtil {

//	// chris
//	public static Integer getNewPotoPaketID(String strPerwakilan, String strKodePerwakilan){
////		Integer count = PotoPaketService.getCount();
////		String idLast = String.format("%05d", count++);
////		String year = DateUtil.getYearOnly(new Date());
////		
////		return "PP-" + strPerwakilan + "-" + strKodePerwakilan + "-" + year.substring(2) + "-" + idLast;
//		
//		Integer count = PotoPaketService.getCount();
//		count++;
//		return count;
//	}

	// chris
	public static Integer getNewDataPaketID(String strPerwakilan, String strKodePerwakilan){
//		Integer count = DataPaketService.getCount();
//		String idLast = String.format("%05d", count++);
//		String year = DateUtil.getYearOnly(new Date());
//		
//		return "DP-" + strPerwakilan + "-" + strKodePerwakilan + "-" + year.substring(2) + "-" + idLast;
		
		Integer count = DataPaketService.getCount();
		count++;
		return count;
	}
	
	// chris
	public String getNewAWBID() {
		String strPerwakilan = PropertiesUtil.getPerwakilan();
		String strKodePerwakilan = PropertiesUtil.getKodePerwakilan();
		
		String CGK = strPerwakilan+strKodePerwakilan;
		String year = DateUtil.getYearOnly(new Date());
		
		Integer count = PotoPaketService.getCountMax(CGK, year.substring(2));
		
		Integer countIncrease = ( count + 1);
		
		String idLast = String.format("%08d", countIncrease);
		
		return strPerwakilan+strKodePerwakilan+year.substring(2)+idLast;
	}

}
