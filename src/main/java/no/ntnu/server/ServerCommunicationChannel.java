package no.ntnu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import no.ntnu.Message;
import no.ntnu.MessageSerializer;
import no.ntnu.command.ToggleActuatorCommand;
import no.ntnu.controlpanel.CommunicationChannel;
import no.ntnu.controlpanel.SensorActuatorNodeInfo;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.message.ActuatorMessage;
import no.ntnu.message.CloseNodeMessage;
import no.ntnu.message.HumidityMessage;
import no.ntnu.message.TemperatureMessage;
import no.ntnu.tools.Parser;

public class ServerCommunicationChannel implements CommunicationChannel {

    private ServerLogic logic;
    private BufferedReader reader;


    public ServerCommunicationChannel(ServerLogic logic, BufferedReader reader) {
        this.logic = logic;
        this.reader = reader;
    }

    @Override
    public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
        Message msg = new ActuatorMessage(nodeId, actuatorId, isOn);
        handleIncomingMessage(msg);
    }

    @Override
    public boolean open() {
        new Thread(this::listen).start();
        return true;
    }

    public void listen() {
        Message msg = null;
        do {
            try {
                if (reader != null) {
                    String rawMessage = reader.readLine();
                    msg = MessageSerializer.fromString(rawMessage);
                    handleIncomingMessage(msg);
                } else {
                    msg = null;
                }
            } catch (IOException e) {
                System.err.println("Error while recieving message " + e.getMessage());
            }
        } while (msg == null);
    }

    private void handleIncomingMessage(Message msg) {
        if (msg instanceof HumidityMessage humidityMessage) {
            ArrayList<SensorReading> reading = new ArrayList<>();
            reading.add(new SensorReading("humidity", humidityMessage.getHumidity(), "%"));
            logic.onSensorData(humidityMessage.getNodeId(), reading);
        } else if (msg instanceof TemperatureMessage temperatureMessage) {
            ArrayList<SensorReading> reading = new ArrayList<>();
            reading.add(new SensorReading("temperature", temperatureMessage.getTemperature(), "C"));
            logic.onSensorData(temperatureMessage.getNodeId(), reading);
        } else if (msg instanceof ToggleActuatorCommand toggleActuatorCommand) {
            toggleActuatorCommand.execute(logic);
            logic.onActuatorStateChanged(toggleActuatorCommand.getNodeId(),
                    toggleActuatorCommand.getId(), toggleActuatorCommand.isOn(logic));
        } else if (msg instanceof ActuatorMessage actuatorMessage) {
            logic.onActuatorStateChanged(actuatorMessage.getNodeId(),
                    actuatorMessage.getActuatorId(), actuatorMessage.isOn());
        } else if (msg instanceof CloseNodeMessage nodeMessage) {
            for (SensorActuatorNode s : logic.nodes) {
                logic.getNodes().remove(logic.getNode(s.getId()));
            }
        }
    }

    public void spawnNode(String specification, int delay) {
        SensorActuatorNodeInfo info = createSensorNodeInfoFrom(specification);
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Spawning node " + specification);
                logic.onNodeAdded(info);
            }
        }, delay * 1000L);
    }

    public void advertiseSensorData(String specification, int delay) {
        if (specification == null || specification.isEmpty()) {
            throw new IllegalArgumentException("Sensor specification can't be empty");
        }
        String[] parts = specification.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Incorrect specification format: " + specification);
        }
        int nodeId = Parser.parseIntegerOrError(parts[0], "Invalid node ID:" + parts[0]);
        List<SensorReading> sensors = parseSensors(parts[1]);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logic.onSensorData(nodeId, sensors);
            }
        }, delay * 1000L);
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

    public void advertiseActuatorState(int nodeId, int actuatorId, boolean on, int delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logic.onActuatorStateChanged(nodeId, actuatorId, on);
            }
        }, delay * 1000L);
    }

    public void advertiseRemovedNode(int nodeId, int delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logic.onNodeRemoved(nodeId);
            }
        }, delay * 1000L);
    }

    private void parseActuator(String actuatorSpecification, SensorActuatorNodeInfo info) {
        String[] parts = actuatorSpecification.split(" ");
        for (String part : parts) {
            parseActuatorInfo(part, info);
        }
    }

    private void parseActuatorInfo(String s, SensorActuatorNodeInfo info) {
        String[] actuatorInfo = s.split("-");
        if (actuatorInfo.length != 2) {
            throw new IllegalArgumentException("Invalid actuator info format: " + s);
        }
        int actuatorCount = Parser.parseIntegerOrError(actuatorInfo[0],
                "Invalid actuator count: " + actuatorInfo[0]);
        String actuatorType = actuatorInfo[1];

        for (int i = 0; i < actuatorCount; i++) {
            Actuator actuator = new Actuator(actuatorType, info.getId());
            actuator.setListener(logic);
            info.addActuator(actuator);
        }
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
