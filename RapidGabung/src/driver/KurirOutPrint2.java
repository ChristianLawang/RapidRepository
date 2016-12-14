package driver;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.Barcode128;

import VO.EntryDataShowVO;
import service.GabungPaketService;
import service.KurirOutService;
import util.DateUtil;

public class KurirOutPrint2 implements Printable {
	private Date now =  DateUtil.getNow();
	private String awb, beartBulat, berat;
	private static List<EntryDataShowVO> dataPrint = new ArrayList<EntryDataShowVO>();
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		
		Barcode128 barcode = new Barcode128();
		barcode.setX(0.75f);
		barcode.setN(1.5f);
		barcode.setChecksumText(true);
		barcode.setGenerateChecksum(true);
		barcode.setSize(10f);
		barcode.setTextAlignment(Element.ALIGN_CENTER);
		barcode.setBaseline(10f);
		barcode.setCode(awb);
		barcode.setBarHeight(15f);

		Image imgBarcode = barcode.createAwtImage(Color.black, Color.white);

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = null;
		try {
			Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			// Now with zxing version 3.2.1 you could change border size (white
			// border size to just 1)
			hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

			byteMatrix = qrCodeWriter.encode(awb, BarcodeFormat.QR_CODE, 150, 150, hintMap);
		} catch (WriterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int crunchifyWidth = byteMatrix.getWidth();
		BufferedImage buffQRCode = new BufferedImage(crunchifyWidth, crunchifyWidth, BufferedImage.TYPE_INT_RGB);
		buffQRCode.createGraphics();
		Graphics2D d = (Graphics2D) buffQRCode.getGraphics();

		d.setColor(Color.white);
		d.fillRect(0, 0, crunchifyWidth, crunchifyWidth);
		d.setColor(Color.black);

		for (int i = 0; i < crunchifyWidth; i++) {
			for (int j = 0; j < crunchifyWidth; j++) {
				if (byteMatrix.get(i, j)) {
					d.fillRect(i, j, 1, 1);
				}
			}
		}

		d.dispose();

		// tulisan rapid x=15 y=30
		g.setFont(new Font("Tahoma", Font.BOLD, 16));
		g.drawString("RAPID", 20, 15);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		
		// barcode atas kanan
		g.setFont(new Font("Tahoma", Font.PLAIN, 16));
		g.drawImage(imgBarcode, 20, 30, null);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 7));
		g.drawString(awb.replace("", " ").trim(), 48, 55);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("No Resi", 20, 110);
		g.drawString(": " +awb , 80, 110); // Nomor Resi
		g.drawString("Berat", 20, 120);
		g.drawString(": " + beartBulat + " Kg " + "(" + berat + ")", 80, 120);
		
		g.drawString("Nama Penerima : .......................................", 20, 140);											
		g.drawString("(..............................................)", 40, 230);										
		
		g.drawString("Tanda Tangan", 78, 245);
		g.drawString("Paket diterima dalam keadaan baik", 40, 255);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 7));
		g.drawString(DateUtil.dateToStdDateLiteral(new Date()) + " " + DateUtil.getStdTimeDisplay(new Date()) + ", "
				+ "cgk " + ", DKI Jakarta", 40, 270);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 16));
		g.drawImage(imgBarcode, 20, 275, null);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 7));
		g.drawString(awb.replace("", " ").trim(), 55, 300);
		
		g.setFont(new Font("Tahoma", Font.BOLD, 7));
		g.drawString("Cust. Service : customercare@rapid.co.id", 30, 310);
		g.setFont(new Font("Tahoma", Font.BOLD, 7));
		g.drawString("Jl. Tanah Abang V No F3, Jakarta Pusat", 30, 320);
		
		g.drawImage(buffQRCode, 150, 50, 50, 50, null);
//		g.drawImage(buffQRCode, 235, 18, 80, 70, null);
		return PAGE_EXISTS;
	}

	public void printConfig(String awb, String beartBulat, String berat) {
		this.awb = awb;
		this.beartBulat = beartBulat;
		this.berat = berat;
		PrinterJob job = PrinterJob.getPrinterJob();
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService printer : printServices) {
			System.out.println("Printer: " + printer.getName());
			if (printer.getName().equals("EPSON TM-T82 Receipt")) {
				try {
					job.setPrintService(printer);
				} catch (PrinterException ex) {
				}
			}
		}
		Paper paper = new Paper();
	    paper.setImageableArea(0, 0,700,10000);
	    paper.setSize(700,10000);

	    PageFormat format = new PageFormat();
	    format.setPaper(paper);
	    format.setOrientation(PageFormat.PORTRAIT);

	    job.setPrintable(this, format);
		
		try {
			job.print();
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}

//	public static void main(String args[]) {
//		KurirOutPrint2 print = new KurirOutPrint2();
//		print.printConfig("1604-310516-02", "12", "12");
//	}
}