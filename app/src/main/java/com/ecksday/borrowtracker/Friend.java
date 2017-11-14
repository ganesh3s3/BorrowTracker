package com.ecksday.borrowtracker;

/**
 * Created by G551JK-DM053H on 31-10-2017.
 */

public class Friend {
    private String friend_id, friend_firstname, friend_lastname;
    private Money current_total;

    public Friend() {
    }

    public Friend(String friend_id, String friend_firstname, String friend_lastname, Money current_total) {
        this.friend_id = friend_id;
        this.friend_firstname = friend_firstname;
        this.friend_lastname = friend_lastname;
        this.current_total = current_total;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getFriend_firstname() {
        return friend_firstname;
    }

    public void setFriend_firstname(String friend_firstname) {
        this.friend_firstname = friend_firstname;
    }

    public String getFriend_lastname() {
        return friend_lastname;
    }

    public void setFriend_lastname(String friend_lastname) {
        this.friend_lastname = friend_lastname;
    }

    public Money getCurrent_total() {
        return current_total;
    }

    public void setCurrent_total(Money current_total) {
        this.current_total = current_total;
    }

    @Override
    public String toString() {
        return friend_firstname + " " + friend_lastname;
    }
}