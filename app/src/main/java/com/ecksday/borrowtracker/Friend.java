package com.ecksday.borrowtracker;

/**
 * Created by G551JK-DM053H on 31-10-2017.
 */

public class Friend {
    private String friend_id, friend_fullname, current_total;

    public Friend() {
    }

    public Friend(String friend_id, String friend_fullname, String current_total) {
        this.friend_id = friend_id;
        this.friend_fullname = friend_fullname;
        this.current_total = current_total;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getFriend_fullname() {
        return friend_fullname;
    }

    public void setFriend_fullname(String friend_fullname) {
        this.friend_fullname = friend_fullname;
    }

    public String getCurrent_total() {
        return current_total;
    }

    public void setCurrent_total(String current_total) {
        this.current_total = current_total;
    }
}