package Input;

import Framework.Events.EventHandler;

public class ExternalEvents {
    public static EventHandler<Boolean> initSimulation = new EventHandler<Boolean>();
    public static EventHandler<Integer> stepPhysics = new EventHandler<Integer>();
}