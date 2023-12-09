package no.ntnu.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import no.ntnu.controlpanel.CommunicationChannel;
import no.ntnu.controlpanel.SensorActuatorNodeInfo;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.listeners.common.ActuatorListener;
import no.ntnu.listeners.common.CommunicationChannelListener;
import no.ntnu.listeners.controlpanel.GreenhouseEventListener;
import no.ntnu.tools.Logger;

public class ServerLogic implements ActuatorListener, CommunicationChannelListener, GreenhouseEventListener {
    
    private final List<GreenhouseEventListener> listeners = new LinkedList<>();
    List<SensorActuatorNode> nodes = new ArrayList<>();
    private CommunicationChannel communicationChannel;
    private CommunicationChannelListener channelListener;

    public ServerLogic() {

    }

    /**
     * Returns the node with the given id.
     * Returns null if no node is found.
     * @param nodeId Id of wanted ndoe.
     * @return Returns the node with the given id. null if not found.
     */
    public SensorActuatorNode getNode(int nodeId) {
        SensorActuatorNode node = null;
        for (SensorActuatorNode san : nodes) {
            if (san.getId() == nodeId) {
                node = san;
            }
        }

        return node;
    }

    public void addNode(SensorActuatorNode node) {
        if (!nodes.contains(node))
                nodes.add(node);
    }

    /**
     * Gets all the nodes connected to the server.
     * @return all the nodes connected to the server.
     */
    public List<SensorActuatorNode> getNodes() {
        return nodes;
    }

    public int getAmountOfNodes() {
        return nodes.size();
    }

    public void addListener(GreenhouseEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void setCommunicationChannelListener(CommunicationChannelListener listener) {
        this.channelListener = listener;
    }

    @Override
    public void onCommunicationChannelClosed() {
        Logger.info("Closing communication");
        if (channelListener != null) {
            channelListener.onCommunicationChannelClosed();
        }
    }

    @Override
    public void actuatorUpdated(int nodeId, Actuator actuator) {
        if (communicationChannel != null) {
            communicationChannel.sendActuatorChange(nodeId, actuator.getId(), actuator.isOn());
        }
    }

    public void setCommunicationChannel(CommunicationChannel channel) {
        this.communicationChannel = channel;
    }

    @Override
    public void onNodeAdded(SensorActuatorNodeInfo info) {
        listeners.forEach(listener -> listener.onNodeAdded(info));
    }

    @Override
    public void onNodeRemoved(int nodeId) {
        listeners.forEach(l -> l.onNodeRemoved(nodeId));
    }

    @Override
    public void onSensorData(int nodeId, List<SensorReading> sensors) {
        listeners.forEach(l -> l.onSensorData(nodeId, sensors));
    }

    @Override
    public void onActuatorStateChanged(int nodeId, int actuatorId, boolean isOn) {
        listeners.forEach(l -> l.onActuatorStateChanged(nodeId, actuatorId, isOn));
    }

}
