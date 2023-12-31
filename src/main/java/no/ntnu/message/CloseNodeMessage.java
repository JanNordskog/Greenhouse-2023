package no.ntnu.message;

import no.ntnu.Message;

/**
 * Close node message.
 */
public class CloseNodeMessage implements Message {

  private int nodeId;

  public CloseNodeMessage(int nodeId) {
    this.nodeId = nodeId;
  }

  public int getNodeId() {
    return this.nodeId;
  }

}
