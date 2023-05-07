package framework.states;

import framework.object.Entity;
import component.Mesh;

import static framework.object.Tag.MODEL;
/**
 * Editor State: Main state for changing parameters, no physics functions running in this state.
 *
 * Copyright (c) 2023 Joseph Sutlive and Tony Zhang
 * All rights reserved
 */
public class EditorState extends State
{
    public EditorState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void enter() {
        // if a physics model is present, reset all objects
        if(findObjectWithTag(MODEL) != null){
            //ensure mesh object is not null, then reset
            for(Entity obj: stateMachine.allObjects) {
                if(obj.getComponent(Mesh.class) == null) continue;
                obj.getComponent(Mesh.class).reset();
            }
        }
    }

    @Override
    public void tick() {
    }

    @Override
    void exit() {
        // save simulation state to json object. Currently not implemented.
        saveInitial();
    }

}
