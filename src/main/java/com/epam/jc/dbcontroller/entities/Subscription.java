package com.epam.jc.dbcontroller.Entities;

import java.sql.Date;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */
public class Subscription {
    private Long orderId;
    private Long magazine_Id;
    private Date startDate;
    private Long months;

    public Subscription(Long orderId, Long magazine_Id, Date startDate, Long months) {
        this.orderId = orderId;
        this.magazine_Id = magazine_Id;
        this.startDate = startDate;
        this.months = months;
    }

    public Long getMagazineId() {
        return magazine_Id;
    }

    public void setMagazine_Id(Long magazine_Id) {
        this.magazine_Id = magazine_Id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
}
