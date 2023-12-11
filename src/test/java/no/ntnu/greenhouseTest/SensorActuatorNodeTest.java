package no.ntnu.greenhousetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import no.ntnu.greenhouse.SensorActuatorNode;
import org.junit.Test;

/**
 * Test class for nodes.
 */
public class SensorActuatorNodeTest {
  
  @Test
  void testGetId() {
    SensorActuatorNode node = new SensorActuatorNode(1);
    assertEquals(node.getId(), 1);
  }

  @Test
  void testIsRunningTrue() {
    SensorActuatorNode node = new SensorActuatorNode(1);
    node.start(null);

    assertTrue(node.isRunning());
  }

  @Test
  void testIsRunningFalse() {
    SensorActuatorNode node = new SensorActuatorNode(1);

    assertFalse(node.isRunning());
  }

}
