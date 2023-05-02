package Framework.Utilities;
/**
 * Time is the custom timer class used to control the physics and render loops of the program. It is recommended that
 * separate timers are used for these components
 *
 * Copyright (c) 2023 Joseph Sutlive
 * All rights reserved
 */
public final class Time {
    // Set frame rate and physics update rate
    private float fps;

    // system time and delta time variables
    // time at initialization
    public long initialTime = System.nanoTime();
    // time since last state initialization
    public long elapsedTime = 0;
    // time since program start
    public long time;
    //not to be confused with state dt/ deltaTime, this controls frame rate
    public long deltaTime;

    // time per physics/rendering step
    private double timePerTickNanoseconds;
    private long lastTime = System.nanoTime();

    public int ticks = 0;
    private double countUpToNextFrame;
    private long frameTimer = 0;

    /**
     * Create or get specific time instance, synchronized across all threads
     *
     */
    public static synchronized Time getTime(float fps){
        return new Time(fps);
    }

    /**
     * Resets simulation time to avoid problems when going between states
     */
    public void reset(){
        initialTime = System.nanoTime();
        elapsedTime = time - initialTime;
    }

    public static long asNanoseconds(float f){
        return (long)f * 1000000000;
    }
    public static float fromNanoseconds(long l) {return l/1000000000f;}

    /**
     * private constructor to prevent objects from creating additional timers
     * Set all constants and reset timer variables to zero
     */
    private Time(float fps) {
        timePerTickNanoseconds = 1000000000f / fps;
        resetCounters();
    }

    /**
     * Set frame counters to 0
     */
    private void resetCounters() {
        countUpToNextFrame = 0;
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
        countUpToNextFrame += deltaTime/timePerTickNanoseconds;
        // iterate on timer variables in preparation of next loop
        frameTimer += deltaTime;
        lastTime = time;
    }

    /**
     * Checks against frame rate to see if rendering system should update
     * @return true if frame should advance
     */
    public boolean isReadyForNextFrame()
    {
        if(countUpToNextFrame >= 1)
        {
            countUpToNextFrame--;
            ticks++;
            return true;
        }

        return false;
    }

    /**
     * prints the current frame rate to console for debugging purposes
     */
    public void printFrameRateAndResetFrameTimer()
    {
        if(frameTimer >= 1000000000)
        {
            System.out.println("Frame Rate: "+ ticks);
            ticks = 0;
            frameTimer = 0;
        }
    }

    public void resetFrameTimer(){
        if(frameTimer >= 1000000000)
        {
            ticks = 0;
            frameTimer = 0;
        }
    }
}