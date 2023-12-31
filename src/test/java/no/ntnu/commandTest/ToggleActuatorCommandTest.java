package no.ntnu.commandtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import no.ntnu.Message;
import no.ntnu.command.ToggleActuatorCommand;
import no.ntnu.message.ErrorMessage;
import no.ntnu.server.ServerLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for toggle command.
 */
public class ToggleActuatorCommandTest {

  private ServerLogic logic;
  private ToggleActuatorCommand command;
  private final int nodeId = 1;
  private final int actuatorId = 1;

  @BeforeEach
  public void setUp() {
    logic = new ServerLogic();
    command = new ToggleActuatorCommand(nodeId, actuatorId);
  }



  @Test
  public void testExecuteFailure() {
    // Setup the state in logic to ensure the command fails
    // For example, no nodes or actuators in the logic, or simulate an exception

    Message result = command.execute(logic);

    assertTrue(result instanceof ErrorMessage);
    // Further assertions to verify the error message
  }

  @Test
  public void testGetters() {
    assertEquals(nodeId, command.getNodeId());
    assertEquals(actuatorId, command.getId());
  }
}
