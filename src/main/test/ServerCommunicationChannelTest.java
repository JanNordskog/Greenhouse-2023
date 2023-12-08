package no.ntnu.run;

import no.ntnu.greenhouse.SensorReading;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ServerCommunicationChannelTest {

    @Test
    void testExtractSensorReadings() {
        ServerCommunicationChannel channel = new ServerCommunicationChannel(null, null);

        String testData = "{ \"nodeId\": 3, \"sensors\": [" +
                "{\"type\": \"humidity\", \"value\": 25.55, \"unit\": \"%\"}, " +
                "{\"type\": \"temperature\", \"value\": 22.5, \"unit\": \"C\"}" +
                "]}";

        List<SensorReading> readings = channel.extractSensorReadings(testData);

        assertNotNull(readings, "Readings should not be null");
        assertEquals(2, readings.size(), "There should be two sensor readings");

        System.out.println(readings);
        SensorReading humidityReading = readings.get(0);
        assertEquals("humidity", humidityReading.getType());
        assertEquals(25.55, humidityReading.getValue());
        assertEquals("%", humidityReading.getUnit());

        SensorReading temperatureReading = readings.get(1);
        assertEquals("temperature", temperatureReading.getType());
        assertEquals(22.5, temperatureReading.getValue());
        assertEquals("C", temperatureReading.getUnit());
    }

    @Test
    public void testRemoveFirstObject() {
        String originalJson = "[{\"type\": \"humidity\", \"value\": 25.55, \"unit\": \"%\"}, {\"type\": \"temperature\", \"value\": 22.5, \"unit\": \"C\"}]";
        String expectedJson = "[ {\"type\": \"temperature\", \"value\": 22.5, \"unit\": \"C\"}]";

        String result = ServerCommunicationChannel.removeFirstObject(originalJson);

        assertEquals(expectedJson, result, "The first object should be removed correctly");
    }

    @Test
    public void testEmptyJson() {
        String originalJson = "[]";
        String expectedJson = "[]";

        String result = ServerCommunicationChannel.removeFirstObject(originalJson);

        assertEquals(expectedJson, result, "Empty JSON should remain unchanged");
    }

    @Test
    public void testInvalidJson() {
        String originalJson = "Not a JSON";
        String expectedJson = "Not a JSON";

        String result = ServerCommunicationChannel.removeFirstObject(originalJson);

        assertEquals(expectedJson, result, "Invalid JSON should remain unchanged");
    }

    @Test
    public void testJsonWithoutObjects() {
        String originalJson = "[[], []]";
        String expectedJson = "[[], []]";

        String result = ServerCommunicationChannel.removeFirstObject(originalJson);

        assertEquals(expectedJson, result, "JSON without objects should remain unchanged");
    }

    @Test
    public void testSingleJsonObject() {
        String originalJson = "[{\"type\": \"humidity\", \"value\": 25.55, \"unit\": \"%\"}]";
        String expectedJson = "[]";

        String result = ServerCommunicationChannel.removeFirstObject(originalJson);

        assertEquals(expectedJson, result, "JSON with a single object should become empty");
    }
}
