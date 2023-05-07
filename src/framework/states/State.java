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
     * Look for a tagged object and return the first object with that tag
     * @param tag specified tag to search all state entities for
     * @return the first entity found with the specified tag
     */
    public Entity findObjectWithTag(Tag tag)
    {
        for (Entity mono: stateMachine.allObjects) {
            if(mono.getTag() == tag) return mono;
        }
        return null;
    }

    /**
     * save a json file with initial position
     */
    protected void saveInitial(){
        //save(findObjectWithTag(Tag.MODEL), "scene");
    }
}
