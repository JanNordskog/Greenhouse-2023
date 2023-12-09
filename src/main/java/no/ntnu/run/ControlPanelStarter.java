package no.ntnu.run;

import static no.ntnu.server.Server.PORT_NUMBER;

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
import no.ntnu.Message;
import no.ntnu.MessageSerializer;
import no.ntnu.controlpanel.CommunicationChannel;
import no.ntnu.controlpanel.FakeCommunicationChannel;
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
  private final boolean fake;
  private Socket socket;
  private ServerLogic logic;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;

  public ControlPanelStarter(boolean fake, ServerLogic logic) {
    this.fake = fake;
    this.logic = logic;
  }

  /**
   * Entrypoint for the application.
   *
   * @param args Command line arguments, only the first one of them used: when it is "fake",
   *             emulate fake events, when it is either something else or not present,
   *             use real socket communication.
   * @throws IOException
   * @throws FileNotFoundException
   * @throws CertificateException
   * @throws KeyStoreException
   * @throws NoSuchAlgorithmException
   * @throws KeyManagementException
   */
  public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
    boolean fake = false;
    if (args.length == 1 && "fake".equals(args[0])) {
      fake = true;
      Logger.info("Using FAKE events");
    }
    ServerLogic logic = new ServerLogic();
    ControlPanelStarter starter = new ControlPanelStarter(false, logic);
    starter.start();
  }

  public void start() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
    CommunicationChannel channel = initiateCommunication(logic, fake);
    ControlPanelApplication.startApp(logic, channel);
    // This code is reached only after the GUI-window is closed
    Logger.info("Exiting the control panel application");
    stopCommunication();
  }

  private CommunicationChannel initiateCommunication(ServerLogic logic, boolean fake) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
    CommunicationChannel channel;
    if (fake) {
      channel = initiateFakeSpawner(logic);
    } else {
      channel = initiateSocketCommunication(logic);
    }
    return channel;
  }

  private CommunicationChannel initiateSocketCommunication(ServerLogic logic) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
    try {
      SslConnection con = new SslConnection(PORT_NUMBER);
      Socket socket = con.client("localhost");
      this.socketWriter = new PrintWriter(socket.getOutputStream(), true);
      this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      ServerCommunicationChannel channel = new ServerCommunicationChannel(logic, socketReader);
      final int START_DELAY = 5;
      channel.spawnNode("4;3-window", START_DELAY);
      channel.spawnNode("1", START_DELAY + 1);
      channel.spawnNode("1", START_DELAY + 2);
      channel.advertiseSensorData("4;temperature=27.4 °C,temperature=26.8 °C,humidity=80 %", START_DELAY + 2);
      channel.spawnNode("8;2-heater", START_DELAY + 3);
      channel.advertiseActuatorState(4, 1, true, START_DELAY + 3);
      channel.advertiseActuatorState(4, 1, false, START_DELAY + 4);
      channel.advertiseActuatorState(4, 1, true, START_DELAY + 5);
      channel.advertiseActuatorState(4, 2, true, START_DELAY + 5);
      channel.advertiseActuatorState(4, 1, false, START_DELAY + 6);
      channel.advertiseActuatorState(4, 2, false, START_DELAY + 6);
      channel.advertiseActuatorState(4, 1, true, START_DELAY + 7);
      channel.advertiseActuatorState(4, 2, true, START_DELAY + 8);
      channel.advertiseSensorData("4;temperature=22.4 °C,temperature=26.0 °C,humidity=81 %", START_DELAY + 9);
      channel.advertiseSensorData("1;humidity=80 %,humidity=82 %", START_DELAY + 10);
      channel.advertiseRemovedNode(4, START_DELAY + 11);
      return channel;
    } catch (IOException e) {
      System.err.println("Could not connect to server " + e.getMessage());
      return null;
    }
  }

  private CommunicationChannel initiateFakeSpawner(ServerLogic logic) {
    // Here we pretend that some events will be received with a given delay
    FakeCommunicationChannel spawner = new FakeCommunicationChannel(logic);
    logic.setCommunicationChannel(spawner);
    final int START_DELAY = 5;
    spawner.spawnNode("4;3_window", START_DELAY);
    spawner.spawnNode("1", START_DELAY + 1);
    spawner.spawnNode("1", START_DELAY + 2);
    spawner.advertiseSensorData("4;temperature=27.4 °C,temperature=26.8 °C,humidity=80 %", START_DELAY + 2);
    spawner.spawnNode("8;2_heater", START_DELAY + 3);
    spawner.advertiseActuatorState(4, 1, true, START_DELAY + 3);
    spawner.advertiseActuatorState(4, 1, false, START_DELAY + 4);
    spawner.advertiseActuatorState(4, 1, true, START_DELAY + 5);
    spawner.advertiseActuatorState(4, 2, true, START_DELAY + 5);
    spawner.advertiseActuatorState(4, 1, false, START_DELAY + 6);
    spawner.advertiseActuatorState(4, 2, false, START_DELAY + 6);
    spawner.advertiseActuatorState(4, 1, true, START_DELAY + 7);
    spawner.advertiseActuatorState(4, 2, true, START_DELAY + 8);
    spawner.advertiseSensorData("4;temperature=22.4 °C,temperature=26.0 °C,humidity=81 %", START_DELAY + 9);
    spawner.advertiseSensorData("1;humidity=80 %,humidity=82 %", START_DELAY + 10);
    spawner.advertiseRemovedNode(8, START_DELAY + 11);
    spawner.advertiseRemovedNode(8, START_DELAY + 12);
    spawner.advertiseSensorData("1;temperature=25.4 °C,temperature=27.0 °C,humidity=67 %", START_DELAY + 13);
    spawner.advertiseSensorData("4;temperature=25.4 °C,temperature=27.0 °C,humidity=82 %", START_DELAY + 14);
    spawner.advertiseSensorData("4;temperature=25.4 °C,temperature=27.0 °C,humidity=82 %", START_DELAY + 16);
    return spawner;
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

  public boolean sendMessage(Message message) {
    boolean sent = false;

    try {
      socketWriter.println(MessageSerializer.toString(message));
      sent = true;
    } catch (Exception e) {
      System.err.println("Could not send message: " + e.getMessage());
    }

    return sent;
  }
}
