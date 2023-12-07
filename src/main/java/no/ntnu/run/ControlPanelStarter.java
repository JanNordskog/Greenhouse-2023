package no.ntnu.run;

import no.ntnu.controlpanel.CommunicationChannel;
import no.ntnu.controlpanel.ControlPanelLogic;
import no.ntnu.controlpanel.FakeCommunicationChannel;
import no.ntnu.gui.controlpanel.ControlPanelApplication;
import no.ntnu.tools.Logger;

import java.io.IOException;

/**
 * Starter class for the control panel.
 * Note: we could launch the Application class directly, but then we would have issues with the
 * debugger (JavaFX modules not found)
 */
public class ControlPanelStarter {
  private final boolean fake;

  public ControlPanelStarter(boolean fake) {
    this.fake = fake;
  }

  /**
   * Entrypoint for the application.
   *
   * @param args Command line arguments, only the first one of them used: when it is "fake",
   *             emulate fake events, when it is either something else or not present,
   *             use real socket communication.
   */
  public static void main(String[] args) throws IOException {
    boolean fake = false;
    if (args.length == 1 && "fake".equals(args[0])) {
      fake = true;
      Logger.info("Using FAKE events");
    }
    ControlPanelStarter starter = new ControlPanelStarter(fake);
    starter.start();
  }

  private void start() throws IOException {
    ControlPanelLogic logic = new ControlPanelLogic();
    CommunicationChannel channel = initiateCommunication(logic, fake);
    ControlPanelApplication.startApp(logic, channel);
    // This code is reached only after the GUI-window is closed
    Logger.info("Exiting the control panel application");
    stopCommunication(channel);
  }

  private CommunicationChannel initiateCommunication(ControlPanelLogic logic, boolean fake) throws IOException {
    CommunicationChannel channel;
    if (fake) {
      channel = initiateFakeSpawner(logic);
    } else {
      channel = initiateSocketCommunication(logic);
    }
    return channel;
  }

  private CommunicationChannel initiateSocketCommunication(ControlPanelLogic logic) throws IOException {
    // Replace the following with your actual host and port configuration
    String host = "localhost";
    int port = 55000;

    SocketCommunicationChannel socketChannel = new SocketCommunicationChannel(logic, port);
    socketChannel.open(); // Open the socket connection
    return socketChannel;
  }

  private CommunicationChannel initiateFakeSpawner(ControlPanelLogic logic) {
    // Your existing fake spawner setup remains unchanged
    FakeCommunicationChannel spawner = new FakeCommunicationChannel(logic);
    logic.setCommunicationChannel(spawner);
    final int START_DELAY = 5;
    // ... (existing fake spawner setup)
    return spawner;
  }

  private void stopCommunication(CommunicationChannel channel) {
    channel.close(); // Close the communication channel
  }
}
