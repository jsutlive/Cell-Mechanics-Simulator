package Framework;

import Framework.States.StateMachine;
import Framework.Timer.Time;

public final class Engine implements Runnable
{
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
        timer = Time.getTime(100f);
        stateMachine = new StateMachine(timer);
    }

    /**
     * Main physics loop
     */
    @Override
    public void run()
    {
        init();

        while(applicationIsRunning) {
            timer.advance();
            if(timer.isReadyForNextFrame()){
                stateMachine.currentState.tick();
            }
            timer.resetFrameTimer();
        }
        // Application stops
        stop();
    }

    /**
     * Begin application thread
     */
    public synchronized void start(Thread[] threads) {
        if(applicationIsRunning){return;}
        applicationIsRunning = true;
        thread = threads[0];
        for(Thread thread: threads) thread.start();
    }

    /**
     * Halt program/ application thread
     */
    public synchronized void stop() {

        if(!applicationIsRunning){return;}
        applicationIsRunning = false;
        try {
            thread.join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
