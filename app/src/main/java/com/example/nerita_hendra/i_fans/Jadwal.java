package com.example.nerita_hendra.i_fans;

public class Jadwal {
    private String namateam, namaliga, tglmain, namastadion, waktumain;

    public Jadwal(String namateam, String namaliga, String tglmain, String namastadion, String waktumain) {
        this.namateam = namateam;
        this.namaliga = namaliga;
        this.tglmain = tglmain;
        this.namastadion = namastadion;
        this.waktumain = waktumain;
    }

    public String getNamateam() {
        return namateam;
    }

    public void setNamateam(String namateam) {
        this.namateam = namateam;
    }

    public String getNamaliga() {
        return namaliga;
    }

    public void setNamaliga(String namaliga) {
        this.namaliga = namaliga;
    }

    public String getTglmain() {
        return tglmain;
    }

    public void setTglmain(String tglmain) {
        this.tglmain = tglmain;
    }

    public String getNamastadion() {
        return namastadion;
    }

    public void setNamastadion(String namastadion) {
        this.namastadion = namastadion;
    }

    public String getWaktumain() {
        return waktumain;
    }

    public void setWaktumain(String waktumain) {
        this.waktumain = waktumain;
    }
}
