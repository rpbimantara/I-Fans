package com.alpha.test.persebayaapp;

public class Donation {
    private String idDonation,namaDonation,imageDonation, waktuDonation,pemilikDonation,terkumpulDonation;

    public String getIdDonation() {
        return idDonation;
    }

    public void setIdDonation(String idDonation) {
        this.idDonation = idDonation;
    }

    public String getNamaDonation() {
        return namaDonation;
    }

    public void setNamaDonation(String namaDonation) {
        this.namaDonation = namaDonation;
    }

    public String getImageDonation() {
        return imageDonation;
    }

    public void setImageDonation(String imageDonation) {
        this.imageDonation = imageDonation;
    }

    public String getWaktuDonation() {
        return waktuDonation;
    }

    public void setWaktuDonation(String waktuDonation) {
        this.waktuDonation = waktuDonation;
    }

    public String getPemilikDonation() {
        return pemilikDonation;
    }

    public void setPemilikDonation(String pemilikDonation) {
        this.pemilikDonation = pemilikDonation;
    }

    public String getTerkumpulDonation() {
        return terkumpulDonation;
    }

    public void setTerkumpulDonation(String terkumpulDonation) {
        this.terkumpulDonation = terkumpulDonation;
    }

    public Donation(String idDonation, String namaDonation, String imageDonation, String waktuDonation, String pemilikDonation, String terkumpulDonation) {
        this.idDonation = idDonation;
        this.namaDonation = namaDonation;
        this.imageDonation = imageDonation;
        this.waktuDonation = waktuDonation;
        this.pemilikDonation = pemilikDonation;
        this.terkumpulDonation = terkumpulDonation;


    }
}
