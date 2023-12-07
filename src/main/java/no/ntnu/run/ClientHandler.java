package no.ntnu.run;

import static no.ntnu.greenhouse.NodeServer.PORT_NUMBER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import no.ntnu.listeners.common.ActuatorListener;
import no.ntnu.listeners.greenhouse.SensorListener;
import no.ntnu.ssl.SslConnection;

public class ClientHandler {
    private static String SERVER_HOST;
    private Socket socket;
    private PrintWriter socketWriter;
    private BufferedReader socketReader;

    public ClientHandler(String host) {
        SERVER_HOST = host;
    }

    public boolean start() throws KeyStoreException, KeyManagementException, NoSuchAlgorithmException, CertificateException {
        boolean connected = false;

        try {
            SslConnection connection = new SslConnection(PORT_NUMBER);

            socket = connection.client(SERVER_HOST);
            socketWriter = new PrintWriter(socket.getOutputStream(), true);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connected = true;
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e);
        }

        return connected;
    }

    public void startListeningThread(SensorListener sensorListener, ActuatorListener actuatorListener) {
        new Thread(() -> {
            
        });
    }
}
