package framework.states;

import framework.object.Entity;
import framework.object.Tag;

/**
 * State is an abstract class which determines what is done in the update (physics) loop. Each state has actions which
 * occur when the state is entered, exited, and during each update cycle.
 *
 * Copyright (c) 2023 Joseph Sutlive
 * All rights reserved
 */
public abstract class State
{
    protected StateMachine stateMachine;

    public State(StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    /**
     * Actions ot be performed when the state starts up
     */
    public abstract void enter();

    /**
     * Performs all calculations to be updated once per frame cycle.
     */
    public abstract void tick();

    /**
     * Actions to be performed just before state is changed from this to another state
     */
    abstract void exit();

    /**
     * save a json file with initial position
     */
    protected void saveInitial(){
        //save(findObjectWithTag(Tag.MODEL), "scene");
    }
}
