package no.ntnu.greenhouse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import no.ntnu.listeners.greenhouse.NodeStateListener;
import no.ntnu.tools.Logger;

/**
 * A simulator for a greenhouse with the capability to send data to a control panel.
 */
public class GreenhouseSimulator {
  private final Map<Integer, SensorActuatorNode> nodes = new HashMap<>();

  private final List<PeriodicSwitch> periodicSwitches = new LinkedList<>();
  private final boolean fake;
  private ServerSocket serverSocket; // Server socket for communication with ControlPanelStarter

  /**
   * Create a greenhouse simulator.
   *
   * @param fake When true, simulate fake periodic events instead of creating socket communication
   */
  public GreenhouseSimulator(boolean fake) {
    this.fake = fake;
  }

  /**
   * Initialize the greenhouse but don't start the simulation just yet.
   */
  public void initialize() {
    createNode(1, 2, 1, 0, 0);
    createNode(1, 0, 0, 2, 1);
    createNode(2, 0, 0, 0, 0);
    Logger.info("Greenhouse initialized");

    // Initialize the server socket for communication with ControlPanelStarter
    try {
      serverSocket = new ServerSocket(55001); // Choose an appropriate port
      Logger.info("Server socket initialized for ControlPanelStarter");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void createNode(int temperature, int humidity, int windows, int fans, int heaters) {
    SensorActuatorNode node = DeviceFactory.createNode(
            temperature, humidity, windows, fans, heaters);
    nodes.put(node.getId(), node);
  }

  /**
   * Start a simulation of a greenhouse - all the sensor and actuator nodes inside it.
   */
  public void start() {
    initiateCommunication();
    for (SensorActuatorNode node : nodes.values()) {
      node.start();
    }
    for (PeriodicSwitch periodicSwitch : periodicSwitches) {
      periodicSwitch.start();
    }

    Logger.info("Simulator started");
  }

  private void initiateCommunication() {
    if (fake) {
      initiateFakePeriodicSwitches();
    } else {
      initiateRealCommunication();
    }
  }

  private void initiateRealCommunication() {
    // TODO - here you can set up the TCP or UDP communication
  }

  private void initiateFakePeriodicSwitches() {
    periodicSwitches.add(new PeriodicSwitch("Window DJ", nodes.get(1), 2, 20000));
    periodicSwitches.add(new PeriodicSwitch("Heater DJ", nodes.get(2), 7, 8000));
  }

  /**
   * Start the server to accept connections from ControlPanelStarter and send data.
   */
  public void startServer() {
    if (serverSocket != null) {
      new Thread(() -> {
        while (true) {
          try {
            Socket clientSocket = serverSocket.accept();
            Logger.info("ControlPanelStarter connected: " + clientSocket.getInetAddress());
            handleControlPanelConnection(clientSocket);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }).start();
    }
  }

  private void handleControlPanelConnection(Socket clientSocket) {
    try {
      OutputStream outputStream = clientSocket.getOutputStream();

      // Simulate sending data to ControlPanelStarter
      for (int i = 0; i < 10; i++) {
        String data = "Sensor " + i + " Reading: " + Math.random() * 100; // Replace with actual data
        outputStream.write(data.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        Thread.sleep(1000); // Simulate sending data every 1 second
      }

      outputStream.close();
      clientSocket.close();
      Logger.info("ControlPanelStarter disconnected");
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Stop the simulation of the greenhouse - all the nodes in it and close the server socket.
   */
  public void stop() {
    stopCommunication();
    for (SensorActuatorNode node : nodes.values()) {
      node.stop();
    }
    if (serverSocket != null) {
      try {
        serverSocket.close();
        Logger.info("Server socket closed");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void stopCommunication() {
    if (fake) {
      for (PeriodicSwitch periodicSwitch : periodicSwitches) {
        periodicSwitch.stop();
      }
    } else {
      // TODO - here you stop the TCP/UDP communication
    }
  }
}
