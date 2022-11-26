package Framework;

import Framework.States.StateMachine;
import Framework.Timer.Time;
import Renderer.Renderer;

public final class Engine implements Runnable
{
    // rendering system reference
    Renderer renderer;

    // Engine state handler (editor v. simulation)
    private StateMachine stateMachine;

    // main application thread
    private Thread thread;

    // application timer
    private Time timer;

    private boolean applicationIsRunning = false;

    /**
     * Prepare state loading and timer system
     */
    private void init() {
        timer = Time.getTime(120f);
        stateMachine = new StateMachine(timer);
    }

    /**
     * Main physics loop
     */
    @Override
    public void run()
    {
        init();
        while(applicationIsRunning)
        {
            timer.advance();
            if(timer.isReadyForNextFrame()){
                stateMachine.currentState.tick();
            }
            timer.resetFrameTimer();
        }
        // Application stops
        Stop();
    }

    /**
     * Begin application thread
     */
    public synchronized void Start(Thread[] threads)
    {
        if(applicationIsRunning){return;}
        applicationIsRunning = true;
        thread = threads[0];
        for(Thread thread: threads) thread.start();
    }

    /**
     * Halt program/ application thread
     */
    public synchronized void Stop()
    {
        if(!applicationIsRunning){return;}
        applicationIsRunning = false;
        try {
            thread.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
