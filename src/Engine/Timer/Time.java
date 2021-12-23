package Engine.Timer;

public class Time {
    // Set frame rate and physics update rate
    public static final int fps = 60;
    public static final int fixedPhysicsSteps = 30;

    // system time and delta time variables
    public static long time;
    public static long deltaTime;

    // time per physics/rendering step
    private static double timePerTickNanoseconds;
    private static double timePerPhysicsNanoseconds;
    private static long lastTime = System.nanoTime();

    public static int ticks = 0;
    private double countUpToNextFrame;
    private double countUpToNextPhysics;
    private long frameTimer = 0;

    public static Time instance;

    /**
     * Create or get specific time instance, synchronized across all threads
     * @return reference to the master timer
     */
    public static synchronized Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }
        return instance;
    }

    /**
     * private constructor to prevent objects from creating additional timers
     * Set all constants and reset timer variables to zero
     */
    private Time() {
        timePerTickNanoseconds = 1000000000 / fps;
        timePerPhysicsNanoseconds = 1000000000/ fixedPhysicsSteps;
        ResetCounters();
    }

    /**
     * Set frame counters to 0
     */
    private void ResetCounters() {
        countUpToNextFrame = 0;
        countUpToNextPhysics = 0;
    }

    /**
     * Every loop of the application cycle, timer advances
     */
    public static void Advance()
    {
        // Get current time and find deltaTime
        time = System.nanoTime();
        deltaTime = time - lastTime;
        // Advance the "time since last update" variables for physics and rendering threads
        instance.countUpToNextFrame += deltaTime/timePerTickNanoseconds;
        instance.countUpToNextPhysics += deltaTime/timePerPhysicsNanoseconds;
        // iterate on timer variables in preparation of next loop
        instance.frameTimer += deltaTime;
        lastTime = time;
    }

    /**
     * Checks against frame rate to see if rendering system should update
     * @return true if frame should advance
     */
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

    /**
     * checks timer against physics update rate to see if physics should update
     * @return true if time is ready to advance physics
     */
    public static boolean isReadyToAdvancePhysics()
    {
        if(instance.countUpToNextPhysics >= 1)
        {
            instance.countUpToNextPhysics--;
            return true;
        }

        return false;
    }

    /**
     * prints the current frame rate to console for debugging purposes
     */
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