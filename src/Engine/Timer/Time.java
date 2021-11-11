package Engine.Timer;

public class Time {
    public static final int fps = 60;
    public static final int fixedPhysicsSteps = 10;
    public static long time;
    public static long deltaTime;

    private static double timePerTickNanoseconds;
    private static double timePerPhysicsNanoseconds;
    private static long lastTime = System.nanoTime();

    public static int ticks = 0;
    private double countUpToNextFrame;
    private double countUpToNextPhysics;
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
        timePerPhysicsNanoseconds = 1000000000/ fixedPhysicsSteps;
        countUpToNextFrame = 0;
        countUpToNextPhysics = 0;
    }

    public static void Advance()
    {
        time = System.nanoTime();
        deltaTime = time - lastTime;
        instance.countUpToNextFrame += deltaTime/timePerTickNanoseconds;
        instance.countUpToNextPhysics += deltaTime/timePerPhysicsNanoseconds;
        instance.frameTimer += deltaTime;
        lastTime = time;

    }

    public static boolean isReadyForNextFrame()
    {
        if(instance.countUpToNextFrame >= 1)
        {
            instance.countUpToNextFrame--;
            Time.ticks++;
            return true;
        }

        return false;
    }

    public static boolean isReadyToAdvancePhysics()
    {
        if(instance.countUpToNextPhysics >= 1)
        {
            instance.countUpToNextPhysics--;
            return true;
        }

        return false;
    }

    public static void printFrameRate()
    {
        if(instance.frameTimer >= 1000000000)
        {
            System.out.println("Frame Rate: "+Time.ticks);
            Time.ticks = 0;
            instance.frameTimer = 0;
        }
    }
}