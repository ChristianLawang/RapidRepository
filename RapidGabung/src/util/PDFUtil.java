package util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import VO.EntryDataShowVO;
import VO.PenandaLunasVO2;

//import com.lowagie.text.Document;
//import com.lowagie.text.DocumentException;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.html.simpleparser.HTMLWorker;
//import com.lowagie.text.html.simpleparser.StyleSheet;
//import com.lowagie.text.pdf.PdfWriter;

import VO.TagihanVO;
import controller.MasterPelangganController.PelangganTV;
import entity.TrPelanggan;

public class PDFUtil {

	public void generateAndCreateFilePDFForEmailKurirIn(
			List<TagihanVO> objs, 
			String path, 
			TrPelanggan plg, 
			String strStart, 
			String strEnd,
			String strPerwakilan){
		String 	content = "";
		String kodePickup = "";
		if(objs.size()>0){
			kodePickup = objs.get(0).getKodePickup();
		}
		content+="" +
				"<br></br>" +
				"<table> " +
						"<tr> " +
							"<td width=25% align=left><img src=c:/dll/rapid.png width=150 height=50></td> " +
							"<td width=75% align=right><font face=Arial size=1>Branch Jakarta : Jalan Tanah Abang V, No F3, Jakarta Pusat</FONT>" +
							"<p align=right><font face=Arial size=1>Pengirim : " + plg.getNamaAkun()+"</font></p>"
							+ "<p align=right><font face=Arial size=1>Kode Pickup : " + kodePickup + "</font></p>"
							+ "<p align=right><font face=Arial size=1>Periode : " + strStart + " - " +strEnd + " </font></p>" + 
//							+ "<p align=right><font face=Arial size=1>Perwakilan : "+strPerwakilan+"</font></p></td> " +
							  "<p align=center><font face=Arial size=1><H2>INVOICE</H2></font></p>" + 
						"</tr> " +
					    "</table> " +
					    "<table align=left>" +
				        "<tr>" +
				        "<td>---------------------------------------------------------------------------------------------------------------------------------</td>" +
				        "</tr>" +
				        "</table>" +
				"<table> " +
					"<tr> " +
						"<td><font face=Arial size=1>No</font></td> " +
						"<td><font face=Arial size=1>No. Resi</font></td> " +
						"<td><font face=Arial size=1>Tujuan</font></td> " +
						"<td><font face=Arial size=1>Penerima</font></td> " +
						"<td><font face=Arial size=1>HP Penerima</font></td> " +
						"<td><font face=Arial size=1>Resi JNE</font></td> " +
						"<td><font face=Arial size=1>Berat</font></td> " +
						"<td><font face=Arial size=1>Biaya</font></td> " +
						"<td><font face=Arial size=1>Asuransi</font></td> " +
						"<td><font face=Arial size=1>Diskon</font></td> " +
						"<td><font face=Arial size=1>Total</font></td> " +
					"</tr> " +
						
				"<tbody> ";
		Integer intInd = 0;
		Integer intBiaya = 0;
		Integer intAsuransi = 0;
		Integer intDiskon = 0;
		Integer intTotalTagihan = 0;
		
		for (TagihanVO kur : objs) {
			intInd++;
			intAsuransi = intAsuransi + (kur.getAsuransi()==null?0:kur.getAsuransi());
			intDiskon = intDiskon + (kur.getDiskonPelanggan()==null?0:kur.getDiskonPelanggan().intValue());
			intBiaya = intBiaya + (kur.getHarga()==null?0:kur.getHarga().intValue());
			intTotalTagihan = intTotalTagihan + (kur.getTotalBiaya()==null?0:kur.getTotalBiaya().intValue());
			
			
			String awb = kur.getAwb()==null?"-":kur.getAwb();
			String tujuan = kur.getTujuan()==null?"-":kur.getTujuan();
			String penerima = kur.getPenerima()==null?"-":kur.getPenerima();
			String telp = kur.getTelpPenerima()==null?"-":kur.getTelpPenerima();
			String JNE = kur.getResiJNE()==null?"-":kur.getResiJNE();
			String berat = kur.getPbFinal()==null?"0":kur.getPbFinal();
			String harga = kur.getHarga()==null?"0":kur.getHarga().toString();
			String asuransi = kur.getAsuransi()==null?"0":kur.getAsuransi().toString();
//			String diskon = kur.getResiJNE()==null?kur.getDiskonRapid().toString():kur.getDiskonJNE()==null?"0":kur.getDiskonJNE().toString();
			String diskon = "";
			if(kur.getResiJNE()==null){
				diskon = kur.getDiskonRapid()==null?"0":kur.getDiskonRapid().toString();
			}else{
				diskon = kur.getDiskonJNE()==null?"0":kur.getDiskonJNE().toString();
			}
			String total = kur.getTotalBiaya()==null?"0": ""+kur.getTotalBiaya().intValue();
			
			String isi = 
					"<tr>" +
							"<td><font face=Arial size=1>" + intInd + "</font></td> " +
							"<td><font face=Arial size=1>" + awb + "</font></td> " +
							"<td><font face=Arial size=1>" + tujuan + "</font></td> " +
							"<td><font face=Arial size=1>" + penerima + "</font></td> " +
							"<td><font face=Arial size=1>" + telp + "</font></td> " +
							"<td><font face=Arial size=1>" + JNE + "</font></td> " +
							"<td><font face=Arial size=1>" + berat + "</font></td> " +
							"<td><font face=Arial size=1>" + formatRupiah.formatIndonesia(Integer.parseInt(harga)) + "</font></td> " +
							"<td><font face=Arial size=1>" + formatRupiah.formatIndonesia(Integer.parseInt(asuransi)) + "</font></td> " +
							"<td><font face=Arial size=1>" + diskon + "%"+"</font></td> " +
							"<td><font face=Arial size=1>" + formatRupiah.formatIndonesia(Integer.parseInt(total)) + "</font></td> " +

					"</tr>";
			
//			System.out.println("--> isi : " + isi );
					
			content+=isi;
		}
			intTotalTagihan = intBiaya+intAsuransi-intDiskon;
			content+="</tbody> " +
					"</table>" +
					"<br></br>" +
					"<br></br>" +
					"<p align=left><font face=Arial size=1>Jumlah Paket : " + intInd + "</font></p>" +
					"<br></br>" +
					"<p align=left><font face=Arial size=1>Total Biaya : Rp."+formatRupiah.formatIndonesia(intBiaya)+"</font></p>" +
					"<br></br>" +
					"<p align=left><font face=Arial size=1>Total Asuransi : Rp."+formatRupiah.formatIndonesia(intAsuransi)+"</font></p>" +
					"<br></br>" +
					"<p align=left><font face=Arial size=1>Total Diskon : Rp."+formatRupiah.formatIndonesia(intDiskon)+"</font></p>" +
					"<br></br>" +
					"<p align=left><font face=Arial size=1>Total Tagihan : Rp."+formatRupiah.formatIndonesia(intTotalTagihan)+"</font></p>" +
					"<br></br>" +
					"<br></br>" +
					"<p align=center><font face=Arial size=1>Mohon untuk dapat melakukan pembayaran ke Bank BCA No Rekening 4823150515 an PT Rapid Express Indonesia.</font></p>" +
					"<br></br>" +
					"<p align=center><font face=Arial size=1>Atau Bank Mandiri No Rekening 121-00150515-88 an PT Rapid Express Indonesia</font></p>" +
					"<br></br>" +
					"<p align=center><font face=Arial size=1>Apabila tedapat ketidaksesuaian, silahkan untuk menghubungi CS kami di nomor 021-3514960 atau email customercare@rapid.co.id.</font></p>" +
					"<br></br>" +
					"<p align=center><font face=Arial size=1>Untuk info lebih lanjut silahkan akses www.rapid.co.id</font></p>" +
					"";
		
//		try {
//		    OutputStream file = new FileOutputStream(new File(path));
//		    Document document = new Document();
//		    PdfWriter.getInstance(document, file);
//		    
//		    document.open();
//		    HTMLWorker htmlWorker = new HTMLWorker(document);
//		    htmlWorker.parse(new StringReader(content));
//		    document.close();
//		    file.close();
//		} catch (DocumentException | IOException e1) {
//			// TODO Auto-genserated catch block
//			e1.printStackTrace();
//		}
	}
	
	public static void testPDF(String path){
		String content =
				
				  "<br>"
				+ "</br>"
				+ "<table> "
					+ "<tr> "
						+ "<td width=25% align=left>"
							+ "<img src=c:/dll/rapid.png width=150 height=50>"
						+ "</td> "
						+ "<td width=75% align=right>"
							+ "<font face=Arial size=1>Branch Jakarta : Jalan Tanah Abang V, No F3, Jakarta Pusat</FONT>"
							+ "<p align=right>"
								+ "<font face=Arial size=1>Pengirim : laclairz</font>"
							+ "</p>"
							+ "<p align=right>"
								+ "<font face=Arial size=1>Kode Pickup : 013900</font>"
							+ "</p>"
							+ "<p align=right>"
								+ "<font face=Arial size=1>Periode : 2016-06-17 - 2016-06-17 </font>"
							+ "</p>"
							+ "<p align=center>"
								+ "<font face=Arial size=1><H2>INVOICE</H2></font>"
							+ "</p>"
						+ "</td>"
					+ "</tr> "
				+ "</table> "
				+ "<table align=left>"
					+ "<tr>"
						+ "<td>---------------------------------------------------------------------------------------------------------------------------------</td>"
					+ "</tr>"
				+ "</table>"
				+ "<table> "
//				+	"<col width='100'>"
//				+  	"<col width='100'>"
//				+  	"<col width='100'>"
//				+  	"<col width='50'>"
//				+  	"<col width='80'>"
//				+  	"<col width='80'>"
//				+  	"<col width='80'>"
//				+  	"<col width='80'>"
//				+  	"<col width='80'>"
//				+  	"<col width='80'>"
//				+  	"<col width='80'>"
				+"<tr> " 
				+	"<td><font face=Arial size=1>No</font></td> " 
				+	"<td><font face=Arial size=1>No. Resi</font></td> " 
				+	"<td><font face=Arial size=1>Tujuan</font></td> " 
				+	"<td><font face=Arial size=1>Penerima</font></td> " 
				+	"<td><font face=Arial size=1>HP Penerima</font></td> " 
				+	"<td><font face=Arial size=1>Resi JNE</font></td> " 
				+	"<td><font face=Arial size=1>Berat</font></td> " 
				+	"<td><font face=Arial size=1>Biaya</font></td> " 
				+	"<td><font face=Arial size=1>Asuransi</font></td> " 
				+	"<td><font face=Arial size=1>Diskon</font></td> " 
				+	"<td><font face=Arial size=1>Total</font></td> " 
				+"</tr> " 
				+ "<tbody> "
				+ "<tr>"
					+ "<td><font face=Arial size='6px'>"
						+ "1"
					+ "</font></td>"
					+ "<td><font face=Arial size='6px'>"
						+ "CGK0011600004342"
					+ "</font></td>"
					+ "<td><font face=Arial size='6px'>"
						+ "-"
					+ "</font></td>"
					+ "<td><font face=Arial size='6px'>"
						+ "-"
					+ "</font></td>"
					+ "<td><font face=Arial size='6px'>"
						+ "-"
					+ "</font></td>"
					+ "<td><font face=Arial size='6px'>"
						+ "-"
					+ "</font></td>"
					+ "<td><font face=Arial size='6px'>"
						+ "1"
					+ "</font></td>"
					+ "<td><font face=Arial size='6px'>"
						+ "0,00"
					+ "</font></td>"
					+ "<td><font face=Arial size='6px'>"
						+ "0,00"
					+ "</font></td>"
					+ "<td><font face=Arial size='6px'>"
						+ "20%"
					+ "</font></td>"
					+ "<td><font face=Arial size='6px'>"
						+ "0,00"
					+ "</font></td>"
				+ "</tr>"
				+ "</tbody> "
			+ "</table>"
			+ "<br>"
			+ "</br>"
			+ "<br>"
			+ "</br>"
				+ "<p align=left>"
					+ "<font face=Arial size=1>Jumlah Paket : 65</font>"
				+ "</p>"
			+ "<br>"
			+ "</br>"
			+ "<p align=left>"
				+ "<font face=Arial size=1>Total Biaya : Rp.657.000,00</font>"
			+ "</p>"
			+ "<br>"
			+ "</br>"
				+ "<p align=left>"
					+ "<font face=Arial size=1>Total Asuransi : Rp.0,00</font>"
				+ "</p>"
				+ "<br>"
				+ "</br>"
				+ "<p align=left>"
					+ "<font face=Arial size=1>Total Diskon : Rp.131.400,00</font>"
				+ "</p>"
				+ "<br>"
				+ "</br>"
				+ "<p align=left><font face=Arial size=1>Total Tagihan : Rp.525.600,00</font></p>"
				+ "<br>"
				+ "</br>"
				+ "<br>"
				+ "</br>"
				+ "<p align=center>"
					+ "<font face=Arial size=1>Mohon untuk dapat melakukan pembayaran ke Bank BCA No Rekening 4823150515 an PT Rapid Express Indonesia.</font>"
				+ "</p>"
				+ "<br>"
				+ "</br>"
				+ "<p align=center>"
					+ "<font face=Arial size=1>Atau Bank Mandiri No Rekening 121-00150515-88 an PT Rapid Express Indonesia</font>"
				+ "</p>"
				+ "<br>"
				+ "</br>"
				+ "<p align=center>"
					+ "<font face=Arial size=1>Apabila tedapat ketidaksesuaian, silahkan untuk menghubungi CS kami di nomor 021-3514960 atau email customercare@rapid.co.id.</font>"
				+ "</p>"
				+ "<br>"
				+ "</br>"
				+ "<p align=center>"
					+ "<font face=Arial size=1>Untuk info lebih lanjut silahkan akses www.rapid.co.id</font>"
				+ "</p>";
		
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader("c:/dll/testpdf2.txt"));
//		
//			content = "";
//			String line;
//
//		
//			while((line = reader.readLine()) != null){
//				content+="\r\n";
//				content+=line;
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	public void generateAndCreateFilePDFForEmailKurirInNew(
			List<TagihanVO> objs, 
			String path, 
			PelangganTV plg, 
			String strStart, 
			String strEnd,
			String strPerwakilan){
		String kodePickup = "";
		if(objs.size()>0){
			kodePickup = objs.get(0).getKodePickup();
		}
	
		try {
			BufferedReader reader = new BufferedReader(new FileReader("c:/dll/invoicedesign.txt"));
		
			String content = "";
			String line;

			Integer intInd = 0;
			Integer intBiaya = 0;
			Integer intAsuransi = 0;
			Integer intDiskon = 0;
			Integer intTotalTagihan = 0;
		
			while((line = reader.readLine()) != null){
				if(line.trim().equalsIgnoreCase("[!TRTR]")){
					content+="\r\n";
					
					for (TagihanVO kur : objs) {
//						kur.print();
						if(!kur.getPenerima().trim().equalsIgnoreCase("XXX")){
							intInd++;
							intAsuransi = intAsuransi + (kur.getAsuransi()==null?0:kur.getAsuransi());
							intDiskon = intDiskon + (kur.getDiskonPelanggan()==null?0:kur.getDiskonPelanggan().intValue());
							intBiaya = intBiaya + (kur.getHarga()==null?0:kur.getHarga().intValue());
							intTotalTagihan = intTotalTagihan + (kur.getTotalBiaya()==null?0:kur.getTotalBiaya().intValue());
							
							String awb = kur.getAwb()==null?"-":kur.getAwb();
							String tujuan = kur.getTujuan()==null?"-":kur.getTujuan();
							String penerima = kur.getPenerima()==null?"-":kur.getPenerima();
							String telp = kur.getTelpPenerima()==null?"-":kur.getTelpPenerima();
							String JNE = kur.getResiJNE()==null?"-":kur.getResiJNE();
							String berat = kur.getPbFinal()==null?"0":kur.getPbFinal();
							String harga = kur.getHarga()==null?"0":kur.getHarga().toString();
							String asuransi = kur.getAsuransi()==null?"0":kur.getAsuransi().toString();
	//						String diskon = kur.getResiJNE()==null?kur.getDiskonRapid().toString():kur.getDiskonJNE()==null?"0":kur.getDiskonJNE().toString();
							String diskon = "";
							if(kur.getResiJNE()==null){
								diskon = kur.getDiskonRapid()==null?"0":kur.getDiskonRapid().toString();
							}else{
								diskon = kur.getDiskonJNE()==null?"0":kur.getDiskonJNE().toString();
							}
							String total = kur.getTotalBiaya()==null?"0": ""+kur.getTotalBiaya().intValue();
							String penerimaRaw = penerima.length()>12?penerima.substring(0,12):penerima;
							String penerimaFix = "";
							String[] nama = penerimaRaw.split(" ");
							for(Integer i=0; i<nama.length;i++){
								if(!nama[i].trim().equals("")){
									penerimaFix += nama[i].substring(0, 1).toUpperCase() + nama[i].substring(1, nama[i].length());
									penerimaFix += " ";
								}
							}
							String noTelpPenerima = "";
							
							if(!telp.equalsIgnoreCase("-")){
								if(!telp.equals("")){
									if(telp.length()>2){
										noTelpPenerima = telp.substring(0, telp.length()-3) + "XXX";
									}
								}
							}
							
							String isi = 
									"<tr height='16px' style='font-size: 9px;font-family: Arial, Helvetica, sans-serif;'>" +
											"<td>" + intInd + "</td> " +
											"<td>" + awb + "</td> " +
											"<td>" + tujuan + "</td> " +
											"<td>" + penerimaFix + "</td> " +
											"<td>" + noTelpPenerima + "</td> " +
											"<td align='center'>" + JNE + "</td> " +
											"<td align='center'>" + berat + "</td> " +
											"<td align='right'>" + formatRupiah.formatIndonesiaTanpaKoma(harga) + "</td> " +
											"<td align='right'>" + formatRupiah.formatIndonesiaTanpaKoma(asuransi) + "</td> " +
											"<td align='center'>" + diskon + "%"+"</td> " +
											"<td align='right'>" + formatRupiah.formatIndonesiaTanpaKoma(total) + "</td> " +
	
									"</tr>";
							content+=isi;
						}
					}
				}else if(line.trim().equals("[!TRJR]")){
					content+="\r\n";
					content+=
						 "<table width='25%' style='font-size: 10px;font-family: Arial, Helvetica, sans-serif;'>>"
							+ "<tr>"
								+ "<td>Jumlah Paket</td>"
								+ "<td style='font-weight: bold;'>: "+intInd+"</td>"										
							+ "</tr>"
							+ "<tr>"
								+ "<td>Total Biaya</td>"
								+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(intBiaya)+"</td>"										
							+ "</tr>"
							+ "<tr>"
								+ "<td>Total Asuransi</td>"
								+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(intAsuransi)+"</td>"										
							+ "</tr>"
							+ "<tr>"
								+ "<td>Total Diskon</td>"
								+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(intDiskon)+"</td>"										
							+ "</tr>"
							+ "<tr>"
								+ "<td>Total Tagihan</td>"
								+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(intTotalTagihan)+"</td>"										
							+ "</tr>"
						+ "</table>";
				}else if(line.trim().equals("[!pengirim]")){
					content+="\r\n";
					content+="Pengirim : " + plg.getNama();
				}else if(line.trim().equals("[!kodepickup]")){
					content+="\r\n";
					content+="Kode Pickup : " + kodePickup;
				}else if(line.trim().equals("[!periode]")){
					content+="\r\n";
					content+="Periode : " + strStart + " - " + strEnd;
				}else{
					content+="\r\n";
					content+=line;
				}
			}

//			System.out.println("--> HTML : " + content);
			System.out.println("--> content : " + content);
		    OutputStream file = new FileOutputStream(new File(path));
		    Document document = new Document();
		    
		    PdfWriter writer = PdfWriter.getInstance(document, file);
		    
		    document.open();
		    
		    InputStream is = new ByteArrayInputStream(content.getBytes());
		    XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
		    
		    document.close();
		    file.close();

		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createPiutang(List<PenandaLunasVO2> dataPDF, TrPelanggan pel, String path) {
		String content = 
				"<table width='100%' border='0' cellspacing='0' cellpadding='0'> " +
				"  <tr> " +
				"    <td width='55%'><img src='C:/DLL/rapid.png' width='191' height='58'></img></td> " +
				"    <td width='45%'> " +
				"	<span style='font-family: Arial, Helvetica, sans-serif; " +
				"	font-size: 12px;font-family: Arial, Helvetica, sans-serif;'> " +
				"	<br></br> " +
				"Pelanggan : "+pel.getNamaAkun()+
				"	<br></br> " +
				"Tanggal : "+DateUtil.getStdDateDisplay(new Date())+ 				
				"	</span></td> " +
				"  </tr> " +
				"  <tr> " +
				"    <td>&nbsp;</td> " +
				"    <td><span class='style2'>PIUTANG</span></td> " +
				"  </tr> " +
				"  <tr> " +
				"    <td>&nbsp;</td> " +
				"    <td>&nbsp;</td> " +
				"  </tr> " +
				"</table> " +
				"<br></br> " +
				"<table width='100%' border='0' cellspacing='0' cellpadding='0'> " +
				"    <tr height='22px' style='font-size: 11px;font-family: Arial, Helvetica, sans-serif; font-weight: bold;background-color: #CCCCCC;'> " +
				"		<td width='20px' align='center'>No</td> " +
				"        <td width='200px' align='center' style='background-color: #CCCCCC;'>Tanggal</td> " +
				"        <td width='100px' align='center' style='background-color: #CCCCCC;'>No. Pickup</td> " +
				"        <td width='300px' align='center' style='background-color: #CCCCCC;'>Nama Pelanggan</td> " +
				"        <td width='200px' align='center' style='background-color: #CCCCCC;'>Total Piutang</td> " +				
				"    </tr> ";
				Integer num = 0;
				Integer total = 0;
				for (PenandaLunasVO2 vo : dataPDF) {
					num++;
					total+=vo.getTotalPiutang().get();
					content+=
					"<tr height='20px' style='font-size: 11px;font-family: Arial, Helvetica, sans-serif;'>"
						+ "<td>"+num+"</td> "
						+ "<td align='center'>"+vo.getTanggal().get()+"</td> "
						+ "<td align='center'>"+vo.getNoPickUp().get()+"</td> "
						+ "<td align='center'>"+vo.getNmPelanggan().get()+"</td> "
						+ "<td align='right'>"+util.formatRupiah.formatIndonesia(vo.getTotalPiutang().get())+"</td> "						
					+ "</tr>";
				}
				content+=
						"<tr height='22px' style='font-size: 14px;font-family: Arial, Helvetica, sans-serif;'>"
							+ "<td>&nbsp;</td> "
							+ "<td align='center'>&nbsp;</td> "
							+ "<td align='center'>&nbsp;</td> "
							+ "<td>Total </td> "
							+ "<td align='right'>"+util.formatRupiah.formatIndonesia(total)+"</td> "						
						+ "</tr>"+
				"</table> " +	
				"<br></br> " +
				"<br></br> " +
				"<table> " +
				"<tr style='font-size: 12px;font-family: Arial, Helvetica, sans-serif;'> " +
				"<td>" +
				"	Terlampir rangkuman pengiriman tagihan transaksi." +
				"	<br></br>	 " +
				"	Mohon untuk dapat melakukan pembayaran ke salah satu rekening berikut :" +
				"	<br></br>	 " +
				"	<li style='font-size: 11px;font-family: Arial, Helvetica, sans-serif; font-weight: bold;'> " +
				"		Bank BCA No. Rek.4823150515 An PT. Rapid Express Indonesia " +
				"	</li> " +
				"	<li style='font-size: 11px;font-family: Arial, Helvetica, sans-serif; font-weight: bold;'> " +
				"		Bank Mandiri No. Rek. 121-00150515-88 An PT.Rapid Express Indonesia " +
				"	</li> " +
				"	<br></br> " +
				"  </td> " +
				    
				"  </tr> " +
				"</table> ";
		try {
			OutputStream file = new FileOutputStream(new File(path));
		    Document document = new Document();
		    
		    PdfWriter writer = PdfWriter.getInstance(document, file);
		    
		    document.open();
		    
		    InputStream is = new ByteArrayInputStream(content.getBytes());
		    
				XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
			
		    
		    document.close();
		    file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createInvoice(
			Integer deposit, 
			List<EntryDataShowVO> dataPDF, 
			TrPelanggan pel, 
			String path) {
		String content = 
				"<table width='100%' border='0' cellspacing='0' cellpadding='0'> " +
				"  <tr> " +
				"    <td width='55%'><img src='C:/DLL/rapid.png' width='191' height='58'></img></td> " +
				"    <td width='45%'> " +
				"	<span style='font-family: Arial, Helvetica, sans-serif; " +
				"	font-size: 12px;font-family: Arial, Helvetica, sans-serif;'> " +
				"	<br></br> " +
				"Pelanggan : "+pel.getNamaAkun()+
				"	<br></br> " +
				"Tanggal : "+DateUtil.getStdDateDisplay(new Date())+ 				
				"	</span></td> " +
				"  </tr> " +
				"  <tr> " +
				"    <td>&nbsp;</td> " +
				"    <td><span class='style2'>LUNAS</span></td> " +
				"  </tr> " +
				"  <tr> " +
				"    <td>&nbsp;</td> " +
				"    <td>&nbsp;</td> " +
				"  </tr> " +
				"</table> " +
				"<br></br> " +
				"<table width='100%' border='0' cellspacing='0' cellpadding='0'> " +
				"    <tr height='22px' style='font-size: 11px;font-family: Arial, Helvetica, sans-serif; font-weight: bold;background-color: #CCCCCC;'> " +
				"		<td width='20px' align='center'>No</td> " +
				"        <td width='200px' align='center' style='background-color: #CCCCCC;'>Tanggal</td> " +
				"        <td width='100px' align='center' style='background-color: #CCCCCC;'>No. Pickup</td> " +
				"        <td width='300px' align='center' style='background-color: #CCCCCC;'>Nama Pelanggan</td> " +
				"        <td width='200px' align='center' style='background-color: #CCCCCC;'>Total Piutang</td> " +				
				"    </tr> ";
				Integer num = 0;
				Integer total = 0;
				for (EntryDataShowVO vo : dataPDF) {
					num++;
					total+=Integer.parseInt(vo.gettBiaya().toString());
					content+=
					"<tr height='20px' style='font-size: 11px;font-family: Arial, Helvetica, sans-serif;'>"
						+ "<td>"+num+"</td> "
						+ "<td align='center'>"+DateUtil.getStdDateDisplay(vo.getTglCreated())+"</td> "
						+ "<td align='center'>"+vo.getKdPickup()+"</td> "
						+ "<td align='center'>"+vo.getKdPelanggan()+"</td> "
						+ "<td align='right'>"+util.formatRupiah.formatIndonesia(Integer.parseInt(vo.gettBiaya().toString()))+"</td> "						
					+ "</tr>";
				}
				content+=
						"<tr height='22px' style='font-size: 14px;font-family: Arial, Helvetica, sans-serif;'>"
							+ "<td>&nbsp;</td> "
							+ "<td align='center'>&nbsp;</td> "
							+ "<td align='center'>&nbsp;</td> "
							+ "<td>Total :</td> "
							+ "<td align='right'>"+util.formatRupiah.formatIndonesia(total)+"</td> "						
						+ "</tr>"
						+ "<tr height='22px' style='font-size: 14px;font-family: Arial, Helvetica, sans-serif;'>"
						+ "<td>&nbsp;</td> "
						+ "<td align='center'>&nbsp;</td> "
						+ "<td align='center'>&nbsp;</td> "
						+ "<td>Sisa Deposito :</td> "
						+ "<td align='right'>"+util.formatRupiah.formatIndonesia(deposit)+"</td> "						
					+ "</tr>"+
				"</table> " +	
				"<br></br> " +
				"<br></br> " +
				"<table> " +
				"<tr style='font-size: 12px;font-family: Arial, Helvetica, sans-serif;'> " +
				"<td>" +
				"	Terima kasih atas kepercayaannya menggunakan jasa pengiriman Rapid Express" +
				"	<br></br>	 " +
				"	Terlampir nota invoice untuk pelunasaan sesuai dengan tanggal yang tertera di atas " +
				"	<br></br>	 " +
				"	dan anda memiiki sisa deposit sebesar Per Periode Rp."+util.formatRupiah.formatIndonesia(deposit)+
				"	<br></br>	 " +
				"	yang dapat digunakan untuk pelunasaan ditagihan anda berikutnya." +				
				"</td> " +
				    
				"  </tr> " +
				"</table> ";
		try {
			OutputStream file = new FileOutputStream(new File(path));
		    Document document = new Document();
		    
		    PdfWriter writer = PdfWriter.getInstance(document, file);
		    
		    document.open();
		    
		    InputStream is = new ByteArrayInputStream(content.getBytes());
		    
				XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
			
		    
		    document.close();
		    file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
