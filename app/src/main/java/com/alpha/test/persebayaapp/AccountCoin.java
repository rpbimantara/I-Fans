package com.alpha.test.persebayaapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AccountCoin implements Comparable<AccountCoin> {
    String id,name,date,price,type;

    public AccountCoin(String id, String name, String date, String price, String type) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.price = price;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(AccountCoin accountCoin) {
        DateFormat f = new SimpleDateFormat("dd MMM yyyy hh:mm");
        try {
            return f.parse(accountCoin.getDate().substring(0,17)).compareTo(f.parse(this.getDate().substring(0,17)));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
