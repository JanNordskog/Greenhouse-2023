package no.ntnu.controlpanel;

import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.listeners.common.ActuatorListener;
import no.ntnu.listeners.common.CommunicationChannelListener;
import no.ntnu.listeners.controlpanel.GreenhouseEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ControlPanelLogicTest {

    private ControlPanelLogic controlPanelLogic;

    @BeforeEach
    public void setUp() {
        controlPanelLogic = new ControlPanelLogic();
    }

    @Test
    public void testOnNodeAdded() {
        SensorActuatorNodeInfo nodeInfo = new SensorActuatorNodeInfo(1);
        assertNotNull(nodeInfo);
    }

    @Test
    public void testOnNodeRemoved() {

        controlPanelLogic.onNodeRemoved(1);
        Actuator actuator = new Actuator(1,"Heater",1);
        controlPanelLogic.onActuatorStateChanged(1, 1, true);
        assertEquals("Heater",actuator.getType());
    }


}
