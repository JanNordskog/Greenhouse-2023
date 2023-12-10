package no.ntnu.message;

import no.ntnu.Message;

/**
 * Error message.
 */
public class ErrorMessage implements Message {
  public final String message;

  public ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }

}
