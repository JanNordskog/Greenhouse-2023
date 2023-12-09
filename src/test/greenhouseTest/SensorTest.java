import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.Sensor;

public class SensorTest {

  private Sensor sensor;

  @BeforeEach
  public void setUp() {
    // Create a Sensor instance for testing
    sensor = new Sensor("Temperature", 10.0, 30.0, 20.0, "°C");
  }

  @Test
  public void testGetters() {
    // Check if the getters return the expected values
    assertEquals("Temperature", sensor.getType());
    assertEquals(20.0, sensor.getReading().getValue());
  }

  @Test
  public void testCreateClone() {
    // Create a clone of the sensor
    Sensor clone = sensor.createClone();

    // Check if the clone has the same values as the original
    assertEquals(sensor.getType(), clone.getType());
    assertEquals(sensor.getReading().getValue(), clone.getReading().getValue());
  }

  @Test
  public void testAddRandomNoise() {
    // Save the initial value of the sensor
    double initialValue = sensor.getReading().getValue();

    // Add random noise to the sensor
    sensor.addRandomNoise();

    // Check if the value has changed (within bounds)
    double newValue = sensor.getReading().getValue();
    assertTrue(newValue >= 10.0 && newValue <= 30.0);
    assertNotEquals(initialValue, newValue);
  }

  @Test
  public void testApplyImpact() {
    // Save the initial value of the sensor
    double initialValue = sensor.getReading().getValue();

    // Apply an impact to the sensor
    sensor.applyImpact(5.0);

    // Check if the value has changed (within bounds)
    double newValue = sensor.getReading().getValue();
    assertTrue(newValue >= 10.0 && newValue <= 30.0);
    assertNotEquals(initialValue, newValue);
  }

  @Test
  public void testToString() {
    // Check if the toString method returns the expected string representation
    assertEquals("{ type=Temperature, value=20.0, unit=°C }", sensor.toString());
  }
}
