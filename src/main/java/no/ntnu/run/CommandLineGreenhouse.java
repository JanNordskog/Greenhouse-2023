package no.ntnu.run;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.tools.Logger;

/**
 * Run a greenhouse simulation using command-line interface (no GUI).
 */
public class CommandLineGreenhouse {
  /**
   * Application entrypoint for the command-line version of the simulator.
   *
   * @param args Command line arguments, only the first one of them used: when it is "fake", emulate
   *        fake events, when it is either something else or not present, use real socket
   *        communication.
   * @throws CertificateException Exception.
   * @throws NoSuchAlgorithmException Exception.
   * @throws KeyStoreException Exception.
   * @throws KeyManagementException Exception.
   * @throws UnrecoverableKeyException Exception.
   */
  public static void main(String[] args) throws KeyManagementException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
    Logger.info("Running greenhouse simulator in command line (without GUI)...");
    boolean fake = false;
    if (args.length == 1 && "fake".equals(args[0])) {
      fake = true;
      Logger.info("Using FAKE events");
    }
    GreenhouseSimulator simulator = new GreenhouseSimulator(fake);
    simulator.initialize();
    simulator.start();
  }
}
