package no.ntnu.message;

public class ErrorMessageTest {

  public static void main(String[] args) {
    testErrorMessageCreation();
  }

  private static void testErrorMessageCreation() {
    String testMessage = "Test error message";
    ErrorMessage errorMessage = new ErrorMessage(testMessage);

    if (testMessage.equals(errorMessage.getMessage())) {
      System.out.println("ErrorMessage creation test passed.");
    } else {
      System.out.println("ErrorMessage creation test failed.");
    }
  }

}

