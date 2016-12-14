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
import java.util.Date;
import java.util.EnumMap;
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

import util.DateUtil;

public class GabungPaketBarcode implements Printable {

	private String kodeGabung;
	private String timbang;
	private String timbangBulat;
	private String user;
	private int Koli;

	public GabungPaketBarcode(String noGabung, String timbang, String timbangBulat, String user, int Koli) {
		this.kodeGabung = noGabung;
		this.timbang = timbang;
		this.timbangBulat = timbangBulat;
		this.user = user;
		this.Koli = Koli;
	}

	@Override
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		Barcode128 barcode = new Barcode128();
		// String data = "CGK0011600000001";
		//
		barcode.setX(0.75f);
		barcode.setN(1.5f);
		barcode.setChecksumText(true);
		barcode.setGenerateChecksum(true);
		barcode.setSize(10f);
		barcode.setTextAlignment(Element.ALIGN_CENTER);
		barcode.setBaseline(10f);
		barcode.setCode(this.kodeGabung);
		barcode.setBarHeight(15f);
		//
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

			byteMatrix = qrCodeWriter.encode(this.kodeGabung, BarcodeFormat.QR_CODE, 150, 150, hintMap);
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

		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());

		// tulisan rapid x=15 y=30
		g.setFont(new Font("Tahoma", Font.BOLD, 19));
		g.drawString("KMS", 30, 35);

		// barcode atas kanan
		g.setFont(new Font("Tahoma", Font.PLAIN, 16));
		g.drawImage(imgBarcode, 20, 2, null);

		g.setFont(new Font("Tahoma", Font.PLAIN, 7));
		g.drawString(this.kodeGabung.replace("", " ").trim(), 115, 25); // txt
																		// CGK

		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("No Kardus", 30, 60);
		g.drawString(": " + this.kodeGabung, 80, 60); // Nomor Resi
		g.drawString("Berat", 30, 70);
		g.drawString(": " + this.timbangBulat + " Kg " + "(" + this.timbang + ")", 80, 70);
		g.drawString("Koli", 30, 80);
		g.drawString(": " + this.Koli, 80, 80);

		g.setFont(new Font("Tahoma", Font.PLAIN, 7));
		g.drawString(DateUtil.dateToStdDateLiteral(new Date()) + " " + DateUtil.getStdTimeDisplay(new Date()) + ", "
				+ this.user + ", Jakarta", 70, 95); // Tanggal dan User
		g.drawImage(imgBarcode, 20, 100, null);

		g.setFont(new Font("Tahoma", Font.PLAIN, 7));
		g.drawString(this.kodeGabung.replace("", " ").trim(), 70, 125); // Barcode
																		// 2

		g.drawImage(buffQRCode, 200, 35, 50, 50, null);
		return PAGE_EXISTS;

	}

	private void printConfig() {
		// this.noKardus = noKardus;

		PrinterJob job = PrinterJob.getPrinterJob();
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService printer : printServices) {
			System.out.println("Printer: " + printer.getName());
			if (printer.getName().equals("TSC TTP-244 Pro")) {
				try {
					job.setPrintService(printer);
				} catch (PrinterException ex) {
					
				}
			}
		}
		
		Paper paper = new Paper();
		paper.setImageableArea(0, 0, 500, 125);
		paper.setSize(500, 125);
		
		PageFormat format = new PageFormat();
		format.setPaper(paper);
		format.setOrientation(PageFormat.PORTRAIT);

		job.setPrintable(this, format);

		job.setPrintable(this);
		try {
			job.print();
		} catch (PrinterException e) {
			e.printStackTrace();
		}

	}

	public void print() {
		this.printConfig();
	}

//	public static void main(String[] args) {
//		GabungPaketBarcode gab = new GabungPaketBarcode("BDO001-010616CGK001", "5", "5", "Rivan", 1);
//		gab.print();
//	}
	
}
