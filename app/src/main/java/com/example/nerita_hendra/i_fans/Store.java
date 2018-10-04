package com.example.nerita_hendra.i_fans;

public class Store {
    private String namabarang;
    private String hargabarang;

    public Store(String namabarang, String hargabarang) {
        this.namabarang = namabarang;
        this.hargabarang = hargabarang;
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
