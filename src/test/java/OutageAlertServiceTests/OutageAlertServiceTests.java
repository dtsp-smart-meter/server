package OutageAlertServiceTests;

import com.ddes.smartmeter.services.OutageAlertService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutageAlertServiceTests {

    OutageAlertService outageAlertService = new OutageAlertService();

    @Test
    public void test01_WhenCreateAlertMethodCalled_EnsureJsonObjectReturnedWithCorrectValues() {
        ObjectNode jsonObject = outageAlertService.createAlert();
        assertEquals("Electricity grid is down.", jsonObject.get("message").asText());
    }
}