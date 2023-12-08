package no.ntnu.message;

import no.ntnu.Message;
import no.ntnu.greenhouse.Actuator;

public class ActuatorMessage implements Message {
    
    Actuator actuator;
    public ActuatorMessage(Actuator actuator) {
        this.actuator = actuator;
    }

    public boolean isOn() {
        return this.actuator.isOn();
    }

}
