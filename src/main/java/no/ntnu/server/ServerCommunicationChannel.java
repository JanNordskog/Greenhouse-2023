package no.ntnu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import no.ntnu.Message;
import no.ntnu.MessageSerializer;
import no.ntnu.command.ToggleActuatorCommand;
import no.ntnu.controlpanel.CommunicationChannel;
import no.ntnu.controlpanel.SensorActuatorNodeInfo;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.message.ActuatorMessage;
import no.ntnu.message.CloseNodeMessage;
import no.ntnu.message.NodeDataMessage;
import no.ntnu.tools.Logger;
import no.ntnu.tools.Parser;

/**
 * Starts the communication channel to the server.
 */
public class ServerCommunicationChannel implements CommunicationChannel {

  private ServerLogic logic;
  private BufferedReader reader;
  private PrintWriter writer;
  private List<SensorActuatorNode> nodes;

  /**
   * Communication channel between client and server.
   *
   * @param logic Logic.
   * @param reader Reader.
   * @param writer Writer.
   */
  public ServerCommunicationChannel(ServerLogic logic, BufferedReader reader, PrintWriter writer) {
    this.reader = reader;
    this.logic = logic;
    this.writer = writer;
  }

  @Override
  public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
    String state = isOn ? "ON" : "off";
    Logger.info("Sending command to greenhouse: turn " + state + " actuator["
        + actuatorId + "] on node " + nodeId);  
    writer.println(MessageSerializer.toString(new ToggleActuatorCommand(nodeId, actuatorId)));
  }

  public void setNodes(List<SensorActuatorNode> nodes) {
    this.nodes = nodes;
  }

  @Override
  public boolean open() {
    new Thread(this::listen).start();
    return true;
  }

  private void listen() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        if (reader != null) {
          String rawMessage = reader.readLine();
          if (rawMessage == null) {
            break;
          }
          Message msg = MessageSerializer.fromString(rawMessage);
          Logger.info("Received message: " + rawMessage);
          handleIncomingMessage(msg, nodes);
        } else {
          Logger.error("Reader is null");
          break;
        }
      } catch (IOException e) {
        Logger.error("Error while receiving message: " + e.getMessage());
        break;
      } catch (Exception e) {
        Logger.error("Error processing message: " + e.getMessage());
      }
    }
  }

  private void handleIncomingMessage(Message msg, List<SensorActuatorNode> nodes) {
    if (msg instanceof NodeDataMessage nodeData) {
      SensorActuatorNode node = nodeData.getNode();
      boolean nodeExist = false;
      for (SensorActuatorNode san : nodes) {
        if (san.getId() == nodeData.getId()) {
          nodeExist = true;
        }
      }
      String actuatorString = nodeData.getId() + ";";
      int amountOfActuators = nodeData.getActuators().size();
      if (!nodeExist) {
        nodes.add(node);
        int i = 1;
        for (Actuator a : nodeData.getActuators()) {
          if (i < amountOfActuators) {
            actuatorString += a.getId() + "_" + a.getType() + ",";
          } else {
            actuatorString += a.getId() + "_" + a.getType();
          }
          i++;
        }
        Logger.info("Spawning node: " + actuatorString);
        //Spawning node: 2;3_fan,4_fan,5_heater,
        spawnNode(actuatorString, 0);

      } else {

        for (Actuator a : nodeData.getActuators()) {
          Logger.info("Node id: " + a.getNodeId() + ", Actuator id: "
              + a.getId() + ", Is on: " + a.isOn()
              + ", Type: " + a.getType());

          advertiseActuatorState(a.getNodeId(), a.getId(), a.isOn(), 0);
        }
      }

      String sensorString = nodeData.getId() + ";";

      for (int i = 0; i < nodeData.getSensors().size(); i++) {
        Sensor s = nodeData.getSensors().get(i);
        if (i < nodeData.getSensors().size() - 1) {
          sensorString +=
              s.getType() + "=" + s.getReading().getValue() + " " + s.getReading().getUnit() + ",";
        } else {
          sensorString +=
              s.getType() + "=" + s.getReading().getValue() + " " + s.getReading().getUnit();
        }
      }
      Logger.info("Advertising sensor data: " + sensorString);
      advertiseSensorData(sensorString, 0);
    } else if (msg instanceof ActuatorMessage am) {
      advertiseActuatorState(am.getNodeId(), am.getActuatorId(), am.isOn(), 0);
    } else if (msg instanceof CloseNodeMessage cnm) {
      advertiseRemovedNode(cnm.getNodeId(), 0);
    }
  }

  private void spawnNode(String specification, int delay) {
    SensorActuatorNodeInfo info = createSensorNodeInfoFrom(specification);
    logic.onNodeAdded(info);
  }

  private void advertiseSensorData(String specification, int delay) {
    if (specification == null || specification.isEmpty()) {
      throw new IllegalArgumentException("Sensor specification can't be empty");
    }
    String[] parts = specification.split(";");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Incorrect specification format: " + specification);
    }
    int nodeId = Parser.parseIntegerOrError(parts[0], "Invalid node ID:" + parts[0]);
    List<SensorReading> sensors = parseSensors(parts[1]);
    logic.onSensorData(nodeId, sensors);
  }

  private List<SensorReading> parseSensors(String sensorInfo) {
    List<SensorReading> readings = new LinkedList<>();
    String[] readingInfo = sensorInfo.split(",");
    for (String reading : readingInfo) {
      readings.add(parseReading(reading));
    }
    return readings;
  }

  private SensorActuatorNodeInfo createSensorNodeInfoFrom(String specification) {
    if (specification == null || specification.isEmpty()) {
      throw new IllegalArgumentException("Node specifications cant be empty");
    }
    String[] parts = specification.split(";");
    if (parts.length > 3) {
      throw new IllegalArgumentException("Incorrect specification format");
    }
    int nodeId = Parser.parseIntegerOrError(parts[0], "Invalid node ID: " + parts[0]);
    SensorActuatorNodeInfo info = new SensorActuatorNodeInfo(nodeId);
    if (parts.length == 2) {
      parseActuator(parts[1], info);
    }
    return info;
  }

  private void advertiseActuatorState(int nodeId, int actuatorId, boolean on, int delay) {
    logic.onActuatorStateChanged(nodeId, actuatorId, on);
  }

  private void advertiseRemovedNode(int nodeId, int delay) {
    logic.onNodeRemoved(nodeId);
  }

  private void parseActuator(String actuatorSpecification, SensorActuatorNodeInfo info) {
    String[] parts = actuatorSpecification.split(",");
    for (String part : parts) {
      parseActuatorInfo(part, info);
    }
  }

  private void parseActuatorInfo(String s, SensorActuatorNodeInfo info) {
    String[] actuatorInfo = s.split("_");
    if (actuatorInfo.length != 2) {
      throw new IllegalArgumentException("Invalid actuator info format: " + s);
    }
    int actuatorId =
        Parser.parseIntegerOrError(actuatorInfo[0],
        "Invalid actuator count: " + actuatorInfo[0]) - 1;
    String actuatorType = actuatorInfo[1];
    Actuator actuator = new Actuator(actuatorId, actuatorType, info.getId());
    actuator.setListener(logic);
    info.addActuator(actuator);
  }

  private SensorReading parseReading(String reading) {
    String[] assignmentParts = reading.split("=");
    if (assignmentParts.length != 2) {
      throw new IllegalArgumentException("Invalid sensor reading specified: " + reading);
    }
    String[] valueParts = assignmentParts[1].split(" ");
    if (valueParts.length != 2) {
      throw new IllegalArgumentException("Invalid sensor value/unit: " + reading);
    }
    String sensorType = assignmentParts[0];
    double value =
        Parser.parseDoubleOrError(valueParts[0], "Invalid sensor value: " + valueParts[0]);
    String unit = valueParts[1];
    return new SensorReading(sensorType, value, unit);
  }

}
