package no.ntnu.serverTest;

import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.listeners.controlpanel.GreenhouseEventListener;
import no.ntnu.server.ServerLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class ServerLogicTest {

  @Mock
  private SensorActuatorNode mockNode;
  @Mock
  private GreenhouseEventListener mockListener;
  private ServerLogic serverLogic;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    serverLogic = new ServerLogic();
  }

  @Test
  public void testAddNode() {
    int nodeId = 1;
    serverLogic.addNode(nodeId, mockNode);
    assertEquals(1, serverLogic.getAmountOfNodes());
    assertSame(mockNode, serverLogic.getNode(nodeId));
  }


}
