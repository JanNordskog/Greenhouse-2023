package no.ntnu.listeners.greenhouse;

import no.ntnu.greenhouse.SensorActuatorNode;

public class NodeStateListenerTest {

  public static void main(String[] args) {
    // Creating an anonymous class that implements NodeStateListener
    NodeStateListener listener = new NodeStateListener() {
      @Override
      public void onNodeReady(SensorActuatorNode node) {
        System.out.println("Node is ready: " + node.getId());
      }

      @Override
      public void onNodeStopped(SensorActuatorNode node) {
        System.out.println("Node has stopped: " + node.getId());
      }
    };

    // Example test calls
    SensorActuatorNode exampleNode = new SensorActuatorNode(1); // Assuming SensorActuatorNode constructor takes an ID
    listener.onNodeReady(exampleNode);
    listener.onNodeStopped(exampleNode);

    // Check the console output to verify that the methods are called correctly
  }
}
