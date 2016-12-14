package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.TrPelanggan;
import entity.TtDataEntry;
import entity.TtGabungPaket;
import entity.TtHeader;
import entity.TtPotoTimbang;
import service.DataPaketService;
import service.DataPotoService;
import service.GabungPaketService;
import service.PelangganService;
import service.PotoPaketService;

public class ImportExportUtil {
	public void exportResiNonJNEForCabang(String path, String cabang, Date dtPeriode){
		
		List<TtHeader> header = DataPotoService.getDataPotoForCabang(dtPeriode);
		List<TtPotoTimbang> potoTimbang = PotoPaketService.getPotoPaketForCabang(dtPeriode);
		List<TtDataEntry> dataEntry = DataPaketService.getDataPaketForCabang(dtPeriode);
		List<TtGabungPaket> dataGabung = GabungPaketService.getGabungPaketForCabang(dtPeriode);
		List<TrPelanggan> dataPelanggan = PelangganService.getPelangganForCabang(dtPeriode);
		
		Map<String, Object> forCabang = new HashMap<String, Object>();
		
		forCabang.put("header", header);
		forCabang.put("potoTimbang", potoTimbang);
		forCabang.put("dataEntry", dataEntry);
		forCabang.put("gabungPaket", dataGabung);
		forCabang.put("dataPelanggan", dataPelanggan);
		
		File file = new File(path);
		try {
			FileOutputStream f = new FileOutputStream(file);
	        ObjectOutputStream s = new ObjectOutputStream(f);
	        s.writeObject(forCabang);
		    s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

	public void importResiNonJNEForCabang(Map<String, Object> imp) {
		List<TtHeader> header = (List<TtHeader>) imp.get("header");
		List<TtPotoTimbang> potoTimbang = (List<TtPotoTimbang>) imp.get("potoTimbang");
		List<TtDataEntry> dataEntry = (List<TtDataEntry>) imp.get("dataEntry");
		List<TtGabungPaket> dataGabung = (List<TtGabungPaket>) imp.get("gabungPaket");
		List<TrPelanggan> dataPelanggan = (List<TrPelanggan>) imp.get("dataPelanggan");
		
		DataPotoService.saveAll(header);
		PotoPaketService.saveAll(potoTimbang);
		DataPaketService.saveAll(dataEntry);
		GabungPaketService.saveAll(dataGabung);
		PelangganService.saveAll(dataPelanggan);
		
		
	}
}
