import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import no.ntnu.controlpanel.*;
import no.ntnu.greenhouse.*;
import no.ntnu.listeners.common.*;
import no.ntnu.listeners.controlpanel.*;
import no.ntnu.tools.*;

public class ControlPanelLogicTest {
  private ControlPanelLogic controlPanelLogic;

  @Mock
  private CommunicationChannel communicationChannel;

  @Mock
  private CommunicationChannelListener communicationChannelListener;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    controlPanelLogic = new ControlPanelLogic();
    controlPanelLogic.setCommunicationChannel(communicationChannel);
    controlPanelLogic.setCommunicationChannelListener(communicationChannelListener);
  }

  @Test
  public void testOnNodeAdded() {
    SensorActuatorNodeInfo nodeInfo = new SensorActuatorNodeInfo(1, "Node 1");
    GreenhouseEventListener listener = mock(GreenhouseEventListener.class);
    controlPanelLogic.addListener(listener);

    controlPanelLogic.onNodeAdded(nodeInfo);

    verify(listener, times(1)).onNodeAdded(nodeInfo);
  }

  // Add similar tests for other methods like onNodeRemoved, onSensorData, onActuatorStateChanged, actuatorUpdated, and onCommunicationChannelClosed.
}
