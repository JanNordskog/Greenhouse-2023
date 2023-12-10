package no.ntnu.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.Command;
import no.ntnu.Message;
import no.ntnu.MessageSerializer;
import no.ntnu.command.RequestDataCommand;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.message.NodeDataMessage;
import no.ntnu.tools.Logger;

public class ClientHandler extends Thread {

  private Socket socket;
  private final GreenhouseSimulator server;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;

  public ClientHandler(Socket socket, GreenhouseSimulator server) throws IOException {
    this.server = server;
    this.socket = socket;
    this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.socketWriter = new PrintWriter(socket.getOutputStream(), true);
  }

  @Override
  public void run() {
    Message response;
    Logger.info("Running clienthandler");
    do {
      Command clientCommand = readClientRequest();
      if (clientCommand != null) {
        System.out.println("Recieved a " + clientCommand.getClass().getSimpleName());
        response = clientCommand.execute(server.getLogic());
        if (response != null) {
          server.sendResponseToClients(response);
        }
      } else {
        response = null;
      }

    } while (response != null);
    System.out.println("Client " + socket.getRemoteSocketAddress() + " Disconnected");
    server.clientDisconnected(this);
  }

  public Command readClientRequest() {
    Message clientCommand = null;
    try {
      String clientRequest = socketReader.readLine();
      clientCommand = MessageSerializer.fromString(clientRequest);
      if (clientCommand instanceof Command) {
        if (clientCommand instanceof RequestDataCommand) {
          server.getLogic().sendData();
        }
      }
    } catch (IOException e) {
      System.err.println("Could not recieve client request: " + e.getMessage());
    }

    return (Command) clientCommand;
  }

  public void sendToClient(Message message) {
    socketWriter.println(MessageSerializer.toString(message));
  }


}
