package util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;
import java.sql.Connection;
import java.sql.SQLException;

public class ExportJasper {

	public static void JasperExportExcell(Map<String, Object> parameters, String lokasiJasper, String LokasiFile, String output, String date) throws JRException,
    ClassNotFoundException, SQLException {
        JasperReport jasperReport =    JasperCompileManager.compileReport(lokasiJasper);
        Connection conn = MysqlConnect.getConnection();
 
        JasperPrint print = JasperFillManager.fillReport(jasperReport,
                parameters, conn);
        
        // Make sure the output directory exists.
        File outDir = new File(LokasiFile);
        outDir.mkdirs();
        
        //xls exporter
        JRXlsExporter exporter = new JRXlsExporter();
        ExporterInput exporterInput = new SimpleExporterInput(print);
 
        
        // ExporterInput
        exporter.setExporterInput(exporterInput);
       
 
        // ExporterOutput
        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
        		LokasiFile+date+" "+output+".xls");
       
        // Output
        exporter.setExporterOutput(exporterOutput);
       
        
        SimpleXlsExporterConfiguration configuration = new SimpleXlsExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        
        
	}
	
	public static void JasperExportPdf(Map<String, Object> parameters, String lokasiJasper, String LokasiFile, String output, String date) throws JRException,
    ClassNotFoundException, SQLException {
        JasperReport jasperReport =    JasperCompileManager.compileReport(lokasiJasper);
        Connection conn = MysqlConnect.getConnection();
 
        JasperPrint print = JasperFillManager.fillReport(jasperReport,
                parameters, conn);
        
        // Make sure the output directory exists.
        File outDir = new File(LokasiFile);
        outDir.mkdirs();
        
        // PDF Exportor.
        JRPdfExporter exporterr = new JRPdfExporter();
        ExporterInput exporterrInput = new SimpleExporterInput(print);
        
        exporterr.setExporterInput(exporterrInput);
        
        OutputStreamExporterOutput exporterrOutput = new SimpleOutputStreamExporterOutput(
        		LokasiFile+date+" "+output+".pdf");
        
        exporterr.setExporterOutput(exporterrOutput);
        
        SimplePdfExporterConfiguration configurationn = new SimplePdfExporterConfiguration();
        exporterr.setConfiguration(configurationn);
        exporterr.exportReport();
	}
}
