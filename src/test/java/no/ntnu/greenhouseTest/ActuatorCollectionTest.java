import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.ActuatorCollection;

public class ActuatorCollectionTest {

  private ActuatorCollection collection;
  private Actuator actuator1;
  private Actuator actuator2;

  @BeforeEach
  public void setUp() {
    // Create an ActuatorCollection instance for testing
    collection = new ActuatorCollection();

    // Create Actuator instances for testing
    actuator1 = new Actuator("Heater", 1);
    actuator2 = new Actuator("Fan", 2);

    // Add actuators to the collection
    collection.add(actuator1);
    collection.add(actuator2);
  }

  @Test
  public void testAddAndGet() {
    // Check if the actuators were added to the collection
    assertEquals(2, collection.size());

    // Check if we can retrieve an actuator by its ID
    Actuator retrievedActuator1 = collection.get(actuator1.getId());
    Actuator retrievedActuator2 = collection.get(actuator2.getId());

    assertNotNull(retrievedActuator1);
    assertNotNull(retrievedActuator2);

    // Check if the retrieved actuators match the original actuators
    assertEquals(actuator1, retrievedActuator1);
    assertEquals(actuator2, retrievedActuator2);
  }

  @Test
  public void testIterator() {
    // Check if the iterator returns all actuators in the collection
    int count = 0;
    for (Actuator actuator : collection) {
      if (actuator == actuator1 || actuator == actuator2) {
        count++;
      }
    }
    assertEquals(2, count);
  }

  @Test
  public void testSize() {
    // Check if the size method returns the correct number of actuators
    assertEquals(2, collection.size());

    // Add another actuator to the collection
    Actuator actuator3 = new Actuator("Light", 3);
    collection.add(actuator3);

    // Check if the size method updates correctly
    assertEquals(3, collection.size());
  }

  @Test
  public void testDebugPrint() {
    // Check if debugPrint() method prints actuator info without errors
    collection.debugPrint();
  }
}
