package Framework.Utilities;

import Framework.Events.EventHandler;

import java.util.ArrayList;
import java.util.List;

import static Framework.Utilities.Message.Type.*;

public final class Debug {
    public EventHandler<Message> onLog = new EventHandler<>();

    public static Debug INSTANCE;
    public List<Message> messageLog = new ArrayList<>();

    public Debug(){
        if(INSTANCE == null) {
            INSTANCE = this;
        }
    }

    public static void Log(String msg){
        if(INSTANCE == null) return;
        Message m = new Message(msg, Log);
        INSTANCE.messageLog.add(m);
        INSTANCE.onLog.invoke(m);
    }

    public static void LogWarning(String msg){
        if(INSTANCE == null) return;
        Message m = new Message(msg, Warning);
        INSTANCE.messageLog.add(m);
        INSTANCE.onLog.invoke(m);
    }

    public static void LogError(String msg){
        if(INSTANCE == null) return;
        Message m = new Message(msg, Error);
        INSTANCE.messageLog.add(m);
        INSTANCE.onLog.invoke(m);
    }

}
