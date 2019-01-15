package com.example.nerita_hendra.i_fans;

public class Store {

    private String imageStore,namabarang,hargabarang,deskripsi;

    public Store(String imageStore, String namabarang, String hargabarang,String deskripsi) {
        this.imageStore = imageStore;
        this.namabarang = namabarang;
        this.hargabarang = hargabarang;
        this.deskripsi = deskripsi;
    }

    public String getImageStore() {
        return imageStore;
    }

    public void setImageStore(String imageStore) {
        this.imageStore = imageStore;
    }

    public String getNamabarang() {
        return namabarang;
    }

    public void setNamabarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public String getHargabarang() {
        return hargabarang;
    }

    public void setHargabarang(String hargabarang) {
        this.hargabarang = hargabarang;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
