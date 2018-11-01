package com.example.nerita_hendra.i_fans;

public class Tiket {

    private String kategoriTiket,hargaTiket,jumlahTiket,sisaTiket;

    public Tiket(String kategoriTiket, String hargaTiket, String jumlahTiket,String sisaTiket) {
        this.kategoriTiket = kategoriTiket;
        this.hargaTiket = hargaTiket;
        this.jumlahTiket = jumlahTiket;
        this.sisaTiket = sisaTiket;
    }

    public String getKategoriTiket() {
        return kategoriTiket;
    }

    public void setKategoriTiket(String kategoriTiket) {
        this.kategoriTiket = kategoriTiket;
    }

    public String getHargaTiket() {
        return hargaTiket;
    }

    public void setHargaTiket(String hargaTiket) {
        hargaTiket = hargaTiket;
    }

    public String getJumlahTiket() {
        return jumlahTiket;
    }

    public void setJumlahTiket(String jumlahTiket) {
        jumlahTiket = jumlahTiket;
    }

    public String getSisaTiket() {
        return sisaTiket;
    }

    public void setSisaTiket(String sisaTiket) {
        this.sisaTiket = sisaTiket;
    }
}
