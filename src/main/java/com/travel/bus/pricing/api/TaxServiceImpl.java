package com.travel.bus.pricing.api;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaxServiceImpl implements TaxService {

    @Override
    public int getTaxByDate(LocalDate date) {
        return 21;
    }
}
