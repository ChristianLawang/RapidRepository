package driver;

import java.util.List;

import entity.TtGabungSementara;
import service.GabungPaketService;

public class TestGabungPaket {

	public static void main(String[] args) {
		GabungPaketService gb = new GabungPaketService();
		List<TtGabungSementara> list = gb.getDataGabungPaketSession("BDO001-240616CGK001");
		for (TtGabungSementara t : list) {
			System.out.println(t.getId());
			System.out.println(t.getTglCreate());
			System.out.println(t.getAwb());
		}
		
	}

}
