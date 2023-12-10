package no.ntnu.messageTest;

import no.ntnu.message.ActuatorMessage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActuatorMessageTest {

  @Test
  public void testActuatorMessageCreation() {
    int nodeId = 1;
    int actuatorId = 2;
    boolean isOn = true;

    ActuatorMessage message = new ActuatorMessage(nodeId, actuatorId, isOn);

    // Testing getters
    assertEquals(nodeId, message.getNodeId(), "Node ID should match the constructor argument");
    assertEquals(actuatorId, message.getActuatorId(), "Actuator ID should match the constructor argument");
    assertEquals(isOn, message.isOn(), "Actuator state should match the constructor argument");
  }
}
