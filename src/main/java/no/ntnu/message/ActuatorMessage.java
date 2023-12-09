package no.ntnu.message;

import no.ntnu.Message;

public class ActuatorMessage implements Message {
    
    int nodeId;
    int actuatorId;
    boolean isOn;

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
