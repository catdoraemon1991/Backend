package com.laioffer.entity;

import java.io.Serializable;

public class ShippingMethod implements Serializable {
    private Integer quantity;
    private Double price;
    private Double dutation;
    private String type;
    private String station;

    public String getStation() {
        return station;
    }

    public ShippingMethod setStation(String station) {
        this.station = station;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ShippingMethod setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public ShippingMethod setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Double getDutation() {
        return dutation;
    }

    public ShippingMethod setDutation(Double dutation) {
        this.dutation = dutation;
        return this;
    }

    public String getType() {
        return type;
    }

    public ShippingMethod setType(String type) {
        this.type = type;
        return this;
    }
}
