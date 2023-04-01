package Framework.Utilities;

import java.awt.*;
import java.time.LocalDateTime;

public class Message {

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
