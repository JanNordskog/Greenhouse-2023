package no.ntnu.server;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.greenhouse.SensorActuatorNode;

public class ServerLogic {
    
    List<SensorActuatorNode> nodes = new ArrayList<>();

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

}
