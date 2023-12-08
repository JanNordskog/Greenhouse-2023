package no.ntnu.message;

import no.ntnu.Message;

public class HumidityMessage implements Message {

    private double humidity;
    private int nodeId;
    
    public HumidityMessage(double humidity, int nodeId) {
        this.humidity = humidity;
        this.nodeId = nodeId;
    }

    public double getHumidity() {
        return this.humidity;
    }

    public int getNodeId() {
        return this.nodeId;
    }

}
