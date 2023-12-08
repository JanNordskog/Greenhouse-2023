package no.ntnu;

import no.ntnu.server.ServerLogic;

public abstract class Command implements Message {

    public abstract Message execute(ServerLogic logic);
    
}
