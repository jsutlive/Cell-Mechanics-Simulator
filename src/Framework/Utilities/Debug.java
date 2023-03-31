package Framework.Utilities;

import Framework.Events.EventHandler;

public final class Debug {
    public EventHandler<String> onLog = new EventHandler<>();
    public EventHandler<String> onLogWarning = new EventHandler<>();
    public EventHandler<String> onLogError = new EventHandler<>();

    private static Debug INSTANCE;

    public Debug(){
        if(INSTANCE == null) {
            INSTANCE = this;
        }
    }

    public static void Log(String msg){
        if(INSTANCE == null) return;
        INSTANCE.onLog.invoke(msg);
    }

    public static void LogWarning(String msg){
        if(INSTANCE == null) return;
        INSTANCE.onLogWarning.invoke(msg);
    }

    public static void LogError(String msg){
        if(INSTANCE == null) return;
        INSTANCE.onLogError.invoke(msg);
    }

}
