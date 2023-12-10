package no.ntnu.message;

import no.ntnu.MessageSerializer;

public class ActuatorMessageTest {

  public static void main(String[] args) {
    testActuatorMessageSerialization();
  }

  private static void testActuatorMessageSerialization() {
    ActuatorMessage actuatorMessage = new ActuatorMessage(1, 2, true);
    String serialized = MessageSerializer.toString(actuatorMessage);
    System.out.println("Serialized ActuatorMessage: " + serialized);
  }

  // You can add more methods if you need to test different scenarios
}
