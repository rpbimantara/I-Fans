package com.alpha.test.i_fans;

public class Tiket {

    private String id,kategoriTiket,hargaTiket,jumlahTiket,sisaTiket,product_id;

    public Tiket(String id,String kategoriTiket, String hargaTiket, String jumlahTiket,String sisaTiket,String product_id) {
        this.id = id;
        this.kategoriTiket = kategoriTiket;
        this.hargaTiket = hargaTiket;
        this.jumlahTiket = jumlahTiket;
        this.sisaTiket = sisaTiket;
        this.product_id = product_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKategoriTiket() {
        return kategoriTiket;
    }

    public void setKategoriTiket(String kategoriTiket) {
        this.kategoriTiket = kategoriTiket;
    }

    public String getHargaTiket() {
        return hargaTiket;
    }

    public void setHargaTiket(String hargaTiket) {
        this.hargaTiket = hargaTiket;
    }

    public String getJumlahTiket() {
        return jumlahTiket;
    }

    public void setJumlahTiket(String jumlahTiket) {
        this.jumlahTiket = jumlahTiket;
    }

    public String getSisaTiket() {
        return sisaTiket;
    }

    public void setSisaTiket(String sisaTiket) {
        this.sisaTiket = sisaTiket;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public String toString() {
        return "Tiket{" +
                "id='" + id + '\'' +
                ", kategoriTiket='" + kategoriTiket + '\'' +
                ", hargaTiket='" + hargaTiket + '\'' +
                ", jumlahTiket='" + jumlahTiket + '\'' +
                ", sisaTiket='" + sisaTiket + '\'' +
                ", product_id='" + product_id + '\'' +
                '}';
    }
}
