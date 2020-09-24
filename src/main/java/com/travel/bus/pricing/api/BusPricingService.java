package com.travel.bus.pricing.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BusPricingService {

    double getBusBasePriceByRoute(String route);

}
