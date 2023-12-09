package no.ntnu.greenhouse;

import no.ntnu.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GreenhouseSimulatorTest {
  private GreenhouseSimulator simulator;

  @BeforeEach
  void setUp() {
    // Create a greenhouse simulator with the "fake" mode for testing
    simulator = new GreenhouseSimulator(true);
  }

  @Test
  void testInitialize() {
    simulator.initialize();
    assertNotNull(simulator, "Simulator should not be null");
  }


}
