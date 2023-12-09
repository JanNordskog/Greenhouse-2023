package no.ntnu.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLServerSocket;
import no.ntnu.Message;
import no.ntnu.run.ClientHandler;
import no.ntnu.ssl.SslConnection;

public class Server {

    public static final int PORT_NUMBER = 1;
    private boolean isServerRunning = false;
    private List<ClientHandler> connectedClients = new ArrayList<>();
    private ServerLogic logic;

    public Server(ServerLogic logic) {
        this.logic = logic;
    }

    public void startServer() throws KeyStoreException, UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
        SslConnection connection = new SslConnection(PORT_NUMBER);

        SSLServerSocket server = connection.server();
        System.out.println("Listening to port " + PORT_NUMBER);

        if (server != null) {
            isServerRunning = true;
            while (isServerRunning) {
                ClientHandler clientHandler = acceptNextClientConnection(server);
                if (clientHandler != null) {
                    connectedClients.add(clientHandler);
                    clientHandler.run();
                }
            }
        }
    }

    public void clientDisconnected(ClientHandler clientHandler) {
        connectedClients.remove(clientHandler);
    }

    private ClientHandler acceptNextClientConnection(SSLServerSocket listeningSocket) {
        ClientHandler client = null;
        try {
            Socket clientSocket = listeningSocket.accept();
            System.out.println("New client connected from: " + clientSocket.getRemoteSocketAddress());
            client = new ClientHandler(clientSocket, this);
            connectedClients.add(client);
        } catch (IOException e) {
            System.err.println("Could not accept client connection: " + e.getMessage());
        }
        return client;
    }

    public void sendResponseToClients(Message msg) {
        for (ClientHandler c : connectedClients) {
            c.sendToClient(msg);
        }
    }

    public ServerLogic getLogic() {
        return this.logic;
    }

    public List<ClientHandler> getConnectedClients() {
        return this.connectedClients;
    }

    public static void main(String[] args) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
        ServerLogic logic = new ServerLogic();
        Server server = new Server(logic);
        server.startServer();
    }

}