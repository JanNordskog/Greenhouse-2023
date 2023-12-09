import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.DeviceFactory;

public class DeviceFactoryTest {

  @Test
  public void testCreateNode() {
    SensorActuatorNode node = DeviceFactory.createNode(2, 1, 3, 1, 2);

    assertEquals(2, node.getTemperatureSensors().size());
    assertEquals(1, node.getHumiditySensors().size());
    assertEquals(3, node.getWindowActuators().size());
    assertEquals(1, node.getFanActuators().size());
    assertEquals(2, node.getHeaterActuators().size());
  }

  @Test
  public void testCreateTemperatureSensor() {
    Sensor sensor = DeviceFactory.createTemperatureSensor();

    assertEquals("temperature", sensor.getType());
    assertTrue(sensor.getMinValue() >= DeviceFactory.MIN_TEMPERATURE);
    assertTrue(sensor.getMaxValue() <= DeviceFactory.MAX_TEMPERATURE);
    assertTrue(sensor.getUnit().equals(DeviceFactory.TEMPERATURE_UNIT));
  }

  @Test
  public void testCreateHumiditySensor() {
    Sensor sensor = DeviceFactory.createHumiditySensor();

    assertEquals("humidity", sensor.getType());
    assertTrue(sensor.getMinValue() >= DeviceFactory.MIN_HUMIDITY);
    assertTrue(sensor.getMaxValue() <= DeviceFactory.MAX_HUMIDITY);
    assertTrue(sensor.getUnit().equals(DeviceFactory.HUMIDITY_UNIT));
  }

  @Test
  public void testCreateWindow() {
    Actuator actuator = DeviceFactory.createWindow(1);

    assertEquals("window", actuator.getType());
    assertTrue(actuator.getId() > 0);
    assertTrue(actuator.isOn() == false);
  }

  @Test
  public void testCreateFan() {
    Actuator actuator = DeviceFactory.createFan(1);

    assertEquals("fan", actuator.getType());
    assertTrue(actuator.getId() > 0);
    assertTrue(actuator.isOn() == false);
  }

  @Test
  public void testCreateHeater() {
    Actuator actuator = DeviceFactory.createHeater(1);

    assertEquals("heater", actuator.getType());
    assertTrue(actuator.getId() > 0);
    assertTrue(actuator.isOn() == false);
  }
}
