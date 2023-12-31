package no.ntnu.command;

import no.ntnu.Command;
import no.ntnu.Message;
import no.ntnu.message.ActuatorMessage;
import no.ntnu.message.ErrorMessage;
import no.ntnu.server.ServerLogic;

/**
 * Command to toggle an actuator on or off.
 */
public class ToggleActuatorCommand extends Command {

  private int nodeId;
  private int id;

  public ToggleActuatorCommand(int nodeId, int id) {
    this.nodeId = nodeId;
    this.id = id;
  }

  @Override
  public Message execute(ServerLogic logic) {
    Message message;
    try {
      logic.getNode(nodeId).getActuators().get(id).toggle();
      message = new ActuatorMessage(logic.getNode(nodeId).getId(),
          logic.getNode(nodeId).getActuators().get(id).getId(),
          logic.getNode(nodeId).getActuators().get(id).isOn());
    } catch (Exception e) {
      message = new ErrorMessage(e.getMessage());
    }

    return message;
  }

  public int getId() {
    return this.id;
  }

  public int getNodeId() {
    return this.nodeId;
  }

  public boolean isOn(ServerLogic logic) {
    return logic.getNode(nodeId).getActuators().get(id).isOn();
  }

}
