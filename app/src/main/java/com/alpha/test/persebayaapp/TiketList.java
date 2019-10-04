package com.alpha.test.persebayaapp;

public class TiketList {
    String id,image,name,date_begin,organizer_id,event_type_id;

    public TiketList(String id, String image,String name, String date_begin, String organizer_id, String event_type_id) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.date_begin = date_begin;
        this.organizer_id = organizer_id;
        this.event_type_id = event_type_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_begin() {
        return date_begin;
    }

    public void setDate_begin(String date_begin) {
        this.date_begin = date_begin;
    }

    public String getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(String organizer_id) {
        this.organizer_id = organizer_id;
    }

    public String getEvent_type_id() {
        return event_type_id;
    }

    public void setEvent_type_id(String event_type_id) {
        this.event_type_id = event_type_id;
    }
}
