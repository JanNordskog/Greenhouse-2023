package no.ntnu.message;

import java.util.List;
import no.ntnu.Message;
import no.ntnu.greenhouse.ActuatorCollection;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;

public class NodeDataMessage implements Message {
    
    private SensorActuatorNode node;
    public NodeDataMessage(SensorActuatorNode node) {
        this.node = node;
    }

    public SensorActuatorNode getNode() {
        return this.node;
    }

    public int getId() {
        return this.node.getId();
    }

    public List<Sensor> getSensors() {
        return this.node.getSensors();
    }

    public ActuatorCollection getActuators() {
        return this.node.getActuators();
    }

}
