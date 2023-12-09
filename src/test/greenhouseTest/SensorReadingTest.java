import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.SensorReading;

public class SensorReadingTest {

  private SensorReading sensorReading;

  @BeforeEach
  public void setUp() {
    // Create a SensorReading instance for testing
    sensorReading = new SensorReading("Temperature", 25.5, "°C");
  }

  @Test
  public void testGetters() {
    // Check if the getters return the expected values
    assertEquals("Temperature", sensorReading.getType());
    assertEquals(25.5, sensorReading.getValue());
    assertEquals("°C", sensorReading.getUnit());
  }

  @Test
  public void testSetValue() {
    // Set a new value for the sensor reading
    sensorReading.setValue(30.0);

    // Check if the new value is set correctly
    assertEquals(30.0, sensorReading.getValue());
  }

  @Test
  public void testToString() {
    // Check if the toString method returns the expected string representation
    assertEquals("{ type=Temperature, value=25.5, unit=°C }", sensorReading.toString());
  }

  @Test
  public void testGetFormatted() {
    // Check if the getFormatted method returns the expected formatted reading
    assertEquals("25.5°C", sensorReading.getFormatted());
  }

  @Test
  public void testEquals() {
    // Create another SensorReading instance with the same values
    SensorReading anotherReading = new SensorReading("Temperature", 25.5, "°C");

    // Check if the two instances are equal
    assertEquals(sensorReading, anotherReading);
  }

  @Test
  public void testNotEquals() {
    // Create another SensorReading instance with different values
    SensorReading anotherReading = new SensorReading("Humidity", 50.0, "%");

    // Check if the two instances are not equal
    assertNotEquals(sensorReading, anotherReading);
  }
}
