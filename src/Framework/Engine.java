package Framework;

import Framework.States.StateMachine;
import Framework.Timer.Time;

/**
 * Engine is the main physics driver which is responsible for
 */
public final class Engine implements Runnable
{
    // Engine state handler (i.e., editor v. simulation)
    private StateMachine stateMachine;

    // main application (physics) thread
    private Thread thread;

    // application (physics) timer
    private Time physicsClock;

    private boolean applicationIsRunning = false;

    /**
     * Prepare state loading and timer system
     */
    private void init() {
        physicsClock = Time.getTime(12f);
        stateMachine = new StateMachine(physicsClock);
    }

    /**
     * Main physics loop
     */
    @Override
    public void run()
    {
        init();

        // advance clock and perform physics update
        while(applicationIsRunning) {
            physicsClock.advance();
            if(physicsClock.isReadyForNextFrame()){
                stateMachine.currentState.tick();
                stateMachine.cycle();
            }
            physicsClock.resetFrameTimer();
        }

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
        stateMachine.deactivate();
        try {
            thread.join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
