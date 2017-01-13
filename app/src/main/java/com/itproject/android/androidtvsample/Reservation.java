package com.itproject.android.androidtvsample;

/**
 * Created by srthg on 1/13/2017.
 */

public class Reservation {

   private String dates;
    private String numberofhours;
    private String playlistkey;
    private String room;
    private String status;
    private String username;

    public Reservation()
    {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getNumberofhours() {
        return numberofhours;
    }

    public void setNumberofhours(String numberofhours) {
        this.numberofhours = numberofhours;
    }

    public String getPlaylistkey() {
        return playlistkey;
    }

    public void setPlaylistkey(String playlistkey) {
        this.playlistkey = playlistkey;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
