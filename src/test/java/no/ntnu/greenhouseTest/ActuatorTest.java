package no.ntnu.greenhouseTest;

import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.listeners.common.ActuatorListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
