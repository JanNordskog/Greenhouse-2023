package no.ntnu.message;

public class HumidityMessageTest {

  public static void main(String[] args) {
    testHumidityMessageCreation();
  }

  private static void testHumidityMessageCreation() {
    double testHumidity = 55.5;
    int testNodeId = 1;
    HumidityMessage humidityMessage = new HumidityMessage(testHumidity, testNodeId);

    boolean testPassed = true;

    if (humidityMessage.getHumidity() != testHumidity) {
      System.out.println("Humidity value test failed.");
      testPassed = false;
    }

    if (humidityMessage.getNodeId() != testNodeId) {
      System.out.println("Node ID test failed.");
      testPassed = false;
    }

    if (testPassed) {
      System.out.println("HumidityMessage creation test passed.");
    }
  }

}
