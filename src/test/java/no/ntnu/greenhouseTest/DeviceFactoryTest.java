package no.ntnu.greenhousetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.DeviceFactory;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for device factory.
 */
public class DeviceFactoryTest {
  private SensorActuatorNode node;

  @BeforeEach
  void setUp() {
    node = DeviceFactory.createNode(2, 1,
        3, 2, 1);
  }

  @Test
  void testCreateTemperatureSensor() {
    Sensor sensor = DeviceFactory.createTemperatureSensor();
    assertNotNull(sensor, "Temperature sensor should not be null");
    assertEquals("temperature", sensor.getType(), "Sensor type should be 'temperature'");
  }

  @Test
  void testCreateHumiditySensor() {
    Sensor sensor = DeviceFactory.createHumiditySensor();
    assertNotNull(sensor, "Humidity sensor should not be null");
    assertEquals("humidity", sensor.getType(), "Sensor type should be 'humidity'");
  }

  @Test
  void testCreateWindow() {
    Actuator window = DeviceFactory.createWindow(node.getId());
    assertNotNull(window, "Window actuator should not be null");
    assertEquals("window", window.getType(), "Actuator type should be 'window'");
  }

  @Test
  void testCreateFan() {
    Actuator fan = DeviceFactory.createFan(node.getId());
    assertNotNull(fan, "Fan actuator should not be null");
    assertEquals("fan", fan.getType(), "Actuator type should be 'fan'");
  }

  @Test
  void testCreateHeater() {
    Actuator heater = DeviceFactory.createHeater(node.getId());
    assertNotNull(heater, "Heater actuator should not be null");
    assertEquals("heater", heater.getType(), "Actuator type should be 'heater'");
  }
}
