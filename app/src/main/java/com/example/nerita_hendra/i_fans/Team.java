package com.example.nerita_hendra.i_fans;

public class Team {

    private String nama,foto,statusPlayer,posisi,no_punggung;

    public Team(String nama, String foto, String statusPlayer, String posisi,String no_punggung) {
        this.nama = nama;
        this.foto = foto;
        this.statusPlayer = statusPlayer;
        this.posisi = posisi;
        this.no_punggung = no_punggung;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getStatusPlayer() {
        return statusPlayer;
    }

    public void setStatusPlayer(String statusPlayer) {
        this.statusPlayer = statusPlayer;
    }

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }

    public String getNo_punggung() {
        return no_punggung;
    }

    public void setNo_punggung(String no_punggung) {
        this.no_punggung = no_punggung;
    }
}