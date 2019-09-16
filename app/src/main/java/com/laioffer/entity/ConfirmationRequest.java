package com.laioffer.entity;

import java.io.Serializable;

public class ConfirmationRequest implements Serializable {
    String destination;
    String shippingAddress;
    Long shippingTime;
    String userId;
    String shippingMethod;
    String stationId;
    Double duration;
    Double price;

    public Double getDuration() {
        return duration;
    }

    public ConfirmationRequest setDuration(Double duration) {
        this.duration = duration;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public ConfirmationRequest setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public ConfirmationRequest setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public ConfirmationRequest setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public Long getShippingTime() {
        return shippingTime;
    }

    public ConfirmationRequest setShippingTime(Long shippingTime) {
        this.shippingTime = shippingTime;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public ConfirmationRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public ConfirmationRequest setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
        return this;
    }

    public String getStationId() {
        return stationId;
    }

    public ConfirmationRequest setStationId(String stationId) {
        this.stationId = stationId;
        return this;
    }
}
