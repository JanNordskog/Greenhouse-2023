package no.ntnu;

import no.ntnu.command.GetTemperatureCommand;
import no.ntnu.message.HumidityMessage;

public class MessageSerializer {
    private final static String humidityUnit = "%";
    private final static String temperatureUnit = "C";
    
    private MessageSerializer() {}

    public static String toString(Message message) {
        String m = "";
        if (message instanceof GetTemperatureCommand) {
            m = "";
        }

        return m;
    }

    public static Message fromString(String s) {
        Message m = null;

        if (isUnitValid(s)) {
            switch (getUnit(s)) {
                case '%' :
                    m = new HumidityMessage(0);
            } 
        }

        return m;
    }

    private static boolean isUnitValid(String message) {
        boolean valid = false;
        char last = message.charAt(message.length()-1);
        if(last == humidityUnit.charAt(0) || last == temperatureUnit.charAt(0)) valid = true;

        return valid;
    }

    public static char getUnit(String message) {
        char c = 0;

        if (isUnitValid(message)) {
            c = message.charAt(message.length()-1);
        }

        return c;
    }

    public static double parseDouble(String s) {
        double value = 0;
        try {
            value = Double.valueOf(s.substring(0, s.length()-1));
        } catch (NumberFormatException e) {
            System.err.println("Could not parse String <" + s + "> to double");
        }
        return value;
    }
}
