package com.ecksday.borrowtracker;

/**
 * Created by G551JK-DM053H on 30-10-2017.
 */

public class Transaction {
    //Data Variables
    private String transaction_id;
    private String friend_id;
    private String transaction_code;
    private String transaction_amount;

    //Getters and Setters
    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String name) {
        this.transaction_amount = name;
    }
}
