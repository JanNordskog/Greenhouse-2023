package no.ntnu.greenhouse;

import static no.ntnu.server.Server.PORT_NUMBER;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import no.ntnu.Message;
import no.ntnu.MessageSerializer;
import no.ntnu.listeners.greenhouse.NodeStateListener;
import no.ntnu.ssl.SslConnection;
import no.ntnu.tools.Logger;

/**
 * Application entrypoint - a simulator for a greenhouse.
 */
public class GreenhouseSimulator {
  private final Map<Integer, SensorActuatorNode> nodes = new HashMap<>();

  private final List<PeriodicSwitch> periodicSwitches = new LinkedList<>();
  private final boolean fake;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;
  private Socket socket;

  /**
   * Create a greenhouse simulator.
   *
   * @param fake When true, simulate a fake periodic events instead of creating
   *             socket communication
   */
  public GreenhouseSimulator(boolean fake) {
    this.fake = fake;
  }

  /**
   * Initialise the greenhouse but don't start the simulation just yet.
   */
  public void initialize() {
    createNode(1, 2, 1, 0, 0);
    createNode(1, 0, 0, 2, 1);
    createNode(2, 0, 0, 0, 0);
    Logger.info("Greenhouse initialized");
  }

  private void createNode(int temperature, int humidity, int windows, int fans, int heaters) {
    SensorActuatorNode node = DeviceFactory.createNode(
        temperature, humidity, windows, fans, heaters);
    nodes.put(node.getId(), node);
  }

  /**
   * Start a simulation of a greenhouse - all the sensor and actuator nodes inside it.
   * @throws CertificateException
   * @throws NoSuchAlgorithmException
   * @throws KeyStoreException
   * @throws KeyManagementException
   */
  public void start() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
    initiateCommunication();
    for (SensorActuatorNode node : nodes.values()) {
      node.start();
    }
    for (PeriodicSwitch periodicSwitch : periodicSwitches) {
      periodicSwitch.start();
    }

    Logger.info("Simulator started");
  }

  private void initiateCommunication() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
    if (fake) {
      initiateFakePeriodicSwitches();
    } else {
      initiateRealCommunication();
    }
  }

  private void initiateRealCommunication() throws KeyStoreException, KeyManagementException, NoSuchAlgorithmException, CertificateException {
    try {
      SslConnection con = new SslConnection(PORT_NUMBER);
      this.socket = con.client("localhost");
      socketWriter = new PrintWriter(this.socket.getOutputStream(), true);
      socketReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

    } catch (IOException e) {
      System.err.println("Could not connect to server: " + e.getMessage());
    }
  }

  private void initiateFakePeriodicSwitches() {
    periodicSwitches.add(new PeriodicSwitch("Window DJ", nodes.get(1), 2, 20000));
    periodicSwitches.add(new PeriodicSwitch("Heater DJ", nodes.get(2), 7, 8000));
  }

  /**
   * Stop the simulation of the greenhouse - all the nodes in it.
   */
  public void stop() {
    stopCommunication();
    for (SensorActuatorNode node : nodes.values()) {
      node.stop();
    }
  }

  private void stopCommunication() {
    if (fake) {
      for (PeriodicSwitch periodicSwitch : periodicSwitches) {
        periodicSwitch.stop();
      }
    } else {
      try {
        if (this.socket != null) {
          this.socket.close();
          this.socket = null;
          this.socketReader = null;
          this.socketWriter = null;
        }
      } catch (IOException e) {
        System.err.println("Error closing: " + e.getMessage());
      }
    }
  }

  /**
   * Add a listener for notification of node staring and stopping.
   *
   * @param listener The listener which will receive notifications
   */
  public void subscribeToLifecycleUpdates(NodeStateListener listener) {
    for (SensorActuatorNode node : nodes.values()) {
      node.addStateListener(listener);
    }
  }

  public boolean sendMessage(Message message) {
    boolean sent = false;

    if (socketWriter != null && socketReader != null) {
      try {
        socketWriter.println(MessageSerializer.toString(message));
        sent = true;
      } catch (Exception e) {
        System.err.println("Could not send message: " + e.getMessage());
      }
    }

    return sent;
  }
}
