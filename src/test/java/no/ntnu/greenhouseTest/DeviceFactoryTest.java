import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.*;

public class DeviceFactoryTest {

  private SensorActuatorNode node;

  @BeforeEach
  public void setUp() {
    // Create a sensor/actuator node using the DeviceFactory
    node = DeviceFactory.createNode(2, 1, 1, 1, 1);
  }

  @Test
  public void testCreateTemperatureSensor() {
    // Check if a temperature sensor is created with the correct type and values
    Sensor temperatureSensor = DeviceFactory.createTemperatureSensor();
    assertEquals("temperature", temperatureSensor.getType());
    assertTrue(temperatureSensor.getValue() >= DeviceFactory.MIN_TEMPERATURE);
    assertTrue(temperatureSensor.getValue() <= DeviceFactory.MAX_TEMPERATURE);
    assertEquals("°C", temperatureSensor.getUnit());
  }

  @Test
  public void testCreateHumiditySensor() {
    // Check if a humidity sensor is created with the correct type and values
    Sensor humiditySensor = DeviceFactory.createHumiditySensor();
    assertEquals("humidity", humiditySensor.getType());
    assertTrue(humiditySensor.getValue() >= DeviceFactory.MIN_HUMIDITY);
    assertTrue(humiditySensor.getValue() <= DeviceFactory.MAX_HUMIDITY);
    assertEquals("%", humiditySensor.getUnit());
  }

  @Test
  public void testCreateWindow() {
    // Check if a window actuator is created with the correct type and impact
    Actuator windowActuator = DeviceFactory.createWindow(node.getId());
    assertEquals("window", windowActuator.getType());
    assertEquals(-5.0, windowActuator.getImpacts().get("temperature"));
    assertEquals(-10.0, windowActuator.getImpacts().get("humidity"));
  }

  @Test
  public void testCreateFan() {
    // Check if a fan actuator is created with the correct type and impact
    Actuator fanActuator = DeviceFactory.createFan(node.getId());
    assertEquals("fan", fanActuator.getType());
    assertEquals(-1.0, fanActuator.getImpacts().get("temperature"));
  }

  @Test
  public void testCreateHeater() {
    // Check if a heater actuator is created with the correct type and impact
    Actuator heaterActuator = DeviceFactory.createHeater(node.getId());
    assertEquals("heater", heaterActuator.getType());
    assertEquals(4.0, heaterActuator.getImpacts().get("temperature"));
  }

  @Test
  public void testCreateNode() {
    // Check if a sensor/actuator node is created with the specified counts
    assertEquals(2, node.getTemperatureSensors().size());
    assertEquals(1, node.getHumiditySensors().size());
    assertEquals(1, node.getWindows().size());
    assertEquals(1, node.getFans().size());
    assertEquals(1, node.getHeaters().size());
  }
}
