package com.travel.bus.pricing.service.dto;

import java.math.BigDecimal;
import java.util.List;

public class PassangerPricingDTO {

    private BigDecimal taxPercent;
    private BigDecimal basePrice;
    private List<Passenger> passengerList;
    BigDecimal totalPrice;

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public BigDecimal getBasePrice() {
        return basePrice.setScale(2);
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
