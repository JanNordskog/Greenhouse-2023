package no.ntnu.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.Command;
import no.ntnu.Message;
import no.ntnu.MessageSerializer;
import no.ntnu.server.Server;

public class ClientHandler extends Thread{
    private Socket socket;
    private final Server server;
    private PrintWriter socketWriter;
    private BufferedReader socketReader;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.socketWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        Message response;

        do {
            Command clientCommand = readClientRequest();
            if (clientCommand != null) {
                System.out.println("Recieved a " + clientCommand.getClass().getSimpleName());
                response = clientCommand.execute();
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
            if (!(clientCommand instanceof Command) || clientCommand != null) {
                System.err.println("Wrong message from client: " + clientCommand);
                clientCommand = null;
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
