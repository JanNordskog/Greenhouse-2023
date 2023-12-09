import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.Actuator;

public class SensorActuatorNodeTest {

  private SensorActuatorNode node;

  @BeforeEach
  public void setUp() {
    // Create a SensorActuatorNode with a unique ID for testing
    node = new SensorActuatorNode(1);
  }

  @Test
  public void testAddSensors() {
    // Add 3 temperature sensors to the node
    node.addSensors(DeviceFactory.createTemperatureSensor(), 3);

    // Check if the node now has 3 sensors
    assertEquals(3, node.getSensors().size());
  }

  @Test
  public void testAddActuator() {
    // Create an actuator and add it to the node
    Actuator actuator = DeviceFactory.createWindow(node.getId());
    node.addActuator(actuator);

    // Check if the node now has the added actuator
    assertEquals(actuator, node.getActuators().get(actuator.getId()));
  }

  @Test
  public void testStartAndStopNode() {
    // Start the node
    node.start();

    // Check if the node is running
    assertTrue(node.isRunning());

    // Stop the node
    node.stop();

    // Check if the node is stopped
    assertFalse(node.isRunning());
  }

  @Test
  public void testToggleActuator() {
    // Create a window actuator and add it to the node
    Actuator windowActuator = DeviceFactory.createWindow(node.getId());
    node.addActuator(windowActuator);

    // Start the node
    node.start();

    // Toggle the window actuator
    node.toggleActuator(windowActuator.getId());

    // Check if the window actuator is toggled on
    assertTrue(windowActuator.isOn());
  }
}
