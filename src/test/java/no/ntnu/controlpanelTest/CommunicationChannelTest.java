package no.ntnu.controlpanel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommunicationChannelTest {

  @Test
  void testOpen() {
    // Create a mock implementation of the CommunicationChannel interface
    CommunicationChannel channel = new CommunicationChannel() {
      @Override
      public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
        // Mock implementation
      }

      @Override
      public boolean open() {
        // Mock implementation for opening the communication channel
        return true;
      }
    };

    // Try opening the communication channel
    boolean isOpened = channel.open();

    // Check if the channel is successfully opened
    assertTrue(isOpened, "Communication channel should be opened successfully");
  }

  @Test
  void testSendActuatorChange() {
    // Create a mock implementation of the CommunicationChannel interface
    CommunicationChannel channel = new CommunicationChannel() {
      @Override
      public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
        // Mock implementation
      }

      @Override
      public boolean open() {
        // Mock implementation for opening the communication channel
        return true;
      }
    };

    // Send an actuator change command
    channel.sendActuatorChange(1, 123, true);

    // No assertions are made here because the interface method is void.
  }
}
