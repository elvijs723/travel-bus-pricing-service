package com.travel.bus.pricing.service.dto;

import java.math.BigDecimal;

public class Item {

    String itemName;
    BigDecimal price;

    public String getItemName() {
        return itemName;
    }

    public Item setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Item setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Item {" +
                "itemName= " + itemName +
                ", price=" + price +
                '}';
    }
}
