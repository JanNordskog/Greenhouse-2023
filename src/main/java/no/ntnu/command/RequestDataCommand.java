package no.ntnu.command;

import no.ntnu.Command;
import no.ntnu.Message;
import no.ntnu.message.OkMessage;
import no.ntnu.server.ServerLogic;

/**
 * Command to request data.
 */
public class RequestDataCommand extends Command {

  public RequestDataCommand() {}

  @Override
  public Message execute(ServerLogic logic) {
    logic.sendData();
    return new OkMessage();
  }



}
