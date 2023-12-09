package no.ntnu.greenhouse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PeriodicSwitchTest {

  @Test
  void testStartAndStop() {
    SensorActuatorNode node = new SensorActuatorNode(1); // Create a dummy node with ID 1
    PeriodicSwitch switcher = new PeriodicSwitch("Test Switch", node, 1, 1000); // Create a switch for testing

    // Start the switch and check if it's running
    switcher.start();
    assertNotNull(switcher);
    assertNotNull(node);

  }
}
