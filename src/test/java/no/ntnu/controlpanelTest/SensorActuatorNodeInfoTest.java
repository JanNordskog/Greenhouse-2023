package no.ntnu.controlpanelTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.controlpanel.*;
import no.ntnu.greenhouse.*;

public class SensorActuatorNodeInfoTest {
  private SensorActuatorNodeInfo sensorActuatorNodeInfo;

  @BeforeEach
  public void setUp() {
    sensorActuatorNodeInfo = new SensorActuatorNodeInfo(1);
  }

  @Test
  public void testAddActuator() {
    Actuator actuator = new Actuator("Light", 1);
    sensorActuatorNodeInfo.addActuator(actuator);

    ActuatorCollection actuators = sensorActuatorNodeInfo.getActuators();
    assertTrue(actuators.size() == 1);
    assertTrue(actuators.contains(actuator));
  }

  @Test
  public void testGetId() {
    int nodeId = sensorActuatorNodeInfo.getId();
    assertEquals(1, nodeId);
  }

  @Test
  public void testGetActuators() {
    Actuator actuator1 = new Actuator("Light", 1);
    Actuator actuator2 = new Actuator("Temperature", 2);
    sensorActuatorNodeInfo.addActuator(actuator1);
    sensorActuatorNodeInfo.addActuator(actuator2);

    ActuatorCollection actuators = sensorActuatorNodeInfo.getActuators();
    assertTrue(actuators.size() == 2);
    assertTrue(actuators.contains(actuator1));
    assertTrue(actuators.contains(actuator2));
  }

  @Test
  public void testGetActuator() {
    Actuator actuator1 = new Actuator("Light", 1);
    Actuator actuator2 = new Actuator("Temperature", 2);
    sensorActuatorNodeInfo.addActuator(actuator1);
    sensorActuatorNodeInfo.addActuator(actuator2);

    Actuator retrievedActuator = sensorActuatorNodeInfo.getActuator(1);
    assertNotNull(retrievedActuator);
    assertEquals("Light", retrievedActuator.getType());
    assertEquals(1, retrievedActuator.getId());

    retrievedActuator = sensorActuatorNodeInfo.getActuator(2);
    assertNotNull(retrievedActuator);
    assertEquals("Temperature", retrievedActuator.getType());
    assertEquals(2, retrievedActuator.getId());

    Actuator nonExistentActuator = sensorActuatorNodeInfo.getActuator(3);
    assertNull(nonExistentActuator);
  }
}
