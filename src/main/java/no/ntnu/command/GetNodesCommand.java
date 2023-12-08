package no.ntnu.command;

import no.ntnu.Command;
import no.ntnu.Message;
import no.ntnu.controlpanel.ControlPanelLogic;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.message.TemperatureMessage;
import no.ntnu.server.ServerLogic;

public class GetNodesCommand extends Command {

    Message m;
    
    @Override
    public Message execute(SensorActuatorNode node, int nodeId) {
        Message message;
        try {
            for (Sensor s : node.getSensors()) {

            }
            double temperature = node.getSensors().get(nodeId).getReading().getValue();
            message = new TemperatureMessage(temperature, 0);
        }
    }
    
}
