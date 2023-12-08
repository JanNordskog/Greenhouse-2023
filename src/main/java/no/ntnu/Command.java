package no.ntnu;

import no.ntnu.greenhouse.SensorActuatorNode;

public abstract class Command implements Message {

    public abstract Message execute(SensorActuatorNode node, int nodeId);
    
}
