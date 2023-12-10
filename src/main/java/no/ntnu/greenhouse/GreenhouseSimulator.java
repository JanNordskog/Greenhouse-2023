package no.ntnu.greenhouse;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.SSLServerSocket;
import no.ntnu.Message;
import no.ntnu.listeners.greenhouse.NodeStateListener;
import no.ntnu.run.ClientHandler;
import no.ntnu.server.ServerLogic;
import no.ntnu.ssl.SslConnection;
import no.ntnu.tools.Logger;

/**
 * Application entrypoint - a simulator for a greenhouse.
 */
public class GreenhouseSimulator {

  private final List<PeriodicSwitch> periodicSwitches = new LinkedList<>();
  private final boolean fake;
  private SSLServerSocket server;
  private ServerLogic logic;
  private boolean isServerRunning = false;
  private List<ClientHandler> connectedClients = new ArrayList<>();

  public static final int PORT_NUMBER = 1002;

  /**
   * Create a greenhouse simulator.
   *
   * @param fake When true, simulate a fake periodic events instead of creating
   *             socket communication
   */
  public GreenhouseSimulator(boolean fake) {
    this.fake = fake;
    this.logic = new ServerLogic();
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
    logic.addNode(node.getId(), node);
  }

  /**
   * Start a simulation of a greenhouse - all the sensor and actuator nodes inside it.
   *
   * @throws CertificateException Exception.
   * @throws NoSuchAlgorithmException Exception.
   * @throws KeyStoreException Exception.
   * @throws KeyManagementException Exception.
   * @throws UnrecoverableKeyException Exception.
   */
  public void start() throws KeyManagementException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
    initiateCommunication();
    logic.start();
    for (PeriodicSwitch periodicSwitch : periodicSwitches) {
      periodicSwitch.start();
    }
    Logger.info("Simulator started");
  }

  private void initiateCommunication() throws KeyManagementException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
    if (fake) {
      initiateFakePeriodicSwitches();
    } else {
      initiateRealCommunication();
    }
  }

  private void initiateRealCommunication() {
    new Thread(() -> {
      try {
        SslConnection con = new SslConnection(PORT_NUMBER);
        this.server = con.server();
        Logger.info("Listening to port: " + this.server.getLocalPort());
        if (this.server != null) {
          this.isServerRunning = true;
          while (this.isServerRunning) {
            ClientHandler handler = acceptNextClientConnection(server);
            if (handler != null) {
              connectedClients.add(handler);
              logic.setClientHandlers(connectedClients);
              handler.start();
            }
          }
        }
  
      } catch (IOException | KeyStoreException | UnrecoverableKeyException
        | KeyManagementException | NoSuchAlgorithmException | CertificateException e) {
        System.err.println("Could not open server: " + e.getMessage());
      }
    }).start();
    
  }

  private ClientHandler acceptNextClientConnection(SSLServerSocket listeningSocket) {
    ClientHandler client = null;
    try {
      Socket clientSocket = listeningSocket.accept();
      Logger.info("New client connected from: " + clientSocket.getRemoteSocketAddress());
      client = new ClientHandler(clientSocket, this);
      return client;
    } catch (Exception e) {
      System.err.println("Could not accept client connection: " + e.getMessage());
      return null;
    }
  }

  private void initiateFakePeriodicSwitches() {
    periodicSwitches.add(new PeriodicSwitch("Window DJ", logic.getNode(1), 2, 20000));
    periodicSwitches.add(new PeriodicSwitch("Heater DJ", logic.getNode(2), 7, 8000));
  }

  /**
   * Stop the simulation of the greenhouse - all the nodes in it.
   */
  public void stop() {
    stopCommunication();
    logic.stop();
  }

  private void stopCommunication() {
    if (fake) {
      for (PeriodicSwitch periodicSwitch : periodicSwitches) {
        periodicSwitch.stop();
      }
      Logger.info("Stopped fake");
    } else {
      try {
        if (this.server != null) {
          this.server.close();
          this.server = null;
          this.isServerRunning = false;
          Logger.info("Stopping server");
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
    logic.subscribeToLifecycleUpdates(listener);
  }

  public ServerLogic getLogic() {
    return this.logic;
  }

  /**
   * Sends a response to all clients.
   *
   * @param response message to send to client.
   */
  public void sendResponseToClients(Message response) {
    for (ClientHandler c : connectedClients) {
      c.sendToClient(response);
    }
  }

  public void clientDisconnected(ClientHandler clientHandler) {
    connectedClients.remove(clientHandler);
  }
}
