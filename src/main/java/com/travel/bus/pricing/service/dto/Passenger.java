package com.travel.bus.pricing.service.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public class Passenger {

    private Boolean isInfant;
    private List<Item> luggageItems;
    private BigDecimal price;

    public Boolean getInfant() {
        return isInfant;
    }
    @JsonProperty("isInfant")
    public Passenger setInfant(Boolean infant) {
        isInfant = infant;
        return this;
    }

    public List<Item> getLuggageItems() {
        return luggageItems;
    }

    public Passenger setLuggageItems(List<Item> luggageItems) {
        this.luggageItems = luggageItems;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Passenger setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Passenger {" +
                "isInfant= " + isInfant +
                ", price=" + getPrice() +
                ", luggageItemLength= " + luggageItems.size() +
                ", luggageItems=" + luggageItems.toString() +
                '}';
    }

}
