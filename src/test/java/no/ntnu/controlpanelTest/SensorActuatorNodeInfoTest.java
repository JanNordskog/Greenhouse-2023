package no.ntnu.controlpaneltest;

import no.ntnu.controlpanel.SensorActuatorNodeInfo;
import no.ntnu.greenhouse.Actuator;

public class SensorActuatorNodeInfoTest {

  public static void main(String[] args) {
    SensorActuatorNodeInfo nodeInfo = new SensorActuatorNodeInfo(1);
    System.out.println("Testing SensorActuatorNodeInfo with nodeId = 1");

    // Test: Get ID of the node
    if (nodeInfo.getId() == 1) {
      System.out.println("getId() passed.");
    } else {
      System.out.println("getId() failed.");
    }

    // Test: Add and get an actuator
    Actuator actuator = new Actuator("testType", 2);
    nodeInfo.addActuator(actuator);

    Actuator retrievedActuator = nodeInfo.getActuator(2);
    if (retrievedActuator != null && retrievedActuator.equals(actuator)) {
      System.out.println("addActuator() and getActuator() passed.");
    } else {
      System.out.println("addActuator() and getActuator() failed.");
    }
  }
}
