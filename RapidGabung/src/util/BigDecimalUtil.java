package util;

import java.math.BigDecimal;

public class BigDecimalUtil {
	
	public String getBigDecimalToString(BigDecimal bgDec){
		return bgDec.toString();
	}
	
	public BigDecimal getStringToBigDecimal(String bgDec){
		return new BigDecimal(bgDec);		
	}
	
	// chris ubah
	public String getBigDecimalToStringRound(BigDecimal bgDec){
//		return bgDec.setScale(0, BigDecimal.ROUND_UP).toString();
		return PerhitunganBiaya.pembulatanRapid(bgDec.toString()).toString();
	}
	
//	// chris
//	public BigDecimal getStringToBigDecimalRound(String bgDec){
//		BigDecimal dec = new BigDecimal(this.getBigDecimalToStringRound(new BigDecimal(bgDec)));
//		return new BigDecimal(PerhitunganBiaya.pembulatanRapid(dec.toString()));
//	}
	
	public static BigDecimal truncateDecimal(double x, int numberofDecimals) {
		if (x > 0) {
			return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
		} else {
			return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
		}
	}

}
