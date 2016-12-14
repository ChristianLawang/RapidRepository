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

import VO.KurirInVO;
import service.GabungPaketService;
import service.KurirInService;
import service.KurirOutService;
import util.BigDecimalUtil;
import util.DateUtil;

public class KurirInPrint implements Printable {
	private Date tglInput;
	String user;
	private String nmKurir;

	public static List<KurirInVO> dataPrint = new ArrayList<KurirInVO>();
	private Date now = DateUtil.getNow();

	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		for (KurirInVO en : dataPrint) {
			nmKurir = en.getIdKurir();
			tglInput = en.getUpdated();
		}
		int width = 264;
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		g.setFont(new Font("Tahoma", Font.BOLD, 16));
		g.drawString("RAPID", 10, 15);
		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("Kurir ", 10, 35);
		g.drawString(": " + nmKurir, 95, 35);
		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("Tanggal Input ", 10, 45);
		g.drawString(": " + DateUtil.dateToStdStampLiteral(tglInput), 95, 45);
		g.drawLine(10, 60, width - 6, 60);
		g.drawString("No Resi", 10, 73);
		g.drawString("Berat", 100, 73);
		g.drawString("Penerima", 130, 73);
		g.drawLine(10, 78, width - 6, 78);

		int heightLooping = 68;
		Double sum = 0.00;
		for (KurirInVO en : dataPrint) {
			sum += Double.valueOf(en.getBfinal());
			g.setFont(new Font("Tahoma", Font.PLAIN, 8));
			g.drawString(en.getIdBarang(), 10, heightLooping + 22);
			g.setFont(new Font("Tahoma", Font.PLAIN, 8));
			g.drawString(en.getBfinal(), 100, heightLooping + 22);
			g.setFont(new Font("Tahoma", Font.PLAIN, 8));
			g.drawString(en.getPenerima(), 130, heightLooping + 22);
			heightLooping = heightLooping + 18;
		}
		g.drawLine(10, heightLooping + 12, width - 6, heightLooping + 12);
		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("Total Koli : ", 10, heightLooping + 22);
		g.drawString(String.valueOf(dataPrint.size()), 95, heightLooping + 24);
		g.drawString("Total Berat : ", 10, heightLooping + 34);
		g.drawString(BigDecimalUtil.truncateDecimal(sum, 2).toString(), 95, heightLooping + 34);
		g.drawLine(10, heightLooping + 40, width - 6, heightLooping + 40);
		g.drawString(DateUtil.dateToStdStampLiteral(now), 10, heightLooping + 51);
		g.drawString("Oleh : ", 10, heightLooping + 61);
		g.drawString(user, 50, heightLooping + 61);
		g.drawString("(................................................)", 10, heightLooping + 160);
		return PAGE_EXISTS;
	}

	public void printConfig(String noKardus, String user) {
		this.user = user;
		dataPrint = KurirInService.getListPrintKurIn(noKardus);
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
//	 KurirInPrint print = new KurirInPrint();
//	 print.printConfig("160073-130616-01", "k001");
//	 }
}
