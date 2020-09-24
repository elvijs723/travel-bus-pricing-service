package com.travel.bus.pricing.util;

import com.travel.bus.pricing.service.PricingComponentImpl;
import com.travel.bus.pricing.service.dto.Item;
import com.travel.bus.pricing.service.dto.Passenger;
import com.travel.bus.pricing.service.dto.PassangerPricingDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassangerPricingFormattedDTO {

    private static final String CURRENCY = "EUR";
    private static final Map<Integer, String> SIZE_LITERAL = new HashMap<>();
    static {
        SIZE_LITERAL.put(0, "No Luggage");
        SIZE_LITERAL.put(1, "One Bag");
        SIZE_LITERAL.put(2, "Two Bags");
        SIZE_LITERAL.put(3, "Three Bags");
        SIZE_LITERAL.put(4, "Four Bags");
        SIZE_LITERAL.put(5, "Five Bags");
        SIZE_LITERAL.put(6, "Six Bags");
        SIZE_LITERAL.put(7, "Seven Bags");
        SIZE_LITERAL.put(8, "Eight Bags");
        SIZE_LITERAL.put(9, "Nine Bags");
    }

    private List<String> ticketPrices;
    private String totalprice;

    public PassangerPricingFormattedDTO(PassangerPricingDTO source) {
        ticketPrices = new ArrayList<>();

        for (Passenger p: source.getPassengerList()) {
            String passangerLine = String.format("%s (%s %s %s) = %s %s",
                    p.getInfant() ? "Infant" : "Adult",
                    source.getBasePrice().toString(),
                    CURRENCY,
                    p.getInfant() ?
                            "x " + PricingComponentImpl.INFANT_PERCENTAGE_OF_BASE_PRICE.toString() + "% + " + source.getTaxPercent().toString() + "%" :
                            "+ " + source.getTaxPercent().toString() + "%"
                    ,
                    p.getPrice().toString(),
                    CURRENCY
                    );
            ticketPrices.add(passangerLine);

            int luggageSize = p.getLuggageItems().size();

            String luggageLine = luggageSize == 0 ? SIZE_LITERAL.get(0) :
                    String.format("%s (%d x %s %s x %s + %s) = %s %s",
                            SIZE_LITERAL.get(luggageSize),
                            luggageSize,
                            source.getBasePrice(),
                            CURRENCY,
                            PricingComponentImpl.ITEM_PERCENTAGE_OF_BASE_PRICE.toString() + "%",
                            source.getTaxPercent() + "%",
                            p.getLuggageItems().stream()
                                    .map(Item::getPrice)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add).toString(),
                            CURRENCY);
            ticketPrices.add(luggageLine);
        }
        this.totalprice = source.getTotalPrice().toString() + " " + CURRENCY;
    }

    public List<String> getTicketPrices() {
        return ticketPrices;
    }

    public void setTicketPrices(List<String> ticketPrices) {
        this.ticketPrices = ticketPrices;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }
}
