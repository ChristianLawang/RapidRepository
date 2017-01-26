package util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.poi.util.SystemOutLogger;
import org.codehaus.groovy.transform.ToStringASTTransformation;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import VO.ReportVO;
import VO.TagihanVO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

public class PDFUtil2 {

	public static void createPDFLaporanPerpelanggan(
			ObservableList<ReportVO> masterData,
			ObservableList<ReportVO> masterData2, 
			Boolean chkAllPelanggan,
			String namaPelanggan,
			Date awlDate,
			Date endDate) {
		String contentPDF = "";
		String header, kolomTabel, isi;
		Integer totAWB = 0;
		Integer totBerat = 0;
		Double totBeratAsli = 0.0;
		Integer totBiaya = 0;
		Integer totHargaSetelahDiskon = 0;
		Integer totDiskon = 0;
		
		Integer totHarga = 0;
		Integer totAsuransi = 0;
		
		if(chkAllPelanggan){
			header = 
					"<table width='100%' border='0' cellspacing='0' cellpadding='0'> " +
					"  <tr> " +
					"    <td width='55%'><img src='C:/DLL/rapid.png' width='191' height='58'></img></td> " +
					"    <td width='45%'> " +
					"	<span style='font-family: Arial, Helvetica, sans-serif; " +
					"	font-size: 10px;font-family: Arial, Helvetica, sans-serif; " +
					"	font-size: 10px;'>Branch Head Office : Jalan Tanah Abang V, No F3, Jakarta Pusat " +
					"	<br></br> " +
				    	DateUtil.getStdDateDisplay(awlDate) + " - " + DateUtil.getStdDateDisplay(endDate) +
					"	</span></td> " +
					"  </tr> " +
					"  <tr> " +
					"    <td>&nbsp;</td> " +
					"    <td><span class='style2'>Laporan Per Pelanggan</span></td> " +
					"  </tr> " +
					"  <tr> " +
					"    <td>&nbsp;</td> " +
					"    <td>&nbsp;</td> " +
					"  </tr> " +
					"</table>";
			kolomTabel = 
					"<table width='100%' border='0' cellspacing='0' cellpadding='0'> " +
					"    <tr height='22px' style='font-size: 10px;font-family: Arial, Helvetica, sans-serif; font-weight: bold;background-color: #CCCCCC;'> " +
					"		<td width='20px' align='center'>No</td> " +
					"        <td width='180px' align='center' style='background-color: #CCCCCC;'>Nama Sales</td> " +
					"        <td width='100px' align='center' style='background-color: #CCCCCC;'>Pengirim</td> " +
					"        <td width='40px' align='center' style='background-color: #CCCCCC;'>AWB</td> " +
					"        <td width='40px' align='center' style='background-color: #CCCCCC;'>Berat</td> " +
					"        <td width='60px' align='center' style='background-color: #CCCCCC;'>Berat Asli</td> " +
					"        <td width='100px' align='center' style='background-color: #CCCCCC;'>Total Biaya</td> " +
					"        <td width='100px' align='center' style='background-color: #CCCCCC;'>Harga Setelah Diskon</td> " +
					"        <td width='100px' align='center' style='background-color: #CCCCCC;'>Total Diskon</td> " +
					"    </tr> " +
					"      [!TRTR] " +
					"</table> ";
			isi = "";
			Integer no = 0;
			for (ReportVO vo : masterData2) {
				totAWB += vo.getJumlahBarang();
				totBerat += vo.getSumBerat();
				totBeratAsli += Double.parseDouble(vo.getSumBeratAsli());
				totBiaya += Integer.parseInt(vo.getBiaya().replace(".", ""));
				totHargaSetelahDiskon += Integer.parseInt(vo.getHargaSetelahDiskon().replace(".", ""));
				totDiskon += Integer.parseInt(vo.getTotalDiskonDiskon().replace(".", ""));
				
				no++;
				isi += 
						"<tr height='16px' style='font-size: 9px;font-family: Arial, Helvetica, sans-serif;'>" +
								"<td>" + no + "</td> " +
								"<td>" + vo.getNmSales().replaceAll("[-+.^:,&]","") + "</td> " +
								"<td>" + vo.getPengirim().replaceAll("[-+.^:,&]","") + "</td> " +
								"<td align='center'>" + vo.getJumlahBarang() + "</td> " +
								"<td align='center'>" + vo.getSumBerat() + "</td> " +
								"<td align='center'>" + vo.getSumBeratAsli() + "</td> " +
								"<td align='right'>" + vo.getBiaya() + "</td> " +
								"<td align='right'>" + vo.getHargaSetelahDiskon() + "</td> " +
								"<td align='right'>" + vo.getTotalDiskonDiskon() + "</td> " +
						"</tr>";
			} 
			contentPDF = header + kolomTabel.replace("[!TRTR]", isi);
			
			contentPDF += "	<br></br> " +			 "<table width='50%' style='font-size: 10px;font-family: Arial, Helvetica, sans-serif;'>"
					+ "<tr>"
						+ "<td>Jumlah AWB</td>"
						+ "<td style='font-weight: bold;'>: "+totAWB+"</td>"										
					+ "</tr>"
					+ "<tr>"
						+ "<td>Total Berat</td>"
						+ "<td style='font-weight: bold;'>: "+totBerat+"</td>"										
					+ "</tr>"
					+ "<tr>"
						+ "<td>Total Berat Asli</td>"
						+ "<td style='font-weight: bold;'>: "+round(totBeratAsli,2)+"</td>"										
					+ "</tr>"
					+ "<tr>"
						+ "<td>Total Biaya</td>"
						+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(totBiaya)+"</td>"										
					+ "</tr>"
					+ "<tr>"
						+ "<td>Total Harga Setelah Diskon</td>"
						+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(totHargaSetelahDiskon)+"</td>"										
					+ "</tr>"
					+ "<tr>"
						+ "<td>Total Diskon</td>"
						+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(totDiskon)+"</td>"										
					+ "</tr>"
				+ "</table>";
			
			try{
				System.out.println("--> content : " + contentPDF);
			    OutputStream file = new FileOutputStream(new File("C:/DLL/PDF/Laporan Perpelanggan.PDF"));
			    Document document = new Document();
			    
			    PdfWriter writer = PdfWriter.getInstance(document, file);
			    
			    document.open();
			    
			    InputStream is = new ByteArrayInputStream(contentPDF.getBytes());
			    XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
			    
			    document.close();
			    file.close();
			    
			    MessageBox.alert("PDF Sukses terbentuk di C:/DLL/PDF/Laporan Perpelanggan.PDF");

			} catch (IOException | DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			
			header = 
					"<table width='100%' border='0' cellspacing='0' cellpadding='0'> " +
					"  <tr> " +
					"    <td width='55%'><img src='C:/DLL/rapid.png' width='191' height='58'></img></td> " +
					"    <td width='45%'> " +
					"	<span style='font-family: Arial, Helvetica, sans-serif; " +
					"	font-size: 10px;font-family: Arial, Helvetica, sans-serif; " +
					"	font-size: 10px;'>Branch Head Office : Jalan Tanah Abang V, No F3, Jakarta Pusat " +
					"	<br></br> " +
				    	DateUtil.getStdDateDisplay(awlDate) + " - " + DateUtil.getStdDateDisplay(endDate) +
					"	</span></td> " +
					"  </tr> " +
					"  <tr> " +
					"    <td>&nbsp;</td> " +
					"    <td><span class='style2'>Laporan Pelanggan "+ namaPelanggan +"</span></td> " +
					"  </tr> " +
					"  <tr> " +
					"    <td>&nbsp;</td> " +
					"    <td>&nbsp;</td> " +
					"  </tr> " +
					"</table>";
			kolomTabel = 
					"<table width='100%' border='0' cellspacing='0' cellpadding='0'> " +
					"    <tr height='22px' style='font-size: 10px;font-family: Arial, Helvetica, sans-serif; font-weight: bold;background-color: #CCCCCC;'> " +
					"		<td width='20px' align='center'>No</td> " +
					"        <td width='120px' align='center' style='background-color: #CCCCCC;'>No Resi</td> " +
					"        <td width='100px' align='center' style='background-color: #CCCCCC;'>Tujuan</td> " +
					"        <td width='80px' align='center' style='background-color: #CCCCCC;'>Penerima</td> " +
					"        <td width='100px' align='center' style='background-color: #CCCCCC;'>HP Penerima</td> " +
					"        <td width='120px' align='center' style='background-color: #CCCCCC;'>Resi JNE</td> " +
					"        <td width='40px' align='center' style='background-color: #CCCCCC;'>Berat</td> " +
					"        <td width='50px' align='center' style='background-color: #CCCCCC;'>Biaya</td> " +
					"        <td width='50px' align='center' style='background-color: #CCCCCC;'>Asuransi</td> " +
					"        <td width='50px' align='center' style='background-color: #CCCCCC;'>Diskon</td> " +
					"        <td width='50px' align='center' style='background-color: #CCCCCC;'>Total</td> " +
					"    </tr> " +
					"      [!TRTR] " +
					"</table> ";
			isi = "";
			Integer no = 0;
			for (ReportVO vo : masterData) {
				
				totDiskon += Integer.parseInt(vo.getBiaya().replace(".", ""));
				totHarga += Integer.parseInt(vo.getHarga().replace(".", ""));
				totAsuransi = vo.getAsuransi();
				
				no++;
				isi += 
						"<tr height='16px' style='font-size: 9px;font-family: Arial, Helvetica, sans-serif;'>" +
								"<td>" + no + "</td> " +
								"<td>" + vo.getAwbData() + "</td> " +
								"<td align='center'>" + vo.getTujuan() + "</td> " +
								"<td align='center'>" + vo.getPenerima() + "</td> " +
								"<td align='center'>" + vo.getTelp() + "</td> " +
								"<td align='right'>" + vo.getResiJNE() + "</td> " +
								"<td align='right'>" + vo.getBfinal() + "</td> " +
								"<td align='right'>" + vo.getHarga() + "</td> " +
								"<td align='right'>" + vo.getAsuransi() + "</td> " +
								"<td align='right'>" + vo.getDiskon() + "</td> " +
								"<td align='right'>" + vo.getBiaya() + "</td> " +
						"</tr>";
			} 
			contentPDF = header + kolomTabel.replace("[!TRTR]", isi);
			
			contentPDF +="	<br></br> " +			 "<table width='50%' style='font-size: 10px;font-family: Arial, Helvetica, sans-serif;'>"
					+ "<tr>"
						+ "<td>Biaya</td>"
						+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(totHarga)+"</td>"										
					+ "</tr>"
					+ "<tr>"
						+ "<td>Asuransi</td>"
						+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(totAsuransi)+"</td>"										
					+ "</tr>"
					+ "<tr>"
						+ "<td>Total</td>"
						+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(totDiskon)+"</td>"										
					+ "</tr>"					
				+ "</table>";
			
			try{
				System.out.println("--> content : " + contentPDF);
			    OutputStream file = new FileOutputStream(new File("C:/DLL/PDF/Laporan Perpelanggan ("+namaPelanggan+").PDF"));
			    Document document = new Document();
			    
			    PdfWriter writer = PdfWriter.getInstance(document, file);
			    
			    document.open();
			    
			    InputStream is = new ByteArrayInputStream(contentPDF.getBytes());
			    XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
			    
			    document.close();
			    file.close();
			    
			    MessageBox.alert("PDF Sukses terbentuk di " + "C:/DLL/PDF/Laporan Perpelanggan ("+namaPelanggan+").PDF");

			} catch (IOException | DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}


		
		
	}
	
	
	// untuk pembulatan double
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}


	public static void createPDFLaporanPerperwakilan(
			ObservableList<ReportVO> masterData, 			
			Date awlDate,
			Date endDate){
		String contentPDF = "";
		String header, kolomTabel, isi;
		
		Integer totalBiaya = 0;
		Integer jumlahBarang = 0;
		Integer jumlahBerat = 0;
		Integer totBiaya = 0;
		
		header = 
				"<table width='100%' border='0' cellspacing='0' cellpadding='0'> " +
				"  <tr> " +
				"    <td width='55%'><img src='C:/DLL/rapid.png' width='191' height='58'></img></td> " +
				"    <td width='45%'> " +
				"	<span style='font-family: Arial, Helvetica, sans-serif; " +
				"	font-size: 10px;font-family: Arial, Helvetica, sans-serif; " +
				"	font-size: 10px;'>Branch Head Office : Jalan Tanah Abang V, No F3, Jakarta Pusat " +
				"	<br></br> " +
			    	DateUtil.getStdDateDisplay(awlDate) + " - " + DateUtil.getStdDateDisplay(endDate) +
				"	</span></td> " +
				"  </tr> " +
				"  <tr> " +
				"    <td>&nbsp;</td> " +
				"    <td><span class='style2'>Laporan PerPerwakilan</span></td> " +
				"  </tr> " +
				"  <tr> " +
				"    <td>&nbsp;</td> " +
				"    <td>&nbsp;</td> " +
				"  </tr> " +
				"</table>";
		kolomTabel = 
				"<table width='100%' border='0' cellspacing='0' cellpadding='0'> " +
				"    <tr height='22px' style='font-size: 10px;font-family: Arial, Helvetica, sans-serif; font-weight: bold;background-color: #CCCCCC;'> " +
				"		<td width='20px' align='center'>No</td> " +
				"        <td width='120px' align='center' style='background-color: #CCCCCC;'>Tujuan</td> " +
				"        <td width='100px' align='center' style='background-color: #CCCCCC;'>ID Kardus Terakhir</td> " +
				"        <td width='80px' align='center' style='background-color: #CCCCCC;'>Jumlah Barang</td> " +
				"        <td width='100px' align='center' style='background-color: #CCCCCC;'>Berat</td> " +
				"        <td width='120px' align='center' style='background-color: #CCCCCC;'>Total Biaya</td> " +
				"    </tr> " +
				"      [!TRTR] " +
				"</table> ";
		isi = "";
		Integer no = 0;
		for (ReportVO vo : masterData) {
			
			totalBiaya  += Integer.parseInt(vo.getBiaya().replace(".", ""));
			jumlahBarang += vo.getJumlahBarang();
			jumlahBerat += vo.getSumBerat();			
			
			no++;
			isi += 
					"<tr height='16px' style='font-size: 9px;font-family: Arial, Helvetica, sans-serif;'>" +
							"<td>" + no + "</td> " +
							"<td>" + vo.getKdPerwakilan() + "</td> " +
							"<td align='right'>" + vo.getResiKardusTerakhir() + "</td> " +			
							"<td align='center'>" + vo.getJumlahBarang() + "</td> " +
							"<td align='center'>" + vo.getSumBerat() + "</td> " +
							"<td align='center'>" + vo.getBiaya() + "</td> " +											
					"</tr>";
		} 
		contentPDF = header + kolomTabel.replace("[!TRTR]", isi);
		
		contentPDF +="	<br></br> " +			 "<table width='50%' style='font-size: 10px;font-family: Arial, Helvetica, sans-serif;'>"
				+ "<tr>"
					+ "<td>Jumlah Record</td>"
					+ "<td style='font-weight: bold;'>: "+masterData.size()+"</td>"										
				+ "</tr>"
				+ "<tr>"
					+ "<td>Jumlah Barang</td>"
					+ "<td style='font-weight: bold;'>: "+jumlahBarang+"</td>"										
				+ "</tr>"
				+ "<tr>"
					+ "<td>Jumlah Berat</td>"
					+ "<td style='font-weight: bold;'>: "+jumlahBerat+"</td>"										
				+ "</tr>"				
				+ "<tr>"
					+ "<td>Total Biaya</td>"
					+ "<td style='font-weight: bold;'>: "+formatRupiah.formatIndonesia(totalBiaya)+"</td>"										
				+ "</tr>"				
			+ "</table>";
		
		try{
			System.out.println("--> content : " + contentPDF);
			OutputStream file = new FileOutputStream(new File("C:/DLL/PDF/Laporan Perperwakilan.PDF"));
		    
		    Document document = new Document();
		    
		    
			PdfWriter writer = PdfWriter.getInstance(document, file);
		    
		    document.open();
		    
		    InputStream is = new ByteArrayInputStream(contentPDF.getBytes());
		    XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
		    
		    document.close();
		    file.close();
		    MessageBox.alert("PDF Sukses terbentuk di " + "C:/DLL/PDF/Laporan Perperwakilan.PDF");

		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
