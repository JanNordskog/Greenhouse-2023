package no.ntnu.toolstest;


import no.ntnu.tools.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

  @Test
  public void testParseIntegerOrErrorWithValidInput() {
    assertEquals(123, Parser.parseIntegerOrError("123", "Error parsing integer"));
  }

  @Test
  public void testParseIntegerOrErrorWithInvalidInput() {
    String errorMessage = "Error parsing integer";
    assertThrows(NumberFormatException.class, () -> {
      Parser.parseIntegerOrError("abc", errorMessage);
    }, errorMessage);
  }

  @Test
  public void testParseDoubleOrErrorWithValidInput() {
    assertEquals(123.45, Parser.parseDoubleOrError("123.45", "Error parsing double"));
  }

  @Test
  public void testParseDoubleOrErrorWithInvalidInput() {
    String errorMessage = "Error parsing double";
    assertThrows(NumberFormatException.class, () -> {
      Parser.parseDoubleOrError("abc", errorMessage);
    }, errorMessage);
  }
}

