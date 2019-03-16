package com.example.nerita_hendra.i_fans;

public class ListBerita {

    private Integer id;
    private String image;
    private String kategori;
    private String headline;
    private String tanggal;


    public ListBerita(Integer id, String image, String kategori, String headline,String tanggal) {
        this.id = id;
        this.image = image;
        this.kategori = kategori;
        this.headline = headline;
        this.tanggal = tanggal;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

}
