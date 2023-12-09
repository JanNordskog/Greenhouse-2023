import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorActuatorNode;

public class ActuatorTest {

  private Actuator actuator;
  private SensorActuatorNode node;

  @BeforeEach
  public void setUp() {
    // Create a SensorActuatorNode instance for testing
    node = new SensorActuatorNode(1);

    // Create an Actuator instance for testing
    actuator = new Actuator("Heater", 1);
  }

  @Test
  public void testGetters() {
    // Check if the getters return the expected values
    assertEquals("Heater", actuator.getType());
    assertEquals(1, actuator.getNodeId());
    assertFalse(actuator.isOn());
    assertNotNull(actuator.getId());
  }

  @Test
  public void testSetImpact() {
    // Set an impact for a sensor type
    actuator.setImpact("temperature", 2.0);

    // Check if the impact was set correctly
    assertTrue(actuator.getImpacts().containsKey("temperature"));
    assertEquals(2.0, actuator.getImpacts().get("temperature"));
  }

  @Test
  public void testToggle() {
    // Toggle the actuator on
    actuator.toggle();

    // Check if the actuator is on
    assertTrue(actuator.isOn());

    // Toggle the actuator off
    actuator.toggle();

    // Check if the actuator is off
    assertFalse(actuator.isOn());
  }

  @Test
  public void testApplyImpact() {
    // Set an impact for a sensor type
    actuator.setImpact("temperature", 2.0);

    // Apply the impact to the node
    actuator.applyImpact(node);

    // Check if the impact was applied correctly to the node's sensors
    double temperatureValue = node.getSensors().stream()
        .filter(sensor -> "temperature".equals(sensor.getType()))
        .findFirst()
        .orElse(null)
        .getReading()
        .getValue();

    assertEquals(2.0, temperatureValue);
  }

  @Test
  public void testToString() {
    // Check if the toString method returns the expected string representation
    assertEquals("Actuator{type='Heater', on=false}", actuator.toString());
  }

  @Test
  public void testSet() {
    // Turn on the actuator
    actuator.set(true);

    // Check if the actuator is on
    assertTrue(actuator.isOn());

    // Turn off the actuator
    actuator.set(false);

    // Check if the actuator is off
    assertFalse(actuator.isOn());
  }
}
