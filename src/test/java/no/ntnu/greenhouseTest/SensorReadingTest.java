package no.ntnu.greenhousetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import no.ntnu.greenhouse.SensorReading;
import org.junit.jupiter.api.Test;

/**
 * Test class for sensor readings.
 */
public class SensorReadingTest {

  @Test
  void testInitialization() {
    SensorReading reading = new SensorReading("Temperature", 25.5, "°C");

    assertEquals("Temperature", reading.getType(), "Sensor type should be 'Temperature'");
    assertEquals(25.5, reading.getValue(), 0.001, "Sensor value should be 25.5");
    assertEquals("°C", reading.getUnit(), "Sensor unit should be '°C'");
  }

  @Test
  void testSetValue() {
    SensorReading reading = new SensorReading("Humidity", 50.0, "%");
    reading.setValue(60.5);
    assertEquals(60.5, reading.getValue(), 0.001, "Sensor value should be 60.5 after setting");
  }

  @Test
  void testFormattedValue() {
    SensorReading reading = new SensorReading("Light", 1000.0, "lux");
    String formattedValue = reading.getFormatted();
    assertEquals("1000.0lux", formattedValue, "Formatted value should be '1000.0lux'");
  }

  @Test
  void testEquals() {
    SensorReading reading1 = new SensorReading("Pressure", 1003.2, "hPa");
    SensorReading reading2 = new SensorReading("Pressure", 1003.2, "hPa");
    assertTrue(reading1.equals(reading2), "Sensor readings should be equal");
    SensorReading reading3 = new SensorReading("Pressure", 1004.0, "hPa");
    assertFalse(reading1.equals(reading3), "Sensor readings should not be equal");
  }
}
