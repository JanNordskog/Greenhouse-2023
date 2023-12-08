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

    private List<SensorReading> extractSensorReadings(String data) {
        List<SensorReading> readings = new ArrayList<>();
        String sensorsData = data.substring(data.indexOf("\"sensors\":") + 10, data.indexOf("}]") + 1);

        String[] sensors = sensorsData.split("\\},\\{");
        for (String sensor : sensors) {
            String type = extractValue(sensor, "\"type\":\"", "\"");
            String valueStr = extractValue(sensor, "\"value\":", ",").replaceAll("[^0-9.]", ""); // Remove non-numeric characters
            double value = Double.parseDouble(valueStr);
            String unit = extractValue(sensor, "\"unit\":\"", "\"");

            readings.add(new SensorReading(type, value, unit));
        }
        return readings;
    }


    private String extractValue(String data, String startDelimiter, String endDelimiter) {
        int start = data.indexOf(startDelimiter) + startDelimiter.length();
        int end = data.indexOf(endDelimiter, start);
        return data.substring(start, end).trim();
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
