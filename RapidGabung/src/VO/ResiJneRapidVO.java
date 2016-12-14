package VO;


	import javafx.beans.property.IntegerProperty;
	import javafx.beans.property.SimpleIntegerProperty;
	import javafx.beans.property.SimpleStringProperty;
	import javafx.beans.property.StringProperty;

	public class ResiJneRapidVO {
		public final StringProperty kdJne;
		public final StringProperty penerima;
		public final StringProperty tujuan;
		public final StringProperty pengirim;
		public final StringProperty service;
		public final IntegerProperty harga;
		public final StringProperty created;
		
		
		public ResiJneRapidVO(String kdJne, String penerima, String tujuan, String pengirim, String service, Integer harga, String created) {
			this.kdJne = new SimpleStringProperty(kdJne);
			this.penerima = new SimpleStringProperty(penerima);
			this.tujuan = new SimpleStringProperty(tujuan);
			this.pengirim = new SimpleStringProperty(pengirim);
			this.service = new SimpleStringProperty(service);
			this.harga = new SimpleIntegerProperty(harga);
			this.created = new SimpleStringProperty(created);
		}
		
		public String getCreated() {
			return created.get();
		}
		
		public void setCreated(String created) {
			this.created.set(created);
		}
		
		public Integer getHarga() {
			return harga.get();
		}
		
		public void setHarga(Integer harga) {
			this.harga.set(harga);
		}
		
		public String getKdJne() {
			return kdJne.get();
		}
		
		public void setKdJne(String kdJne) {
			this.kdJne.set(kdJne);
		}

		public String getPenerima() {
			return penerima.get();
		}
		
		public void setPenerima(String penerima) {
			this.penerima.set(penerima);
		}

		public String getTujuan() {
			return tujuan.get();
		}
		
		public void setTujuan(String tujuan) {
			this.tujuan.set(tujuan);
		}

		public String getPengirim() {
			return pengirim.get();
		}
		
		public void setPengirim(String pengirim) {
			this.pengirim.set(pengirim);
		}

		public String getService() {
			return service.get();
		}
		
		public void setService(String service) {
			this.service.set(service);
		}


		
		public StringProperty kdJNEProperty() {
			return kdJne;
		}
		public StringProperty penerimaProperty() {
			return penerima;
		}
		public StringProperty pengirimProperty() {
			return pengirim;
		}
		public StringProperty serviceProperty() {
			return service;
		}
		public StringProperty tujuanProperty() {
			return tujuan;
		}
		public IntegerProperty hargaProperty(){
			return harga;
		}
		public StringProperty createdProperty(){
			return created;
		}
	}
