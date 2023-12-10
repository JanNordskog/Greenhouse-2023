package no.ntnu;

import no.ntnu.command.RequestDataCommand;
import no.ntnu.command.ToggleActuatorCommand;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.message.ActuatorMessage;
import no.ntnu.message.CloseNodeMessage;
import no.ntnu.message.NodeDataMessage;
import no.ntnu.tools.Parser;

/**
 * Serialized messages and commands sent and recieved.
 */
public class MessageSerializer {
  private static final String humidityUnit = "%";
  private static final String temperatureUnit = "C";
  private static final String actuatorMessage = "M";
  private static final String closeNodeMessage = "N";
  private static final String SENSOR_NODE_INFO = "I";

  private static final String actuatorCommand = "A";
  private static final String requestDataCommand = "R";


  private MessageSerializer() {}

  /**
   * Turns a incoming message to its respected String format.
   *
   * @param message The message to turn into a string.
   * @return The message string.
   */
  public static String toString(Message message) {
    String m = "";
    if (message instanceof RequestDataCommand) {
      m = requestDataCommand;
    } else if (message instanceof NodeDataMessage nodeData) {
      String holder = SENSOR_NODE_INFO + nodeData.getId() + ";";
      for (Actuator a : nodeData.getActuators()) {
        holder += a.getId() + "_" + a.getType() + ",";
      }

      for (Sensor s : nodeData.getSensors()) {
        holder += s.getType() + "=" + s.getReading().getValue() + ",";
      }

      holder = holder.substring(0, holder.length() - 1);

      m = holder;
    } else if (message instanceof ToggleActuatorCommand ac) {
      m = actuatorCommand + ac.getNodeId() + "," + ac.getId();
    } else if (message instanceof ActuatorMessage am) {
      m = actuatorMessage + am.getNodeId() + "," + am.getActuatorId() + "," + am.isOn();
    }


    return m;
  }

  /**
   * Turns string to a message.
   *
   * @param s string to turn into a message.
   * @return message.
   */
  public static Message fromString(String s) {
    Message m = null;

    System.out.println(s);
    if (s != null) {
      if (s.startsWith(actuatorCommand)) {
        m = new ToggleActuatorCommand(parseInteger(s, 0), parseInteger(s, 1));
      } else if (s.startsWith(actuatorMessage)) {
        m = new ActuatorMessage(parseInteger(s, 0), parseInteger(s, 1), parseBoolean(s, 2));
      } else if (s.startsWith(closeNodeMessage)) {
        m = new CloseNodeMessage(parseInteger(s, 0));
      } else if (s.startsWith(requestDataCommand)) {
        m = new RequestDataCommand();
      } else if (s.startsWith(SENSOR_NODE_INFO)) {
        m = new NodeDataMessage(parseSensorFromData(s.substring(1)));
      }
    }

    return m;
  }

  /**
   * Makes a node from information in string format and returns the node.
   *
   * @param nodeInformation Information about a node in string format.
   * @return returns a node.
   */
  private static SensorActuatorNode parseSensorFromData(String nodeInformation) {
    String[] parts = nodeInformation.split(";");

    Integer nodeId =
        Parser.parseIntegerOrError(parts[0], "Could not find in for node :" + parts[0]);
    String[] containers = parts[1].split(",");
    SensorActuatorNode node = new SensorActuatorNode(nodeId);
    for (String s : containers) {
      if (s.contains("_")) {
        String[] actuatorParts = s.split("_");
        Actuator a = new Actuator(Parser.parseIntegerOrError(actuatorParts[0], 
            "Could not parse actuator ID"),
            actuatorParts[1], nodeId);
        node.addActuator(a);
      } else if (s.contains("=")) {
        String[] sensorParts = s.split("=");
        Sensor sen = null;
        if (sensorParts[0].equalsIgnoreCase("temperature")) {
          sen = new Sensor(sensorParts[0], 15, 40,
              Parser.parseDoubleOrError(sensorParts[1], "Could not parse temperature"), "C");
        } else if (sensorParts[0].equalsIgnoreCase("humidity")) {
          sen = new Sensor(sensorParts[0], 50, 100,
              Parser.parseDoubleOrError(sensorParts[1], "Could not parse humidity"), "%");
        } else {
          throw new IllegalArgumentException("Sensor not recognised");
        }

        node.addSensors(sen, 1);

      } else {
        throw new IllegalArgumentException(
            "Incorrect information format <" + nodeInformation + ">");
      }
    }

    return node;
  }

  private static boolean parseBoolean(String s, int position) {
    boolean bool = false;

    String[] split = s.split(",");
    try {
      bool = Boolean.parseBoolean(split[position]);
    } catch (Exception e) {
      System.err.println("Could not find boolean in <" + s + ">");
    }

    return bool;
  }

  private static boolean isUnitValid(String message) {
    boolean valid = false;
    if (message != null && message.length() > 0) {
      String[] split = message.split(",");
      char last = split[0].charAt(split[0].length() - 1);
      if (last == humidityUnit.charAt(0) || last == temperatureUnit.charAt(0)) {
        valid = true;
      }
    }

    return valid;
  }

  private static Integer parseInteger(String s, int position) {
    Integer i = null;
    String[] split;
    if (isUnitValid(s)) {
      split = s.substring(0, s.length() - 1).split(",");
    } else {
      split = s.substring(1).split(",");
    }
    try {
      Integer.valueOf(split[position]);
    } catch (Exception e) {
      System.err.println("Could not parse integer <" + s + ">");
    }

    return i;
  }
}
