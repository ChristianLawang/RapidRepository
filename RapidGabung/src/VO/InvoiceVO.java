package VO;

import java.util.Date;

public class InvoiceVO {
	private Integer id, bankId;
	private String kdPickup;
	private Date tglBayar;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getKdPickup() {
		return kdPickup;
	}

	public void setKdPickup(String kdPickup) {
		this.kdPickup = kdPickup;
	}

	public Date getTglBayar() {
		return tglBayar;
	}

	public void setTglBayar(Date tglBayar) {
		this.tglBayar = tglBayar;
	}

}
