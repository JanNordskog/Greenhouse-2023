package no.ntnu.toolsTest;


import no.ntnu.tools.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @BeforeEach
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void testInfoLogsToSystemOut() {
    String testMessage = "Test Info";
    Logger.info(testMessage);
    assertEquals(testMessage + "\n", outContent.toString());
  }

  @Test
  public void testInfoNoNewlineLogsToSystemOut() {
    String testMessage = "Test Info No Newline";
    Logger.infoNoNewline(testMessage);
    assertEquals(testMessage, outContent.toString());
  }

  @Test
  public void testErrorLogsToSystemErr() {
    String testMessage = "Test Error";
    Logger.error(testMessage);
    assertEquals(testMessage + "\n", errContent.toString());
  }
}

