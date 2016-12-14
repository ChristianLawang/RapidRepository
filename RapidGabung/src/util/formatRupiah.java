package util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class formatRupiah {
	public static String formatIndonesiaWithtRp(int bdRupiah){
		String res = "";
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
		res = nf.format(bdRupiah);
		return res;
	}
	public static String formatIndonesia(int bdRupiah){
		String res = "";
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
		res = nf.format(bdRupiah);
		return res.replace("Rp", "").replaceAll(",00", "");
	}
	
	public static void main(String [] args){
//		formatRupiah rup = new formatRupiah();
		String a = formatIndonesia(10000);
		int b = Integer.parseInt(a);
		System.out.println(a);
	}
	
	// chris
	public static String formatIndonesiaTanpaKoma(String bdRupiah){
//		String number = bdRupiah.toString();
		double amount = Double.parseDouble(bdRupiah);
		DecimalFormat formatter = new DecimalFormat("#,###");
		return formatter.format(amount);
	}
	
	public static String formatIndonesiaTanpaTitik(String rupiah) {
		return rupiah.replaceAll("\\.", "");
	}
}
