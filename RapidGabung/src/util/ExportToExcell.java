package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import VO.EntryDataShowVO;
import VO.LaporanPenerimaVO;
import VO.LaporanPerKecamatanVO;
import VO.ReportVO;
import controller.LapPerKecamatanController.LapPerKecamatanTV;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


public class ExportToExcell {
	public static void exportToExcell3(List<EntryDataShowVO> en, String strPerwakilan, String title, String date ) {
		try {
			FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORTEXCEL/" + title + "-" + strPerwakilan + "-" + date + ".xls");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet("MANIFEST CABANG");
			HSSFRow row1 = null;
			
			int no=0;
			row1 = worksheet.createRow((short) no++);
			
			HSSFCell cellNo = row1.createCell((short) 0);
			cellNo.setCellValue("NO");
			
			HSSFCell cellNoResi = row1.createCell((short) 1);
			cellNoResi.setCellValue("NO RESI");

			HSSFCell cellTanggalMasuk= row1.createCell((short) 2);
			cellTanggalMasuk.setCellValue("TANGGAL MASUK PAKET");
			
			HSSFCell cellNamaPaket= row1.createCell((short) 3);
			cellNamaPaket.setCellValue("NAMA PAKET");
			
			HSSFCell cellNoTelepon= row1.createCell((short) 4);
			cellNoTelepon.setCellValue("NO TELEPON");
			
			HSSFCell cellIdKurir= row1.createCell((short) 5);
			cellIdKurir.setCellValue("ID KURIR");
			
			HSSFCell cellKurir= row1.createCell((short) 6);
			cellKurir.setCellValue("NAMA KURIR");
			
			HSSFCell cellTglKirim= row1.createCell((short) 7);
			cellTglKirim.setCellValue("TANGGAL KIRIM");
			
			HSSFCell cellNmPenerima= row1.createCell((short) 8);
			cellNmPenerima.setCellValue("NAMA PENERIMA");
			int noCol=1;
			
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
			cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
			
			for (EntryDataShowVO data : en) {
				
				row1 = worksheet.createRow((short) no++);
				HSSFCell cellA0 = row1.createCell((short) 0);
				cellA0.setCellValue(noCol++);
				
				HSSFCell cellA1 = row1.createCell((short) 1);
				cellA1.setCellValue(data.getAwbData());
				
				HSSFCell cellB1 = row1.createCell((short) 2);
				cellB1.setCellValue(data.getCreated());
				
				HSSFCell cellC1 = row1.createCell((short) 3);
				cellC1.setCellValue(data.getPenerima());
				
				HSSFCell cellD1 = row1.createCell((short) 4);
				cellD1.setCellValue(data.getNoTlpn());
				
				cellB1.setCellStyle(cellStyle);
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		} catch (IOException x) {
			x.printStackTrace();
		}
	}
	
	
	public static void exportToExcellReportTagihan(List<EntryDataShowVO> en, String title, String date ) {
		try {
			FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + date  + " " + title + ".xlsx");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet(title);
			HSSFRow row1 = null;
			
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
			cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
			
			int no=0;
			row1 = worksheet.createRow((short) no++);
			
			HSSFCell cellNo = row1.createCell((short) 0);
			cellNo.setCellValue("NO");
			
			HSSFCell cellAsalPaket = row1.createCell((short) 1);
			cellAsalPaket.setCellValue("ASAL PAKET");

			HSSFCell cellPengirim= row1.createCell((short) 2);
			cellPengirim.setCellValue("PENGIRIM");
			
			HSSFCell cellKdPickup= row1.createCell((short) 3);
			cellKdPickup.setCellValue("KD PICKUP");
			
			HSSFCell cellAwb= row1.createCell((short) 4);
			cellAwb.setCellValue("AWB");
			
			HSSFCell cellCreated= row1.createCell((short) 5);
			cellCreated.setCellValue("CRETAED");
			
			HSSFCell cellTujuan= row1.createCell((short) 6);
			cellTujuan.setCellValue("TUJUAN");
			
			HSSFCell cellPenerima= row1.createCell((short) 7);
			cellPenerima.setCellValue("PENERIMA");
			
			HSSFCell cellNoTlp= row1.createCell((short) 8);
			cellNoTlp.setCellValue("NO TELEPON");
			
			HSSFCell cellResiJne= row1.createCell((short) 9);
			cellResiJne.setCellValue("RESI JNE");
			
			HSSFCell cellBerat= row1.createCell((short) 10);
			cellBerat.setCellValue("BERAT");
			
			HSSFCell cellBeratPembulatan= row1.createCell((short) 11);
			cellBeratPembulatan.setCellValue("BERAT PEMBULATAN");
			
			HSSFCell cellHarga= row1.createCell((short) 12);
			cellHarga.setCellValue("HARGA");
			
			HSSFCell cellAsuransi= row1.createCell((short) 13);
			cellAsuransi.setCellValue("ASURANSI");
			
			HSSFCell cellTotalDiskon= row1.createCell((short) 14);
			cellTotalDiskon.setCellValue("TOTAL DISKON");
			
			HSSFCell cellDiskonPel= row1.createCell((short) 15);
			cellDiskonPel.setCellValue("DISKON PEL");
			
			HSSFCell cellBSebelumDiskon= row1.createCell((short) 16);
			cellBSebelumDiskon.setCellValue("BIAYA SBLM DISKON");
			
			int noCol=1;
			for (EntryDataShowVO data : en) {
				String noTeleponFinal = null;
				if (data.getNoTlpn() == null || data.getNoTlpn().equals("")) {
					noTeleponFinal = "-";
				} else {
					noTeleponFinal = data.getNoTlpn().substring(1);
					noTeleponFinal = "62" + noTeleponFinal;
				}
				row1 = worksheet.createRow((short) no++);
				HSSFCell cellA0 = row1.createCell((short) 0);
				cellA0.setCellValue(noCol++);
				
				HSSFCell cellA1 = row1.createCell((short) 1);
				cellA1.setCellValue(data.getAsalPaket());
				
				HSSFCell cellB1 = row1.createCell((short) 2);
				cellB1.setCellValue(data.getPengirim());
				
				HSSFCell cellC1 = row1.createCell((short) 3);
				cellC1.setCellValue(data.getKdPickup());
				
				HSSFCell cellD1 = row1.createCell((short) 4);
				cellD1.setCellValue(data.getAwbData());
				
				HSSFCell cellE1 = row1.createCell((short) 5);
				cellE1.setCellValue(data.getCreated());
				
				HSSFCell cellF1 = row1.createCell((short) 6);
				cellF1.setCellValue(data.getTujuan());
				
				HSSFCell cellG1 = row1.createCell((short) 7);
				cellG1.setCellValue(data.getPenerima());
				
				HSSFCell cellH1 = row1.createCell((short) 8);
				cellH1.setCellValue(noTeleponFinal);
				
				HSSFCell cellI1 = row1.createCell((short) 9);
				cellI1.setCellValue(data.getResiJne());
				
				HSSFCell cellJ1 = row1.createCell((short) 10);
				cellJ1.setCellValue(data.getbFinal());
				
				HSSFCell cellK1 = row1.createCell((short) 11);
				cellK1.setCellValue(data.getBpFinal());
				
				HSSFCell cellL1 = row1.createCell((short) 12);
				cellL1.setCellValue(data.getHarga());
				
				HSSFCell cellM1 = row1.createCell((short) 13);
				cellM1.setCellValue(data.getAsuransi());
				
				HSSFCell cellN1 = row1.createCell((short) 14);
				cellN1.setCellValue(data.getDiskon());
				
				HSSFCell cellO1 = row1.createCell((short) 15);
				cellO1.setCellValue(data.getDiskonPel().intValue());
				
				HSSFCell cellP1 = row1.createCell((short) 16);
				cellP1.setCellValue(data.getBiayaSblmDiskon());
				
				cellE1.setCellStyle(cellStyle);
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		} catch (IOException x) {
			x.printStackTrace();
		}
	}
	
	public static void exportToExcellReportResi(List<EntryDataShowVO> en, String title, String date ) {
		try {
			FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + date  + " " + title + ".xlsx");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet(title);
			HSSFRow row1 = null;
			
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
			cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
			
			int no=0;
			row1 = worksheet.createRow((short) no++);
			
			HSSFCell cellNo = row1.createCell((short) 0);
			cellNo.setCellValue("NO");
			
			HSSFCell cellAsalPaket = row1.createCell((short) 1);
			cellAsalPaket.setCellValue("NO RESI");

			HSSFCell cellPengirim= row1.createCell((short) 2);
			cellPengirim.setCellValue("NO TELEPON");
			
			HSSFCell cellKdPickup= row1.createCell((short) 3);
			cellKdPickup.setCellValue("PENERIMA");
			
			HSSFCell cellAwb= row1.createCell((short) 4);
			cellAwb.setCellValue("RESSELER");
			
			HSSFCell cellCreated= row1.createCell((short) 5);
			cellCreated.setCellValue("RESI JNE");
			
			HSSFCell cellTujuan= row1.createCell((short) 6);
			cellTujuan.setCellValue("CREATED");
			
			int noCol=1;
			for (EntryDataShowVO data : en) {
				String noTeleponFinal = null;
				if (data.getNoTlpn() == null || data.getNoTlpn().equals("")) {
					noTeleponFinal = "-";
				} else {
					noTeleponFinal = data.getNoTlpn().substring(1);
					noTeleponFinal = "62" + noTeleponFinal;
				}
				row1 = worksheet.createRow((short) no++);
				HSSFCell cellA0 = row1.createCell((short) 0);
				cellA0.setCellValue(noCol++);
				
				HSSFCell cellA1 = row1.createCell((short) 1);
				cellA1.setCellValue(data.getAwbData());
				
				HSSFCell cellB1 = row1.createCell((short) 2);
				cellB1.setCellValue(noTeleponFinal);
				
				HSSFCell cellC1 = row1.createCell((short) 3);
				cellC1.setCellValue(data.getPenerima());
				
				HSSFCell cellD1 = row1.createCell((short) 4);
				cellD1.setCellValue(data.getResseler());
				
				HSSFCell cellE1 = row1.createCell((short) 5);
				cellE1.setCellValue(data.getResiJne());
				
				HSSFCell cellF1 = row1.createCell((short) 6);
				cellF1.setCellValue(data.getCreated());
				
				cellF1.setCellStyle(cellStyle);
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		} catch (IOException x) {
			x.printStackTrace();
		}
	}
	public static void exportToExcellReportPenerima(ObservableList<LaporanPenerimaVO> masterData, String title, String date ) {
		try {
			FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + date  + " " + title + ".xls");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet(title);
			HSSFRow row1 = null;
			
			int no=0;
			row1 = worksheet.createRow((short) no++);
			
			HSSFCell cellNo = row1.createCell((short) 0);
			cellNo.setCellValue("NO");
			
			HSSFCell cellTgl = row1.createCell((short) 1);
			cellTgl.setCellValue("TANGGAL");

			HSSFCell cellAwb= row1.createCell((short) 2);
			cellAwb.setCellValue("AWB");
			
			HSSFCell cellPengirim= row1.createCell((short) 3);
			cellPengirim.setCellValue("PENGIRIM");
			
			HSSFCell cellPenerima= row1.createCell((short) 4);
			cellPenerima.setCellValue("PENERIMA");
			
			HSSFCell cellTelpPenerima= row1.createCell((short) 5);
			cellTelpPenerima.setCellValue("TELP PENERIMA");
			
			HSSFCell cellTujuan= row1.createCell((short) 6);
			cellTujuan.setCellValue("TUJUAN");
			
			HSSFCell cellZona= row1.createCell((short) 7);
			cellZona.setCellValue("ZONA");
			
			HSSFCell cellBerat= row1.createCell((short) 8);
			cellBerat.setCellValue("BERAT");
			
			HSSFCell cellETD= row1.createCell((short) 9);
			cellETD.setCellValue("ETD");
			
			HSSFCell cellKeterangan= row1.createCell((short) 10);
			cellKeterangan.setCellValue("KETERANGAN");
			
			HSSFCell cellTglTerima= row1.createCell((short) 11);
			cellTglTerima.setCellValue("TGL TERIMA");
			
			HSSFCell cellWaktuTerima= row1.createCell((short) 12);
			cellWaktuTerima.setCellValue("WAKTU TERIMA");
			
			HSSFCell cellStatus= row1.createCell((short) 13);
			cellStatus.setCellValue("STATUS");
			
			HSSFCell cellDiterimaOleh= row1.createCell((short) 14);
			cellDiterimaOleh.setCellValue("DITERIMA OLEH");
			
			
			
			int noCol=1;
			
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
			cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
			
			for (LaporanPenerimaVO data : masterData) {
			
				row1 = worksheet.createRow((short) no++);
				HSSFCell cellA0 = row1.createCell((short) 0);
				cellA0.setCellValue(noCol++);
				
				HSSFCell cellA1 = row1.createCell((short) 1);
				cellA1.setCellValue(data.getTanggal());
				
				HSSFCell cellB1 = row1.createCell((short) 2);
				cellB1.setCellValue(data.getAwb());
				
				HSSFCell cellC1 = row1.createCell((short) 3);
				cellC1.setCellValue(data.getPengirim());
				
				HSSFCell cellD1 = row1.createCell((short) 4);
				cellD1.setCellValue(data.getPenerima());
				
				HSSFCell cellE1 = row1.createCell((short) 5);
				cellE1.setCellValue(data.getTelpPenerima());
			
				HSSFCell cellF1 = row1.createCell((short) 6);
				cellF1.setCellValue(data.getTujuan());
				
				HSSFCell cellG1 = row1.createCell((short) 7);
				cellG1.setCellValue(data.getZona());
				
				HSSFCell cellH1 = row1.createCell((short) 8);
				cellH1.setCellValue(data.getBerat());
				
				HSSFCell cellI1 = row1.createCell((short) 9);
				cellI1.setCellValue(data.getEtd());
				
				HSSFCell cellJ1 = row1.createCell((short) 10);
				cellJ1.setCellValue(data.getKeterangan());
				
				HSSFCell cellK1 = row1.createCell((short) 11);
				cellK1.setCellValue(data.getTglPenerima());
				
				HSSFCell cellL1 = row1.createCell((short) 12);
				cellL1.setCellValue(data.getWaktuPenerima());
				
				HSSFCell cellM1 = row1.createCell((short) 13);
				cellM1.setCellValue(data.getStatus());
				
				HSSFCell cellN1 = row1.createCell((short) 14);
				cellN1.setCellValue(data.getPenerimaPaket());
				
//				cellF1.setCellStyle(cellStyle);
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		} catch (IOException x) {
			x.printStackTrace();
		}
	}


	public static void exportToExcellReportPerKecamatan(ObservableList<LapPerKecamatanTV> masterData, String title,
			String dateFile) {
			try {
			FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + dateFile  + " " + title + ".xls");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet worksheet = workbook.createSheet(title);
			XSSFRow row1 = null;
//			HSSFWorkbook workbook = new HSSFWorkbook();
//			HSSFSheet worksheet = workbook.createSheet(title);
//			HSSFRow row1 = null;
			
			int no=0;
			row1 = worksheet.createRow((short) no++);
			
			XSSFCell cellNo = row1.createCell((short) 0);
			cellNo.setCellValue("NO");
			
			XSSFCell cellAwb = row1.createCell((short) 1);
			cellAwb.setCellValue("AWB");
	
			XSSFCell cellTgl= row1.createCell((short) 2);
			cellTgl.setCellValue("TANGGAL");
			
//			HSSFCell cellPenerima= row1.createCell((short) 3);
//			cellPenerima.setCellValue("PENERIMA");
			
			XSSFCell cellTujuan= row1.createCell((short) 3);
			cellTujuan.setCellValue("TUJUAN");
			
			XSSFCell cellPerwakilan= row1.createCell((short) 4);
			cellPerwakilan.setCellValue("PERWAKILAN");
			
			//FA
			XSSFCell cellZona= row1.createCell((short) 5);
			cellZona.setCellValue("ZONA");
			
			XSSFCell cellKecamatan= row1.createCell((short) 6);
			cellKecamatan.setCellValue("KECAMATAN");
			
			XSSFCell cellKabupaten= row1.createCell((short) 7);
			cellKabupaten.setCellValue("KABUPATEN");
			
			XSSFCell cellPropinsi= row1.createCell((short) 8);
			cellPropinsi.setCellValue("PROPINSI");
			
			XSSFCell cellLayanan= row1.createCell((short) 9);
			cellLayanan.setCellValue("LAYANAN");
			
			XSSFCell cellHarga= row1.createCell((short) 10);
			cellHarga.setCellValue("HARGA");
			
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
			cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
			
			Integer noCol=1;
			for (LapPerKecamatanTV data : masterData) {
			
				row1 = worksheet.createRow((Integer) no++);
				
				XSSFCell cellA0 = row1.createCell((short) 0);
				cellA0.setCellValue(noCol++);
				
				XSSFCell cellA1 = row1.createCell((short) 1);
				cellA1.setCellValue(data.getAwb());
				
				XSSFCell cellB1 = row1.createCell((short) 2);
				cellB1.setCellValue(data.getTglCreate());
				cellB1.setCellStyle(cellStyle);
				
//				HSSFCell cellC1 = row1.createCell((short) 3);
//				cellC1.setCellValue(data.getPenerima());
				
				XSSFCell cellC1 = row1.createCell((short) 3);
				cellC1.setCellValue(data.getTujuan());
				
				XSSFCell cellD1 = row1.createCell((short) 4);
				cellD1.setCellValue(data.getKodePerwakilan());
				
				//FA
				XSSFCell cellE1 = row1.createCell((short) 5);
				cellE1.setCellValue(data.getZona());
				
				XSSFCell cellF1 = row1.createCell((short) 6);
				cellF1.setCellValue(data.getKecamatan());
				
				XSSFCell cellG1 = row1.createCell((short) 7);
				cellG1.setCellValue(data.getKabupaten());
				
				XSSFCell cellH1 = row1.createCell((short) 8);
				cellH1.setCellValue(data.getPropinsi());
				
				XSSFCell cellI1 = row1.createCell((short) 9);
				cellI1.setCellValue(data.getLayanan());
				
				XSSFCell cellJ1 = row1.createCell((short) 10);
				cellJ1.setCellValue(data.getHarga());
				
				
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
//		} catch (FileNotFoundException x) {
//			x.printStackTrace();
		} catch (IOException x) {
			x.printStackTrace();
		}
	}


	public static void exportToExcellReportPerPelanggan(Boolean checklist, ObservableList<ReportVO> masterData, ObservableList<ReportVO> masterData2, String namaFile,
			String dateFile) {
		if(!checklist){ // single pelanggan
			try {
				FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + dateFile  + " " + namaFile + ".xls");
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet worksheet = workbook.createSheet(namaFile);
				HSSFRow row1 = null;
				
				int no=0;
				row1 = worksheet.createRow((short) no++);
				
				HSSFCell cellNo = row1.createCell((short) 0);
				cellNo.setCellValue("NO");
				
				HSSFCell cellTgl = row1.createCell((short) 1);
				cellTgl.setCellValue("NO RESI");

				HSSFCell cellAwb= row1.createCell((short) 2);
				cellAwb.setCellValue("TUJUAN");
				
				HSSFCell cellPengirim= row1.createCell((short) 3);
				cellPengirim.setCellValue("PENERIMA");
				
				HSSFCell cellPenerima= row1.createCell((short) 4);
				cellPenerima.setCellValue("HP PENERIMA");
				
				HSSFCell cellTelpPenerima= row1.createCell((short) 5);
				cellTelpPenerima.setCellValue("RESI JNE");
				
				HSSFCell cellTujuan= row1.createCell((short) 6);
				cellTujuan.setCellValue("BERAT");
				
				HSSFCell cellZona= row1.createCell((short) 7);
				cellZona.setCellValue("BIAYA");
				
				HSSFCell cellBerat= row1.createCell((short) 8);
				cellBerat.setCellValue("ASURANSI");
				
				HSSFCell cellETD= row1.createCell((short) 9);
				cellETD.setCellValue("DISKON");
				
				HSSFCell cellKeterangan= row1.createCell((short) 10);
				cellKeterangan.setCellValue("TOTAL");
				
				int noCol=1;
				
				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
				cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
				
				for (ReportVO data : masterData) {
				
					row1 = worksheet.createRow((short) no++);
					HSSFCell cellA0 = row1.createCell((short) 0);
					cellA0.setCellValue(noCol++);
					
					HSSFCell cellA1 = row1.createCell((short) 1);
					cellA1.setCellValue(data.getAwbData());
					
					HSSFCell cellB1 = row1.createCell((short) 2);
					cellB1.setCellValue(data.getTujuan());
					
					HSSFCell cellC1 = row1.createCell((short) 3);
					cellC1.setCellValue(data.getPenerima());
					
					HSSFCell cellD1 = row1.createCell((short) 4);
					cellD1.setCellValue(data.getTelp());
					
					HSSFCell cellE1 = row1.createCell((short) 5);
					cellE1.setCellValue(data.getResiJNE());
				
					HSSFCell cellF1 = row1.createCell((short) 6);
					cellF1.setCellValue(data.getBfinal());
					
					HSSFCell cellG1 = row1.createCell((short) 7);
					cellG1.setCellValue(Integer.parseInt(data.getHarga().replace(".", "")));
					
					HSSFCell cellH1 = row1.createCell((short) 8);
					cellH1.setCellValue(data.getAsuransi());
					
					HSSFCell cellI1 = row1.createCell((short) 9);
					cellI1.setCellValue(data.getDiskon());
					
					HSSFCell cellJ1 = row1.createCell((short) 10);
					cellJ1.setCellValue(Integer.parseInt(data.getBiaya().replace(".", "")));			
									
//					cellF1.setCellStyle(cellStyle);
				}
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
			} catch (FileNotFoundException x) {
				x.printStackTrace();
			} catch (IOException x) {
				x.printStackTrace();
			}
		}else{			
			try {
				FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + dateFile  + " " + namaFile + ".xls");
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet worksheet = workbook.createSheet(namaFile);
				HSSFRow row1 = null;
				
				int no=0;
				row1 = worksheet.createRow((short) no++);
				
				HSSFCell cellNo = row1.createCell((short) 0);
				cellNo.setCellValue("NO");
				
				HSSFCell cellTgl = row1.createCell((short) 1);
				cellTgl.setCellValue("NO SALES");

				HSSFCell cellAwb= row1.createCell((short) 2);
				cellAwb.setCellValue("PENGIRIM");
				
				HSSFCell cellPengirim= row1.createCell((short) 3);
				cellPengirim.setCellValue("AWB");
				
				HSSFCell cellPenerima= row1.createCell((short) 4);
				cellPenerima.setCellValue("BERAT");
				
				HSSFCell cellTelpPenerima= row1.createCell((short) 5);
				cellTelpPenerima.setCellValue("BERAT ASLI");
				
				HSSFCell cellTujuan= row1.createCell((short) 6);
				cellTujuan.setCellValue("TOTAL BIAYA");
				
				HSSFCell cellZona= row1.createCell((short) 7);
				cellZona.setCellValue("HARGA SETELAH DISKON");
				
				HSSFCell cellBerat= row1.createCell((short) 8);
				cellBerat.setCellValue("TOTAL DISKON");				
				
				int noCol=1;
				
				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
				cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
				
				for (ReportVO data : masterData2) {
				
					row1 = worksheet.createRow((short) no++);
					HSSFCell cellA0 = row1.createCell((short) 0);
					cellA0.setCellValue(noCol++);
					
					HSSFCell cellA1 = row1.createCell((short) 1);
					cellA1.setCellValue(data.getNmSales());
					
					HSSFCell cellB1 = row1.createCell((short) 2);
					cellB1.setCellValue(data.getPengirim());
					
					HSSFCell cellC1 = row1.createCell((short) 3);
					cellC1.setCellValue(data.getJumlahBarang());
					
					HSSFCell cellD1 = row1.createCell((short) 4);
					cellD1.setCellValue(data.getSumBerat());
					
					HSSFCell cellE1 = row1.createCell((short) 5);
					cellE1.setCellValue(data.getSumBeratAsli());
				
					HSSFCell cellF1 = row1.createCell((short) 6);
					cellF1.setCellValue(Integer.parseInt(data.getBiaya().replace(".", "")));
					
					HSSFCell cellG1 = row1.createCell((short) 7);
					cellG1.setCellValue(Integer.parseInt(data.getHargaSetelahDiskon().replace(".", "")));
					
					HSSFCell cellH1 = row1.createCell((short) 8);
					cellH1.setCellValue(Integer.parseInt(data.getTotalDiskonDiskon().replace(".", "")));									
									
//					cellF1.setCellStyle(cellStyle);
				}
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
			} catch (FileNotFoundException x) {
				x.printStackTrace();
			} catch (IOException x) {
				x.printStackTrace();
			}
		}
		
		
	}


//	public static void exportToExcellReportPerKecamatan(
//			ObservableList<LaporanPerKecamatanVO> masterData, 
//			String title,
//			String dateFile) {
//
//		try {
//			FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + dateFile  + " " + title + ".xls");
//			HSSFWorkbook workbook = new HSSFWorkbook();
//			HSSFSheet worksheet = workbook.createSheet(title);
//			HSSFRow row1 = null;
//			
//			int no=0;
//			row1 = worksheet.createRow((short) no++);
//			
//			HSSFCell cellNo = row1.createCell((short) 0);
//			cellNo.setCellValue("NO");
//			
//			HSSFCell cellAwb = row1.createCell((short) 1);
//			cellAwb.setCellValue("AWB");
//
//			HSSFCell cellTgl= row1.createCell((short) 2);
//			cellTgl.setCellValue("TANGGAL");
//			
//			HSSFCell cellPengirim= row1.createCell((short) 3);
//			cellPengirim.setCellValue("PENGIRIM");
//			
//			HSSFCell cellPenerima= row1.createCell((short) 4);
//			cellPenerima.setCellValue("PENERIMA");
//			
//			HSSFCell cellKec= row1.createCell((short) 5);
//			cellPenerima.setCellValue("KECAMATAN");
//			
//			HSSFCell cellKdPerwakilan= row1.createCell((short) 6);
//			cellKdPerwakilan.setCellValue("KD PERWAKILAN");			
//			
//			HSSFCellStyle cellStyle = workbook.createCellStyle();
//			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
//			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
//			cellStyle = workbook.createCellStyle();
//			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
//			
//			int noCol=1;
//			for (LaporanPerKecamatanVO data : masterData) {
//			
//				row1 = worksheet.createRow((short) no++);
//				HSSFCell cellA0 = row1.createCell((short) 0);
//				cellA0.setCellValue(noCol++);
//				
//				HSSFCell cellA1 = row1.createCell((short) 1);
//				cellA1.setCellValue(data.getAwb());
//				
//				HSSFCell cellB1 = row1.createCell((short) 2);
//				cellB1.setCellValue(data.getTglEntry());
//				
//				HSSFCell cellC1 = row1.createCell((short) 3);
//				cellC1.setCellValue(data.getPengirim());
//				
//				HSSFCell cellD1 = row1.createCell((short) 4);
//				cellD1.setCellValue(data.getPenerima());
//				
//				HSSFCell cellE1 = row1.createCell((short) 5);
//				cellE1.setCellValue(data.getKecamatan());
//				
//				HSSFCell cellF1 = row1.createCell((short) 6);
//				cellF1.setCellValue(data.getPerwakilan());
//				
//				cellF1.setCellStyle(cellStyle);
//			}
//			workbook.write(fileOut);
//			fileOut.flush();
//			fileOut.close();
//		} catch (FileNotFoundException x) {
//			x.printStackTrace();
//		} catch (IOException x) {
//			x.printStackTrace();
//		}
//	}


}