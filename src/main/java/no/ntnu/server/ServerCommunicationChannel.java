package no.ntnu.server;

import no.ntnu.controlpanel.CommunicationChannel;

public class ServerCommunicationChannel implements CommunicationChannel {

    @Override
    public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendActuatorChange'");
    }

    @Override
    public boolean open() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'open'");
    }
    
}
