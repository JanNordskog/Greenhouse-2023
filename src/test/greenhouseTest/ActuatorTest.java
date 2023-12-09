import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.*;
import no.ntnu.listeners.common.ActuatorListener;

public class ActuatorTest {
  private Actuator actuator;

  @BeforeEach
  public void setUp() {
    actuator = new Actuator("Light", 1);
  }

  @Test
  public void testSetListener() {
    ActuatorListener listener = new ActuatorListener() {
      @Override
      public void actuatorUpdated(int nodeId, Actuator actuator) {
        // Listener implementation for testing
      }
    };

    actuator.setListener(listener);
    assertNotNull(actuator.getListener());
    assertEquals(listener, actuator.getListener());
  }

  @Test
  public void testSetImpact() {
    actuator.setImpact("Temperature", 2.0);

    assertTrue(actuator.hasImpact("Temperature"));
    assertEquals(2.0, actuator.getImpact("Temperature"));
  }

  @Test
  public void testCreateClone() {
    actuator.setImpact("Humidity", 1.5);
    Actuator clonedActuator = actuator.createClone();

    assertNotNull(clonedActuator);
    assertEquals(actuator.getType(), clonedActuator.getType());
    assertEquals(actuator.getNodeId(), clonedActuator.getNodeId());
    assertTrue(clonedActuator.hasImpact("Humidity"));
    assertEquals(1.5, clonedActuator.getImpact("Humidity"));
  }

  @Test
  public void testToggle() {
    assertFalse(actuator.isOn());
    actuator.toggle();
    assertTrue(actuator.isOn());
    actuator.toggle();
    assertFalse(actuator.isOn());
  }

  @Test
  public void testApplyImpact() {
    SensorActuatorNode node = new SensorActuatorNode(1);
    actuator.setImpact("CO2", -100.0);
    actuator.applyImpact(node);

    assertEquals(-100.0, node.getSensorReading("CO2"));
    actuator.toggle();
    actuator.applyImpact(node);
    assertEquals(100.0, node.getSensorReading("CO2"));
  }

  @Test
  public void testTurnOn() {
    assertFalse(actuator.isOn());
    actuator.turnOn();
    assertTrue(actuator.isOn());
  }

  @Test
  public void testTurnOff() {
    actuator.turnOn();
    assertTrue(actuator.isOn());
    actuator.turnOff();
    assertFalse(actuator.isOn());
  }

  @Test
  public void testSet() {
    assertFalse(actuator.isOn());
    actuator.set(true);
    assertTrue(actuator.isOn());
    actuator.set(false);
    assertFalse(actuator.isOn());
  }
}
