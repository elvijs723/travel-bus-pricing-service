package com.travel.bus.pricing;

import com.travel.bus.pricing.service.PricingController;
import com.travel.bus.pricing.service.dto.Item;
import com.travel.bus.pricing.service.dto.PassangerPricingDTO;
import com.travel.bus.pricing.service.dto.Passenger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PricingControllerTest {

    private static final int SCALE = 2;
    private static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    private static final double BASE_PRICE = 10.00D;
    private static final int TAX = 21;

    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(29.04D).setScale(PricingControllerTest.SCALE, PricingControllerTest.DEFAULT_ROUNDING_MODE);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("Test getDraftPricing response structure")
    @Test
    public void testGetDraftPricing() throws Exception {

        final String baseUrl = "http://localhost:" + port + PricingApplication.CONTEXT_PATH + "/draft/Valmiera-Kuldiga";

        List<Passenger> passengerList = getPassengerList();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request
        HttpEntity<List<Passenger>> request = new HttpEntity<>(passengerList, headers);

        // Receive response
        ResponseEntity<PassangerPricingDTO> response =
                restTemplate.exchange(baseUrl, HttpMethod.POST, request, new ParameterizedTypeReference<PassangerPricingDTO>() {});

        // OK
        assert (response.getStatusCode() == HttpStatus.OK);
        assert (response.getBody() != null);

        PassangerPricingDTO responseBody = response.getBody();

        // Tax, BasePrice and TotalPrice
        assertEquals(responseBody.getTaxPercent(), BigDecimal.valueOf(TAX));
        assertEquals(responseBody.getBasePrice(), BigDecimal.valueOf(BASE_PRICE)
                .setScale(PricingControllerTest.SCALE, DEFAULT_ROUNDING_MODE));
        assertEquals(responseBody.getTotalPrice(), TOTAL_PRICE);

        // Passenger and Luggage COUNT + PRICES
        List<Passenger> responseBodyPassengerList = responseBody.getPassengerList();

        assertEquals(responseBodyPassengerList.size(), passengerList.size());
        for (int i=0; i<passengerList.size(); i++) {
            assertNotNull (responseBodyPassengerList.get(i).getPrice());
            assertEquals(passengerList.get(i).getLuggageItems().size(), responseBodyPassengerList.get(i).getLuggageItems().size());
            for (Item luggageItem : responseBodyPassengerList.get(i).getLuggageItems()) {
                assertNotNull (luggageItem.getPrice());
            }
        }
    }

    @Disabled
    @DisplayName("Test getDraftPricing formatted response structure")
    @Test
    public void testGetDraftPricingFormatted() throws Exception {
        final String baseUrl = "http://localhost:" + port + PricingApplication.CONTEXT_PATH + "/draft/Valmiera-Kuldiga";
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
