package no.ntnu.listeners.controlpanel;

import no.ntnu.controlpanel.SensorActuatorNodeInfo;
import no.ntnu.greenhouse.SensorReading;

import java.util.ArrayList;
import java.util.List;

public class GreenhouseEventListenerTest {

  public static void main(String[] args) {
    // Creating an anonymous class that implements GreenhouseEventListener
    GreenhouseEventListener listener = new GreenhouseEventListener() {
      @Override
      public void onNodeAdded(SensorActuatorNodeInfo nodeInfo) {
        System.out.println("Node added: " + nodeInfo.getId());
      }

      @Override
      public void onNodeRemoved(int nodeId) {
        System.out.println("Node removed: " + nodeId);
      }

      @Override
      public void onSensorData(int nodeId, List<SensorReading> sensors) {
        System.out.println("Sensor data received from node " + nodeId + ": " + sensors.size() + " readings");
      }

      @Override
      public void onActuatorStateChanged(int nodeId, int actuatorId, boolean isOn) {
        String state = isOn ? "on" : "off";
        System.out.println("Actuator " + actuatorId + " on node " + nodeId + " turned " + state);
      }
    };

    // Example test calls
    listener.onNodeAdded(new SensorActuatorNodeInfo(1)); // Assuming SensorActuatorNodeInfo constructor takes an ID
    listener.onNodeRemoved(2);

    List<SensorReading> exampleSensorReadings = new ArrayList<>(); // Populate this list as needed
    listener.onSensorData(3, exampleSensorReadings);

    listener.onActuatorStateChanged(4, 5, true);

    // Check the console output to verify that the methods are called correctly
  }
}
