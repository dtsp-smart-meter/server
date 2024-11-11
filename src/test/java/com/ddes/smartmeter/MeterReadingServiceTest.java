package com.ddes.smartmeter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.ddes.smartmeter.entities.MeterReading;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;


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

        MeterReading meterReading = new MeterReading(clientId.toString(), currentUsage, timestamp);

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

    //Test to ensure that when usage is zero, the cost is calculated as zero.

    @Test
    void test02_when_currentUsageIsZero_CostShouldBeZero() {
        // Arrange
        UUID clientId = UUID.randomUUID();
        long timestamp = 1234567890L;
        double currentUsage = 0.0;

        MeterReading meterReading = new MeterReading(clientId.toString(), currentUsage, timestamp);

        // Act
        String jsonMessage = meterReadingService.processedMeterReading(meterReading);

        // Assert
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonMessage);
            ObjectNode objectNode = (ObjectNode) jsonNode;

            double cost = objectNode.get("cost").asDouble();
            double expectedCost = 0.0; // Expected cost for zero usage

            assertEquals(expectedCost, cost, "Cost should be zero when usage is zero.");
        } catch (Exception e) {
            fail("Exception while parsing the JSON response: " + e.getMessage());
        }
    }

    //Test to check the behavior when null MeterReading is passed.
    @Test
    void test03_when_nullMeterReadingIsPassed_ThrowNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            meterReadingService.processedMeterReading(null);
        }, "A NullPointerException should be thrown when a null MeterReading is passed.");
    }

    //Test to ensure that when a large usage value is passed, the cost is calculated correctly.
    @Test
    void test04_when_currentUsageIsLarge_CostShouldBeCalculatedCorrectly() {
        // Arrange
        UUID clientId = UUID.randomUUID();
        long timestamp = 1234567890L;
        double currentUsage = 1_000_000.0; // Large usage value

        MeterReading meterReading = new MeterReading(clientId.toString(), currentUsage, timestamp);

        // Act
        String jsonMessage = meterReadingService.processedMeterReading(meterReading);

        // Assert
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonMessage);
            ObjectNode objectNode = (ObjectNode) jsonNode;

            double cost = objectNode.get("cost").asDouble();
            double expectedCost = currentUsage * 0.12; // Assuming the cost is 0.12 per kWh

            assertEquals(expectedCost, cost, "Cost should be calculated correctly for large usage.");
        } catch (Exception e) {
            fail("Exception while parsing the JSON response: " + e.getMessage());
        }
    }

    //Test to ensure that an exception is thrown when usage is negative.
    @Test
    void test05_when_currentUsageIsNegative_ThrowIllegalArgumentException() {
        // Arrange
        UUID clientId = UUID.randomUUID();
        long timestamp = System.currentTimeMillis(); // Current timestamp
        double currentUsage = -50.0; // Negative usage

        MeterReading meterReading = new MeterReading(clientId.toString(), currentUsage, timestamp);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            meterReadingService.processedMeterReading(meterReading);
        }, "An IllegalArgumentException should be thrown for negative usage.");
    }


    //Negative tests - should fail

    // Test to ensure that the cost calculation fails with incorrect expected value.
    @Test
    void test06_when_currentUsageIsAValidNumber_EnsureCostIsCalculatedCorrectly() {
        UUID clientId = UUID.randomUUID();
        long timestamp = 1234567890;
        double currentUsage = 100;

        MeterReading meterReading = new MeterReading(clientId.toString(), currentUsage, timestamp);

        String jsonMessage = meterReadingService.processedMeterReading(meterReading);

        double cost = 0;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonMessage);
            ObjectNode objectNode = (ObjectNode) jsonNode;

            cost = objectNode.get("cost").asDouble();
        } catch (Exception e) {
            fail("Failed to parse JSON: " + e.getMessage());
        }

        double incorrectExpectedCost = 200.0; // Incorrect expected cost

        assertEquals(incorrectExpectedCost, cost, "This should fail because the expected cost is incorrect.");
    }

    // Test to ensure that the cost calculation fails with incorrect expected value when usage is zero.
    @Test
    void test07_when_currentUsageIsZero_CostShouldBeZero() {
        UUID clientId = UUID.randomUUID();
        long timestamp = 1234567890L;
        double currentUsage = 0.0;

        MeterReading meterReading = new MeterReading(clientId.toString(), currentUsage, timestamp);

        String jsonMessage = meterReadingService.processedMeterReading(meterReading);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonMessage);
            ObjectNode objectNode = (ObjectNode) jsonNode;

            double cost = objectNode.get("cost").asDouble();
            double incorrectExpectedCost = 2.0; // Incorrect expected cost

            assertEquals(incorrectExpectedCost, cost, "This should fail because the expected cost is incorrect when usage is zero.");
        } catch (Exception e) {
            fail("Exception while parsing the JSON response: " + e.getMessage());
        }
    }

    // Test to check the behavior when null MeterReading is passed, expecting IllegalArgumentException instead.
    @Test
    void test08_when_nullMeterReadingIsPassed_ThrowNullPointerException() {
        // Change the expected exception type to an unexpected type or simply call the method without asserting.
        assertThrows(IllegalArgumentException.class, () -> {
            meterReadingService.processedMeterReading(null);
        }, "This test should fail because it expects an IllegalArgumentException instead of NullPointerException.");
    }


    // Test to ensure that when a large usage value is passed, an incorrect expected cost causes the test to fail.
    @Test
    void test09_when_currentUsageIsLarge_CostShouldBeCalculatedCorrectly() {
        UUID clientId = UUID.randomUUID();
        long timestamp = 1234567890L;
        double currentUsage = 1_000_000.0; // Large usage value

        MeterReading meterReading = new MeterReading(clientId.toString(), currentUsage, timestamp);

        String jsonMessage = meterReadingService.processedMeterReading(meterReading);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonMessage);
            ObjectNode objectNode = (ObjectNode) jsonNode;

            double cost = objectNode.get("cost").asDouble();
            double incorrectExpectedCost = 1_000_000.0; // Incorrect expected cost

            assertEquals(incorrectExpectedCost, cost, "This should fail because the expected cost for large usage is incorrect.");
        } catch (Exception e) {
            fail("Exception while parsing the JSON response: " + e.getMessage());
        }
    }

    // Test to ensure that an exception is expected for negative usage but it should not be thrown.
    @Test
    void test10_when_currentUsageIsNegative_ThrowIllegalArgumentException() {
        UUID clientId = UUID.randomUUID();
        long timestamp = System.currentTimeMillis();
        double currentUsage = -50.0; // Negative usage

        MeterReading meterReading = new MeterReading(clientId.toString(), currentUsage, timestamp);

        // Expecting no exception (this should fail since it should throw IllegalArgumentException)
        assertDoesNotThrow(() -> {
            meterReadingService.processedMeterReading(meterReading);
        }, "This should fail because an exception is expected for negative usage.");
    }
}