package Engine.Timer;

public class Time {
    public static final int fps = 2;
    public static long time;
    public static long deltaTime;

    private static double timePerTickNanoseconds;
    private static long lastTime = System.nanoTime();

    private int ticks = 0;
    private double countUpToNextFrame;
    private long frameTimer = 0;

    public static Time instance;

    public static synchronized Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }
        return instance;
    }

    private Time() {
        timePerTickNanoseconds = 1000000000 / fps;
        countUpToNextFrame = 0;
    }

    public static void Advance()
    {
        time = System.nanoTime();
        deltaTime = time - lastTime;
        instance.countUpToNextFrame += deltaTime/timePerTickNanoseconds;
        instance.frameTimer += deltaTime;
        lastTime = time;

    }

    public static boolean isReadyForNextFrame()
    {
        if(instance.countUpToNextFrame >= 1)
        {
            instance.countUpToNextFrame--;
            instance.ticks++;
            return true;
        }

        return false;
    }

    public static void printFrameRate()
    {
        if(instance.frameTimer >= 1000000000)
        {
            System.out.println("Frame Rate: "+instance.ticks);
            instance.ticks = 0;
            instance.frameTimer = 0;
        }
    }
}