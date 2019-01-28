package com.example.nerita_hendra.i_fans;

public class Variant {
    String id,variant_text,qty;

    public Variant(String id, String variant_text, String qty) {
        this.id = id;
        this.variant_text = variant_text;
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVariant_text() {
        return variant_text;
    }

    public void setVariant_text(String variant_text) {
        this.variant_text = variant_text;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
