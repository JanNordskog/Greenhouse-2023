package no.ntnu.gui.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.tools.Logger;

/**
 * A section of GUI displaying sensor data.
 */
public class SensorPane extends TitledPane {
  private final List<SimpleStringProperty> sensorProps = new ArrayList<>();
  private final VBox contentBox = new VBox();
  private final HBox side = new HBox();

  /**
   * Create a sensor pane.
   *
   * @param sensors The sensor data to be displayed on the pane.
   */
  public SensorPane(Iterable<SensorReading> sensors) {
    super();
    initialize(sensors);
  }

  private void initialize(Iterable<SensorReading> sensors) {
    setText("Sensors");
    sensors.forEach(sensor ->
            contentBox.getChildren().add(createAndRememberSensorLabel(sensor))
    );
    setContent(contentBox);
  }

  /**
   * Create an empty sensor pane, without any data.
   */
  public SensorPane() {
    initialize(new LinkedList<>());
  }

  /**
   * Create a sensor pane.
   * Wrapper for the other constructor with SensorReading-iterable parameter
   *
   * @param sensors The sensor data to be displayed on the pane.
   */
  public SensorPane(List<Sensor> sensors) {
    initialize(sensors.stream().map(Sensor::getReading).toList());
  }

  /**
   * Update the GUI according to the changes in sensor data.
   *
   * @param sensors The sensor data that has been updated
   */
  public void update(Iterable<SensorReading> sensors) {
    int index = 0;
    for (SensorReading sensor : sensors) {
      updateSensorLabel(sensor, index++);
    }
  }

  /**
   * Update the GUI according to the changes in sensor data.
   * Wrapper for the other method with SensorReading-iterable parameter
   *
   * @param sensors The sensor data that has been updated
   */
  public void update(List<Sensor> sensors) {
    update(sensors.stream().map(Sensor::getReading).toList());
  }

  private Node createAndRememberSensorLabel(SensorReading sensor) {
    // Create the label for the sensor reading
    SimpleStringProperty props = new SimpleStringProperty(generateSensorText(sensor));
    sensorProps.add(props);
    Label label = new Label();
    label.textProperty().bind(props);
    ImageView imageView;

    System.out.println("This is the label guys " + props);
    if (props.get().contains("humidity")) {
      imageView = createhumidityIcon();


    } else if (props.get().contains("temperature")) {
      imageView = createtemperatureIcon();


    }else {
      imageView = createImageView();
    }

    // Create an HBox to hold both the label and the image
    HBox hbox = new HBox(imageView, label);
    hbox.setSpacing(5); // Set some spacing between the image and the label

    return hbox;
  }

  private ImageView createhumidityIcon() {
    ImageView imageView = new ImageView();
    try {
      InputStream fileContent = new FileInputStream("images/water.png"); // Replace with your image path
      imageView.setImage(new Image(fileContent));
      imageView.setFitWidth(10); // Set the width of the image
      imageView.setPreserveRatio(true);
    } catch (FileNotFoundException e) {
      // Handle the case where the image file is not found
      imageView.setImage(new Image("images/white.jpg")); // Optional: Set a default image
    }
    return imageView;
  }

  private ImageView createtemperatureIcon() {
    ImageView imageView = new ImageView();
    try {
      InputStream fileContent = new FileInputStream("images/temp.png"); // Replace with your image path
      imageView.setImage(new Image(fileContent));
      imageView.setFitWidth(10); // Set the width of the image
      imageView.setPreserveRatio(true);
    } catch (FileNotFoundException e) {
      // Handle the case where the image file is not found
      imageView.setImage(new Image("images/white.jpg")); // Optional: Set a default image
    }
    return imageView;
  }

  private ImageView createImageView() {
    ImageView imageView = new ImageView();
    try {
      InputStream fileContent = new FileInputStream("images/white.jpg"); // Replace with your image path
      imageView.setImage(new Image(fileContent));
      imageView.setFitWidth(10); // Set the width of the image
      imageView.setPreserveRatio(true);
    } catch (FileNotFoundException e) {
      // Handle the case where the image file is not found
      imageView.setImage(new Image("images/white.jpg")); // Optional: Set a default image
    }
    return imageView;
  }

  private String generateSensorText(SensorReading sensor) {
    return sensor.getType() + ": " + sensor.getFormatted();
  }

  private void updateSensorLabel(SensorReading sensor, int index) {
    if (sensorProps.size() > index) {
      SimpleStringProperty props = sensorProps.get(index);
      Platform.runLater(() -> props.set(generateSensorText(sensor)));
    } else {
      Logger.info("Adding sensor[" + index + "]");
      Platform.runLater(() -> contentBox.getChildren().add(createAndRememberSensorLabel(sensor)));
    }
  }
}
