package no.ntnu.ssl;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Creates a Secure Socket Layer connection between server and client.
 */
public class SslConnection {

  private Dotenv dotenv = Dotenv.configure().directory("./").load();
  private final String password = dotenv.get("KEYSTORE_PASSWORD");

  private char[] keyStorePass;
  private KeyStore keyStore;
  private final int portNumber;

  /**
   * Creates an ssl connection.
   *
   * @param portNumber the port for the server.
   * @throws KeyStoreException exception.
   */
  public SslConnection(int portNumber) throws KeyStoreException {
    this.keyStorePass = password.toCharArray();
    this.keyStore = KeyStore.getInstance("JKS");
    this.portNumber = portNumber;
  }

  /**
   * Returns the ssl socket for the server.
   *
   * @return Server ssl socket.
   * @throws KeyStoreException keystore exeption.
   * @throws NoSuchAlgorithmException no such algorytm.
   * @throws CertificateException certificate exception.
   * @throws FileNotFoundException file not found.
   * @throws IOException IO exeption.
   * @throws UnrecoverableKeyException unrecoverable key exception.
   * @throws KeyManagementException key management exception.
   */
  public SSLServerSocket server()
      throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
      FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException {

    this.keyStore.load(new FileInputStream("server.keystore"), this.keyStorePass);

    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
    keyManagerFactory.init(this.keyStore, this.keyStorePass);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

    SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
    SSLServerSocket serverSocket =
        (SSLServerSocket) serverSocketFactory.createServerSocket(portNumber);

    return serverSocket;
  }

  /**
   * Connection for client to smart tv.
   *
   * @param adress Servers IP adress.
   * 
   * @return the client SSL socket.
   * @throws KeyStoreException keystore exeption.
   * @throws NoSuchAlgorithmException no such algorytm.
   * @throws CertificateException certificate exception.
   * @throws FileNotFoundException file not found.
   * @throws IOException IO exeption.
   * @throws KeyManagementException key management exception.
   */
  public SSLSocket client(String adress) throws NoSuchAlgorithmException, KeyStoreException,
      CertificateException, FileNotFoundException, IOException, KeyManagementException {

    this.keyStore.load(new FileInputStream("server.keystore"), this.keyStorePass);
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
    trustManagerFactory.init(this.keyStore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

    SSLSocketFactory socketFactory = sslContext.getSocketFactory();
    SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(adress, portNumber);

    sslSocket.startHandshake();

    return sslSocket;
  }

}
