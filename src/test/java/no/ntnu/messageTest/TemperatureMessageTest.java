package no.ntnu.message;

public class TemperatureMessageTest {

  public static void main(String[] args) {
    testTemperatureMessageCreation();
  }

  private static void testTemperatureMessageCreation() {
    double testTemperature = 22.5;
    int testNodeId = 1;
    TemperatureMessage temperatureMessage = new TemperatureMessage(testTemperature, testNodeId);

    boolean testPassed = true;

    if (temperatureMessage.getTemperature() != testTemperature) {
      System.out.println("Temperature value test failed.");
      testPassed = false;
    }

    if (temperatureMessage.getNodeId() != testNodeId) {
      System.out.println("Node ID test failed.");
      testPassed = false;
    }

    if (testPassed) {
      System.out.println("TemperatureMessage creation test passed.");
    }
  }

}
