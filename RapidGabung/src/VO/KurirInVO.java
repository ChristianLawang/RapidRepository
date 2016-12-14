package VO;

import java.util.Date;

import entity.TrCabang;
import entity.TrKurir;
import entity.TrPelanggan;

public class KurirInVO{
	private int idStatusKirim;
	private String idKurir;
	private String noStrukKurir;
	private String status;
	private String penerima;
	
	
	public KurirInVO(String awbData, String penerima) {
		this.awbData=awbData;
		this.penerima=penerima;
	}
	public KurirInVO() {
		// TODO Auto-generated constructor stub
	}
	public int getIdStatusKirim() {
		return idStatusKirim;
	}
	public void setIdStatusKirim(int idStatusKirim) {
		this.idStatusKirim = idStatusKirim;
	}
	public String getIdKurir() {
		return idKurir;
	}
	public void setIdKurir(String idKurir) {
		this.idKurir = idKurir;
	}
	public String getNoStrukKurir() {
		return noStrukKurir;
	}
	public void setNoStrukKurir(String noStrukKurir) {
		this.noStrukKurir = noStrukKurir;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPenerima() {
		return penerima;
	}
	public void setPenerima(String penerima) {
		this.penerima = penerima;
	}
	public Integer getMasalah() {
		return masalah;
	}
	public void setMasalah(Integer masalah) {
		this.masalah = masalah;
	}
	public Date getWaktu() {
		return waktu;
	}
	public void setWaktu(Date waktu) {
		this.waktu = waktu;
	}
	public String getIdBarang() {
		return idBarang;
	}
	public void setIdBarang(String idBarang) {
		this.idBarang = idBarang;
	}
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	public Integer getJumlahPelanggan() {
		return jumlahPelanggan;
	}
	public void setJumlahPelanggan(Integer jumlahPelanggan) {
		this.jumlahPelanggan = jumlahPelanggan;
	}
	public Integer getIdDataPaket() {
		return idDataPaket;
	}
	public void setIdDataPaket(Integer idDataPaket) {
		this.idDataPaket = idDataPaket;
	}
	public String getAsalPaket() {
		return asalPaket;
	}
	public void setAsalPaket(String asalPaket) {
		this.asalPaket = asalPaket;
	}
	public String getPengirim() {
		return pengirim;
	}
	public void setPengirim(String pengirim) {
		this.pengirim = pengirim;
	}
	public String getTujuan() {
		return tujuan;
	}
	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}
	public String getTelpPenerima() {
		return telpPenerima;
	}
	public void setTelpPenerima(String telpPenerima) {
		this.telpPenerima = telpPenerima;
	}
	public String getReseller() {
		return reseller;
	}
	public void setReseller(String reseller) {
		this.reseller = reseller;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getBfinal() {
		return bfinal;
	}
	public void setBfinal(String bfinal) {
		this.bfinal = bfinal;
	}
	public String getPbfinal() {
		return pbfinal;
	}
	public void setPbfinal(String pbfinal) {
		this.pbfinal = pbfinal;
	}
	public Integer getHarga() {
		return harga;
	}
	public void setHarga(Integer harga) {
		this.harga = harga;
	}
	public Integer getBiaya() {
		return biaya;
	}
	public void setBiaya(Integer biaya) {
		this.biaya = biaya;
	}
	public String getKodePerwakilan() {
		return kodePerwakilan;
	}
	public void setKodePerwakilan(String kodePerwakilan) {
		this.kodePerwakilan = kodePerwakilan;
	}
	public Integer getJneFlag() {
		return jneFlag;
	}
	public void setJneFlag(Integer jneFlag) {
		this.jneFlag = jneFlag;
	}
	public Integer getAsuransi() {
		return asuransi;
	}
	public void setAsuransi(Integer asuransi) {
		this.asuransi = asuransi;
	}
	public Integer getTotalDiskon() {
		return totalDiskon;
	}
	public void setTotalDiskon(Integer totalDiskon) {
		this.totalDiskon = totalDiskon;
	}
	public Integer getTotalBiaya() {
		return totalBiaya;
	}
	public void setTotalBiaya(Integer totalBiaya) {
		this.totalBiaya = totalBiaya;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Integer getFlagEntry() {
		return flagEntry;
	}
	public void setFlagEntry(Integer flagEntry) {
		this.flagEntry = flagEntry;
	}
	public Integer getSampah() {
		return sampah;
	}
	public void setSampah(Integer sampah) {
		this.sampah = sampah;
	}
	public Integer getIdPotoPaket() {
		return idPotoPaket;
	}
	public void setIdPotoPaket(Integer idPotoPaket) {
		this.idPotoPaket = idPotoPaket;
	}
	public TrCabang getTrCabang() {
		return trCabang;
	}
	public void setTrCabang(TrCabang trCabang) {
		this.trCabang = trCabang;
	}
	public TrPelanggan getTrPelanggan() {
		return trPelanggan;
	}
	public void setTrPelanggan(TrPelanggan trPelanggan) {
		this.trPelanggan = trPelanggan;
	}
	public String getKodePickup() {
		return kodePickup;
	}
	public void setKodePickup(String kodePickup) {
		this.kodePickup = kodePickup;
	}
	public Integer getBarangjm() {
		return barangjm;
	}
	public void setBarangjm(Integer barangjm) {
		this.barangjm = barangjm;
	}
	public String getAwbData() {
		return awbData;
	}
	public void setAwbData(String awbData) {
		this.awbData = awbData;
	}
	public Integer getAwbNumber() {
		return awbNumber;
	}
	public void setAwbNumber(Integer awbNumber) {
		this.awbNumber = awbNumber;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getBvolume() {
		return bvolume;
	}
	public void setBvolume(String bvolume) {
		this.bvolume = bvolume;
	}
	public String getBpvolume() {
		return bpvolume;
	}
	public void setBpvolume(String bpvolume) {
		this.bpvolume = bpvolume;
	}
	public String getTimbang() {
		return timbang;
	}
	public void setTimbang(String timbang) {
		this.timbang = timbang;
	}
	public String getBtimbang() {
		return btimbang;
	}
	public void setBtimbang(String btimbang) {
		this.btimbang = btimbang;
	}
	public String getBpfinal() {
		return bpfinal;
	}
	public void setBpfinal(String bpfinal) {
		this.bpfinal = bpfinal;
	}
	public String getLayanan() {
		return layanan;
	}
	public void setLayanan(String layanan) {
		this.layanan = layanan;
	}
	public String getGambar() {
		return gambar;
	}
	public void setGambar(String gambar) {
		this.gambar = gambar;
	}
	public Date getTglGambar() {
		return tglGambar;
	}
	public void setTglGambar(Date tglGambar) {
		this.tglGambar = tglGambar;
	}
	public Integer getKoli() {
		return koli;
	}
	public void setKoli(Integer koli) {
		this.koli = koli;
	}
	public String getStatusAt() {
		return statusAt;
	}
	public void setStatusAt(String statusAt) {
		this.statusAt = statusAt;
	}
	public String getAdminInput() {
		return adminInput;
	}
	public void setAdminInput(String adminInput) {
		this.adminInput = adminInput;
	}
	public String getTheUser() {
		return theUser;
	}
	public void setTheUser(String theUser) {
		this.theUser = theUser;
	}
	public Integer getIdDataPoto() {
		return idDataPoto;
	}
	public void setIdDataPoto(Integer idDataPoto) {
		this.idDataPoto = idDataPoto;
	}
	public TrKurir getTrKurir() {
		return trKurir;
	}
	public void setTrKurir(TrKurir trKurir) {
		this.trKurir = trKurir;
	}
	public String getIdKeranjang() {
		return idKeranjang;
	}
	public void setIdKeranjang(String idKeranjang) {
		this.idKeranjang = idKeranjang;
	}
	public Integer getIdUpload() {
		return idUpload;
	}
	public void setIdUpload(Integer idUpload) {
		this.idUpload = idUpload;
	}
	public Date getWkSortir() {
		return wkSortir;
	}
	public void setWkSortir(Date wkSortir) {
		this.wkSortir = wkSortir;
	}
	public Integer getSortirFlag() {
		return sortirFlag;
	}
	public void setSortirFlag(Integer sortirFlag) {
		this.sortirFlag = sortirFlag;
	}
	public Date getWkGabungPaket() {
		return wkGabungPaket;
	}
	public void setWkGabungPaket(Date wkGabungPaket) {
		this.wkGabungPaket = wkGabungPaket;
	}
	public Integer getGabungPaketFlag() {
		return gabungPaketFlag;
	}
	public void setGabungPaketFlag(Integer gabungPaketFlag) {
		this.gabungPaketFlag = gabungPaketFlag;
	}
	public String getIdKardus() {
		return idKardus;
	}
	public void setIdKardus(String idKardus) {
		this.idKardus = idKardus;
	}
	public Integer getFinalKardus() {
		return finalKardus;
	}
	public void setFinalKardus(Integer finalKardus) {
		this.finalKardus = finalKardus;
	}
	public Integer getInboundFlag() {
		return inboundFlag;
	}
	public void setInboundFlag(Integer inboundFlag) {
		this.inboundFlag = inboundFlag;
	}
	public Date getWkInbound() {
		return wkInbound;
	}
	public void setWkInbound(Date wkInbound) {
		this.wkInbound = wkInbound;
	}
	public Integer getBarangTerkirimFlag() {
		return barangTerkirimFlag;
	}
	public void setBarangTerkirimFlag(Integer barangTerkirimFlag) {
		this.barangTerkirimFlag = barangTerkirimFlag;
	}
	public Integer getSubmitFlag() {
		return submitFlag;
	}
	public void setSubmitFlag(Integer submitFlag) {
		this.submitFlag = submitFlag;
	}
	public String getMemberC() {
		return memberC;
	}
	public void setMemberC(String memberC) {
		this.memberC = memberC;
	}
	public String getMemberU() {
		return memberU;
	}
	public void setMemberU(String memberU) {
		this.memberU = memberU;
	}
	public String getResiJne() {
		return resiJne;
	}
	public void setResiJne(String resiJne) {
		this.resiJne = resiJne;
	}
	private Integer masalah;
	private Date waktu;
	private String idBarang;
	private Date tanggal;
	private Integer jumlahPelanggan;
	
	private Integer idDataPaket;
	private String asalPaket;
	private String pengirim;
	private String tujuan;
	private String telpPenerima;
	private String reseller;
	private String keterangan;
	private String bfinal;
	private String pbfinal;
	private Integer harga;
	private Integer biaya;
	private String kodePerwakilan;
	private Integer jneFlag;
	private Integer asuransi;
	private Integer totalDiskon;
	private Integer totalBiaya;
	private String user;
	private Date created;
	private Date updated;
	private Integer flagEntry;
	private Integer sampah;
	
	private Integer idPotoPaket;
	private TrCabang trCabang;
	private TrPelanggan trPelanggan;
	private String kodePickup;
	private Integer barangjm;
	private String awbData;
	private Integer awbNumber;
	private String volume;
	private String bvolume;
	private String bpvolume;
	private String timbang;
	private String btimbang;
	private String bpfinal;
	private String layanan;	
	private String gambar;
	private Date tglGambar;
	private Integer koli;
	private String statusAt;
	private String adminInput;
	private String theUser;
	
	private Integer idDataPoto;
	private TrKurir trKurir;
	private String idKeranjang;
	private Integer idUpload;
	private Date wkSortir;
	private Integer sortirFlag;
	private Date wkGabungPaket;
	private Integer gabungPaketFlag;
	private String idKardus;
	private Integer finalKardus;
	private Integer inboundFlag;
	private Date wkInbound;
	private Integer barangTerkirimFlag;
	private Integer submitFlag;
	private String memberC;
	private String memberU;
	private String resiJne;

	private String kodeZona;
	public String getKodeZona() {
		return kodeZona;
	}
	public void setKodeZona(String kodeZona) {
		this.kodeZona = kodeZona;
	}
	public String getPropinsi() {
		return propinsi;
	}
	public void setPropinsi(String propinsi) {
		this.propinsi = propinsi;
	}
	public String getKodeAsal() {
		return kodeAsal;
	}
	public void setKodeAsal(String kodeAsal) {
		this.kodeAsal = kodeAsal;
	}
	public String getKabupaten() {
		return kabupaten;
	}
	public void setKabupaten(String kabupaten) {
		this.kabupaten = kabupaten;
	}
	public String getKecamatan() {
		return kecamatan;
	}
	public void setKecamatan(String kecamatan) {
		this.kecamatan = kecamatan;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public Integer getReg() {
		return reg;
	}
	public void setReg(Integer reg) {
		this.reg = reg;
	}
	public String getEtd() {
		return etd;
	}
	public void setEtd(String etd) {
		this.etd = etd;
	}
	public Integer getOne() {
		return one;
	}
	public void setOne(Integer one) {
		this.one = one;
	}
	private String propinsi;
	private String kodeAsal;
	private String kabupaten;
	private String kecamatan;
	private String zona;
	private Integer reg;
	private String etd;
	private Integer one;

}
