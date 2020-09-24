package com.travel.bus.pricing.api;

import org.springframework.stereotype.Component;

@Component
public class BusPricingServiceImpl implements BusPricingService {

    @Override
    public double getBusBasePriceByRoute(String route) {
        return 10.00D;
    }

}
