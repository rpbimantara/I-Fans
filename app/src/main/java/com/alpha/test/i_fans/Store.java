package com.alpha.test.i_fans;

public class Store {

    private String id,imageStore,namabarang,hargabarang;

    public Store(String id, String imageStore, String namabarang, String hargabarang) {
        this.id = id;
        this.imageStore = imageStore;
        this.namabarang = namabarang;
        this.hargabarang = hargabarang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageStore() {
        return imageStore;
    }

    public void setImageStore(String imageStore) {
        this.imageStore = imageStore;
    }

    public String getNamabarang() {
        return namabarang;
    }

    public void setNamabarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public String getHargabarang() {
        return hargabarang;
    }

    public void setHargabarang(String hargabarang) {
        this.hargabarang = hargabarang;
    }

}
