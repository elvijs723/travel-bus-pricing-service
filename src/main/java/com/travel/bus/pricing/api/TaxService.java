package com.travel.bus.pricing.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TaxService {

    int getTaxByDate(LocalDate date);

}
