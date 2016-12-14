package VO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import util.BigDecimalUtil;
import util.PerhitunganBiaya;

public class TimbangVO {
	BigDecimal gross;
	BigDecimal tare;
	BigDecimal net;
	
	public TimbangVO() {
		String content = "";
		Process process;
		try {
			process = new ProcessBuilder("MyWork1.exe").start();
		
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				content += line;
			}
		
			System.out.println(content);
			String grossChar = "Gross";
			String grossKgChar = "kgTare";
			
			Integer grossPos = content.indexOf(grossChar);
			Integer grossKgPos = content.indexOf(grossKgChar);
			
//			System.out.println(content.substring(grossPos+grossChar.length(), grossKgPos).trim());
			
			String grossIncome = content.substring(grossPos+grossChar.length(), grossKgPos).trim();
			String tareIncome = content.substring(57, 61);
			String netIncome = content.substring(73, 77);
			
			this.gross = new BigDecimal(grossIncome.trim());
			this.tare = new BigDecimal(tareIncome.trim());
			this.net = new BigDecimal(netIncome.trim());
			
		} catch (IOException e) {
			this.gross = new BigDecimal("0.00");
			this.tare = new BigDecimal("0.00");
			this.net = new BigDecimal("0.00");
		}
		
	}

	public BigDecimal getGross() {
		return gross;
	}

	public BigDecimal getTare() {
		return tare;
	}

	public BigDecimal getNet() {
		return net;
	}

	public BigDecimal getGrossRoundUp() {
//		BigDecimalUtil BU = new BigDecimalUtil();		
//		return this.gross.setScale(0, BigDecimal.ROUND_UP);
//		return gross;
		
		return new BigDecimal(PerhitunganBiaya.pembulatanRapid(gross.toString()));
		
	}
}
