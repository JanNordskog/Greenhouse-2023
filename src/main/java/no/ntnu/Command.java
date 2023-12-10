package no.ntnu;

import no.ntnu.server.ServerLogic;

/**
 * Abstract class to create commands.
 */
public abstract class Command implements Message {

  /**
   * Method to run when executing command.
   *
   * @param logic Server logic.
   *
   * @return Message after command is executed.
   */
  public abstract Message execute(ServerLogic logic);
    
}
