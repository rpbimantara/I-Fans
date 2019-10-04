package com.alpha.test.persebayaapp;

public class MatchMoment {

    private String id,time,club_name,event,player_name,sub_player_name;

    public MatchMoment(String id, String time,String club_name, String event, String player_name, String sub_player_name) {
        this.id = id;
        this.time = time;
        this.club_name = club_name;
        this.event = event;
        this.player_name = player_name;
        this.sub_player_name = sub_player_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public String getSub_player_name() {
        return sub_player_name;
    }

    public void setSub_player_name(String sub_player_name) {
        this.sub_player_name = sub_player_name;
    }

    @Override
    public String toString() {
        return "MatchMoment{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", club_name='" + club_name + '\'' +
                ", event='" + event + '\'' +
                ", player_name='" + player_name + '\'' +
                ", sub_player_name='" + sub_player_name + '\'' +
                '}';
    }
}
