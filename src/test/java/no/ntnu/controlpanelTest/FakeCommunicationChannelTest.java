package no.ntnu.controlpanel;

import java.lang.reflect.Method;

public class FakeCommunicationChannelTest {

  public static void main(String[] args) {
    testCreateSensorNodeInfoFromValidInput();
    testCreateSensorNodeInfoFromInvalidInput();
  }

  private static void testCreateSensorNodeInfoFromValidInput() {
    try {
      FakeCommunicationChannel channel = new FakeCommunicationChannel(null);
      Method method = FakeCommunicationChannel.class.getDeclaredMethod("createSensorNodeInfoFrom", String.class);
      method.setAccessible(true);

      String validSpecification = "1;2_switch";
      Object result = method.invoke(channel, validSpecification);

      assert result != null : "NodeInfo should not be null for valid input";
    } catch (Exception e) {
      assert false : "Exception should not be thrown for valid input: " + e.getMessage();
    }
  }

  private static void testCreateSensorNodeInfoFromInvalidInput() {
    try {
      FakeCommunicationChannel channel = new FakeCommunicationChannel(null);
      Method method = FakeCommunicationChannel.class.getDeclaredMethod("createSensorNodeInfoFrom", String.class);
      method.setAccessible(true);

      String invalidSpecification = "invalid_input";
      method.invoke(channel, invalidSpecification);

      assert false : "Exception should be thrown for invalid input";
    } catch (IllegalArgumentException e) {
      // Expected behavior
    } catch (Exception e) {
      assert false : "Incorrect exception type for invalid input: " + e.getMessage();
    }
  }

}
