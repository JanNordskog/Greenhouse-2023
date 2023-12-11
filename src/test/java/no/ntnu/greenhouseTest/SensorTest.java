package no.ntnu.greenhousetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import no.ntnu.greenhouse.Sensor;
import org.junit.jupiter.api.Test;

/**
 * Test for sensor class.
 */
public class SensorTest {

  @Test
  void testInitialization() {
    Sensor sensor = new Sensor("Temperature", -10.0, 40.0, 20.0, "°C");
    assertEquals("Temperature", sensor.getType(), "Sensor type should be 'Temperature'");
  }

  @Test
  void testCreateClone() {
    Sensor sensor = new Sensor("Humidity", 0.0, 100.0, 50.0, "%");

    Sensor clone = sensor.createClone();

    assertEquals(sensor.getType(), clone.getType(), "Sensor types should be equal");
    assertEquals(sensor.getReading().getValue(), clone.getReading().getValue(),
        0.001, "Sensor values should be equal");

    assertEquals(sensor.getReading().getUnit(),
        clone.getReading().getUnit(), "Sensor units should be equal");
  }

  @Test
  void testAddRandomNoise() {
    Sensor sensor = new Sensor("Light", 0.0, 1000.0, 500.0, "lux");
    sensor.addRandomNoise();
    assertTrue(sensor.getReading().getValue() >= 0.0 
        && sensor.getReading().getValue() <= 1000.0, "Sensor value should be within bounds");
  }

  @Test
  void testApplyImpact() {
    Sensor sensor = new Sensor("Pressure", 900.0, 1100.0, 1000.0, "hPa");

    sensor.applyImpact(10.0);

    assertEquals(1010.0, sensor.getReading().getValue(),
        0.001, "Sensor value should be updated correctly");
  }
}
