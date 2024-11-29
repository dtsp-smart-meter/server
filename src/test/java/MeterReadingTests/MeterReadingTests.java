package MeterReadingTests;

import com.ddes.smartmeter.entities.MeterReading;
import org.junit.jupiter.api.Test;

public class MeterReadingTests {

    @Test
    public void test01_WhenObjectBuilt_EnsureObjectNoExceptionsThrownAndValuesAreCorrect() {
        MeterReading meterReading = new MeterReading("123e4567-e89b-12d3-a456-426614174000", 10.0, 1234567890);

        assert(meterReading.getClientId().toString().equals("123e4567-e89b-12d3-a456-426614174000"));
        assert(meterReading.getCurrentUsage() == 10.0);
        assert(meterReading.getCurrentCost() == 1.2);
        assert(meterReading.getTimestamp() == 1234567890);
    }
}