package framework.utilities;

import java.awt.*;
import java.time.LocalDateTime;

/**
 * Message is the system by which stored log strings are encapsulated in the system.
 *
 * Copyright (c) 2023 Joseph Sutlive
 * All rights reserved
 */
public class Message {

    /**
     * Type of message: Log, Warning, or Error.
     */
    public enum Type{
        Log,
        Warning,
        Error
    }

    private String msg;
    private Type type;
    private LocalDateTime dateTime;

    public Message(String msg, Type type){
        this.msg = msg;
        this.type = type;
        this.dateTime = LocalDateTime.now();
    }

    public String getText(){
        return msg;
    }

    public Type getType(){
        return type;
    }

    /**
     * Returns color associated with a given message type
     * @return
     */
    public Color getTypeColor(){
        switch (type){
            case Log:
                return Color.GREEN;
            case Warning:
                return Color.YELLOW;
            case Error:
                return Color.RED;
        }
        return Color.white;
    }

    public LocalDateTime getDateTime(){
        return dateTime;
    }


}
