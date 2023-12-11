# Communication Protocol

## Terminology
- **Sensor**: A device that measures environmental parameters (e.g., temperature, humidity) and produces an integer value.
- **Actuator**: A device that can affect the environment (e.g., fan, window opener/closer).
- **Sensor and Actuator Node**: A computer with direct access to sensors and actuators, connected to the Internet.
- **Control-Panel Node**: An Internet-connected device that visualizes sensor/actuator node status and sends control commands.
- **Graphical User Interface (GUI)**: A graphical interface for system interaction.

## Underlying Transport Protocol
- **Protocol Used**: TCP, for its reliability and ordered data transmission.
- **Port Number(s)**: 1 (as indicated in the code, but typically a different number would be chosen).
- **Reason for Choice**: TCP is chosen for its reliable, connection-oriented nature, which is crucial for consistent communication in a distributed control system.

## Architecture
- **Network Structure**: The network consists of a central server (control-panel node) and multiple client nodes (sensor/actuator nodes).
- **Diagram**: Include a network diagram here showing the server and client nodes.

## Flow of Information and Events
- **Sensor/Actuator Nodes**: Periodically send sensor readings to the server and receive actuator commands. React to incoming packets by updating actuator states.
- **Control Panel Nodes**: Receive sensor data, display it on the GUI, and send actuator control commands based on user input.

## Connection and State
- **Connection Type**: Connection-oriented (due to TCP usage).
- **State**: Stateless, with each message containing all necessary information for processing.

## Types, Constants
- **Value Types**: Sensor readings are integers. Actuator states are booleans (on/off).

## Message Format
- **General Format**: Each message contains a header (type of message, node ID) and a body (sensor data or actuator command).
- **Specific Messages**:
    - Sensor Data Message: `2;temperature=28.03`
    - Actuator Command Message: `2;5_fan`

## Error Messages
- **Error Handling**: Error messages indicate connection issues or invalid commands, structured as `{ Type: 'Error', Message: '[Description]' }`.

## Example Scenario
- **Startup**: Sensor nodes 1, 2, and 3 start and establish a TCP server.
- **Data Broadcasting**: After 5 seconds, sensor nodes send their first sensor data to the server.
- **Control Commands**:
    - Can turn on and off actuators trough client user-interface
    - Control panel 2 sends a command to turn off all actuators.
- **Responses**: Nodes respond to commands by updating actuator states and acknowledging the reception of commands.

## Reliability and Security
- **Reliability**: The use of TCP ensures message delivery and order. Retries and timeouts handle transient network issues.
- **Security**: SSL/TLS encryption for secure data transmission. Authentication mechanisms for nodes to prevent unauthorized access.
