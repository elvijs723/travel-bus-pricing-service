package com.travel.bus.pricing;

import com.travel.bus.pricing.api.BusPricingService;
import com.travel.bus.pricing.api.TaxService;
import com.travel.bus.pricing.service.PricingComponent;
import com.travel.bus.pricing.service.PricingComponentImpl;
import com.travel.bus.pricing.service.dto.Item;
import com.travel.bus.pricing.service.dto.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PricingComponentTest {

    private static final int SCALE = 2;
    private static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    private static final double BASE_PRICE = 10.00D;
    private static final int TAX = 21;

    private static final BigDecimal INFANT_PRICE = BigDecimal.valueOf(6.05D).setScale(PricingComponentTest.SCALE, PricingComponentTest.DEFAULT_ROUNDING_MODE);
    private static final BigDecimal ADULT_PRICE = BigDecimal.valueOf(12.10D).setScale(PricingComponentTest.SCALE, PricingComponentTest.DEFAULT_ROUNDING_MODE);
    private static final BigDecimal LUGGAGE_ITEM_PRICE = BigDecimal.valueOf(3.63D).setScale(PricingComponentTest.SCALE, PricingComponentTest.DEFAULT_ROUNDING_MODE);
    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(29.04D).setScale(PricingComponentTest.SCALE, PricingComponentTest.DEFAULT_ROUNDING_MODE);

    @Mock
    private BusPricingService busPricingService;

    @Mock
    private TaxService taxService;

    @InjectMocks
    private PricingComponent pricingComponent = new PricingComponentImpl();

    @BeforeEach
    void mockApiResponses() {
        when(busPricingService.getBusBasePriceByRoute(anyString())).thenReturn(BASE_PRICE);
        when(taxService.getTaxByDate(any(LocalDate.class))).thenReturn(TAX);
    }

    @DisplayName("Test getTaxPercent BigDecimal with SCALE 0")
    @Test
    void testGetTaxPercent() {
        assertEquals(BigDecimal.valueOf(TAX), pricingComponent.getTaxPercent());
    }

    @DisplayName("Test getBasePrice BigDecimal with SCALE 2 and ROUNDING_HALF_UP(4)")
    @Test
    void testGetBasePrice() {
        double basePrice = 15.2360D;
        BigDecimal expected = BigDecimal.valueOf(basePrice).setScale(SCALE, DEFAULT_ROUNDING_MODE);
        when(busPricingService.getBusBasePriceByRoute(anyString())).thenReturn(basePrice);
        assertEquals(expected, pricingComponent.getBasePrice(anyString()));
    }

    @DisplayName("Test setPassengerPrices with TAX and BigDecimal with SCALE 2 and ROUNDING_HALF_UP(4)")
    @Test
    void testSetPassengerPrices() {

        List<Passenger> passengerList = getPassengerList();

        pricingComponent.setPassengerPrices(passengerList, anyString());

        for (Passenger passenger : passengerList) {
            assertEquals(passenger.getInfant() ? INFANT_PRICE : ADULT_PRICE, passenger.getPrice());
            for (Item item : passenger.getLuggageItems()) {
                assertEquals(LUGGAGE_ITEM_PRICE, item.getPrice());
            }
        }
    }

    @DisplayName("Test getTotalPrice BigDecimal with SCALE 2 and ROUNDING_HALF_UP(4)")
    @Test
    void testGetTotalPrice() {

        List<Passenger> passengerList = getPassengerList();
        pricingComponent.setPassengerPrices(passengerList, anyString());

        assertEquals(TOTAL_PRICE, pricingComponent.getTotalPrice(passengerList));
    }

    private List<Passenger> getPassengerList() {
        return Arrays.asList(
            new Passenger().setInfant(true).setLuggageItems(
                    Arrays.asList(
                            new Item().setItemName("Bag 1")
                    )
            ),
            new Passenger().setInfant(false).setLuggageItems(
                    Arrays.asList(
                            new Item().setItemName("Bag 2"),
                            new Item().setItemName("Bag 3")
                    )
            )
        );
    }

}
