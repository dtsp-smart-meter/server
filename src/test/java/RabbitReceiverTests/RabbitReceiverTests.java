package RabbitReceiverTests;

import com.ddes.smartmeter.entities.MeterReading;
import com.ddes.smartmeter.rabbit.RabbitReceiver;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RabbitReceiverTests {

    RabbitReceiver rabbitReceiver = new RabbitReceiver();

    @Test
    public void test01_WhenCreateJsonObjectCalled_EnsureObjectBuiltWithCorrectValues() {
        try {
            UUID clientId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

            MeterReading meterReading = new MeterReading(clientId.toString(), 10.0, 1234567890);
            double totalBill = 100.0;

            String jsonString = rabbitReceiver.createJsonString(meterReading, totalBill);

            assertEquals("{\"clientId\":\"123e4567-e89b-12d3-a456-426614174000\",\"currentUsage\":10.0,\"currentCost\":1.2,\"totalBill\":100.0,\"timestamp\":1234567890}", jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test02_WhenCreateJsonObjectCalledByMeterReadingWithNegativeElectricValues_EnsureObjectBuiltWithThoseValues() {
        try {
            UUID clientId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

            MeterReading meterReading = new MeterReading(clientId.toString(), -10.0, 1234567890);
            double totalBill = 100.0;

            String jsonString = rabbitReceiver.createJsonString(meterReading, totalBill);

            assertEquals("{\"clientId\":\"123e4567-e89b-12d3-a456-426614174000\",\"currentUsage\":-10.0,\"currentCost\":-1.2,\"totalBill\":100.0,\"timestamp\":1234567890}", jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
