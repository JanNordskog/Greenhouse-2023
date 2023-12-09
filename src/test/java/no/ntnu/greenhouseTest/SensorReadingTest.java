package no.ntnu.greenhouse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SensorReadingTest {

  @Test
  void testInitialization() {
    // Create a sensor reading
    SensorReading reading = new SensorReading("Temperature", 25.5, "°C");

    // Check if the sensor reading is initialized correctly
    assertEquals("Temperature", reading.getType(), "Sensor type should be 'Temperature'");
    assertEquals(25.5, reading.getValue(), 0.001, "Sensor value should be 25.5");
    assertEquals("°C", reading.getUnit(), "Sensor unit should be '°C'");
  }

  @Test
  void testSetValue() {
    // Create a sensor reading
    SensorReading reading = new SensorReading("Humidity", 50.0, "%");

    // Set a new value for the sensor reading
    reading.setValue(60.5);

    // Check if the new value is set correctly
    assertEquals(60.5, reading.getValue(), 0.001, "Sensor value should be 60.5 after setting");
  }

  @Test
  void testFormattedValue() {
    // Create a sensor reading
    SensorReading reading = new SensorReading("Light", 1000.0, "lux");

    // Get the formatted value
    String formattedValue = reading.getFormatted();

    // Check if the formatted value is correct
    assertEquals("1000.0lux", formattedValue, "Formatted value should be '1000.0lux'");
  }

  @Test
  void testEquals() {
    // Create two sensor readings with the same values
    SensorReading reading1 = new SensorReading("Pressure", 1003.2, "hPa");
    SensorReading reading2 = new SensorReading("Pressure", 1003.2, "hPa");

    // Check if the readings are equal
    assertTrue(reading1.equals(reading2), "Sensor readings should be equal");

    // Create a sensor reading with different values
    SensorReading reading3 = new SensorReading("Pressure", 1004.0, "hPa");

    // Check if the readings are not equal
    assertFalse(reading1.equals(reading3), "Sensor readings should not be equal");
  }
}
