package no.ntnu.run;

import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;

public class NodeSimulation {
    public static void main(String[] args) {
        // Example server details
        String serverAddress = "localhost";
        int serverPort = 55000;

        // Create and start multiple nodes
        for (int i = 0; i < 5; i++) {
            SensorActuatorNode node = new SensorActuatorNode(i, serverAddress, serverPort);

            // Configure the node with sensors
            // Example: Adding a temperature sensor to each node
            Sensor temperatureSensor = new Sensor("temperature", 0, 50, 25, "C");
            node.addSensors(temperatureSensor, 1); // Adding one temperature sensor

            Sensor humiditysensor = new Sensor("humidity", 25, 70, 50, "%");
            node.addSensors(humiditysensor, 1); // Adding one temperature sensor


            // Configure the node with actuators
            // Example: Adding a heating actuator to each node
            Actuator heatingActuator = new Actuator("heater", i);
            heatingActuator.setImpact("temperature", 5); // Setting impact on temperature
            node.addActuator(heatingActuator);

            node.start();
        }

        // The nodes will now start sending data to the server at regular intervals
    }
}
