package no.ntnu.run;

import no.ntnu.greenhouse.SensorReading;
import no.ntnu.controlpanel.ControlPanelLogic;
import no.ntnu.run.ServerCommunicationChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

// Mock Listener for testing


public class ControlPanelLogicTest {

    private ControlPanelLogic logic;



    @BeforeEach
    public void setUp() {
        logic = new ControlPanelLogic();

    }

    @Test
    public void testUpdateSensorData() {
        ServerCommunicationChannel channel = new ServerCommunicationChannel(null, null);
        String testData = "{ \"nodeId\": 3, \"sensors\": [" +
                "{\"type\": \"humidity\", \"value\": 25.55, \"unit\": \"%\"}, " +
                "{\"type\": \"temperature\", \"value\": 22.5, \"unit\": \"C\"}" +
                "]}";
        List<SensorReading> readings = channel.extractSensorReadings(testData);

        int testNodeId = 1;
        List<SensorReading> testReadings = new ArrayList<>();
        testReadings.add(new SensorReading("temperature", 25.0, "C"));
        testReadings.add(new SensorReading("humidity", 50.0, "%"));
        logic.updateSensorData(testNodeId, readings);

    }

    // Additional tests can be added here
}
