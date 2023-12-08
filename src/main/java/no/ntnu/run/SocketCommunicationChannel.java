package no.ntnu.run;

import no.ntnu.controlpanel.CommunicationChannel;

import no.ntnu.controlpanel.ControlPanelLogic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketCommunicationChannel implements CommunicationChannel {

    private final ControlPanelLogic logic;
    private final ServerSocket serverSocket;
    private Socket clientSocket;
    private OutputStream outputStream;

    private ExecutorService executorService;

    public SocketCommunicationChannel(ControlPanelLogic logic, int port) throws IOException {
        this.logic = logic;
        this.serverSocket = new ServerSocket(port);
        this.executorService = Executors.newFixedThreadPool(2);

        // Listen for incoming connections in a separate thread
        executorService.submit(this::acceptConnections);
    }

    private void acceptConnections() {
        try {
            // Accept incoming connections
            clientSocket = serverSocket.accept();
            outputStream = clientSocket.getOutputStream();

            // Start a new thread to handle incoming data
            executorService.submit(this::receiveData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveData() {
        try (InputStream inputStream = clientSocket.getInputStream()) {
            while (true) {
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);

                if (bytesRead == -1) {
                    // End of stream, connection closed
                    logic.onCommunicationChannelClosed();
                    break;
                }

                String receivedData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                // Process the received data as needed
                processReceivedData(receivedData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processReceivedData(String receivedData) {
        // Add your logic to handle the received data
        System.out.println("Received data: " + receivedData);
    }

    @Override
    public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
        String state = isOn ? "ON" : "off";
        String data = "ACTUATOR_CHANGE:" + nodeId + "," + actuatorId + "," + (isOn ? 1 : 0) + "\n";
        sendData(data);
    }

    private void sendData(String data) {
        try {
            outputStream.write(data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean open() {
        // Nothing specific to do for opening a socket connection as it is handled in the constructor
        return true;
    }

    @Override
    public void close() {
        try {
            // Close the server socket and associated resources
            serverSocket.close();
            clientSocket.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
