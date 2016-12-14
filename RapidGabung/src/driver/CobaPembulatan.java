package driver;

import util.PerhitunganBiaya;

public class CobaPembulatan {

	public static void main(String[] args) {
		PerhitunganBiaya pb = new PerhitunganBiaya();
		System.out.println("--> getFixDecimal2Digit " + pb.getFixDecimal2Digit("0.23523535"));
	}

}
