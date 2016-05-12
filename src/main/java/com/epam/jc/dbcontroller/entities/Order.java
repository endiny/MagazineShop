package com.epam.jc.DbController.Entities;

import org.json.simple.JSONObject;

import java.sql.Timestamp;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */
public class Order {
    private Long id;

    private Long userId;
    private Timestamp orderTime;
    private Double toPay;
    private Boolean paid;
    private String shipAddress;

    @Override
    public String toString() {
        return toJSON().toJSONString();
    }

    public JSONObject toJSON() {
        JSONObject order = new JSONObject();
        order.put("id", getId());
        order.put("timestamp", getOrderTime().getTime());
        order.put("ship", getShipAddress());
        order.put("sum", getToPay());
        order.put("paid", isPaid());
        return order;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public Order(Long userId, Timestamp orderTime, Double toPay, Boolean paid, String shipAddress) {
        this.userId = userId;
        this.orderTime = orderTime;
        this.toPay = toPay;
        this.paid = paid;
        this.shipAddress = shipAddress;
    }

    public Order(Long id, Long userId, Timestamp orderTime, Double toPay, Boolean paid, String shipAddress) {
        this.id = id;
        this.userId = userId;
        this.orderTime = orderTime;
        this.toPay = toPay;
        this.paid = paid;
        this.shipAddress = shipAddress;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public Double getToPay() {
        return toPay;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setToPay(Double toPay) {

        this.toPay = toPay;
    }

    public Boolean isPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

}
