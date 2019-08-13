package com.alpha.test.i_fans;

public class Team {

    private Integer id;
    private String nama,foto,statusPlayer,posisi,no_punggung;

    public Team(Integer id,String nama, String foto, String statusPlayer, String posisi,String no_punggung) {
        this.id = id;
        this.nama = nama;
        this.foto = foto;
        this.statusPlayer = statusPlayer;
        this.posisi = posisi;
        this.no_punggung = no_punggung;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
