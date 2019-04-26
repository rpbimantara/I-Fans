package com.alpha.test.i_fans;

public class Checkout {
    String id,nama,harga,qty,image,stock;

    public Checkout(String id, String nama, String harga, String qty, String image,String stock) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.qty = qty;
        this.image = image;
        this.stock =  stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
