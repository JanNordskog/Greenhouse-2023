package no.ntnu.run;

import static no.ntnu.greenhouse.GreenhouseSimulator.PORT_NUMBER;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import no.ntnu.Command;
import no.ntnu.MessageSerializer;
import no.ntnu.command.RequestDataCommand;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.gui.controlpanel.ControlPanelApplication;
import no.ntnu.server.ServerCommunicationChannel;
import no.ntnu.server.ServerLogic;
import no.ntnu.ssl.SslConnection;
import no.ntnu.tools.Logger;

/**
 * Starter class for the control panel.
 * Note: we could launch the Application class directly, but then we would have issues with the
 * debugger (JavaFX modules not found)
 */
public class ControlPanelStarter {
  private Socket socket;
  private ServerLogic logic;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;
  private List<SensorActuatorNode> nodes;

  /**
   * Opens the control panel client.
   *
   * @param logic The client logic.
   */
  public ControlPanelStarter(ServerLogic logic) {
    this.logic = logic;
    this.nodes = new ArrayList<>();
  }

  /**
   * Entrypoint for the application.
   *
   * @param args Command line arguments, only the first one of them used: when it is "fake",
   *             emulate fake events, when it is either something else or not present,
   *             use real socket communication.
   * @throws IOException Exception.
   * @throws FileNotFoundException Exception.
   * @throws CertificateException Exception.
   * @throws KeyStoreException Exception.
   * @throws NoSuchAlgorithmException Exception.
   * @throws KeyManagementException Exception.
   */
  public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException,
      KeyStoreException, CertificateException, FileNotFoundException, IOException {
    ServerLogic logic = new ServerLogic();
    logic.start();
    ControlPanelStarter starter = new ControlPanelStarter(logic);
    starter.start();
  }

  /**
   * Starts the application.
   *
   * @throws KeyManagementException Exception.
   * @throws NoSuchAlgorithmException Exception.
   * @throws KeyStoreException Exception.
   * @throws CertificateException Exception.
   * @throws FileNotFoundException Exception.
   * @throws IOException Exception.
   */
  public void start() throws KeyManagementException, NoSuchAlgorithmException,
      KeyStoreException, CertificateException, FileNotFoundException, IOException {
    ServerCommunicationChannel channel = initiateCommunication(this.logic);
    logic.setCommunicationChannel(channel);
    channel.setNodes(nodes);
    sendCommand(new RequestDataCommand());
    this.requestNodeDataSchedule(5000);
    ControlPanelApplication.startApp(logic, channel);
    Logger.info("Exiting the control panel application");
    stopCommunication();
  }

  private ServerCommunicationChannel initiateCommunication(ServerLogic logic)
      throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
      CertificateException, FileNotFoundException, IOException {
    ServerCommunicationChannel channel;
    channel = initiateSocketCommunication(this.logic);
    return channel;
  }

  private ServerCommunicationChannel initiateSocketCommunication(ServerLogic logic)
      throws KeyManagementException, NoSuchAlgorithmException,
      KeyStoreException, CertificateException, FileNotFoundException, IOException {
    try {
      SslConnection con = new SslConnection(PORT_NUMBER);
      Socket socket = con.client("localhost");
      this.socketWriter = new PrintWriter(socket.getOutputStream(), true);
      this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      ServerCommunicationChannel channel = new ServerCommunicationChannel(this.logic,
          socketReader, socketWriter);
      return channel;
    } catch (IOException e) {
      System.err.println("Could not connect to server " + e.getMessage());
      return null;
    }
  }

  private void requestNodeDataSchedule(int delay) {
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        sendCommand(new RequestDataCommand());
      }
    }, delay, delay);
  }

  private void stopCommunication() {
    if (socket != null) {
      try {
        socket.close();
        socket = null;
        socketWriter = null;
        socketReader = null;
      } catch (IOException e) {
        System.err.println("Error closing socket: " + e.getMessage());
      }
    }
  }

  private boolean sendCommand(Command command) {
    boolean sent = false;

    try {
      String commandString = MessageSerializer.toString(command);
      if (commandString.length() > 0) {
        socketWriter.println(MessageSerializer.toString(command));
        sent = true;
      }
    } catch (Exception e) {
      System.err.println("Could not send message: " + e.getMessage());
    }

    return sent;
  }
}
