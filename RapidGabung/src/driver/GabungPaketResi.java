package driver;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import VO.EntryDataShowVO;
import service.GabungPaketService;
import util.BigDecimalUtil;
import util.DateUtil;
import util.PerhitunganBiaya;

public class GabungPaketResi implements Printable {

	private String noKardus, user;
	public static List<EntryDataShowVO> dataPrint = new ArrayList<EntryDataShowVO>();
	private Date now = DateUtil.getNow();

	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		int width = 264;
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		g.setFont(new Font("Tahoma", Font.BOLD, 16));
		g.drawString("RAPID", 10, 20);
		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("No Kardus ", 10, 35);
		g.drawString(": " + noKardus, 75, 35);
		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("Tanggal ", 10, 45);
		g.drawString(": " + DateUtil.dateToStdStampLiteral(now), 75, 45);
		g.drawLine(10, 50, width - 6, 50);
		g.drawString("No Resi", 10, 63);
		g.drawString("Berat", 100, 63);
		g.drawString("Penerima", 130, 63);
		g.drawLine(10, 68, width - 6, 68);
		int heightLooping = 68;
		Double sum = 0.00;
		int sumBulat = 0;
		for (EntryDataShowVO en : dataPrint) {
			sum += Double.valueOf(en.getbFinal());
			sumBulat += Integer.parseInt(en.getBpFinal());
			g.setFont(new Font("Tahoma", Font.PLAIN, 8));
			g.drawString(en.getAwbData(), 10, heightLooping + 12);
			g.setFont(new Font("Tahoma", Font.PLAIN, 8));
			g.drawString(en.getbFinal(), 100, heightLooping + 12);
			g.setFont(new Font("Tahoma", Font.PLAIN, 8));
			g.drawString(en.getPenerima(), 130, heightLooping + 12);
			heightLooping = heightLooping + 12;
		}
		g.drawLine(10, heightLooping + 6, width - 6, heightLooping + 6);
		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("Total Koli : ", 10, heightLooping + 22);
		g.drawString(String.valueOf(dataPrint.size()), 100, heightLooping + 24);
		g.drawString("Total Berat : ", 10, heightLooping + 34);
		g.drawString(BigDecimalUtil.truncateDecimal(sum, 2).toString() + " (" +sumBulat + "Kg)", 100, heightLooping + 34);
		g.drawLine(10, heightLooping + 40, width - 6, heightLooping + 40);
		g.drawString("Oleh : ", 10, heightLooping + 51);
		g.drawString(user + ", DKI JAKARTA ", 50, heightLooping + 51);
		g.drawString("(................................................)", 10, heightLooping + 140);
		return PAGE_EXISTS;
	}

	public void printConfig(String noKardus, String user) {
		this.noKardus = noKardus;
		this.user = user;
		dataPrint.clear();
		dataPrint = GabungPaketService.getDataGabPaketPrint(noKardus);
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
		paper.setImageableArea(0, 0, 700, 10000);
		paper.setSize(700, 10000);

		PageFormat format = new PageFormat();
		format.setPaper(paper);
		format.setOrientation(PageFormat.PORTRAIT);

		job.setPrintable(this, format);

		try {
			for (int p = 1; p <= 2; p++) {
				job.print();
			}
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}

//	 public static void main(String args[]) {
//	 GabungPaketResi res = new GabungPaketResi();
//	 res.printConfig("BDO001-110616CGK001", "Rivan");
//	 }
}