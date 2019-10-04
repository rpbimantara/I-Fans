package com.alpha.test.persebayaapp;

public class Jadwal {
    private String namateam,fototeam, namaliga, tglmain, namastadion, waktumain,jadwal_id,status_jadwal;
    private Integer statusimage;

    public Jadwal(String namateam,String fototeam,Integer statusimage, String namaliga, String tglmain, String namastadion, String waktumain,String jadwal_id,String status_jadwal) {
        this.namateam = namateam;
        this.fototeam = fototeam;
        this.namaliga = namaliga;
        this.tglmain = tglmain;
        this.namastadion = namastadion;
        this.waktumain = waktumain;
        this.statusimage = statusimage;
        this.jadwal_id = jadwal_id;
        this.status_jadwal = status_jadwal;
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

    public String getJadwal_id() {
        return jadwal_id;
    }

    public void setJadwal_id(String jadwal_id) {
        this.jadwal_id = jadwal_id;
    }

    public String getStatus_jadwal() {
        return status_jadwal;
    }

    public void setStatus_jadwal(String status_jadwal) {
        this.status_jadwal = status_jadwal;
    }
}
