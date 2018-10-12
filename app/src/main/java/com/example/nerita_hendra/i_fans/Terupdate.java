package com.example.nerita_hendra.i_fans;

public class Terupdate {
    private Integer id;
    private String title;
    private String kategori;
    private String headline;
    private String konten;
    private String tanggal;
    private String penulis;

    public Terupdate(Integer id, String title, String kategori, String headline, String konten, String tanggal, String penulis) {
        this.id = id;
        this.title = title;
        this.kategori = kategori;
        this.headline = headline;
        this.konten = konten;
        this.tanggal = tanggal;
        this.penulis = penulis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getKonten() {
        return konten;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }
}
