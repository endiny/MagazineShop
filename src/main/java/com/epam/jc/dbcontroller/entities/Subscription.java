package com.epam.jc.DbController.Entities;

import org.json.simple.JSONObject;

import java.sql.Date;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */
public class Subscription {
    private Long orderId;
    private Long magazineId;
    private Date startDate;
    private Long months;

    public Subscription(Long orderId, Long magazineId, Long months) {
        this.orderId = orderId;
        this.magazineId = magazineId;
        this.months = months;
    }

    public Long getMagazineId() {
        return magazineId;
    }

    public void setMagazineId(Long magazineId) {
        this.magazineId = magazineId;
    }

    public Long getMonths() {
        return months;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setMonths(Long months) {
        this.months = months;

    }

    @Override
    public String toString() {
        return toJSON().toJSONString();
    }

    public JSONObject toJSON() {
        JSONObject out = new JSONObject();
        out.put("orderId", orderId);
        out.put("magazineId", magazineId);
        out.put("months", months);
        return out;
    }
}
