package com.example.nerita_hendra.i_fans;

public class Jadwal {
    private String namateam,fototeam, namaliga, tglmain, namastadion, waktumain;
    private Integer statusimage;

    public Jadwal(String namateam,String fototeam,Integer statusimage, String namaliga, String tglmain, String namastadion, String waktumain) {
        this.namateam = namateam;
        this.fototeam = fototeam;
        this.namaliga = namaliga;
        this.tglmain = tglmain;
        this.namastadion = namastadion;
        this.waktumain = waktumain;
        this.statusimage = statusimage;
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

    public String getFototeam() {
        return fototeam;
    }

    public void setFototeam(String fototeam) {
        this.fototeam = fototeam;
    }

    public Integer getStatusimage() {
        return statusimage;
    }

    public void setStatusimage(Integer statusimage) {
        this.statusimage = statusimage;
    }
}
