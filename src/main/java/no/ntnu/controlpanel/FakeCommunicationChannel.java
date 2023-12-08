package no.ntnu.controlpanel;

import no.ntnu.tools.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * A fake communication channel. Emulates the node discovery (over the Internet).
 * In practice - spawn some events at specified time (specified delay).
 * Note: this class is used only for debugging, you can remove it in your final project!
 */
public class FakeCommunicationChannel implements CommunicationChannel {

  private final no.ntnu.controlpanel.ControlPanelLogic logic;

  private Socket tcpSocket;
  private OutputStream outputStream;

  /**
   * Create a new fake communication channel.
   *
   * @param logic The application logic of the control panel node.
   */
  public FakeCommunicationChannel(no.ntnu.controlpanel.ControlPanelLogic logic) {
    this.logic = logic;
  }

  // Method to initialize the TCP connection
  private void initializeTCPConnection() {
    try {
      // Change the following values as needed (host and port)
      String host = "localhost";
      int port = 12345;

      tcpSocket = new Socket(host, port);
      outputStream = tcpSocket.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to send data over the TCP connection
  private void sendDataOverTCP(String data) {
    try {
      outputStream.write(data.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to close the TCP connection
  private void closeTCPConnection() {
    try {
      tcpSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // ... (rest of the class remains the same)

  @Override
  public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
    String state = isOn ? "ON" : "off";
    Logger.info("Sending command to greenhouse: turn " + state + " actuator"
            + "[" + actuatorId + "] on node " + nodeId);

    // Assuming you want to send the actuator change data over TCP
    String data = "ACTUATOR_CHANGE:" + nodeId + "," + actuatorId + "," + (isOn ? 1 : 0);
    sendDataOverTCP(data);
  }

  @Override
  public boolean open() {
    Logger.info("Opening fake communication channel...");

    // Initialize the TCP connection
    initializeTCPConnection();

    // Do other setup if needed

    return true;
  }

  @Override
  public void close() {
    Logger.info("Closing fake communication channel...");

    // Close the TCP connection
    closeTCPConnection();

    // Do other cleanup if needed
  }
}
