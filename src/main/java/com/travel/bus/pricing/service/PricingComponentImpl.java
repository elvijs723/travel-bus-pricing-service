package com.travel.bus.pricing.service;

import com.travel.bus.pricing.api.BusPricingService;
import com.travel.bus.pricing.api.TaxService;
import com.travel.bus.pricing.service.dto.Item;
import com.travel.bus.pricing.service.dto.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
public class PricingComponentImpl implements PricingComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingComponentImpl.class);

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final int SCALE = 2;
    public static final BigDecimal INFANT_PERCENTAGE_OF_BASE_PRICE = new BigDecimal("50");
    public static final BigDecimal ITEM_PERCENTAGE_OF_BASE_PRICE = new BigDecimal("30");
    private static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    @Autowired
    TaxService taxService;

    @Autowired
    BusPricingService busPricingService;

    public BigDecimal getTaxPercent() {
        return BigDecimal
                .valueOf(taxService.getTaxByDate(LocalDate.now()));
    }

    public BigDecimal getBasePrice(String route) {
        return BigDecimal
                .valueOf(busPricingService.getBusBasePriceByRoute(route))
                .setScale(SCALE, DEFAULT_ROUNDING_MODE);
    }


    public void setPassengerPrices(List<Passenger> passengerList, String route) {
        BigDecimal basePrice = this.getBasePrice(route);
        BigDecimal taxPercent = this.getTaxPercent();

        LOGGER.debug("Setting passenger prices :" + passengerList.toString());
        passengerList.forEach(passenger -> {
                this.setPassengerPriceWithTax(passenger, basePrice, taxPercent);
                this.setLuggageItemPricesWithTax(passenger, basePrice, taxPercent);
            }
        );
        LOGGER.debug("Result passenger prices :" + passengerList.toString());
    }

    public BigDecimal getTotalPrice(List<Passenger> passengerList) {

        BigDecimal personTotalPrice = passengerList.stream()
                .map(Passenger::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal luggageTotalPrice = passengerList.stream()
                .map(Passenger::getLuggageItems)
                .flatMap(Collection::stream)
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return personTotalPrice.add(luggageTotalPrice);
    }

    private void setPassengerPriceWithTax(Passenger passenger, BigDecimal basePrice, BigDecimal taxPercent) {
        BigDecimal taxable = Boolean.TRUE.equals(passenger.getInfant()) ?
                basePrice.multiply(INFANT_PERCENTAGE_OF_BASE_PRICE).divide(ONE_HUNDRED, DEFAULT_ROUNDING_MODE)
                : basePrice;
        passenger.setPrice(applyTax(taxable, taxPercent));
    }

    private void setLuggageItemPricesWithTax(Passenger passenger, BigDecimal basePrice, BigDecimal taxPercent) {
        passenger.getLuggageItems().forEach(
                item -> {
                    BigDecimal taxable = basePrice.multiply(ITEM_PERCENTAGE_OF_BASE_PRICE).divide(ONE_HUNDRED, DEFAULT_ROUNDING_MODE);
                    item.setPrice(applyTax(taxable, taxPercent));
                }
        );
    }

    private BigDecimal applyTax(BigDecimal price, BigDecimal tax) {
        return price.add(
                price.multiply(tax).divide(ONE_HUNDRED, DEFAULT_ROUNDING_MODE)
        );
    }
}
