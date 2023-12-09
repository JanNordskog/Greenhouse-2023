package no.ntnu.run;

import no.ntnu.tools.Logger;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private ServerSocket serverSocket;
    private Socket controlPanelSocket;
    private PrintWriter controlPanelOut;
    private static int connectedNodeCount = 0;
    private static Set<String> uniqueNodeNames = new HashSet<>();
    private static CopyOnWriteArrayList<Socket> clientSockets = new CopyOnWriteArrayList<>();

    public void start(int port, int controlPanelPort) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is running on port " + port);

            new Thread(() -> {
                try (ServerSocket controlPanelServerSocket = new ServerSocket(controlPanelPort)) {
                    System.out.println("Waiting for control panel to connect on port " + controlPanelPort);
                    controlPanelSocket = controlPanelServerSocket.accept();
                    controlPanelOut = new PrintWriter(controlPanelSocket.getOutputStream(), true);
                    System.out.println("Control panel connected.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                connectedNodeCount++;
                System.out.println("Node connected. Total connected nodes: " + connectedNodeCount);
                new ClientHandler(clientSocket, this).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendDataToControlPanel(String data) {
        if (controlPanelOut != null) {
            controlPanelOut.println(data);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(55000, 55001);
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private final Server server;
        private BufferedReader in;

        public ClientHandler(Socket socket, Server server) {
            this.clientSocket = socket;
            this.server = server;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // Print the entire received string from the client
                    System.out.println("Received from client: " + inputLine);

                    // Then process the data as before
                    String formattedData = formatDataForControlPanel(inputLine);
                    if (formattedData != null) {
                        server.sendDataToControlPanel(formattedData);
                    }
                }

                in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clientSockets.remove(clientSocket);
            }
        }

        private String formatDataForControlPanel(String data) {
            if (data.contains("Actuator States")) {
                return processActuatorStates(data);
            }
            if (data.contains("Actuator States")) {
                // Handle actuator state data here
                // For example, log it, process it, or just return null if no further action is needed
                Logger.info("Received actuator state info: " + data);
                return null;
            }
            try {
                // Example input: "Node #1: 23.5°C 50%"
                String[] parts = data.split(":");
                String nodeIdPart = parts[0].trim();
                int nodeId = Integer.parseInt(nodeIdPart.split("#")[1].trim());

                String[] sensorReadings = parts[1].trim().split(" ");
                List<Map<String, String>> sensors = new ArrayList<>();
                for (String reading : sensorReadings) {
                    String[] readingParts = reading.split("°C|%");
                    Map<String, String> sensorData = new HashMap<>();
                    sensorData.put("type", reading.contains("°C") ? "temperature" : "humidity");
                    sensorData.put("value", readingParts[0]);
                    sensorData.put("unit", reading.contains("°C") ? "°C" : "%");
                    sensors.add(sensorData);
                }

                // Constructing a JSON-like string
                StringBuilder json = new StringBuilder();
                json.append("{ \"nodeId\": ").append(nodeId).append(", \"sensors\": [");
                for (int i = 0; i < sensors.size(); i++) {
                    Map<String, String> sensor = sensors.get(i);
                    json.append("{")
                            .append("\"type\": \"").append(sensor.get("type")).append("\", ")
                            .append("\"value\": ").append(sensor.get("value")).append(", ")
                            .append("\"unit\": \"").append(sensor.get("unit")).append("\"}");
                    if (i < sensors.size() - 1) {
                        json.append(", ");
                    }
                }
                json.append("]}");
                return json.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        private String processActuatorStates(String data) {
            // Assuming the format: "Node #3 Actuator States: [heater ID: 4 State: OFF]"
            Pattern pattern = Pattern.compile("Node #(\\d+) Actuator States: \\[(.*) ID: (\\d+) State: (ON|OFF)\\]");
            Matcher matcher = pattern.matcher(data);

            if (matcher.find()) {
                int nodeId = Integer.parseInt(matcher.group(1));
                String actuatorType = matcher.group(2).trim();
                int actuatorId = Integer.parseInt(matcher.group(3));
                String actuatorState = matcher.group(4);

                // Format into JSON-like structure
                return String.format("{ \"nodeId\": %d, \"actuators\": [{ \"type\": \"%s\", \"id\": %d, \"state\": \"%s\" }] }",
                        nodeId, actuatorType, actuatorId, actuatorState);
            }

            return null; // Return null if the pattern does not match
        }
    }
}
