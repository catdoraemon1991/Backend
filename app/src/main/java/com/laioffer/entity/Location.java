package com.laioffer.entity;

import java.io.Serializable;

public class Location implements Serializable {
    private Double lat;
    private Double log;

    public Double getLat() {
        return lat;
    }

    public Location setLat(Double lat) {
        this.lat = lat;
        return this;
    }

    public Double getLog() {
        return log;
    }

    public Location setLog(Double log) {
        this.log = log;
        return this;
    }
}
