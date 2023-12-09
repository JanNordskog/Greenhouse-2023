import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.ActuatorCollection;

public class ActuatorCollectionTest {
  private ActuatorCollection actuatorCollection;

  @BeforeEach
  public void setUp() {
    actuatorCollection = new ActuatorCollection();
  }

  @Test
  public void testAddAndGet() {
    Actuator actuator1 = new Actuator("Light", 1);
    Actuator actuator2 = new Actuator("Temperature", 2);

    actuatorCollection.add(actuator1);
    actuatorCollection.add(actuator2);

    assertEquals(actuator1, actuatorCollection.get(actuator1.getId()));
    assertEquals(actuator2, actuatorCollection.get(actuator2.getId()));
  }

  @Test
  public void testIterator() {
    Actuator actuator1 = new Actuator("Light", 1);
    Actuator actuator2 = new Actuator("Temperature", 2);

    actuatorCollection.add(actuator1);
    actuatorCollection.add(actuator2);

    int count = 0;
    for (Actuator actuator : actuatorCollection) {
      count++;
    }

    assertEquals(2, count);
  }

  @Test
  public void testSize() {
    Actuator actuator1 = new Actuator("Light", 1);
    Actuator actuator2 = new Actuator("Temperature", 2);

    actuatorCollection.add(actuator1);
    actuatorCollection.add(actuator2);

    assertEquals(2, actuatorCollection.size());
  }

  @Test
  public void testDebugPrint() {
    Actuator actuator1 = new Actuator("Light", 1);
    Actuator actuator2 = new Actuator("Temperature", 2);

    actuatorCollection.add(actuator1);
    actuatorCollection.add(actuator2);

    // Since this method prints to Logger, it's challenging to capture the output for testing.
    // You can manually inspect the output in the console when running the test.
    actuatorCollection.debugPrint();
  }
}
