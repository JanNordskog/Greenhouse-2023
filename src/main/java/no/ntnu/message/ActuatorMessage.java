package no.ntnu.message;

import no.ntnu.Message;

/**
 * Actuator message.
 */
public class ActuatorMessage implements Message {

  int nodeId;
  int actuatorId;
  boolean isOn;

  /**
   * Creates an actuator message.
   *
   * @param nodeId the id of the node connected to the actuator.
   * @param actuatorId the id of the actuator.
   * @param on the on or off status of the actuator. false if off, true if on.
   */
  public ActuatorMessage(int nodeId, int actuatorId, boolean on) {
    this.nodeId = nodeId;
    this.actuatorId = actuatorId;
    this.isOn = on;
  }

  public int getNodeId() {
    return this.nodeId;
  }

  public int getActuatorId() {
    return this.actuatorId;
  }

  public boolean isOn() {
    return isOn;
  }

}
