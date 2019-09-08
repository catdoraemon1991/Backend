package com.laioffer.botlogistics;

public class Order {
    private String pick_up;
    private String drop_off;
    private String time_picker;
    private String size;
    private String[] orderId;


    public String getPick_up() {
        return pick_up;
    }

    public void setPick_up(String pick_up) {
        this.pick_up = pick_up;
    }

    public String getDrop_off() {
        return drop_off;
    }

    public void setDrop_off(String drop_off) {
        this.drop_off = drop_off;
    }

    public String getTime_picker() {
        return time_picker;
    }

    public void setTime_picker(String time_picker) {
        this.time_picker = time_picker;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String user_email) {
        this.size = size;
    }

    public String[] getUser_orderId() {
        return orderId;
    }

    public void setUser_orderId(String user_orderId) {
        this.orderId = orderId;
    }
}
