package com.example.nerita_hendra.i_fans;

public class Terupdate {
    private Integer id;
    private String imageTerupdate;
    private String kategori;
    private String headline;

    public Terupdate(Integer id,String imageTerupdate, String kategori, String headline) {
        this.id = id;
        this.imageTerupdate = imageTerupdate;
        this.kategori = kategori;
        this.headline = headline;
    }

    public Integer getId() {
        return id;
    }

    public String getImageTerupdate() {
        return imageTerupdate;
    }

    public void setImageTerupdate(String imageTerupdate) {
        this.imageTerupdate = imageTerupdate;
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

}
