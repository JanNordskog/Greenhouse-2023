package no.ntnu.greenhouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActuatorCollectionTest {
  private ActuatorCollection collection;

  @BeforeEach
  void setUp() {
    collection = new ActuatorCollection();
  }

  @Test
  void testAddAndGetActuator() {
    Actuator actuator = new Actuator("type 1", 1);
    collection.add(actuator);
    assertEquals(actuator, collection.get(1), "Actuator should be retrieved correctly by ID");
  }


}
