package no.ntnu.greenhouse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import no.ntnu.listeners.common.ActuatorListener;
import no.ntnu.listeners.common.CommunicationChannelListener;
import no.ntnu.listeners.greenhouse.NodeStateListener;
import no.ntnu.listeners.greenhouse.SensorListener;
import no.ntnu.tools.Logger;

/**
 * Represents one node with sensors and actuators.
 */
public class SensorActuatorNode implements ActuatorListener, CommunicationChannelListener {
  // How often to generate new sensor values, in seconds.
  private static final long SENSING_DELAY = 5000;
  private final int id;

  private final List<Sensor> sensors = new LinkedList<>();
  private final ActuatorCollection actuators = new ActuatorCollection();

  private final List<SensorListener> sensorListeners = new LinkedList<>();
  private final List<ActuatorListener> actuatorListeners = new LinkedList<>();
  private final List<NodeStateListener> stateListeners = new LinkedList<>();

  Timer sensorReadingTimer;

  private boolean running;
  private final Random random = new Random();


  private String serverAddress;
  private int serverPort;


  private Timer actuatorStateTimer;
  // Define the delay for actuator state reporting
  private static final long ACTUATOR_REPORT_DELAY = 10000; // 10 seconds, for example

  // Existing constructor and methods...

  // Modify the start method
  public void start() {
    if (!running) {
      startPeriodicSensorReading();
      startPeriodicActuatorReporting(); // Start actuator reporting
      running = true;
      notifyStateChanges(true);
    }
  }

  // Modify the stop method
  public void stop() {
    if (running) {
      stopPeriodicSensorReading();
      stopPeriodicActuatorReporting(); // Stop actuator reporting
      running = false;
      notifyStateChanges(false);
    }
  }

  // New method to start periodic actuator reporting
  private void startPeriodicActuatorReporting() {
    actuatorStateTimer = new Timer();
    TimerTask actuatorStateTask = new TimerTask() {
      @Override
      public void run() {
        reportActuatorStates();
      }
    };
    actuatorStateTimer.scheduleAtFixedRate(actuatorStateTask, ACTUATOR_REPORT_DELAY, ACTUATOR_REPORT_DELAY);
  }

  // New method to stop periodic actuator reporting
  private void stopPeriodicActuatorReporting() {
    if (actuatorStateTimer != null) {
      actuatorStateTimer.cancel();
    }
  }

  // New method to generate and send actuator state data
  private void reportActuatorStates() {
    StringBuilder sb = new StringBuilder();
    sb.append("Node #").append(id).append(" Actuator States: ");
    for (Actuator actuator : actuators) {
      sb.append("[").append(actuator.getType())
              .append(" ID: ").append(actuator.getId())
              .append(" State: ").append(actuator.isOn() ? "ON" : "OFF")
              .append("] ");
    }
    sendDataToServer(sb.toString());
  }


  public SensorActuatorNode(int id, String serverAddress, int serverPort) {
    this.id = id;
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
    this.running = false;
  }

  /**
   * Update the sensor readings of this node.
   *
   * @param newSensorReadings A list of new sensor readings.
   */
  public void updateSensorData(List<SensorReading> newSensorReadings) {
    for (SensorReading newReading : newSensorReadings) {
      Sensor sensor = findSensorByType(newReading.getType());
      if (sensor != null) {
        sensor.getReading().setValue(newReading.getValue());
        // Optionally, you could also update the unit, if it's expected to change
      } else {
        // Log or handle the case where a sensor type is not found
        Logger.error("Sensor type '" + newReading.getType() + "' not found on node " + id);
      }
    }

    // Notify all sensor listeners about the update
    notifySensorChanges();
  }

  /**
   * Find a sensor by its type.
   *
   * @param type The type of sensor to find.
   * @return The sensor if found, otherwise null.
   */
  private Sensor findSensorByType(String type) {
    for (Sensor sensor : sensors) {
      if (sensor.getType().equals(type)) {
        return sensor;
      }
    }
    return null;
  }

  private void sendDataToServer(String data) {
    try (Socket socket = new Socket(serverAddress, serverPort);
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      out.println(data);
    } catch (IOException e) {
      Logger.error("Error sending data to server: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Create a sensor/actuator node. Note: the node itself does not check whether the ID is unique.
   * This is done at the greenhouse-level.
   *
   * @param id A unique ID of the node
   */
  public SensorActuatorNode(int id) {
    this.id = id;
    this.running = false;
  }

  /**
   * Get the unique ID of the node.
   *
   * @return the ID
   */
  public int getId() {
    return id;
  }

  /**
   * Add sensors to the node.
   *
   * @param template The template to use for the sensors. The template will be cloned.
   *                 This template defines the type of sensors, the value range, value
   *                 generation algorithms, etc.
   * @param n        The number of sensors to add to the node.
   */
  public void addSensors(Sensor template, int n) {
    if (template == null) {
      throw new IllegalArgumentException("Sensor template is missing");
    }
    String type = template.getType();
    if (type == null || type.isEmpty()) {
      throw new IllegalArgumentException("Sensor type missing");
    }
    if (n <= 0) {
      throw new IllegalArgumentException("Can't add a negative number of sensors");
    }

    for (int i = 0; i < n; ++i) {
      sensors.add(template.createClone());
    }
  }

  /**
   * Add an actuator to the node.
   *
   * @param actuator The actuator to add
   */
  public void addActuator(Actuator actuator) {
    actuator.setListener(this);
    actuators.add(actuator);
    Logger.info("Created " + actuator.getType() + "[" + actuator.getId() + "] on node " + id);
  }

  /**
   * Register a new listener for sensor updates.
   *
   * @param listener The listener which will get notified every time sensor values change.
   */
  public void addSensorListener(SensorListener listener) {
    if (!sensorListeners.contains(listener)) {
      sensorListeners.add(listener);
    }
  }

  /**
   * Register a new listener for actuator updates.
   *
   * @param listener The listener which will get notified every time actuator state changes.
   */
  public void addActuatorListener(ActuatorListener listener) {
    if (!actuatorListeners.contains(listener)) {
      actuatorListeners.add(listener);
    }
  }

  /**
   * Register a new listener for node state updates.
   *
   * @param listener The listener which will get notified when the state of this node changes
   */
  public void addStateListener(NodeStateListener listener) {
    if (!stateListeners.contains(listener)) {
      stateListeners.add(listener);
    }
  }


  /**
   * Check whether the node is currently running.
   *
   * @return True if it is in a running-state, false otherwise
   */
  public boolean isRunning() {
    return running;
  }

  private void startPeriodicSensorReading() {
    sensorReadingTimer = new Timer();
    TimerTask newSensorValueTask = new TimerTask() {
      @Override
      public void run() {
        generateNewSensorValues();
      }
    };
    long randomStartDelay = random.nextLong(SENSING_DELAY);
    sensorReadingTimer.scheduleAtFixedRate(newSensorValueTask, randomStartDelay, SENSING_DELAY);
  }

  private void stopPeriodicSensorReading() {
    if (sensorReadingTimer != null) {
      sensorReadingTimer.cancel();
    }
  }

  /**
   * Generate new sensor values and send a notification to all listeners.
   */
  public void generateNewSensorValues() {
    Logger.infoNoNewline("Node #" + id);
    String sensorData = convertSensorDataToString();
    addRandomNoiseToSensors();
    notifySensorChanges();
    debugPrint();
    sendDataToServer(sensorData);
  }

  private String convertSensorDataToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Node #").append(id).append(": ");
    for (Sensor sensor : sensors) {
      sb.append(sensor.getReading().getFormatted()).append(" ");
    }
    // Add more details or format it as JSON/XML as per your requirement
    return sb.toString();
  }

  private void addRandomNoiseToSensors() {
    for (Sensor sensor : sensors) {
      sensor.addRandomNoise();
    }
  }

  private void debugPrint() {
    for (Sensor sensor : sensors) {
      Logger.infoNoNewline(" " + sensor.getReading().getFormatted());
    }
    Logger.infoNoNewline(" :");
    actuators.debugPrint();
    Logger.info("");
  }

  /**
   * Toggle an actuator attached to this device.
   *
   * @param actuatorId The ID of the actuator to toggle
   * @throws IllegalArgumentException If no actuator with given configuration is found on this node
   */
  public void toggleActuator(int actuatorId) {
    Actuator actuator = getActuator(actuatorId);
    if (actuator == null) {
      throw new IllegalArgumentException("actuator[" + actuatorId + "] not found on node " + id);
    }
    actuator.toggle();
  }

  private Actuator getActuator(int actuatorId) {
    return actuators.get(actuatorId);
  }

  private void notifySensorChanges() {
    for (SensorListener listener : sensorListeners) {
      listener.sensorsUpdated(sensors);
    }
  }

  @Override
  public void actuatorUpdated(int nodeId, Actuator actuator) {
    actuator.applyImpact(this);
    notifyActuatorChange(actuator);

    // Construct a message to send actuator state
    String actuatorState = "Node #" + nodeId + " Actuator #" + actuator.getId() +
            " State: " + (actuator.isOn() ? "ON" : "OFF");
    sendDataToServer(actuatorState);
  }


  private void notifyActuatorChange(Actuator actuator) {
    String onOff = actuator.isOn() ? "ON" : "off";
    Logger.info(" => " + actuator.getType() + " on node " + id + " " + onOff);
    for (ActuatorListener listener : actuatorListeners) {
      listener.actuatorUpdated(id, actuator);
    }
  }


  /**
   * Notify the listeners that the state of this node has changed.
   *
   * @param isReady When true, let them know that this node is ready;
   *                when false - that this node is shut down
   */
  private void notifyStateChanges(boolean isReady) {
    Logger.info("Notify state changes for node " + id);
    for (NodeStateListener listener : stateListeners) {
      if (isReady) {
        listener.onNodeReady(this);
      } else {
        listener.onNodeStopped(this);
      }
    }
  }

  /**
   * An actuator has been turned on or off. Apply an impact from it to all sensors of given type.
   *
   * @param sensorType The type of sensors affected
   * @param impact     The impact to apply
   */
  public void applyActuatorImpact(String sensorType, double impact) {
    for (Sensor sensor : sensors) {
      if (sensor.getType().equals(sensorType)) {
        sensor.applyImpact(impact);
      }
    }
  }

  /**
   * Get all the sensors available on the device.
   *
   * @return List of all the sensors
   */
  public List<Sensor> getSensors() {
    return sensors;
  }

  /**
   * Get all the actuators available on the node.
   *
   * @return A collection of the actuators
   */
  public ActuatorCollection getActuators() {
    return actuators;
  }

  @Override
  public void onCommunicationChannelClosed() {
    Logger.info("Communication channel closed for node " + id);
    stop();
  }

  /**
   * Set an actuator to a desired state.
   *
   * @param actuatorId ID of the actuator to set.
   * @param on         Whether it should be on (true) or off (false)
   */
  public void setActuator(int actuatorId, boolean on) {
    Actuator actuator = getActuator(actuatorId);
    if (actuator != null) {
      actuator.set(on);
    }
  }

  /**
   * Set all actuators to desired state.
   *
   * @param on Whether the actuators should be on (true) or off (false)
   */
  public void setAllActuators(boolean on) {
    for (Actuator actuator : actuators) {
      actuator.set(on);
    }
  }


}
