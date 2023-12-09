package no.ntnu.run;

import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.ActuatorCollection;
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

    /*
    @Test
    public void testExtractActuatorCollections_NormalCase() {
        ServerCommunicationChannel channel = new ServerCommunicationChannel(null, null);
        String testData = "{ \"nodeId\": 3, \"actuators\": [{ \"type\": \"heater\", \"id\": 4, \"state\": \"ON\" }] }";

        Actuator actuator = channel.extractActuator(testData);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should contain one actuator collection");

        ActuatorCollection actuatorCollection = result.get(0);
        assertNotNull(actuatorCollection, "Actuator collection should not be null");
        assertEquals(1, actuatorCollection.size(), "Actuator collection should contain one actuator");

        Actuator actuator = actuatorCollection.get(4); // Assuming id is 4 as per testData
        assertNotNull(actuator, "Actuator should not be null");
        assertEquals("heater", actuator.getType(), "Actuator type should be 'heater'");
        assertTrue(actuator.isOn(), "Actuator state should be ON");


    } */

    @Test
    public void testExtractActuator() {
        String data = "{\"nodeId\": 3, \"actuators\": [{ \"type\": \"heater\", \"id\": 4, \"state\": \"ON\" }] }";
        ServerCommunicationChannel channel = new ServerCommunicationChannel(null, null);

        Actuator actuator = channel.extractActuator(data);

        assertNotNull(actuator);
        assertEquals(4, actuator.getId());
        assertEquals("heater", actuator.getType());
        assertEquals(3, actuator.getNodeId());
    }

    @Test
    public void testExtractActuatorEmptyData() {
        String data = "{\"nodeId\": 3, \"actuators\": [] }";
        ServerCommunicationChannel channel = new ServerCommunicationChannel(null, null);

        Actuator actuator = channel.extractActuator(data);

        assertNull(actuator);
    }


}
