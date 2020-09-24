package com.travel.bus.pricing.service;

import com.travel.bus.pricing.service.dto.Passenger;
import com.travel.bus.pricing.service.dto.PassangerPricingDTO;
import com.travel.bus.pricing.util.PassangerPricingFormattedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping
public class PricingController {

    public static final String DRAFT_ROUTE_MAPPING = "/draft/{route}";

    @Autowired
    PricingComponent pricingComponent;

    @PostMapping(DRAFT_ROUTE_MAPPING)
    public ResponseEntity<?> getDraftPricing(@PathVariable("route") String route, @PathParam("format") Boolean format,
                                             @RequestBody List<Passenger> passengerList) {

        PassangerPricingDTO pricingDTO = new PassangerPricingDTO();

        pricingDTO.setBasePrice(pricingComponent.getBasePrice(route));
        pricingDTO.setTaxPercent(pricingComponent.getTaxPercent());

        pricingDTO.setPassengerList(passengerList);
        pricingComponent.setPassengerPrices(
                pricingDTO.getPassengerList(), route
        );

        pricingDTO.setTotalPrice(
                pricingComponent.getTotalPrice(
                        pricingDTO.getPassengerList())
        );

        return Boolean.TRUE.equals(format) ?
                ResponseEntity.ok(new PassangerPricingFormattedDTO(pricingDTO))
                : ResponseEntity.ok(pricingDTO);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException ex) {
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
