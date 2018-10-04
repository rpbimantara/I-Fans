package com.example.nerita_hendra.i_fans;

public class lelang {

    private String namalelang, waktulelang, bidlelang, binlelang;

    public lelang(String namalelang, String waktulelang, String bidlelang, String binlelang) {
        this.namalelang = namalelang;
        this.waktulelang = waktulelang;
        this.bidlelang = bidlelang;
        this.binlelang = binlelang;
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
