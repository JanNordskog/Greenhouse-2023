package no.ntnu.message;

import no.ntnu.Message;

public class ActuatorMessage implements Message {

    private boolean isOn;
    
    public ActuatorMessage(boolean isOn) {
        this.isOn = isOn;
    }

    public boolean isOn() {
        return isOn;
    }

}
