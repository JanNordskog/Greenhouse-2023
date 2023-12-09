package no.ntnu.greenhouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SensorActuatorNodeTest {
  private SensorActuatorNode node;

  @BeforeEach
  void setUp() {
    // Create a new SensorActuatorNode for each test
    node = new SensorActuatorNode(1);
  }

  @Test
  void testInitialization() {
    // Check if the node is initially not running
    assertFalse(node.isRunning(), "Node should not be running initially");

    // Check if the node ID is set correctly
    assertEquals(1, node.getId(), "Node ID should be 1");
  }

  @Test
  void testStartAndStop() {
    // Start the node and check if it's running
    node.start();
    assertTrue(node.isRunning(), "Node should be running after starting");

    // Stop the node and check if it's not running
    node.stop();
    assertFalse(node.isRunning(), "Node should not be running after stopping");
  }


  @Test
  void testAddActuator() {
    // Add an actuator
    Actuator actuator = new Actuator("Heater", 1);
    node.addActuator(actuator);

    // Check if the actuator is added correctly
    assertEquals(1, node.getActuators().size(), "Number of actuators added should be 1");
  }

  @Test
  void testToggleActuator() {
    // Add an actuator
    Actuator actuator = new Actuator("Heater", 1);
    node.addActuator(actuator);

    // Start the node and toggle the actuator
    node.start();
    node.toggleActuator(1);

    // Check if the actuator state is toggled
    assertTrue(actuator.isOn(), "Actuator should be ON after toggling");

    // Stop the node
    node.stop();
  }
}
