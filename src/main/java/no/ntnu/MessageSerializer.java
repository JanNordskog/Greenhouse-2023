package no.ntnu;

import no.ntnu.command.ToggleActuatorCommand;
import no.ntnu.message.HumidityMessage;
import no.ntnu.message.TemperatureMessage;

public class MessageSerializer {
    private final static String humidityUnit = "%";
    private final static String temperatureUnit = "C";
    private final static String actuatorCommand = "A";
    
    private MessageSerializer() {}

    public static String toString(Message message) {
        String m = "";
        if (message instanceof HumidityMessage humidityMessage) {
            m = humidityMessage.getHumidity() + "%," + humidityMessage.getNodeId();
        } else if (message instanceof ToggleActuatorCommand toggleActuatorCommand) {
            m = actuatorCommand + toggleActuatorCommand.getNodeId() + "," + toggleActuatorCommand.getId();
        } else if (message instanceof TemperatureMessage temperatureMessage) {
            m = temperatureMessage.getTemperature() + "C," + temperatureMessage.getNodeId();
        }

        return m;
    }

    public static Message fromString(String s) {
        Message m = null;

        if (isUnitValid(s)) {
            switch (getUnit(s)) {
                case '%' :
                    m = new HumidityMessage(parseDouble(s,0), parseInteger(s, 1));
                    break;
                case 'C':
                    m = new TemperatureMessage(parseDouble(s, 0), parseInteger(s, 1));
            } 
        } else if (s.startsWith(actuatorCommand)) {
            m = new ToggleActuatorCommand(parseInteger(s, 0), parseInteger(s, 1));
        }

        return m;
    }

    private static boolean isUnitValid(String message) {
        boolean valid = false;
        String[] split = message.split(",");
        char last = split[0].charAt(message.length()-1);
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

    public static double parseDouble(String s, int position) {
        String[] split = s.substring(0, s.length()-1).split(",");
        double value = 0;
        try {
            value = Double.valueOf(split[position]);
        } catch (NumberFormatException e) {
            System.err.println("Could not parse String <" + s + "> to double");
        }
        return value;
    }

    private static Integer parseInteger(String s, int position) {
        Integer i = null;
        String[] split;
        if (isUnitValid(s)) {
            split = s.substring(0, s.length()-1).split(",");
        } else {
            split = s.substring(1).split(",");
        }
        try {
            Integer.valueOf(split[position]);
        } catch (Exception e) {
            System.err.println("Could not parse integer <" + s + ">");
        }

        return i;
    }

    public static Message ParseParameterizedMessage(String s) {
        Message m = null;

        String parameter = s.substring(1);
        if (s.startsWith(actuatorCommand)) {
            Integer nodeId = parseInteger(parameter, 0);
            Integer actuatorId = parseInteger(parameter, 1);
            m = new ToggleActuatorCommand(nodeId, actuatorId);
        }

        return m;
    }
}
