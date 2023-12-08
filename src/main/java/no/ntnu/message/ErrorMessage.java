package no.ntnu.message;

import no.ntnu.Message;

public class ErrorMessage implements Message {
    public final String message;
    
    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
