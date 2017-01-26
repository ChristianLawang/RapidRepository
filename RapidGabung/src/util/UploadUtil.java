package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;

import VO.SMSExportVO;
import entity.TrHarga;
import entity.TrPerwakilan;
import entity.TtMappingResiJne;
import service.GenericService;
import service.MapingJneService;
import service.MasterHargaService;
import service.MasterPerwakilanService;

public class UploadUtil {

	private String path;
	public UploadUtil(String pathS){
		this.path = pathS;
	}
	
	public void upload(){
		
		File myFile = new File(this.path);
        FileInputStream fis;
		try {
			fis = new FileInputStream(myFile);
		
		    // Finds the workbook instance for XLSX file
		    XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
		      
		    // Return first sheet from the XLSX workbook
		    XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		       
		    // Get iterator to all the rows in current sheet
		    Iterator<Row> rowIterator = mySheet.iterator();
		    List<TtMappingResiJne> lstUpload = new ArrayList<TtMappingResiJne>();
		    
		    // Traversing over each row of XLSX file
		    while (rowIterator.hasNext()) {		        
		    	Row row = rowIterator.next();
		    	TtMappingResiJne upl = new TtMappingResiJne();
		        // For each row, iterate through each columns
		        Iterator<Cell> cellIterator = row.cellIterator();
		        Integer col = 0;
		        while (cellIterator.hasNext()) {
		            Cell cell = cellIterator.next();
		            switch(col){
		        	case 0: // resi
		        		upl.setResiJne((String) heyWhatsUp(cell));
		        		break;
		        	case 1: // penerima
		        		upl.setPenerima((String) heyWhatsUp(cell));
		        		break;
		        	case 2: // pengirim
		        		upl.setPengirim((String) heyWhatsUp(cell));
		        		break;
		        	case 3: // tujuan
		        		upl.setTujuan((String) heyWhatsUp(cell));
		        		break;
		        	case 4: // servis
		        		upl.setService((String) heyWhatsUp(cell));
		        		break;
		        	case 5: // harga
		        		Double dbl = cell.getNumericCellValue();
		        		upl.setHarga(dbl.intValue());
		        		break;
		        	}
		            col++;
		        }
		        upl.setTglCreate(new Date());
		        upl.setUploadFlag(0);
		        upl.setFlag(0);
		        MapingJneService.save(upl);
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	private static Object heyWhatsUp(Cell cell) {
		switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_STRING:
	            return (String) cell.getStringCellValue();
	        case Cell.CELL_TYPE_NUMERIC:
	        	Double d = (Double) cell.getNumericCellValue();
	            return (Integer) d.intValue();
	        case Cell.CELL_TYPE_BOOLEAN:
	            return (Boolean) cell.getBooleanCellValue();
	        default :
		}
		return null;
	}
	
	// chris
	public static void generateExcel(List<SMSExportVO> data, String filename) throws FileNotFoundException, IOException {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Java Books");
		CreationHelper createHelper = workbook.getCreationHelper();

		CellStyle cellStyle = workbook.createCellStyle();
	    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
	    
		int rowCount = 0;
		Row row = sheet.createRow(rowCount);
		Cell cell = row.createCell(0);
		cell.setCellValue("no_telp");
		cell = row.createCell(1);
		cell.setCellValue("penerima");
		cell = row.createCell(2);
		cell.setCellValue("pengirim");
		cell = row.createCell(3);
		cell.setCellValue("tanggal");
		cell = row.createCell(4);
		cell.setCellValue("resi");
		cell = row.createCell(5);
		cell.setCellValue("web");
		rowCount++;
		for (SMSExportVO everyRow : data) {
			row = sheet.createRow(rowCount);
			cell = row.createCell(0);
			cell.setCellValue(everyRow.getTelpPenerima());
			cell = row.createCell(1);
			cell.setCellValue(everyRow.getPenerima());
			cell = row.createCell(2);
			cell.setCellValue(everyRow.getPengirim());
			cell = row.createCell(3);
			cell.setCellValue(everyRow.getTanggal());
			cell.setCellStyle(cellStyle);
			cell = row.createCell(4);
			cell.setCellValue(everyRow.getAWB());
			cell = row.createCell(5);
			cell.setCellValue(everyRow.getWeb());
			rowCount++;
		}
		
		try (FileOutputStream outputStream = new FileOutputStream(filename)) {
			workbook.write(outputStream);
		}

		System.out.println("done!");
		
	}
	
	public static void generateExcel(String strFileName) throws FileNotFoundException, IOException {
		List<TrPerwakilan> data = MasterPerwakilanService.getDataPerwakilan1();
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Java Books");
		CreationHelper createHelper = workbook.getCreationHelper();

		CellStyle cellStyle = workbook.createCellStyle();
	    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
	    
		int rowCount = 0;

		for (TrPerwakilan everyRow : data) {
			
			Row row = sheet.createRow(rowCount);
			
			Cell cell = row.createCell(0);
			cell.setCellValue(everyRow.getKodeZona());
			cell = row.createCell(1);
			cell.setCellValue(everyRow.getPropinsi());
			cell = row.createCell(2);
			cell.setCellValue(everyRow.getKabupaten());
			cell = row.createCell(3);
			cell.setCellValue(everyRow.getKecamatan());
			cell = row.createCell(4);
			cell.setCellValue(everyRow.getKodePerwakilan());
			cell = row.createCell(5);
			cell.setCellValue(everyRow.getZona());
			cell = row.createCell(6);
			cell.setCellValue(everyRow.getRegperwakilan());
			cell = row.createCell(7);
			cell.setCellValue(everyRow.getOneperwakilan());
			cell = row.createCell(8);
			cell.setCellValue(everyRow.getTglCreate());
			cell.setCellStyle(cellStyle);
			cell = row.createCell(9);
			cell.setCellValue(everyRow.getTglUpdate());
			cell.setCellStyle(cellStyle);
			cell = row.createCell(10);
			cell.setCellValue((Integer) everyRow.getFlag());
			rowCount++;
		}
		
		try (FileOutputStream outputStream = new FileOutputStream(strFileName)) {
			workbook.write(outputStream);
		}

		System.out.println("done!");

	}

	public void uploadMasterHarga() {
//		MasterHargaService.emptyHarga();
		File myFile = new File(this.path);
        FileInputStream fis;
		try {
			fis = new FileInputStream(myFile);
		
		    // Finds the workbook instance for XLSX file
		    XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
		      
		    // Return first sheet from the XLSX workbook
		    XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		       
		    // Get iterator to all the rows in current sheet
		    Iterator<Row> rowIterator = mySheet.iterator();
//		    List<TtMappingResiJne> lstUpload = new ArrayList<TtMappingResiJne>();
		    
		    Session s = HibernateUtil.openSession();
		    // Traversing over each row of XLSX file
		    Row row = rowIterator.next();
		    while (rowIterator.hasNext()) {		        
		    	row = rowIterator.next();
		    	TrHarga uplHarga = new TrHarga();
		    	TrPerwakilan uplPerwakilan = new TrPerwakilan();
		        // For each row, iterate through each columns
		        Iterator<Cell> cellIterator = row.cellIterator();
		        Integer col = 0;
		        while (cellIterator.hasNext()) {
		            Cell cell = cellIterator.next();
		            switch(col){
			        	case 0: // kode zona
					        System.out.println("--> kodezona : " + heyWhatsUp(cell));					        
			        		uplHarga.getPk().setKodeZona((String) heyWhatsUp(cell));
			        		uplPerwakilan.getPk().setKodeZona((String) heyWhatsUp(cell));
			        		break;
			        	case 1: // propinsi
			        		uplHarga.setPropinsi((String) heyWhatsUp(cell));
			        		uplPerwakilan.setPropinsi((String) heyWhatsUp(cell));
			        		break;
			        	case 2: // kode asal
			        		uplHarga.getPk().setKodeAsal((String) heyWhatsUp(cell));
			        		uplPerwakilan.getPk().setKodeAsal((String) heyWhatsUp(cell));
			        		break;
			        	case 3: // kabupaten
			        		uplHarga.setKabupaten((String) heyWhatsUp(cell));
			        		uplPerwakilan.setKabupaten((String) heyWhatsUp(cell));
			        		break;
			        	case 4: // kecamatan
			        		uplHarga.setKecamatan((String) heyWhatsUp(cell));
			        		uplPerwakilan.setKecamatan((String) heyWhatsUp(cell));
			        		break;
			        	case 5: // kode perwakilan
			        		System.out.println("--> kodePerwakilan : " + heyWhatsUp(cell));
			        		uplHarga.setKodePerwakilan((String) heyWhatsUp(cell));
			        		uplPerwakilan.setKodePerwakilan((String) heyWhatsUp(cell));
			        		break;
			        	case 6: // zona
			        		String zona = (String) heyWhatsUp(cell);
			        		uplHarga.setZona(zona);
			        		uplPerwakilan.setZona(zona);
			        		break;
			        	case 7: // reg
			        		System.out.println("--> cell : " + cell);
			        		Double dblReg = cell.getNumericCellValue();
			        		uplHarga.setReg(dblReg.intValue());			        		
			        		break;
			        	case 8: // etd
			        		uplHarga.setEtd((String) heyWhatsUp(cell));
			        		break;
			        	case 9: // one
			        		Double dblOne = cell.getNumericCellValue();
			        		uplHarga.setOne(dblOne.intValue());			        		
			        		break;
			        	case 10: // regPerwakilan
			        		uplPerwakilan.setRegperwakilan((String) heyWhatsUp(cell));
			        		break;
			        	case 11: // onePerwakilan
			        		uplPerwakilan.setOneperwakilan((String) heyWhatsUp(cell));
			        		break;
			        	}
		            col++;
		        }
		        uplHarga.setTglCreate(new Date());
		        uplHarga.setTglUpdate(new Date());
		        uplHarga.setFlag(0);
		        
		        uplPerwakilan.setTglCreate(new Date());
		        uplPerwakilan.setTglUpdate(new Date());
		        uplPerwakilan.setFlag(0);

//		        GenericService.saveOrUpdate(TrHarga.class, uplHarga);
//		        GenericService.saveOrUpdate(TrPerwakilan.class, uplPerwakilan);
//		        if(MasterHargaService.isHargaExist(uplHarga.getKodeZona(), uplHarga.getKodeAsal())){
//		        	MasterHargaService.updateDataHarga(
//		        			uplHarga.getKodeZona(), 
//		        			uplHarga.getKodeAsal(), 
//		        			uplHarga.getPropinsi(), 
//		        			uplHarga.getKabupaten(), 
//		        			uplHarga.getKecamatan(),
//		        			uplHarga.getKodePerwakilan(),
//		        			uplHarga.getZona(),
//		        			uplHarga.getReg(),
//		        			uplHarga.getEtd(),
//		        			uplHarga.getOne());
//		        }else{
		        	GenericService.saveOrUpdate(s, TrHarga.class, uplHarga);
		        	GenericService.saveOrUpdate(s, TrPerwakilan.class, uplPerwakilan);
//		        }
		    }
		    s.getTransaction().commit();
		    MessageBox.alert("Upload Harga baru selesai");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
