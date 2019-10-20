package com.alpha.test.persebayaapp;

public class History {
    String id,order_name,product_name,owner,state,qty,harga,image,date;
    Boolean is_send,is_received;

    public History(String id, String order_name, String product_name, String owner, String state, String qty, String harga, String image, String date, Boolean is_send, Boolean is_received) {
        this.id = id;
        this.order_name = order_name;
        this.product_name = product_name;
        this.owner = owner;
        this.state = state;
        this.qty = qty;
        this.harga = harga;
        this.image = image;
        this.date = date;
        this.is_send = is_send;
        this.is_received = is_received;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getIs_send() {
        return is_send;
    }

    public void setIs_send(Boolean is_send) {
        this.is_send = is_send;
    }

    public Boolean getIs_received() {
        return is_received;
    }

    public void setIs_received(Boolean is_received) {
        this.is_received = is_received;
    }
}
