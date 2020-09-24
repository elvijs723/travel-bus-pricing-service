package com.travel.bus.pricing.service;
import com.travel.bus.pricing.service.dto.Passenger;
import java.math.BigDecimal;
import java.util.List;

public interface PricingComponent {

    BigDecimal getBasePrice(String route) ;

    BigDecimal getTaxPercent();

    void setPassengerPrices(List<Passenger> passengerList, String route);

    BigDecimal getTotalPrice(List<Passenger> passengerList);
}
