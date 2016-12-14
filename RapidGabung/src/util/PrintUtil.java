package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.Barcode128;

import VO.TimbangVO;

public class PrintUtil {
	public void printBarcode(BufferedImage image){
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(new Printable() {
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				if (pageIndex != 0) {
					return NO_SUCH_PAGE;
				}
				graphics.drawImage(image, 100, 100, image.getWidth(), image.getHeight(), null);
				return PAGE_EXISTS;
			}
		});
		try {
			printJob.print();
		} catch (PrinterException e1) {
			e1.printStackTrace();
		}

	}
	
	public BufferedImage getBarcodeImage(TimbangVO timbang, String data, String user){
		Barcode128 barcode = new Barcode128();
		barcode.setX(0.75f);
		barcode.setN(1.5f);
		barcode.setChecksumText(true);
		barcode.setGenerateChecksum(true);
		barcode.setSize(10f);
		barcode.setTextAlignment(Element.ALIGN_CENTER);
		barcode.setBaseline(10f);
		barcode.setCode(data);
		barcode.setBarHeight(15f);

		Image imgBarcode = barcode.createAwtImage(Color.black, Color.white);
		

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = null;
		
		try {
			Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			
			// Now with zxing version 3.2.1 you could change border size (white border size to just 1)
			hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	
			byteMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 150, 150, hintMap);
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
		
		//Ukuran Label Rapid 10 x 5 cm
		int width = 264;  //377= 10 cm
		int height = 147; //377= 10 cm

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = bufferedImage.createGraphics();

		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.black);
		g.setFont(new Font("Tahoma", Font.BOLD, 16));
		g.drawString("Rapid", 11, 25);
		
		g.drawLine(9, 40, width-6, 40);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("No Resi",10, 55); g.drawString(": " + data, 70, 55); //Nomor Resi
		g.drawString("Berat",10, 65); g.drawString(": " + timbang.getGrossRoundUp() +" Kg " + "(" + timbang.getGross() + ")", 70, 65);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 9));
		g.drawString(DateUtil.dateToStdDateLiteral(new Date())+ " " + DateUtil.getStdTimeDisplay(new Date()) + ", ANDI, Jakarta", 10, 85);
		g.drawImage(imgBarcode, 10, 87, null);
		g.drawString(data.replace("", " ").trim(), 20, 110); //txt CGK
		g.drawImage(imgBarcode, 80, 10 , null);
		g.setFont(new Font("Tahoma", Font.PLAIN, 9));
		g.drawString(data.replace("", " ").trim(), 100, 35);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("Cust. Service Phone. 021-12345678 Email. cs@rapid.com", 10, 130);
		g.drawString("Jl. Slipi raya no. 5 Kecamatan Kelurahan Jakarta Barat", 10, 140);
		
		g.drawImage(buffQRCode, 210, 50, 50, 50, null);
		
		g.dispose();

		File file = new File("z:/barcode.png");
		try {
			ImageIO.write(bufferedImage, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bufferedImage;
	}
}
