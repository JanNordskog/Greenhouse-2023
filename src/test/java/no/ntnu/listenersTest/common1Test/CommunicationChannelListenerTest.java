package no.ntnu.listeners.common;

public class CommunicationChannelListenerTest {

  public static void main(String[] args) {
    // Creating an anonymous class that implements CommunicationChannelListener
    CommunicationChannelListener listener = new CommunicationChannelListener() {
      @Override
      public void onCommunicationChannelClosed() {
        System.out.println("Communication channel closed event received.");
      }
    };

    // Trigger the onCommunicationChannelClosed event
    listener.onCommunicationChannelClosed();

    // Check the console output to verify that the method is called correctly
  }
}
