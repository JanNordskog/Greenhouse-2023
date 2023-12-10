package no.ntnu.listeners.common;

import no.ntnu.greenhouse.Actuator;

public class ActuatorListenerTest {

  public static void main(String[] args) {
    // Creating an anonymous class that implements ActuatorListener
    ActuatorListener listener = new ActuatorListener() {
      @Override
      public void actuatorUpdated(int nodeId, Actuator actuator) {
        System.out.println("Actuator updated: Node ID " + nodeId + ", Actuator ID " + actuator.getId());
      }
    };

    Actuator testActuator = new Actuator("testType", 1);

    // Trigger the actuatorUpdated method
    listener.actuatorUpdated(1, testActuator);

    // Check the console output to verify that the method is called correctly
  }
}
