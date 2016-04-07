package com.epam.jc.dbcontroller.Entities;

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

    public Order(Long id, Long userId, Timestamp orderTime, Double toPay, Boolean paid, String shipAddress) {
        this.id = id;
        this.userId = userId;
        this.orderTime = orderTime;
        this.toPay = toPay;
        this.paid = paid;
        this.shipAddress = shipAddress;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public Order(Long id, Long userId, Long orderTime, Double toPay, Boolean paid, String shipAddress) {
        this.id = id;
        this.userId = userId;
        this.orderTime = new Timestamp(orderTime);
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
