package no.ntnu.greenhousetest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.ActuatorCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Actuator collection test.
 */
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
