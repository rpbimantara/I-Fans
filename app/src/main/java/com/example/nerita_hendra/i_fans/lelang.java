package com.example.nerita_hendra.i_fans;

public class lelang {

    private String idlelang,namalelang,lelangimage, waktulelang, bidlelang, binlelang,inclelang,pemiliklelang;

    public lelang(String idlelang,String namalelang,String lelangimage ,String waktulelang, String bidlelang, String binlelang, String inclelang, String pemiliklelang) {
        this.idlelang = idlelang;
        this.namalelang = namalelang;
        this.lelangimage = lelangimage;
        this.waktulelang = waktulelang;
        this.bidlelang = bidlelang;
        this.binlelang = binlelang;
        this.inclelang = inclelang;
        this.pemiliklelang = pemiliklelang;
    }

    public String getIdlelang() {
        return idlelang;
    }

    public void setIdlelang(String idlelang) {
        this.idlelang = idlelang;
    }

    public String getInclelang() {
        return inclelang;
    }

    public String getLelangimage() {
        return lelangimage;
    }

    public void setLelangimage(String lelangimage) {
        this.lelangimage = lelangimage;
    }

    public void setInclelang(String inclelang) {
        this.inclelang = inclelang;
    }

    public String getPemiliklelang() {
        return pemiliklelang;
    }

    public void setPemiliklelang(String pemiliklelang) {
        this.pemiliklelang = pemiliklelang;
    }

    public String getNamalelang() {
        return namalelang;
    }

    public void setNamalelang(String namalelang) {
        this.namalelang = namalelang;
    }

    public String getWaktulelang() {
        return waktulelang;
    }

    public void setWaktulelang(String waktulelang) {
        this.waktulelang = waktulelang;
    }

    public String getBidlelang() {
        return bidlelang;
    }

    public void setBidlelang(String bidlelang) {
        this.bidlelang = bidlelang;
    }

    public String getBinlelang() {
        return binlelang;
    }

    public void setBinlelang(String binlelang) {
        this.binlelang = binlelang;
    }
}
