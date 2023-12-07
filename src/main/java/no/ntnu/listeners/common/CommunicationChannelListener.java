package no.ntnu.listeners.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * A listener who will get notified about events happening on the communication channel.
 * The channel can be a TCP socket, or other types of channels.
 */
public interface CommunicationChannelListener {

  /**
   * This event is fired when the communication channel is closed.
   */
  void onCommunicationChannelClosed();
}

class TCPCommunicationChannelListener implements CommunicationChannelListener {

  private Socket socket;
  private InputStream inputStream;

  // Constructor to initialize the TCPCommunicationChannelListener with a TCP socket
  public TCPCommunicationChannelListener(Socket socket) throws IOException {
    this.socket = socket;
    this.inputStream = socket.getInputStream();

    // Start a separate thread to listen for incoming data
    startListeningThread();
  }

  // Method to start a separate thread for listening to incoming data
  private void startListeningThread() {
    Thread listenerThread = new Thread(() -> {
      try {
        while (true) {
          // Read data from the input stream
          int bytesRead = inputStream.read();

          // Check if the stream is closed
          if (bytesRead == -1) {
            // Notify the listener that the communication channel is closed
            onCommunicationChannelClosed();
            break;
          }

          // Process the received data as needed
          processReceivedData(bytesRead);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    listenerThread.start();
  }

  // Method to process the received data
  private void processReceivedData(int bytesRead) {
    // Add your logic to handle the received data
    System.out.println("Received data: " + bytesRead);
  }

  // Override the onCommunicationChannelClosed method
  @Override
  public void onCommunicationChannelClosed() {
    try {
      // Close the socket and any associated resources
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add any additional cleanup logic as needed

    System.out.println("Communication channel closed.");
  }
}
