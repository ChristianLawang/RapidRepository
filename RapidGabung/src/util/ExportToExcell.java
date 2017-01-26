package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import VO.EntryDataShowVO;
import VO.LaporanKurirVO;
import VO.LaporanPenerimaVO;
import VO.LaporanPerKecamatanVO;
import VO.ReportVO;
import VO.TagihanVO;
import controller.LapPerKecamatanController.LapPerKecamatanTV;
import controller.LaporanDiskonController.LapDiskonTV;
import controller.LaporanKomisiController.LapKomisiTV;
import controller.MasterHargaController;
import controller.MasterHargaController.MasterHargaTV;
import controller.MasterPelangganController.PelangganTV;
import entity.TrPelanggan;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


public class ExportToExcell {
	// FA
	//Laporan Komisi
		public static void exportToExcellReportKomisi(ObservableList<LapKomisiTV> masterData, String title,
				String dateFile) {
			try {
				FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + dateFile  + " " + title + ".xls");
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet worksheet = workbook.createSheet(title);
				XSSFRow row1 = null;
				
				int no=0;
				row1 = worksheet.createRow((short) no++);
				
				XSSFCell cellNo = row1.createCell((short) 0);
				cellNo.setCellValue("NO");
				
				XSSFCell cellAwb = row1.createCell((short) 1);
				cellAwb.setCellValue("NAMA SALES");
		
				XSSFCell cellTgl= row1.createCell((short) 2);
				cellTgl.setCellValue("NAMA PELANGGAN");
				
				XSSFCell cellTujuan= row1.createCell((short) 3);
				cellTujuan.setCellValue("AWB");
				
				XSSFCell cellPerwakilan= row1.createCell((short) 4);
				cellPerwakilan.setCellValue("BERAT");
				
				XSSFCell cellZona= row1.createCell((short) 5);
				cellZona.setCellValue("BERAT ASLI");
				
				XSSFCell cellKecamatan= row1.createCell((short) 6);
				cellKecamatan.setCellValue("TOTAL");
				
				XSSFCell cellKabupaten= row1.createCell((short) 7);
				cellKabupaten.setCellValue("DISKON");
				
				XSSFCell cellPropinsi= row1.createCell((short) 8);
				cellPropinsi.setCellValue("HARGA SETELAH DISKON");
				
				XSSFCell cellLayanan= row1.createCell((short) 9);
				cellLayanan.setCellValue("STATUS PEMBAYARAN");
				
				XSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
				cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
				
				Integer noCol=1;
				
				for (LapKomisiTV data : masterData) {
					row1 = worksheet.createRow((Integer) no++);
					
					XSSFCell cellA0 = row1.createCell((short) 0);
					cellA0.setCellValue(noCol++);
					
					XSSFCell cellA1 = row1.createCell((short) 1);
					cellA1.setCellValue(data.getNamaSales());
					
					XSSFCell cellB1 = row1.createCell((short) 2);
					cellB1.setCellValue(data.getNamaPengirim());
//					cellB1.setCellStyle(cellStyle);
					
					XSSFCell cellC1 = row1.createCell((short) 3);
					cellC1.setCellValue(Integer.valueOf(data.getAwb()));
					
					XSSFCell cellD1 = row1.createCell((short) 4);
					cellD1.setCellValue(Integer.valueOf(data.getBerat()));
					
					XSSFCell cellE1 = row1.createCell((short) 5);
					cellE1.setCellValue(Double.parseDouble(data.getBeratAsli()));
					
					XSSFCell cellF1 = row1.createCell((short) 6);
					cellF1.setCellValue(Integer.valueOf(data.getHarga()));
					
					XSSFCell cellG1 = row1.createCell((short) 7);
					cellG1.setCellValue(Integer.valueOf(data.getDiskon()));
					
					XSSFCell cellH1 = row1.createCell((short) 8);
					cellH1.setCellValue(Integer.valueOf(data.getHargaStlhDiskon()));
					
					XSSFCell cellI1 = row1.createCell((short) 9);
					cellI1.setCellValue(data.getStatus());
				}
				
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
			} catch (IOException x) {
				x.printStackTrace();
			}
		}
		
		//Laporan Diskon
		public static void exportToExcellReportDiskon(ObservableList<LapDiskonTV> masterData, String title,
				String dateFile) {
			try {
				FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + dateFile  + " " + title + ".xls");
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet worksheet = workbook.createSheet(title);
				XSSFRow row1 = null;
				
				int no=0;
				row1 = worksheet.createRow((short) no++);
				
				XSSFCell cellNo = row1.createCell((short) 0);
				cellNo.setCellValue("NO");
				
				XSSFCell cellAwb = row1.createCell((short) 1);
				cellAwb.setCellValue("NAMA PELANGGAN");
		
				XSSFCell cellTgl= row1.createCell((short) 2);
				cellTgl.setCellValue("DISKON RAPID");
				
				XSSFCell cellTujuan= row1.createCell((short) 3);
				cellTujuan.setCellValue("DISKON JNE");
				
				XSSFCell cellPerwakilan= row1.createCell((short) 4);
				cellPerwakilan.setCellValue("TANGGAL MULAI DISKON");
				
				XSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
				cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
				
				Integer noCol=1;
				
				for (LapDiskonTV data : masterData) {
					row1 = worksheet.createRow((Integer) no++);
					
					XSSFCell cellA0 = row1.createCell((short) 0);
					cellA0.setCellValue(noCol++);
					
					XSSFCell cellA1 = row1.createCell((short) 1);
					cellA1.setCellValue(data.getNamaPelanggan());
					
					XSSFCell cellB1 = row1.createCell((short) 2);
					cellB1.setCellValue(Integer.valueOf(data.getDiskonRapid()));
//					cellB1.setCellStyle(cellStyle);
					
					XSSFCell cellC1 = row1.createCell((short) 3);
					cellC1.setCellValue(Integer.valueOf(data.getDiskonJNE()));
					
					XSSFCell cellD1 = row1.createCell((short) 4);
					cellD1.setCellValue(data.getTglDiskon());
					cellD1.setCellStyle(cellStyle);
				}
				
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
			} catch (IOException x) {
				x.printStackTrace();
			}
		}
	
		//FA
		//FA
		public static void exportToReportKurir (ObservableList<LaporanKurirVO> masterDataHeader, ObservableList<LaporanKurirVO> masterDataDetail, 
				ObservableList<LaporanKurirVO> masterDataDetail2, ObservableList<LaporanKurirVO> masterDataFooter, String title, String dateFile) {
			try {
				FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + dateFile  + " " + title + ".xls");
					
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet worksheet = workbook.createSheet(title);
				XSSFRow row1 = null;
					
				CellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
				cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
				cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				cellStyle.setBorderRight(CellStyle.BORDER_THIN);
				cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
				cellStyle.setBorderTop(CellStyle.BORDER_THIN);
				cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
				
				int no=0;
				int countCol = 0;
				
				int startPersentase = 0;
				int persenSize = 1;
				
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellEmpty = row1.createCell((short) 0);
				cellEmpty.setCellValue("");
				cellEmpty.setCellStyle(cellStyle);
					
				XSSFCell cellNama = row1.createCell((short) 1);
				cellNama.setCellValue("Nama");
				cellNama.setCellStyle(cellStyle);
					
				XSSFCell cellEmpty2 = row1.createCell((short) 2);
				cellEmpty2.setCellValue("");
				cellEmpty2.setCellStyle(cellStyle);
					
					
				for (int i = 0; i < masterDataHeader.size(); i++ ) {
					XSSFCell cellTgl = row1.createCell((short) i+3);
					cellTgl.setCellValue(masterDataHeader.get(i).getDate());
					cellTgl.setCellStyle(cellStyle);
						
				}
					
				countCol = 2 + masterDataHeader.size();
					
				System.out.println("countCol : " + countCol);
					
				//Mulai test merged row border
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellNo = row1.createCell((short) 0);
				cellNo.setCellValue("No");
				cellNo.setCellStyle(cellStyle);
					
				worksheet.addMergedRegion(new CellRangeAddress(1, 4, 0, 0));
					
				XSSFCell cellTotal = row1.createCell((short) 1);
				cellTotal.setCellValue("Total");
				cellTotal.setCellStyle(cellStyle);
				worksheet.addMergedRegion(new CellRangeAddress(1, 4, 1, 1));
					
				XSSFCell cellKirim = row1.createCell((short) 2);
				cellKirim.setCellValue("Kirim");
				cellKirim.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataHeader.size(); i++ ) {
					XSSFCell cellTgl = row1.createCell((short) i+3);
					cellTgl.setCellValue(Integer.valueOf(masterDataHeader.get(i).getTotalDeliver()));
					cellTgl.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				//test
				XSSFCell cellNo2 = row1.createCell((short) 0);
				cellNo2.setCellStyle(cellStyle);
					
				//test
				XSSFCell cellTotal2 = row1.createCell((short) 1);
				cellTotal2.setCellStyle(cellStyle);
					
				XSSFCell cellTerima = row1.createCell((short) 2);
				cellTerima.setCellValue("Terima");
				cellTerima.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataHeader.size(); i++ ) {
					XSSFCell cellTgl = row1.createCell((short) i+3);
					cellTgl.setCellValue(Integer.valueOf(masterDataHeader.get(i).getTotalReceive()));
					cellTgl.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
					//test
				XSSFCell cellNo3 = row1.createCell((short) 0);
				cellNo3.setCellStyle(cellStyle);
					
					//test
				XSSFCell cellTotal3 = row1.createCell((short) 1);
				cellTotal3.setCellStyle(cellStyle);
					
				XSSFCell cellSisa = row1.createCell((short) 2);
				cellSisa.setCellValue("Sisa");
				cellSisa.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataHeader.size(); i++ ) {
					XSSFCell cellTgl = row1.createCell((short) i+3);
					cellTgl.setCellValue(Integer.valueOf(masterDataHeader.get(i).getTotalRemaining()));
					cellTgl.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
				
				//test
				XSSFCell cellNo4 = row1.createCell((short) 0);
				cellNo4.setCellStyle(cellStyle);
					
				//test
				XSSFCell cellTotal4 = row1.createCell((short) 1);
				cellTotal4.setCellStyle(cellStyle);
					
				XSSFCell cellPercentage = row1.createCell((short) 2);
				cellPercentage.setCellValue("Persentase");
				cellPercentage.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataHeader.size(); i++ ) {
					XSSFCell cellTgl = row1.createCell((short) i+3);
					cellTgl.setCellValue(Double.valueOf(masterDataHeader.get(i).getTotalPercentage()));
					cellTgl.setCellStyle(cellStyle);
				}
				
				startPersentase = countCol + 2;
				
				XSSFCell cellTitle = row1.createCell((short) startPersentase++);
				cellTitle.setCellValue("Persenstase keberhasilan pengiriman (di urutkan dr yg terbaik)");
				
				startPersentase = 0;
					
				row1 = worksheet.createRow((short) no++);
				
				int colNoStart = 5;
				int colNoEnd = 7;
					
				int colNameStart = 5;
				int colNameEnd = 6;
					
				int colLabel = 2;
				int colValue = 3;
					
				int colTotal = 1;
				
				int startRow = 6;
				int startCol = 5;
					
				int totalNo = 1;
				
				for (int i = 0; i < masterDataDetail.size(); i++ ) {
					if (i == 0) {
						XSSFCell cellCount = row1.createCell((short) 0);
						cellCount.setCellValue((i+1));
						cellCount.setCellStyle(cellStyle);
						worksheet.addMergedRegion(new CellRangeAddress(colNoStart, colNoEnd, 0, 0));
							
						XSSFCell cellName = row1.createCell((short) 1);
						cellName.setCellValue(masterDataDetail.get(i).getName());
						cellName.setCellStyle(cellStyle);
						worksheet.addMergedRegion(new CellRangeAddress(colNameStart, colNameEnd, 1, 1));
							
						XSSFCell cellKirimD = row1.createCell((short) colLabel);
						cellKirimD.setCellValue("Kirim");
						cellKirimD.setCellStyle(cellStyle);
							
						XSSFCell cellKirimV = row1.createCell((short) colValue);
						cellKirimV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalDeliver()));
						cellKirimV.setCellStyle(cellStyle);
							
						for (int j = 0; j < masterDataDetail.size(); j++) {
							if (j > 0 && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
								colValue++;
								XSSFCell cellKirimVL = row1.createCell((short) colValue);
								cellKirimVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalDeliver()));
								cellKirimVL.setCellStyle(cellStyle);
							}
						}
							
						for (int k = colValue; k <= countCol; k++) {
							if (k != colValue) {
								XSSFCell cellKirimVL = row1.createCell((short) k);
								cellKirimVL.setCellValue(0);
								cellKirimVL.setCellStyle(cellStyle);
							}
								
						}
						
						startPersentase = countCol + 2;
						
						XSSFCell cellNamaKurir = row1.createCell((short) startPersentase++);
						cellNamaKurir.setCellValue(masterDataDetail2.get(i).getName());
						
						XSSFCell cellKurirPersentase = row1.createCell((short) startPersentase++);
						cellKurirPersentase.setCellValue(masterDataDetail2.get(i).getTotalPercentage());
						
						colValue = 3;
						startPersentase = 0;
							
						row1 = worksheet.createRow((short) no++);
							
						//testL
						XSSFCell cellCount2 = row1.createCell((short) 0);
						cellCount2.setCellStyle(cellStyle);
							
						XSSFCell cellTerimaD = row1.createCell((short) colLabel);
						cellTerimaD.setCellValue("Terima");
						cellTerimaD.setCellStyle(cellStyle);
							
						XSSFCell cellTerimaV = row1.createCell((short) colValue);
						cellTerimaV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalReceive()));
						cellTerimaV.setCellStyle(cellStyle);
							
						for (int j = 0; j < masterDataDetail.size(); j++) {
							if (j > 0 && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
								colValue++;
								XSSFCell cellTerimaVL = row1.createCell((short) colValue);
								cellTerimaVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalReceive()));
								cellTerimaVL.setCellStyle(cellStyle);
							}
						}
							
						for (int k = colValue; k <= countCol; k++) {
							if (k != colValue) {
								XSSFCell cellKirimVL = row1.createCell((short) k);
								cellKirimVL.setCellValue(0);
								cellKirimVL.setCellStyle(cellStyle);
							}
								
						}
						
						colValue = 3;
							
						row1 = worksheet.createRow((short) no++);
							
						//testL
						XSSFCell cellCount3 = row1.createCell((short) 0);
						cellCount3.setCellStyle(cellStyle);
							
						XSSFCell cellSisaD = row1.createCell((short) colLabel);
						cellSisaD.setCellValue("Sisa");
						cellSisaD.setCellStyle(cellStyle);
							
						XSSFCell cellSisaV = row1.createCell((short) colValue);
						cellSisaV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalRemaining()));
						cellSisaV.setCellStyle(cellStyle);
							
						for (int j = 0; j < masterDataDetail.size(); j++) {
							if (j > 0 && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
								colValue++;
								XSSFCell cellSisaVL = row1.createCell((short) colValue);
								cellSisaVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalRemaining()));
								cellSisaVL.setCellStyle(cellStyle);
							}
						}
							
						for (int k = colValue; k <= countCol; k++) {
							if (k != colValue) {
								XSSFCell cellKirimVL = row1.createCell((short) k);
								cellKirimVL.setCellValue(0);
								cellKirimVL.setCellStyle(cellStyle);
							}
								
						}
						
						colValue = 3;
							
						XSSFCell cellTotalV = row1.createCell((short) colTotal);
						cellTotalV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getJumlahTerima()));
						cellTotalV.setCellStyle(cellStyle);
					}
						
					if (i > 0) {
						if (!masterDataDetail.get(i-1).getName().equals(masterDataDetail.get(i).getName())) {
							row1 = worksheet.createRow((short) no++);
							colNoStart += 3;
							colNoEnd += 3;
							colNameStart += 3;
							colNameEnd += 3; 
							totalNo++;
								
							XSSFCell cellCount = row1.createCell((short) 0);
							cellCount.setCellValue(totalNo);
							cellCount.setCellStyle(cellStyle);
							worksheet.addMergedRegion(new CellRangeAddress(colNoStart, colNoEnd, 0, 0));
								
							XSSFCell cellName = row1.createCell((short) 1);
							cellName.setCellValue(masterDataDetail.get(i).getName());
							cellName.setCellStyle(cellStyle);
							worksheet.addMergedRegion(new CellRangeAddress(colNameStart, colNameEnd, 1, 1));
								
							XSSFCell cellKirimD = row1.createCell((short) colLabel);
							cellKirimD.setCellValue("Kirim");
							cellKirimD.setCellStyle(cellStyle);
								
							XSSFCell cellKirimV = row1.createCell((short) colValue);
							cellKirimV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalDeliver()));
							cellKirimV.setCellStyle(cellStyle);
								
							for (int j = 0; j < masterDataDetail.size(); j++) {
								if (j > i && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
									colValue++;
									XSSFCell cellKirimVL = row1.createCell((short) colValue);
									cellKirimVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalDeliver()));
									cellKirimVL.setCellStyle(cellStyle);
								}
							}
							
							for (int k = colValue; k <= countCol; k++) {
								if (k != colValue) {
									XSSFCell cellKirimVL = row1.createCell((short) k);
									cellKirimVL.setCellValue(0);
									cellKirimVL.setCellStyle(cellStyle);
								}
									
							}
							
							startPersentase = countCol + 2;
							
							XSSFCell cellNamaKurir = row1.createCell((short) startPersentase++);
							cellNamaKurir.setCellValue(masterDataDetail2.get(persenSize).getName());

							XSSFCell cellKurirPersentase = row1.createCell((short) startPersentase++);
							cellKurirPersentase.setCellValue(masterDataDetail2.get(persenSize).getTotalPercentage());

							persenSize++;
								
							colValue = 3;
							startPersentase = 0;
								
							row1 = worksheet.createRow((short) no++);
								
							//testL
							XSSFCell cellCount2 = row1.createCell((short) 0);
							cellCount2.setCellStyle(cellStyle);
								
							XSSFCell cellTerimaD = row1.createCell((short) colLabel);
							cellTerimaD.setCellValue("Terima");
							cellTerimaD.setCellStyle(cellStyle);
								
							XSSFCell cellTerimaV = row1.createCell((short) colValue);
							cellTerimaV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalReceive()));
							cellTerimaV.setCellStyle(cellStyle);
								
							for (int j = 0; j < masterDataDetail.size(); j++) {
								if (j > i && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
									colValue++;
									XSSFCell cellTerimaVL = row1.createCell((short) colValue);
									cellTerimaVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalReceive()));
									cellTerimaVL.setCellStyle(cellStyle);
								}
							}
								
							for (int k = colValue; k <= countCol; k++) {
								if (k != colValue) {
									XSSFCell cellKirimVL = row1.createCell((short) k);
									cellKirimVL.setCellValue(0);
									cellKirimVL.setCellStyle(cellStyle);
								}
									
							}
							
							colValue = 3;
								
							row1 = worksheet.createRow((short) no++);
								
							//testL
							XSSFCell cellCount3 = row1.createCell((short) 0);
							cellCount3.setCellStyle(cellStyle);
								
							XSSFCell cellSisaD = row1.createCell((short) colLabel);
							cellSisaD.setCellValue("Sisa");
							cellSisaD.setCellStyle(cellStyle);
								
							XSSFCell cellSisaV = row1.createCell((short) colValue);
							cellSisaV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalRemaining()));
							cellSisaV.setCellStyle(cellStyle);
								
							for (int j = 0; j < masterDataDetail.size(); j++) {
								if (j > i && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
									colValue++;
									XSSFCell cellSisaVL = row1.createCell((short) colValue);
									cellSisaVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalRemaining()));
									cellSisaVL.setCellStyle(cellStyle);
								}
							}
								
							for (int k = colValue; k <= countCol; k++) {
								if (k != colValue) {
									XSSFCell cellKirimVL = row1.createCell((short) k);
									cellKirimVL.setCellValue(0);
									cellKirimVL.setCellStyle(cellStyle);
								}
									
							}
							
							colValue = 3;
								
							XSSFCell cellTotalV = row1.createCell((short) colTotal);
							cellTotalV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getJumlahTerima()));
							cellTotalV.setCellStyle(cellStyle);
						}
					}
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellAuL = row1.createCell((short) 2);
				cellAuL.setCellValue("AU");
				cellAuL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellAuV = row1.createCell((short) i+3);
					cellAuV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getAu()));
					cellAuV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
				
				XSSFCell cellBaL = row1.createCell((short) 2);
				cellBaL.setCellValue("BA");
				cellBaL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellBaV = row1.createCell((short) i+3);
					cellBaV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getBa()));
					cellBaV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellCodaL = row1.createCell((short) 2);
				cellCodaL.setCellValue("CODA");
				cellCodaL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellCodaV = row1.createCell((short) i+3);
					cellCodaV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getCoda()));
					cellCodaV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellNthL = row1.createCell((short) 2);
				cellNthL.setCellValue("NTH");
				cellNthL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellCodaV = row1.createCell((short) i+3);
					cellCodaV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getNth()));
					cellCodaV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellCneeL = row1.createCell((short) 2);
				cellCneeL.setCellValue("CNEE U");
				cellCneeL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellCneeV = row1.createCell((short) i+3);
					cellCneeV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getCnee()));
					cellCneeV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellMissL = row1.createCell((short) 2);
				cellMissL.setCellValue("MISS ROUTE");
				cellMissL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellMissV = row1.createCell((short) i+3);
					cellMissV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getMiss()));
					cellMissV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellJneL = row1.createCell((short) 2);
				cellJneL.setCellValue("JNE");
				cellJneL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellJneV = row1.createCell((short) i+3);
					cellJneV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getJne()));
					cellJneV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellTotalL = row1.createCell((short) 2);
				cellTotalL.setCellValue("TOTAL");
				cellTotalL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellTotalV = row1.createCell((short) i+3);
					cellTotalV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getTotal()));
					cellTotalV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
			} catch (FileNotFoundException x) {
				x.printStackTrace();
			} catch (IOException x) {
				x.printStackTrace();
			}
		}
	// ===
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
			
			HSSFCell cellKecamatan= row1.createCell((short) 9);
			cellKecamatan.setCellValue("KECAMATAN");
			
			HSSFCell cellService= row1.createCell((short) 10);
			cellService.setCellValue("SERVICE");
			
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
				
				HSSFCell cellX1 = row1.createCell((short) 9);
				cellX1.setCellValue(data.getKecamatan());
				
				HSSFCell cellZ1 = row1.createCell((short) 10);
				cellZ1.setCellValue(data.getService());
				
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
			FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + date  + " " + title + ".xls");
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
			MessageBox.alert("Export Berhasil Di Drive C:/DLL/REPORT/EXPORT/" + date  + " " + title + ".xls");
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		} catch (IOException x) {
			x.printStackTrace();
		}
	}
	
	public static void exportToExcellReportResi(List<EntryDataShowVO> en, String title, String date ) {
		try {
			FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/" + date  + " " + title + ".xls");
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
			cellAwb.setCellValue("PENGIRIM");
			
			HSSFCell cellCreated= row1.createCell((short) 5);
			cellCreated.setCellValue("RESI JNE");
			
			HSSFCell cellTujuan= row1.createCell((short) 6);
			cellTujuan.setCellValue("CREATED");
			
			HSSFCell cellPerwakilan= row1.createCell((short) 7);
			cellPerwakilan.setCellValue("PERWAKILAN");
			
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
				cellD1.setCellValue(data.getPengirim());
				
				HSSFCell cellE1 = row1.createCell((short) 5);
				cellE1.setCellValue(data.getResiJne());
				
				HSSFCell cellF1 = row1.createCell((short) 6);
				cellF1.setCellValue(data.getCreated());
				
				HSSFCell cellG1 = row1.createCell((short) 7);
				cellG1.setCellValue(data.getKdPerwakilan());
				
				cellF1.setCellStyle(cellStyle);
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			MessageBox.alert("Export Berhasil Di Drive C:/DLL/REPORT/EXPORT/" + date  + " " + title + ".xls");
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
				cellH1.setCellValue(Double.parseDouble(data.getBerat()));
				
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
			
			MessageBox.alert("File excel berhasil terbentuk di " + "C:/DLL/REPORT/EXPORT/" + date  + " " + title + ".xls");
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
				cellJ1.setCellValue(Integer.parseInt(data.getHarga()));
				
				
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
					cellF1.setCellValue(Integer.parseInt(data.getBfinal()));
					
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
					cellE1.setCellValue(Double.parseDouble(data.getSumBeratAsli()));
				
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
	
	// FA
	//FA
		public static void exportToReportKurir (ObservableList<LaporanKurirVO> masterDataHeader, ObservableList<LaporanKurirVO> masterDataDetail, 
				ObservableList<LaporanKurirVO> masterDataDetail2, ObservableList<LaporanKurirVO> masterDataFooter, String title) {
			try {
				FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/"+ title + ".xls");
					
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet worksheet = workbook.createSheet(title);
				XSSFRow row1 = null;
					
				CellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
				cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
				cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				cellStyle.setBorderRight(CellStyle.BORDER_THIN);
				cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
				cellStyle.setBorderTop(CellStyle.BORDER_THIN);
				cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
				
				int no=0;
				int countCol = 0;
				
				int startPersentase = 0;
				int persenSize = 1;
				
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellEmpty = row1.createCell((short) 0);
				cellEmpty.setCellValue("");
				cellEmpty.setCellStyle(cellStyle);
					
				XSSFCell cellNama = row1.createCell((short) 1);
				cellNama.setCellValue("Nama");
				cellNama.setCellStyle(cellStyle);
					
				XSSFCell cellEmpty2 = row1.createCell((short) 2);
				cellEmpty2.setCellValue("");
				cellEmpty2.setCellStyle(cellStyle);
					
					
				for (int i = 0; i < masterDataHeader.size(); i++ ) {
					XSSFCell cellTgl = row1.createCell((short) i+3);
					cellTgl.setCellValue(masterDataHeader.get(i).getDate());
					cellTgl.setCellStyle(cellStyle);
						
				}
					
				countCol = 2 + masterDataHeader.size();
					
				System.out.println("countCol : " + countCol);
					
				//Mulai test merged row border
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellNo = row1.createCell((short) 0);
				cellNo.setCellValue("No");
				cellNo.setCellStyle(cellStyle);
					
				worksheet.addMergedRegion(new CellRangeAddress(1, 4, 0, 0));
					
				XSSFCell cellTotal = row1.createCell((short) 1);
				cellTotal.setCellValue("Total");
				cellTotal.setCellStyle(cellStyle);
				worksheet.addMergedRegion(new CellRangeAddress(1, 4, 1, 1));
					
				XSSFCell cellKirim = row1.createCell((short) 2);
				cellKirim.setCellValue("Kirim");
				cellKirim.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataHeader.size(); i++ ) {
					XSSFCell cellTgl = row1.createCell((short) i+3);
					cellTgl.setCellValue(Integer.valueOf(masterDataHeader.get(i).getTotalDeliver()));
					cellTgl.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				//test
				XSSFCell cellNo2 = row1.createCell((short) 0);
				cellNo2.setCellStyle(cellStyle);
					
				//test
				XSSFCell cellTotal2 = row1.createCell((short) 1);
				cellTotal2.setCellStyle(cellStyle);
					
				XSSFCell cellTerima = row1.createCell((short) 2);
				cellTerima.setCellValue("Terima");
				cellTerima.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataHeader.size(); i++ ) {
					XSSFCell cellTgl = row1.createCell((short) i+3);
					cellTgl.setCellValue(Integer.valueOf(masterDataHeader.get(i).getTotalReceive()));
					cellTgl.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
					//test
				XSSFCell cellNo3 = row1.createCell((short) 0);
				cellNo3.setCellStyle(cellStyle);
					
					//test
				XSSFCell cellTotal3 = row1.createCell((short) 1);
				cellTotal3.setCellStyle(cellStyle);
					
				XSSFCell cellSisa = row1.createCell((short) 2);
				cellSisa.setCellValue("Sisa");
				cellSisa.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataHeader.size(); i++ ) {
					XSSFCell cellTgl = row1.createCell((short) i+3);
					cellTgl.setCellValue(Integer.valueOf(masterDataHeader.get(i).getTotalRemaining()));
					cellTgl.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
				
				//test
				XSSFCell cellNo4 = row1.createCell((short) 0);
				cellNo4.setCellStyle(cellStyle);
					
				//test
				XSSFCell cellTotal4 = row1.createCell((short) 1);
				cellTotal4.setCellStyle(cellStyle);
					
				XSSFCell cellPercentage = row1.createCell((short) 2);
				cellPercentage.setCellValue("Persentase");
				cellPercentage.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataHeader.size(); i++ ) {
					XSSFCell cellTgl = row1.createCell((short) i+3);
					cellTgl.setCellValue(Double.valueOf(masterDataHeader.get(i).getTotalPercentage()));
					cellTgl.setCellStyle(cellStyle);
				}
				
				startPersentase = countCol + 2;
				
				XSSFCell cellTitle = row1.createCell((short) startPersentase++);
				cellTitle.setCellValue("Persenstase keberhasilan pengiriman (di urutkan dr yg terbaik)");
				
				startPersentase = 0;
					
				row1 = worksheet.createRow((short) no++);
				
				int colNoStart = 5;
				int colNoEnd = 7;
					
				int colNameStart = 5;
				int colNameEnd = 6;
					
				int colLabel = 2;
				int colValue = 3;
					
				int colTotal = 1;
				
				int startRow = 6;
				int startCol = 5;
					
				int totalNo = 1;
				
				for (int i = 0; i < masterDataDetail.size(); i++ ) {
					if (i == 0) {
						XSSFCell cellCount = row1.createCell((short) 0);
						cellCount.setCellValue((i+1));
						cellCount.setCellStyle(cellStyle);
						worksheet.addMergedRegion(new CellRangeAddress(colNoStart, colNoEnd, 0, 0));
							
						XSSFCell cellName = row1.createCell((short) 1);
						cellName.setCellValue(masterDataDetail.get(i).getName());
						cellName.setCellStyle(cellStyle);
						worksheet.addMergedRegion(new CellRangeAddress(colNameStart, colNameEnd, 1, 1));
							
						XSSFCell cellKirimD = row1.createCell((short) colLabel);
						cellKirimD.setCellValue("Kirim");
						cellKirimD.setCellStyle(cellStyle);
							
						XSSFCell cellKirimV = row1.createCell((short) colValue);
						cellKirimV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalDeliver()));
						cellKirimV.setCellStyle(cellStyle);
							
						for (int j = 0; j < masterDataDetail.size(); j++) {
							if (j > 0 && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
								colValue++;
								XSSFCell cellKirimVL = row1.createCell((short) colValue);
								cellKirimVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalDeliver()));
								cellKirimVL.setCellStyle(cellStyle);
							}
						}
							
						for (int k = colValue; k <= countCol; k++) {
							if (k != colValue) {
								XSSFCell cellKirimVL = row1.createCell((short) k);
								cellKirimVL.setCellValue(0);
								cellKirimVL.setCellStyle(cellStyle);
							}
								
						}
						
						startPersentase = countCol + 2;
						
						XSSFCell cellNamaKurir = row1.createCell((short) startPersentase++);
						cellNamaKurir.setCellValue(masterDataDetail2.get(i).getName());
						
						XSSFCell cellKurirPersentase = row1.createCell((short) startPersentase++);
						cellKurirPersentase.setCellValue(masterDataDetail2.get(i).getTotalPercentage());
						
						colValue = 3;
						startPersentase = 0;
							
						row1 = worksheet.createRow((short) no++);
							
						//testL
						XSSFCell cellCount2 = row1.createCell((short) 0);
						cellCount2.setCellStyle(cellStyle);
							
						XSSFCell cellTerimaD = row1.createCell((short) colLabel);
						cellTerimaD.setCellValue("Terima");
						cellTerimaD.setCellStyle(cellStyle);
							
						XSSFCell cellTerimaV = row1.createCell((short) colValue);
						cellTerimaV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalReceive()));
						cellTerimaV.setCellStyle(cellStyle);
							
						for (int j = 0; j < masterDataDetail.size(); j++) {
							if (j > 0 && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
								colValue++;
								XSSFCell cellTerimaVL = row1.createCell((short) colValue);
								cellTerimaVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalReceive()));
								cellTerimaVL.setCellStyle(cellStyle);
							}
						}
							
						for (int k = colValue; k <= countCol; k++) {
							if (k != colValue) {
								XSSFCell cellKirimVL = row1.createCell((short) k);
								cellKirimVL.setCellValue(0);
								cellKirimVL.setCellStyle(cellStyle);
							}
								
						}
						
						colValue = 3;
							
						row1 = worksheet.createRow((short) no++);
							
						//testL
						XSSFCell cellCount3 = row1.createCell((short) 0);
						cellCount3.setCellStyle(cellStyle);
							
						XSSFCell cellSisaD = row1.createCell((short) colLabel);
						cellSisaD.setCellValue("Sisa");
						cellSisaD.setCellStyle(cellStyle);
							
						XSSFCell cellSisaV = row1.createCell((short) colValue);
						cellSisaV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalRemaining()));
						cellSisaV.setCellStyle(cellStyle);
							
						for (int j = 0; j < masterDataDetail.size(); j++) {
							if (j > 0 && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
								colValue++;
								XSSFCell cellSisaVL = row1.createCell((short) colValue);
								cellSisaVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalRemaining()));
								cellSisaVL.setCellStyle(cellStyle);
							}
						}
							
						for (int k = colValue; k <= countCol; k++) {
							if (k != colValue) {
								XSSFCell cellKirimVL = row1.createCell((short) k);
								cellKirimVL.setCellValue(0);
								cellKirimVL.setCellStyle(cellStyle);
							}
								
						}
						
						colValue = 3;
							
						XSSFCell cellTotalV = row1.createCell((short) colTotal);
						cellTotalV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getJumlahTerima()));
						cellTotalV.setCellStyle(cellStyle);
					}
						
					if (i > 0) {
						if (!masterDataDetail.get(i-1).getName().equals(masterDataDetail.get(i).getName())) {
							row1 = worksheet.createRow((short) no++);
							colNoStart += 3;
							colNoEnd += 3;
							colNameStart += 3;
							colNameEnd += 3; 
							totalNo++;
								
							XSSFCell cellCount = row1.createCell((short) 0);
							cellCount.setCellValue(totalNo);
							cellCount.setCellStyle(cellStyle);
							worksheet.addMergedRegion(new CellRangeAddress(colNoStart, colNoEnd, 0, 0));
								
							XSSFCell cellName = row1.createCell((short) 1);
							cellName.setCellValue(masterDataDetail.get(i).getName());
							cellName.setCellStyle(cellStyle);
							worksheet.addMergedRegion(new CellRangeAddress(colNameStart, colNameEnd, 1, 1));
								
							XSSFCell cellKirimD = row1.createCell((short) colLabel);
							cellKirimD.setCellValue("Kirim");
							cellKirimD.setCellStyle(cellStyle);
								
							XSSFCell cellKirimV = row1.createCell((short) colValue);
							cellKirimV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalDeliver()));
							cellKirimV.setCellStyle(cellStyle);
								
							for (int j = 0; j < masterDataDetail.size(); j++) {
								if (j > i && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
									colValue++;
									XSSFCell cellKirimVL = row1.createCell((short) colValue);
									cellKirimVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalDeliver()));
									cellKirimVL.setCellStyle(cellStyle);
								}
							}
							
							for (int k = colValue; k <= countCol; k++) {
								if (k != colValue) {
									XSSFCell cellKirimVL = row1.createCell((short) k);
									cellKirimVL.setCellValue(0);
									cellKirimVL.setCellStyle(cellStyle);
								}
									
							}
							
							startPersentase = countCol + 2;
							
							XSSFCell cellNamaKurir = row1.createCell((short) startPersentase++);
							cellNamaKurir.setCellValue(masterDataDetail2.get(persenSize).getName());

							XSSFCell cellKurirPersentase = row1.createCell((short) startPersentase++);
							cellKurirPersentase.setCellValue(masterDataDetail2.get(persenSize).getTotalPercentage());

							persenSize++;
								
							colValue = 3;
							startPersentase = 0;
								
							row1 = worksheet.createRow((short) no++);
								
							//testL
							XSSFCell cellCount2 = row1.createCell((short) 0);
							cellCount2.setCellStyle(cellStyle);
								
							XSSFCell cellTerimaD = row1.createCell((short) colLabel);
							cellTerimaD.setCellValue("Terima");
							cellTerimaD.setCellStyle(cellStyle);
								
							XSSFCell cellTerimaV = row1.createCell((short) colValue);
							cellTerimaV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalReceive()));
							cellTerimaV.setCellStyle(cellStyle);
								
							for (int j = 0; j < masterDataDetail.size(); j++) {
								if (j > i && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
									colValue++;
									XSSFCell cellTerimaVL = row1.createCell((short) colValue);
									cellTerimaVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalReceive()));
									cellTerimaVL.setCellStyle(cellStyle);
								}
							}
								
							for (int k = colValue; k <= countCol; k++) {
								if (k != colValue) {
									XSSFCell cellKirimVL = row1.createCell((short) k);
									cellKirimVL.setCellValue(0);
									cellKirimVL.setCellStyle(cellStyle);
								}
									
							}
							
							colValue = 3;
								
							row1 = worksheet.createRow((short) no++);
								
							//testL
							XSSFCell cellCount3 = row1.createCell((short) 0);
							cellCount3.setCellStyle(cellStyle);
								
							XSSFCell cellSisaD = row1.createCell((short) colLabel);
							cellSisaD.setCellValue("Sisa");
							cellSisaD.setCellStyle(cellStyle);
								
							XSSFCell cellSisaV = row1.createCell((short) colValue);
							cellSisaV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getTotalRemaining()));
							cellSisaV.setCellStyle(cellStyle);
								
							for (int j = 0; j < masterDataDetail.size(); j++) {
								if (j > i && masterDataDetail.get(i).getName().equals(masterDataDetail.get(j).getName())) {
									colValue++;
									XSSFCell cellSisaVL = row1.createCell((short) colValue);
									cellSisaVL.setCellValue(Integer.valueOf(masterDataDetail.get(j).getTotalRemaining()));
									cellSisaVL.setCellStyle(cellStyle);
								}
							}
								
							for (int k = colValue; k <= countCol; k++) {
								if (k != colValue) {
									XSSFCell cellKirimVL = row1.createCell((short) k);
									cellKirimVL.setCellValue(0);
									cellKirimVL.setCellStyle(cellStyle);
								}
									
							}
							
							colValue = 3;
								
							XSSFCell cellTotalV = row1.createCell((short) colTotal);
							cellTotalV.setCellValue(Integer.valueOf(masterDataDetail.get(i).getJumlahTerima()));
							cellTotalV.setCellStyle(cellStyle);
						}
					}
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellAuL = row1.createCell((short) 2);
				cellAuL.setCellValue("AU");
				cellAuL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellAuV = row1.createCell((short) i+3);
					cellAuV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getAu()));
					cellAuV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
				
				XSSFCell cellBaL = row1.createCell((short) 2);
				cellBaL.setCellValue("BA");
				cellBaL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellBaV = row1.createCell((short) i+3);
					cellBaV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getBa()));
					cellBaV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellCodaL = row1.createCell((short) 2);
				cellCodaL.setCellValue("CODA");
				cellCodaL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellCodaV = row1.createCell((short) i+3);
					cellCodaV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getCoda()));
					cellCodaV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellNthL = row1.createCell((short) 2);
				cellNthL.setCellValue("NTH");
				cellNthL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellCodaV = row1.createCell((short) i+3);
					cellCodaV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getNth()));
					cellCodaV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellCneeL = row1.createCell((short) 2);
				cellCneeL.setCellValue("CNEE U");
				cellCneeL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellCneeV = row1.createCell((short) i+3);
					cellCneeV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getCnee()));
					cellCneeV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellMissL = row1.createCell((short) 2);
				cellMissL.setCellValue("MISS ROUTE");
				cellMissL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellMissV = row1.createCell((short) i+3);
					cellMissV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getMiss()));
					cellMissV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellJneL = row1.createCell((short) 2);
				cellJneL.setCellValue("JNE");
				cellJneL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellJneV = row1.createCell((short) i+3);
					cellJneV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getJne()));
					cellJneV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				XSSFCell cellTotalL = row1.createCell((short) 2);
				cellTotalL.setCellValue("TOTAL");
				cellTotalL.setCellStyle(cellStyle);
					
				for (int i = 0; i < masterDataFooter.size(); i++ ) {
					XSSFCell cellTotalV = row1.createCell((short) i+3);
					cellTotalV.setCellValue(Integer.valueOf(masterDataFooter.get(i).getTotal()));
					cellTotalV.setCellStyle(cellStyle);
				}
					
				row1 = worksheet.createRow((short) no++);
					
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
			} catch (FileNotFoundException x) {
				x.printStackTrace();
			} catch (IOException x) {
				x.printStackTrace();
			}
		}

		public static void exportMasterHarga(List<MasterHargaController.MasterHargaTV> masterData) {
			try {
				FileOutputStream fileOut = new FileOutputStream("C:/DLL/REPORT/EXPORT/MASTER_HARGA.xls");
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet worksheet = workbook.createSheet("MASTER_HARGA");
				HSSFRow row1 = null;
				
				int no=0;
				row1 = worksheet.createRow((short) no++);
				
				HSSFCell cellKodeZona = row1.createCell((short) 0);
				cellKodeZona.setCellValue("KODE_ZONA");
				
				HSSFCell cellPropinsi = row1.createCell((short) 1);
				cellPropinsi.setCellValue("PROPINSI");

				HSSFCell cellKodeAsal= row1.createCell((short) 2);
				cellKodeAsal.setCellValue("KODE_ASAL");
				
				HSSFCell cellKabupaten= row1.createCell((short) 3);
				cellKabupaten.setCellValue("KABUPATEN");
				
				HSSFCell cellKecamatan= row1.createCell((short) 4);
				cellKecamatan.setCellValue("KECAMATAN");
				
				HSSFCell cellKodePerwakilan= row1.createCell((short) 5);
				cellKodePerwakilan.setCellValue("KODE_PERWAKILAN");
				
				HSSFCell cellZona= row1.createCell((short) 6);
				cellZona.setCellValue("ZONA");
				
				HSSFCell cellReg= row1.createCell((short) 7);
				cellReg.setCellValue("REG");
				
				HSSFCell cellEtd= row1.createCell((short) 8);
				cellEtd.setCellValue("ETD");
				
				HSSFCell cellOne= row1.createCell((short) 9);
				cellOne.setCellValue("ONE");
				
				HSSFCell cellRegPerwakilan= row1.createCell((short) 10);
				cellRegPerwakilan.setCellValue("REGPERWAKILAN");
				
				HSSFCell cellOnePerwakilan= row1.createCell((short) 11);
				cellOnePerwakilan.setCellValue("ONEPERWAKILAN");
			
				int noCol=1;
				
				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
				cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
				
				for (MasterHargaController.MasterHargaTV data : masterData) {
				
					row1 = worksheet.createRow((short) no++);
					
					HSSFCell cellA1 = row1.createCell((short) 0);
					cellA1.setCellValue(data.getKodeZona());
					
					HSSFCell cellB1 = row1.createCell((short) 1);
					cellB1.setCellValue(data.getPropinsi());
					
					HSSFCell cellC1 = row1.createCell((short) 2);
					cellC1.setCellValue(data.getKodeAsal());
					
					HSSFCell cellD1 = row1.createCell((short) 3);
					cellD1.setCellValue(data.getKabupaten());
					
					HSSFCell cellE1 = row1.createCell((short) 4);
					cellE1.setCellValue(data.getKecamatan());
				
					HSSFCell cellF1 = row1.createCell((short) 5);
					cellF1.setCellValue(data.getPerwakilan());
				
					String zona = data.getZona();
					HSSFCell cellG1 = row1.createCell((short) 6);
					cellG1.setCellValue(zona==null?"-":zona);
					
					HSSFCell cellH1 = row1.createCell((short) 7);
					cellH1.setCellValue(Integer.parseInt(data.getReg()));
					
					HSSFCell cellI1 = row1.createCell((short) 8);
					cellI1.setCellValue(data.getEtd());
					
					HSSFCell cellJ1 = row1.createCell((short) 9);
					cellJ1.setCellValue(Integer.parseInt(data.getOne()));
					
					HSSFCell cellK1 = row1.createCell((short) 10);
					cellK1.setCellValue(data.getRegPerwakilan());
					
					HSSFCell cellL1 = row1.createCell((short) 11);
					cellL1.setCellValue(data.getOnePerwakilan());
				
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

		public static void exportToExcelEmailTagihan(List<TagihanVO> masterData, String path2) {
			try {
				FileOutputStream fileOut = new FileOutputStream(path2);
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet worksheet = workbook.createSheet("LIST TAGIHAN");
				HSSFRow row1 = null;
				
				int no=0;
				row1 = worksheet.createRow((short) no++);
				
				HSSFCell cellKodeZona = row1.createCell((short) 0);
				cellKodeZona.setCellValue("NOMOR RESI");
				
				HSSFCell cellPropinsi = row1.createCell((short) 1);
				cellPropinsi.setCellValue("TUJUAN");

				HSSFCell cellKodeAsal= row1.createCell((short) 2);
				cellKodeAsal.setCellValue("PENERIMA");
				
				HSSFCell cellKabupaten= row1.createCell((short) 3);
				cellKabupaten.setCellValue("HP PENERIMA");
				
				HSSFCell cellKecamatan= row1.createCell((short) 4);
				cellKecamatan.setCellValue("RESI JNE");
				
				HSSFCell cellKodePerwakilan= row1.createCell((short) 5);
				cellKodePerwakilan.setCellValue("BERAT");
				
				HSSFCell cellZona= row1.createCell((short) 6);
				cellZona.setCellValue("BIAYA");
				
				HSSFCell cellReg= row1.createCell((short) 7);
				cellReg.setCellValue("ASURANSI");
				
				HSSFCell cellEtd= row1.createCell((short) 8);
				cellEtd.setCellValue("DISKON");
				
				HSSFCell cellOne= row1.createCell((short) 9);
				cellOne.setCellValue("TOTAL");
			
				int noCol=1;
				
				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);	
				cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
				
				for (TagihanVO data : masterData) {
				
					row1 = worksheet.createRow((short) no++);
					
					HSSFCell cellA1 = row1.createCell((short) 0);
					cellA1.setCellValue(data.getAwb());
					
					HSSFCell cellB1 = row1.createCell((short) 1);
					cellB1.setCellValue(data.getTujuan());
					
					HSSFCell cellC1 = row1.createCell((short) 2);
					cellC1.setCellValue(data.getPenerima());
					
					HSSFCell cellD1 = row1.createCell((short) 3);
					cellD1.setCellValue(data.getTelpPenerima());
					
					HSSFCell cellF1 = row1.createCell((short) 4);
					cellF1.setCellValue(data.getResiJNE());
				
					HSSFCell cellG1 = row1.createCell((short) 5);
					cellG1.setCellValue(Integer.parseInt(data.getPbFinal()));
					
					HSSFCell cellH1 = row1.createCell((short) 6);
					cellH1.setCellValue(data.getHarga());
					
					HSSFCell cellI1 = row1.createCell((short) 7);
					cellI1.setCellValue(data.getAsuransi());
					
					HSSFCell cellJ1 = row1.createCell((short) 8);
					cellJ1.setCellValue(data.getDiskon());
					
					HSSFCell cellK1 = row1.createCell((short) 9);
					cellK1.setCellValue(Double.parseDouble(data.getTotalBiaya().toString()));
				
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