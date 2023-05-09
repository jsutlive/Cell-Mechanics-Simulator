package framework.utilities;

import framework.events.EventHandler;

import java.util.ArrayList;
import java.util.List;

import static framework.utilities.Message.Type.*;

/**
 * Debug is a custom logger for sending messages, mainly helpful for interfacing with the GUI.
 *
 * Copyright (c) 2023 Joseph Sutlive
 * All rights reserved
 */
public final class Debug {
    // logger event
    public final EventHandler<Message> onLog = new EventHandler<>();

    public static Debug INSTANCE;
    public final List<Message> messageLog = new ArrayList<>();

    public Debug(){
        if(INSTANCE == null) {
            INSTANCE = this;
        }
    }

    /**
     * Log a simple message that is not a warning or error, and store that in the program's message log.
     * @param msg string to be logged to the system.
     */
    public static void Log(String msg){
        if(INSTANCE == null) return;
        Message m = new Message(msg, Log);
        INSTANCE.messageLog.add(m);
        INSTANCE.onLog.invoke(m);
    }


    /**
     * Log a warning message, and store that in the program's message log.
     * @param msg string to be logged to the system.
     */
    public static void LogWarning(String msg){
        if(INSTANCE == null) return;
        Message m = new Message(msg, Warning);
        INSTANCE.messageLog.add(m);
        INSTANCE.onLog.invoke(m);
    }

    /**
     * Log an error message (indicates a problem has occurred but will not crash the program), and store that in the
     * program's message log.
     * @param msg string to be logged to the system.
     */
    public static void LogError(String msg){
        if(INSTANCE == null) return;
        Message m = new Message(msg, Error);
        INSTANCE.messageLog.add(m);
        INSTANCE.onLog.invoke(m);
    }

}
