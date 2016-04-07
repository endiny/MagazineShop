package com.epam.jc.dbcontroller.entities;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */
public class Order {
    private Long id;

    private Long userId;
    private LocalDateTime orderTime;
    private Double toPay;
    private Double paid;
    private String shipAddress;
    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public Order(Long id, Long userId, Long orderTime, Double toPay, Double paid, String shipAddress) {
        this.id = id;
        this.userId = userId;
        this.orderTime = LocalDateTime.ofEpochSecond(orderTime, 0, ZoneOffset.UTC);
        this.toPay = toPay;
        this.paid = paid;
        this.shipAddress = shipAddress;
    }

    public void setOrderTime(LocalDateTime orderTime) {
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

    public Double getPaid() {
        return paid;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }
}
