package VO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class LapPerKecamatanVO {
	private String idDataPaket, nmSales, awbData, kdPickup, pengirim, penerima, tujuan, ketTujuan, kdPerwakilan, ket,
			volume, bVolume, bpVolume, timbang, btTimbang, bFinal, bpFinal, resseler, asalPaket, user, gambar, noTlpn,
			layanan, kecamatan, kabupaten, porvinsi, resiJne, kdPelanggan, penerimaPaket, bank, statusLaporan,
			pelanggaranKurir, notes, status, noLaporan2;
	private int cgkreg, harga, totalasuransi, asuransi, totalBiaya, diskonRapid, biaya, diskon, diskonJne,
			biayaSblmDiskon, diskonPelanggan, inboundFlag, flagLunas, id, noLaporan;

	private String userPoto, userEntry, userManifest, idKardusManifest, userTerima, idKardusTerima, userGabung,
			userPerwakilan, nmKurir, masalah, email;

	private Date tglPoto, tglEntry, tglManifest, tglTerima, tglGabung, tglPerwakilan, tglKirim;

	private BigInteger jmlhPaket, sudahReport, blmReport, jmlhMasalah, stockCabang;

	private java.sql.Timestamp tglTerimaPaket;

	private Date tglCreated, tglBayar, tglSelesai, tglUpdated;

	public String getNoLaporan2() {
		return noLaporan2;
	}

	public void setNoLaporan2(String noLaporan2) {
		this.noLaporan2 = noLaporan2;
	}

	public int getNoLaporan() {
		return noLaporan;
	}

	public void setNoLaporan(int nolaporan) {
		this.noLaporan = nolaporan;
	}

	public Date getTglUpdated() {
		return tglUpdated;
	}

	public void setTglUpdated(Date tglUpdated) {
		this.tglUpdated = tglUpdated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPelanggaranKurir() {
		return pelanggaranKurir;
	}

	public void setPelanggaranKurir(String pelanggaranKurir) {
		this.pelanggaranKurir = pelanggaranKurir;
	}

	public Date getTglSelesai() {
		return tglSelesai;
	}

	public void setTglSelesai(Date tglSelesai) {
		this.tglSelesai = tglSelesai;
	}

	public String getStatusLaporan() {
		return statusLaporan;
	}

	public void setStatusLaporan(String statusLaporan) {
		this.statusLaporan = statusLaporan;
	}

	public int getFlagLunas() {
		return flagLunas;
	}

	public void setFlagLunas(int flagLunas) {
		this.flagLunas = flagLunas;
	}

	public Date getTglBayar() {
		return tglBayar;
	}

	public void setTglBayar(Date tglBayar) {
		this.tglBayar = tglBayar;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public Date getTglCreated() {
		return tglCreated;
	}

	public void setTglCreated(Date tglCreated) {
		this.tglCreated = tglCreated;
	}

	public java.sql.Timestamp getTglTerimaPaket() {
		return tglTerimaPaket;
	}

	public void setTglTerimaPaket(java.sql.Timestamp tglTerimaPaket) {
		this.tglTerimaPaket = tglTerimaPaket;
	}

	public String getPenerimaPaket() {
		return penerimaPaket;
	}

	public void setPenerimaPaket(String penerimaPaket) {
		this.penerimaPaket = penerimaPaket;
	}

	public BigInteger getJmlhPaket() {
		return jmlhPaket;
	}

	public void setJmlhPaket(BigInteger jmlhPaket) {
		this.jmlhPaket = jmlhPaket;
	}

	public BigInteger getSudahReport() {
		return sudahReport;
	}

	public void setSudahReport(BigInteger sudahReport) {
		this.sudahReport = sudahReport;
	}

	public BigInteger getBlmReport() {
		return blmReport;
	}

	public void setBlmReport(BigInteger blmReport) {
		this.blmReport = blmReport;
	}

	public BigInteger getJmlhMasalah() {
		return jmlhMasalah;
	}

	public void setJmlhMasalah(BigInteger jmlhMasalah) {
		this.jmlhMasalah = jmlhMasalah;
	}

	public BigInteger getStockCabang() {
		return stockCabang;
	}

	public void setStockCabang(BigInteger stockCabang) {
		this.stockCabang = stockCabang;
	}

	public int getInboundFlag() {
		return inboundFlag;
	}

	public void setInboundFlag(int inboundFlag) {
		this.inboundFlag = inboundFlag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserPoto() {
		return userPoto;
	}

	public void setUserPoto(String userPoto) {
		this.userPoto = userPoto;
	}

	public String getUserEntry() {
		return userEntry;
	}

	public void setUserEntry(String userEntry) {
		this.userEntry = userEntry;
	}

	public String getUserManifest() {
		return userManifest;
	}

	public void setUserManifest(String userManifest) {
		this.userManifest = userManifest;
	}

	public String getIdKardusManifest() {
		return idKardusManifest;
	}

	public void setIdKardusManifest(String idKardusManifest) {
		this.idKardusManifest = idKardusManifest;
	}

	public String getUserTerima() {
		return userTerima;
	}

	public void setUserTerima(String userTerima) {
		this.userTerima = userTerima;
	}

	public String getIdKardusTerima() {
		return idKardusTerima;
	}

	public void setIdKardusTerima(String idKardusTerima) {
		this.idKardusTerima = idKardusTerima;
	}

	public String getUserGabung() {
		return userGabung;
	}

	public void setUserGabung(String userGabung) {
		this.userGabung = userGabung;
	}

	public String getUserPerwakilan() {
		return userPerwakilan;
	}

	public void setUserPerwakilan(String userPerwakilan) {
		this.userPerwakilan = userPerwakilan;
	}

	public String getNmKurir() {
		return nmKurir;
	}

	public void setNmKurir(String nmKurir) {
		this.nmKurir = nmKurir;
	}

	public String getMasalah() {
		return masalah;
	}

	public void setMasalah(String masalah) {
		this.masalah = masalah;
	}

	public Date getTglPoto() {
		return tglPoto;
	}

	public void setTglPoto(Date tglPoto) {
		this.tglPoto = tglPoto;
	}

	public Date getTglEntry() {
		return tglEntry;
	}

	public void setTglEntry(Date tglEntry) {
		this.tglEntry = tglEntry;
	}

	public Date getTglManifest() {
		return tglManifest;
	}

	public void setTglManifest(Date tglManifest) {
		this.tglManifest = tglManifest;
	}

	public Date getTglTerima() {
		return tglTerima;
	}

	public void setTglTerima(Date tglTerima) {
		this.tglTerima = tglTerima;
	}

	public Date getTglGabung() {
		return tglGabung;
	}

	public void setTglGabung(Date tglGabung) {
		this.tglGabung = tglGabung;
	}

	public Date getTglPerwakilan() {
		return tglPerwakilan;
	}

	public void setTglPerwakilan(Date tglPerwakilan) {
		this.tglPerwakilan = tglPerwakilan;
	}

	public Date getTglKirim() {
		return tglKirim;
	}

	public void setTglKirim(Date tglKirim) {
		this.tglKirim = tglKirim;
	}

	public String getKdPelanggan() {
		return kdPelanggan;
	}

	public void setKdPelanggan(String kdPelanggan) {
		this.kdPelanggan = kdPelanggan;
	}

	public String getIdDataPaket() {
		return idDataPaket;
	}

	public void setIdDataPaket(String idDataPaket) {
		this.idDataPaket = idDataPaket;
	}

	public int getTotalasuransi() {
		return totalasuransi;
	}

	public void setTotalasuransi(int totalasuransi) {
		this.totalasuransi = totalasuransi;
	}

	public BigDecimal getHargaSetelahDiskon() {
		return hargaSetelahDiskon;
	}

	public void setHargaSetelahDiskon(BigDecimal hargaSetelahDiskon) {
		this.hargaSetelahDiskon = hargaSetelahDiskon;
	}

	public BigDecimal getTotalDiskon() {
		return totalDiskon;
	}

	public void setTotalDiskon(BigDecimal totalDiskon) {
		this.totalDiskon = totalDiskon;
	}

	private BigDecimal diskonPel, tBiaya, hargaSetelahDiskon, totalDiskon;
	private BigInteger count, jmlhPelanggan, jmlhBarang;
	private Double sumBerat, sumBeratAsli;

	public BigInteger getJmlhBarang() {
		return jmlhBarang;
	}

	public void setJmlhBarang(BigInteger jmlhBarang) {
		this.jmlhBarang = jmlhBarang;
	}

	public Double getSumBeratAsli() {
		return sumBeratAsli;
	}

	public void setSumBeratAsli(Double sumBeratAsli) {
		this.sumBeratAsli = sumBeratAsli;
	}

	public String getNmSales() {
		return nmSales;
	}

	public void setNmSales(String nmSales) {
		this.nmSales = nmSales;
	}

	public Double getSumBerat() {
		return sumBerat;
	}

	public void setSumBerat(Double sumBerat) {
		this.sumBerat = sumBerat;
	}

	public BigInteger getJmlhPelanggan() {
		return jmlhPelanggan;
	}

	public void setJmlhPelanggan(BigInteger jmlhPelanggan) {
		this.jmlhPelanggan = jmlhPelanggan;
	}

	public BigDecimal gettBiaya() {
		return tBiaya;
	}

	public void settBiaya(BigDecimal tBiaya) {
		this.tBiaya = tBiaya;
	}

	public int getDiskonRapid() {
		return diskonRapid;
	}

	public void setDiskonRapid(int diskonRapid) {
		this.diskonRapid = diskonRapid;
	}

	public int getDiskonJne() {
		return diskonJne;
	}

	public void setDiskonJne(int diskonJne) {
		this.diskonJne = diskonJne;
	}

	public int getDiskonPelanggan() {
		return diskonPelanggan;
	}

	public void setDiskonPelanggan(int diskonPelanggan) {
		this.diskonPelanggan = diskonPelanggan;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}

	public BigDecimal getDiskonPel() {
		return diskonPel;
	}

	public void setDiskonPel(BigDecimal diskonPel) {
		this.diskonPel = diskonPel;
	}

	public int getBiayaSblmDiskon() {
		return biayaSblmDiskon;
	}

	public void setBiayaSblmDiskon(int biayaSblmDiskon) {
		this.biayaSblmDiskon = biayaSblmDiskon;
	}

	private Date created;

	public int getDiskon() {
		return diskon;
	}

	public void setDiskon(int diskon) {
		this.diskon = diskon;
	}

	public String getResiJne() {
		return resiJne;
	}

	public void setResiJne(String resiJne) {
		this.resiJne = resiJne;
	}

	public String getKecamatan() {
		return kecamatan;
	}

	public void setKecamatan(String kecamatan) {
		this.kecamatan = kecamatan;
	}

	public String getKabupaten() {
		return kabupaten;
	}

	public void setKabupaten(String kabupaten) {
		this.kabupaten = kabupaten;
	}

	public String getPorvinsi() {
		return porvinsi;
	}

	public void setPorvinsi(String porvinsi) {
		this.porvinsi = porvinsi;
	}

	public String getLayanan() {
		return layanan;
	}

	public void setLayanan(String layanan) {
		this.layanan = layanan;
	}

	public int getBiaya() {
		return biaya;
	}

	public void setBiaya(int biaya) {
		this.biaya = biaya;
	}

	public String getNoTlpn() {
		return noTlpn;
	}

	public void setNoTlpn(String noTlpn) {
		this.noTlpn = noTlpn;
	}

	public String getGambar() {
		return gambar;
	}

	public void setGambar(String gambar) {
		this.gambar = gambar;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAsalPaket() {
		return asalPaket;
	}

	public void setAsalPaket(String asalPaket) {
		this.asalPaket = asalPaket;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getbVolume() {
		return bVolume;
	}

	public void setbVolume(String bVolume) {
		this.bVolume = bVolume;
	}

	public String getAwbData() {
		return awbData;
	}

	public void setAwbData(String awbData) {
		this.awbData = awbData;
	}

	public String getKdPickup() {
		return kdPickup;
	}

	public void setKdPickup(String kdPickup) {
		this.kdPickup = kdPickup;
	}

	public String getPengirim() {
		return pengirim;
	}

	public void setPengirim(String pengirim) {
		this.pengirim = pengirim;
	}

	public String getPenerima() {
		return penerima;
	}

	public void setPenerima(String penerima) {
		this.penerima = penerima;
	}

	public String getTujuan() {
		return tujuan;
	}

	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}

	public String getKetTujuan() {
		return ketTujuan;
	}

	public void setKetTujuan(String ketTujuan) {
		this.ketTujuan = ketTujuan;
	}

	public String getKdPerwakilan() {
		return kdPerwakilan;
	}

	public void setKdPerwakilan(String kdPerwakilan) {
		this.kdPerwakilan = kdPerwakilan;
	}

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getBpVolume() {
		return bpVolume;
	}

	public void setBpVolume(String bpVolume) {
		this.bpVolume = bpVolume;
	}

	public String getTimbang() {
		return timbang;
	}

	public void setTimbang(String timbang) {
		this.timbang = timbang;
	}

	public String getBtTimbang() {
		return btTimbang;
	}

	public void setBtTimbang(String btTimbang) {
		this.btTimbang = btTimbang;
	}

	public String getbFinal() {
		return bFinal;
	}

	public void setbFinal(String bFinal) {
		this.bFinal = bFinal;
	}

	public String getBpFinal() {
		return bpFinal;
	}

	public void setBpFinal(String bpFinal) {
		this.bpFinal = bpFinal;
	}

	public int getCgkreg() {
		return cgkreg;
	}

	public void setCgkreg(int cgkreg) {
		this.cgkreg = cgkreg;
	}

	public int getHarga() {
		return harga;
	}

	public void setHarga(int harga) {
		this.harga = harga;
	}

	public int getAsuransi() {
		return asuransi;
	}

	public void setAsuransi(int asuransi) {
		this.asuransi = asuransi;
	}

	public int getTotalBiaya() {
		return totalBiaya;
	}

	public void setTotalBiaya(int totalBiaya) {
		this.totalBiaya = totalBiaya;
	}

	public String getResseler() {
		return resseler;
	}

	public void setResseler(String resseler) {
		this.resseler = resseler;
	}

}
