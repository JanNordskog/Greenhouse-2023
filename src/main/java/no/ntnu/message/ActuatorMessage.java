package no.ntnu.message;

import no.ntnu.Message;
import no.ntnu.greenhouse.Actuator;

public class ActuatorMessage implements Message {
    
    Actuator actuator;
    public ActuatorMessage(Actuator actuator) {
        this.actuator = actuator;
    }

    public void toggle() {
        this.actuator.toggle();
    }

    public boolean isOn() {
        return this.actuator.isOn();
    }

}
