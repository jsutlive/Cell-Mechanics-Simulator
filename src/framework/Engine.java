package framework;

import framework.states.StateMachine;
import framework.utilities.Time;

/**
 * Engine is the main physics driver which is responsible for handling the timing of the physics update loop and
 * creation of the primary state machine
 *
 * Copyright (c) 2023 Joseph Sutlive
 * All rights reserved
 */
public final class Engine implements Runnable
{
    // Engine state handler (i.e., editor v. simulation)
    private StateMachine stateMachine;

    // main application (physics) thread
    private Thread thread;

    // application (physics) timer
    private final Time physicsClock = Time.getTime(50f);

    private boolean applicationIsRunning = false;

    private String[] runtimeArgs;

    /**
     * Prepare state loading and timer system
     */
    private void init() {
        stateMachine = new StateMachine(physicsClock, runtimeArgs[0].equals("-h"));
    }

    /**
     * Main physics loop
     */
    @Override
    public void run() {
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
    public synchronized void start(Thread[] threads, String[] args) {
        if(applicationIsRunning){return;}
        applicationIsRunning = true;
        runtimeArgs = args;
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
