package Framework.Timer;

public final class Time {
    // Set frame rate and physics update rate
    public static final int fps = 60;
    public static final int fixedPhysicsSteps = 120;

    // system time and delta time variables
    // time at initialization
    public static long initialTime = System.nanoTime();
    // time since last state initialization
    public static long elapsedTime = 0;
    // time since program start
    public static long time;
    //not to be confused with state dt/ deltaTime, this controls frame rate
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
     *
     */
    public static synchronized Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }
        return instance;
    }

    /**
     * Resets simulation time to avoid problems when going between states
     */
    public static void reset(){
        initialTime = System.nanoTime();
    }

    public static long asNanoseconds(float f){
        return (long)f * 1000000000;
    }

    /**
     * private constructor to prevent objects from creating additional timers
     * Set all constants and reset timer variables to zero
     */
    private Time() {
        timePerTickNanoseconds = 1000000000f / fps;
        timePerPhysicsNanoseconds = 1000000000f/ fixedPhysicsSteps;
        resetCounters();
    }

    /**
     * Set frame counters to 0
     */
    private void resetCounters() {
        countUpToNextFrame = 0;
        countUpToNextPhysics = 0;
    }

    /**
     * Every loop of the application cycle, timer advances
     */
    public void advance()
    {
        // Get current time and find deltaTime
        time = System.nanoTime();
        deltaTime = time - lastTime;
        elapsedTime = time - initialTime;
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
    public boolean isReadyForNextFrame()
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
    public boolean isReadyToAdvancePhysics()
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
    public void printFrameRate()
    {
        if(instance.frameTimer >= 1000000000)
        {
            System.out.println("Frame Rate: "+Time.ticks);
            Time.ticks = 0;
            instance.frameTimer = 0;
        }
    }
}