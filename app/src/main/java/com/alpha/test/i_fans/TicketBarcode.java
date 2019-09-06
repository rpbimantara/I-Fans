package com.alpha.test.i_fans;

public class TicketBarcode {
    String id,name,date_open,event_name,ticket_type,barcode;

    public TicketBarcode(String id, String name, String date_open, String event_name, String ticket_type, String barcode) {
        this.id = id;
        this.name = name;
        this.date_open = date_open;
        this.event_name = event_name;
        this.ticket_type = ticket_type;
        this.barcode = barcode;
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

    public String getDate_open() {
        return date_open;
    }

    public void setDate_open(String date_open) {
        this.date_open = date_open;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(String ticket_type) {
        this.ticket_type = ticket_type;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
