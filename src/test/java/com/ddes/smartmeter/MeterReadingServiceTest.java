package com.ddes.smartmeter;
import com.ddes.smartmeter.services.MeterReadingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.bytebuddy.asm.MemberSubstitution;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.ddes.smartmeter.entities.MeterReading;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MeterReadingServiceTest {

    @Autowired
    private MeterReadingService meterReadingService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MeterReadingService meterReadingService = new MeterReadingService();
    }

    @Test
    void test01_when_currentUsageIsAValidNumber_EnsureCostIsCalculatedCorrectly() {
        UUID clientId = UUID.randomUUID();
        long timestamp = 1234567890;
        double currentUsage = 100;

        MeterReading  meterReading = new MeterReading(clientId.toString(), currentUsage, timestamp);

        String jsonMessage = meterReadingService.processedMeterReading(meterReading);

        double cost = 0;

        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert the string into a JsonNode (or ObjectNode)
            JsonNode jsonNode = objectMapper.readTree(jsonMessage);

            // If you know it's an object, you can cast it to ObjectNode
            ObjectNode objectNode = (ObjectNode) jsonNode;

            cost = objectNode.get("cost").asDouble();
        } catch (Exception e) {

        }

        double expectedCost = 100.0 * 0.12; // Usage * Cost per kWh

        assertEquals(cost, expectedCost);
    }

}
