package no.ntnu.message;

import no.ntnu.Message;

public class HumidityMessage implements Message {

    private double humidity;
    
    public HumidityMessage(double humidity) {
        this.humidity = humidity;
    }

    public double getHumidity() {
        return this.humidity;
    }

}
