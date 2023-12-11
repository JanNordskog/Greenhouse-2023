package no.ntnu.greenhousetest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import no.ntnu.greenhouse.Actuator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for actuators.
 */
public class ActuatorTest {
  private Actuator actuator;

  @BeforeEach
  void setUp() {
    actuator = new Actuator("type 1", 1);
  }

  @Test
  void testToggle() {
    boolean initialState = actuator.isOn();
    actuator.toggle();
    assertEquals(!initialState, actuator.isOn(), "Toggle should change the state");
  }


}
