package no.ntnu.listeners.greenhouse;

import no.ntnu.greenhouse.Sensor;

import java.util.ArrayList;
import java.util.List;

public class SensorListenerTest {

  public static void main(String[] args) {
    // Creating an anonymous class that implements SensorListener
    SensorListener listener = new SensorListener() {
      @Override
      public void sensorsUpdated(List<Sensor> sensors) {
        System.out.println("Sensors updated: " + sensors.size() + " sensors have new values.");
      }
    };

    // Example test calls
    List<Sensor> exampleSensors = new ArrayList<>();
    exampleSensors.add(new Sensor("Temperature", 20.0, 0.0, 0.0, "°C")); // Adjust these values as per the actual constructor
    exampleSensors.add(new Sensor("Humidity", 40.0, 0.0, 0.0, "%")); // Adjust these values as per the actual constructor

    listener.sensorsUpdated(exampleSensors);

    // Check the console output to verify that the method is called correctly
  }
}
