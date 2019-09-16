package com.laioffer.entity;

public class User {
    private double user_timestamp;
    private String user_account;
    private String user_password;
    private String user_email;
    private String[] orderId;


    public double getUser_timestamp() {
        return user_timestamp;
    }

    public void setUser_timestamp(double user_timestamp) {
        this.user_timestamp = user_timestamp;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String[] getUser_orderId() {
        return orderId;
    }

    public void setUser_orderId(String user_orderId) {
        this.orderId = orderId;
    }
}
