package util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.mail.search.IntegerComparisonTerm;

public class PerhitunganBiaya {

	public static int getTotalAsuransi(String hargaInput) {
		int hInput = formatInteger.toIntegerVersiNullJadiNol(hargaInput);
		if (hInput != 0) {
			int Asuransi = hInput * 2 / 1000 + 5000;
			return Asuransi;
		} else {
			return 0;
		}
	}
	
	public static int getHargaSebaliknya(int hAsuransi) {
		//harga = (asuransi - 5000) / 0.02 
		if (hAsuransi != 0) {
			return (int) ((hAsuransi-5000)/0.02);
		} else {
			return 0;
		}
	}

	public static int getTotalBiaya(String Asuransi, String hargaPerKg, String beratBulat) {
		int asuransiFinal = formatInteger.toIntegerVersiNullJadiNol(Asuransi);
		int hargaPerKgFinal = formatInteger.toIntegerVersiNullJadiNol(hargaPerKg);
		int beratBulatFinal = formatInteger.toIntegerVersiNullJadiNol(beratBulat);
		int totalBiaya = hargaPerKgFinal * beratBulatFinal + asuransiFinal;
		return totalBiaya;
	}

	public static int getHarga(String berat, String bKg) {
		int ber = formatInteger.toIntegerVersiNullJadiNol(berat);
		int biayaKg = formatInteger.toIntegerVersiNullJadiNol(bKg);
		int totBiaya = biayaKg * ber;
		System.out.println("---------------------BIAYA HARGA :  " + totBiaya);
		if (totBiaya == 0) {
			totBiaya = biayaKg;
		}
		return totBiaya;
	}

	// chris perbaikan di belakang koma jadi tidak akan lebih dari 2 digit
	public static Integer pembulatanRapid(String doubleAsText) {
		Double number = Double.parseDouble(doubleAsText);
		int i = Integer.valueOf(number.intValue());
			if (doubleAsText.length() != 1) {
				int fractional = Integer.parseInt(doubleAsText.split("\\.")[1]);
				
//				String coba = String.format("%02d", fractional);
//				if(coba.length()>2){
//					fractional = Integer.parseInt(coba.substring(0, 2));
//				}else if(coba.length()==1){
//					fractional = Integer.parseInt(coba+"0");
//				}
				System.out.println("------" + fractional);
					if (fractional > 30) {
						i = i + 1;
					} else {
						if (i == 0) {
							i = i + 1;
						} else {
						}
					}
				} else {
					System.out.println("NILAI HANYA 1 Bukan Double");
					i = i;
				}
		return i;
	}
	
	// chris	
	public static String getFixDecimal2Digit(String dec){
		BigDecimal raw = new BigDecimal(dec);
		
		return raw.setScale(2, RoundingMode.CEILING).toString();
	}
}
