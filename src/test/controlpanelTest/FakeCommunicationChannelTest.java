import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import no.ntnu.controlpanel.*;
import no.ntnu.greenhouse.*;

public class FakeCommunicationChannelTest {
  private FakeCommunicationChannel fakeCommunicationChannel;

  private ControlPanelLogic controlPanelLogic;

  @BeforeEach
  public void setUp() {
    controlPanelLogic = new ControlPanelLogic();
    fakeCommunicationChannel = new FakeCommunicationChannel(controlPanelLogic);
  }

  @Test
  public void testSpawnNode() {
    String specification = "1;2_Light 3_Temperature";
    int delay = 5;

    fakeCommunicationChannel.spawnNode(specification, delay);

    // Sleep to allow the timer task to run
    try {
      Thread.sleep(delay * 1000 + 100); // Adding some extra time for execution
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Verify that the logic's onNodeAdded method is called after the specified delay
    List<GreenhouseEventListener> listeners = controlPanelLogic.getListeners();
    assertTrue(listeners.size() == 1);
    for (GreenhouseEventListener listener : listeners) {
      assertTrue(listener instanceof ControlPanelLogic);
    }
  }

  // Add similar tests for other methods like advertiseSensorData, advertiseRemovedNode, and advertiseActuatorState.
}
