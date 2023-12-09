import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.PeriodicSwitch;
import no.ntnu.greenhouse.SensorActuatorNode;

public class PeriodicSwitchTest {

  private PeriodicSwitch periodicSwitch;
  private SensorActuatorNode node;

  @BeforeEach
  public void setUp() {
    // Create a mock SensorActuatorNode for testing
    node = new SensorActuatorNode(1);

    // Create a PeriodicSwitch with a 1000ms delay (1 second) for testing
    periodicSwitch = new PeriodicSwitch("TestSwitch", node, 1, 1000);
  }

  @Test
  public void testStartAndStopSwitch() {
    // Start the periodic switch
    periodicSwitch.start();

    // Check if the switch started successfully
    assertTrue(periodicSwitch.isSwitchRunning());

    // Stop the switch
    periodicSwitch.stop();

    // Check if the switch stopped successfully
    assertFalse(periodicSwitch.isSwitchRunning());
  }

  @Test
  public void testSwitchToggle() {
    // Start the periodic switch
    periodicSwitch.start();

    // Wait for a few toggles to occur
    try {
      Thread.sleep(3000); // Wait for 3 seconds (3 toggles)
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Stop the switch
    periodicSwitch.stop();

    // Check if the actuator was toggled at least once
    assertTrue(node.getActuator(1).isToggled());
  }
}
