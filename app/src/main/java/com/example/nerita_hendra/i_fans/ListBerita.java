package com.example.nerita_hendra.i_fans;

public class ListBerita {

    private String kategori;
    private String headline;
    private String tanggal;

    public ListBerita(String kategori, String headline, String tanggal) {
        this.kategori = kategori;
        this.headline = headline;
        this.tanggal = tanggal;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    @Override
    public String toString() {
        return "ListBerita{" +
                "kategori='" + kategori + '\'' +
                ", headline='" + headline + '\'' +
                ", tanggal='" + tanggal + '\'' +
                '}';
    }
}
