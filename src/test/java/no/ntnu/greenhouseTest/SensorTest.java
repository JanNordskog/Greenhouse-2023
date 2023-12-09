package no.ntnu.greenhouse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SensorTest {

  @Test
  void testInitialization() {
    // Create a sensor
    Sensor sensor = new Sensor("Temperature", -10.0, 40.0, 20.0, "°C");

    // Check if the sensor is initialized correctly
    assertEquals("Temperature", sensor.getType(), "Sensor type should be 'Temperature'");
  }

  @Test
  void testCreateClone() {
    // Create a sensor
    Sensor sensor = new Sensor("Humidity", 0.0, 100.0, 50.0, "%");

    // Create a clone of the sensor
    Sensor clone = sensor.createClone();

    // Check if the clone has the same properties as the original
    assertEquals(sensor.getType(), clone.getType(), "Sensor types should be equal");
    assertEquals(sensor.getReading().getValue(), clone.getReading().getValue(), 0.001, "Sensor values should be equal");
    assertEquals(sensor.getReading().getUnit(), clone.getReading().getUnit(), "Sensor units should be equal");
  }

  @Test
  void testAddRandomNoise() {
    // Create a sensor
    Sensor sensor = new Sensor("Light", 0.0, 1000.0, 500.0, "lux");

    // Add random noise to the sensor
    sensor.addRandomNoise();

    // Check if the sensor value is within the allowed bounds
    assertTrue(sensor.getReading().getValue() >= 0.0 && sensor.getReading().getValue() <= 1000.0, "Sensor value should be within bounds");
  }

  @Test
  void testApplyImpact() {
    // Create a sensor
    Sensor sensor = new Sensor("Pressure", 900.0, 1100.0, 1000.0, "hPa");

    // Apply an impact to the sensor
    sensor.applyImpact(10.0);

    // Check if the sensor value is updated correctly
    assertEquals(1010.0, sensor.getReading().getValue(), 0.001, "Sensor value should be updated correctly");
  }
}
