package com.alpha.test.i_fans;

public class MatchLineUp {
    private String id,jadwal_id,player,player_number,position,player_club,player_state;

    public MatchLineUp(String id, String jadwal_id, String player, String player_number, String position, String player_club, String player_state ) {
        this.id = id;
        this.jadwal_id = jadwal_id;
        this.player = player;
        this.player_number = player_number;
        this.position = position;
        this.player_club = player_club;
        this.player_state = player_state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJadwal_id() {
        return jadwal_id;
    }

    public void setJadwal_id(String jadwal_id) {
        this.jadwal_id = jadwal_id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayer_number() {
        return player_number;
    }

    public void setPlayer_number(String player_number) {
        this.player_number = player_number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPlayer_club() {
        return player_club;
    }

    public void setPlayer_club(String player_club) {
        this.player_club = player_club;
    }

    public String getPlayer_state() {
        return player_state;
    }

    public void setPlayer_state(String player_state) {
        this.player_state = player_state;
    }


    @Override
    public String toString() {
        return "MatchLineUp{" +
                "id='" + id + '\'' +
                ", jadwal_id='" + jadwal_id + '\'' +
                ", player='" + player + '\'' +
                ", player_number='" + player_number + '\'' +
                ", position='" + position + '\'' +
                ", player_club='" + player_club + '\'' +
                ", player_state='" + player_state + '\'' +
                '}';
    }
}
