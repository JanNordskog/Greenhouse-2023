package no.ntnu.message;

import no.ntnu.Message;

public class TemperatureMessage implements Message{

    private double temperature;
    private int nodeId;
    
    public TemperatureMessage(double temperature, int nodeId) {
        this.temperature = temperature;
        this.nodeId = nodeId;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public int getNodeId() {
        return this.nodeId;
    }

}
