package no.ntnu.run;

import no.ntnu.controlpanel.CommunicationChannel;
import no.ntnu.controlpanel.ControlPanelLogic;
import no.ntnu.controlpanel.FakeCommunicationChannel;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.gui.controlpanel.ControlPanelApplication;
import no.ntnu.tools.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ControlPanelStarter {
    private final boolean fake;

    public ControlPanelStarter(boolean fake) {
        this.fake = fake;
    }

    public static void main(String[] args) {
        boolean fake = false;
        if (args.length == 1 && "fake".equals(args[0])) {
            fake = true;
            Logger.info("Using FAKE events");
        }
        ControlPanelStarter starter = new ControlPanelStarter(fake);
        starter.start();
    }

    private void start() {
        ControlPanelLogic logic = new ControlPanelLogic();
        CommunicationChannel channel = initiateCommunication(logic, fake);
        ControlPanelApplication.startApp(logic, channel);
        Logger.info("Exiting the control panel application");
        stopCommunication(channel);
    }

    private CommunicationChannel initiateCommunication(ControlPanelLogic logic, boolean fake) {
        CommunicationChannel channel;
        if (fake) {
            channel = initiateFakeSpawner(logic);
        } else {
            channel = initiateServerCommunication(logic);
        }
        return channel;
    }

    private CommunicationChannel initiateServerCommunication(ControlPanelLogic logic) {
        try {
            String serverHost = "127.0.0.1"; // Replace with your server's host address or IP
            int serverPort = 55001; // Replace with your server's port number

            Socket socket = new Socket(serverHost, serverPort);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ServerCommunicationChannel serverChannel = new ServerCommunicationChannel(logic, input);
            serverChannel.open();
            return serverChannel;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the error appropriately in your application
        }
    }

    private CommunicationChannel initiateFakeSpawner(ControlPanelLogic logic) {
        // ... (existing fake spawner setup)
        // No changes needed here
        return new FakeCommunicationChannel(logic);
    }

    private void stopCommunication(CommunicationChannel channel) {
        channel.close();
    }
}

// Additional class for ServerCommunicationChannel
class ServerCommunicationChannel implements CommunicationChannel {

    private final ControlPanelLogic logic;
    private final BufferedReader input;

    public ServerCommunicationChannel(ControlPanelLogic logic, BufferedReader input) {
        this.logic = logic;
        this.input = input;
    }

    @Override
    public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
        // Implement sending messages to server if needed
    }

    @Override
    public boolean open() {
        new Thread(this::listen).start();
        return true;
    }

    private void listen() {
        try {
            String line;
            while ((line = input.readLine()) != null) {
                // Handle incoming data from server
                processReceivedData(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processReceivedData(String data) {
        try {
            int nodeId = extractNodeId(data);
            List<SensorReading> sensorReadings = extractSensorReadings(data);
            logic.updateSensorData(nodeId, sensorReadings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int extractNodeId(String data) {
        // Extracting nodeId from the data string
        String nodeIdPart = data.substring(data.indexOf("\"nodeId\":") + 9, data.indexOf(","));
        return Integer.parseInt(nodeIdPart.trim());
    }

    public List<SensorReading> extractSensorReadings(String data) {
        List<SensorReading> readings = new ArrayList<>();

        String sensorsData = data.substring(data.indexOf("\"sensors\":") + 10, data.lastIndexOf("}]") + 2);

        while (!sensorsData.isEmpty() && sensorsData.contains("{") && sensorsData.contains("}")) {
            // Splitting at the first occurrence of "},"
            int splitIndex = sensorsData.indexOf("},");
            String sensor;
            if (splitIndex != -1) {
                sensor = sensorsData.substring(1, splitIndex + 1);
                sensorsData = sensorsData.substring(splitIndex + 2);
            } else {
                sensor = sensorsData.substring(1, sensorsData.length() - 1);
                sensorsData = "";
            }

            // Extract and process sensor data
            String type = removeFirstFourChars(extractValue(sensor, "type", "\","));
            String valueStr = extractSensorValue(sensor, type);
            if (!valueStr.isEmpty()) {
                try {
                    double value = Double.parseDouble(valueStr);
                    String unit = removeFirstFourChars(extractValue(sensor, "unit", "\"}"));
                    readings.add(new SensorReading(type, value, unit));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid sensor value format: " + valueStr);
                }
            } else {
                System.err.println("Sensor value not found for sensor: " + sensor);
            }
        }

        return readings;
    }


    private String extractSensorValue(String sensor, String type) {
        String valueStr;
        if (type.equals("temperature")) {
            // Assuming temperature values end with 'C'
            int valueEnd = sensor.indexOf("C");
            if (valueEnd != -1) {
                valueStr = sensor.substring(sensor.indexOf("\"value\":") + 8, valueEnd).trim();
            } else {
                // If 'C' not found, return empty string or log error
                return "";
            }
        } else { // For humidity and other types
            valueStr = extractValue(sensor, "\"value\":", ",").trim();
        }
        return valueStr.replaceAll("[^0-9.]", ""); // Remove non-numeric characters
    }

    private String extractValue(String data, String startDelimiter, String endDelimiter) {
        int start = data.indexOf(startDelimiter);
        if (start == -1) {
            return ""; // Start delimiter not found
        }

        start += startDelimiter.length(); // Move to the end of the start delimiter

        int end = data.indexOf(endDelimiter, start);
        if (end == -1) {
            return ""; // End delimiter not found after start
        }

        return data.substring(start, end).trim();
    }





    public static String removeFirstObject(String jsonString) {
        // Find the index of the first opening and closing braces
        int firstOpeningBrace = jsonString.indexOf('{');
        int firstClosingBrace = jsonString.indexOf('}', firstOpeningBrace);

        // Check if both braces are found
        if (firstOpeningBrace == -1 || firstClosingBrace == -1) {
            return jsonString; // Return original string if no object is found
        }

        // Build the new string
        StringBuilder modifiedString = new StringBuilder();
        modifiedString.append(jsonString.substring(0, firstOpeningBrace));

        // Check and handle comma after the first object
        if (firstClosingBrace + 1 < jsonString.length() && jsonString.charAt(firstClosingBrace + 1) == ',') {
            firstClosingBrace++;
        }

        modifiedString.append(jsonString.substring(firstClosingBrace + 1));

        return modifiedString.toString();
    }

    public static String removeFirstFourChars(String str) {
        // Check if the string is null or its length is less than 4
        if (str == null || str.length() <= 4) {
            return "";
        }

        // Using StringBuilder to construct the new string
        StringBuilder sb = new StringBuilder();
        for (int i = 4; i < str.length(); i++) {
            sb.append(str.charAt(i));
        }
        str =sb.toString();

        return str;
    }



    @Override
    public void close() {
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
