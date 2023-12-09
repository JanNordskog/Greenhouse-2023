import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.listeners.greenhouse.NodeStateListener;

public class GreenhouseSimulatorTest {

  private GreenhouseSimulator simulator;

  @BeforeEach
  public void setUp() {
    simulator = new GreenhouseSimulator(true); // Use fake communication for testing
    simulator.initialize();
  }

  @Test
  public void testStartAndStopSimulation() {
    // Start the simulation
    simulator.start();

    // Check if the simulator started successfully
    assertTrue(simulator.isSimulationRunning());

    // Stop the simulation
    simulator.stop();

    // Check if the simulator stopped successfully
    assertFalse(simulator.isSimulationRunning());
  }

  @Test
  public void testSubscribeToLifecycleUpdates() {
    NodeStateListener listener = new NodeStateListener() {
      @Override
      public void onNodeStarted(int nodeId) {
        // Handle node started event
      }

      @Override
      public void onNodeStopped(int nodeId) {
        // Handle node stopped event
      }
    };

    // Subscribe the listener to lifecycle updates
    simulator.subscribeToLifecycleUpdates(listener);

    // Check if the listener is subscribed to all nodes
    assertEquals(simulator.getNodeCount(), listener.getSubscribedNodeIds().size());
  }
}
