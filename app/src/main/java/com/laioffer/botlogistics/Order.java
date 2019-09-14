package com.laioffer.botlogistics;

import java.io.Serializable;

public class Order implements Serializable {
    private String orderId;
    private Long deliveryTime;
    private Long departTime;
    private String destination;
    private String machineId;
    private Long pickupTime;
    private String shippingAddress;
    private String shippingMethod;
    private Long shippingTime;
    private String userId;
    public Order(){
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Long getDepartTime() {
        return departTime;
    }

    public void setDepartTime(Long departTime) {
        this.departTime = departTime;
    }

    public Long getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Long pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Long getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(Long shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
