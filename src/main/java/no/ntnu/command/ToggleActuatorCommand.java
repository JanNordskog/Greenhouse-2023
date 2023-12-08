package no.ntnu.command;

import no.ntnu.Command;
import no.ntnu.Message;
import no.ntnu.message.ActuatorMessage;
import no.ntnu.message.ErrorMessage;
import no.ntnu.server.ServerLogic;

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
            message = new ActuatorMessage(logic.getNode(nodeId).getActuators().get(id));
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
    
}
