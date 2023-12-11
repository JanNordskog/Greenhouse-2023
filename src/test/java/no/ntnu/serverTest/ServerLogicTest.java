package no.ntnu.servertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.server.ServerLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test server logic.
 */
public class ServerLogicTest {

  private SensorActuatorNode node;
  private ServerLogic serverLogic;

  @BeforeEach
  public void setUp() {
    this.serverLogic = new ServerLogic();
  }

  @Test
  public void testAddNode() {
    int nodeId = 1;
    this.node = new SensorActuatorNode(nodeId);
    serverLogic.addNode(nodeId, node);
    assertEquals(1, serverLogic.getAmountOfNodes());
    assertSame(node, serverLogic.getNode(nodeId));
  }

  


}
