package framework.states;


import framework.object.Entity;

import java.util.Collections;
import static renderer.uiElements.panels.PlayPanel.onTimestepSliderChanged;

/**
 * RunState is the main physics state where behaviors are updating with changes in the update loop.
 *
 * Copyright (c) 2023 Joseph Sutlive and Tony Zhang
 * All rights reserved
 */
public class RunState extends State
{
    // Little bit hacky method used to manage force timestep.
    protected static float dt  = (float) Math.pow(2,-14);
    public static float deltaTime;

    public RunState(StateMachine stateMachine) {
        super(stateMachine);
    }

    /**
     * Instantiation of entities occurs here. Each behavior will have its awake and start methods called.
     */
    @Override
    public void enter() {
        Collections.shuffle(stateMachine.allObjects);
        onTimestepSliderChanged.subscribe(this::setTimeStep);
        for (Entity obj :(stateMachine.allObjects)) {
            obj.start();
        }
    }

    private void setTimeStep(float f){
        dt = f;
    }

    /**
     * utilities.Physics Loop. All physics objects updated here (earlyUpdate, update, and lateUpdate methods)
     */
    @Override
    public void tick() {
        deltaTime = dt;

        for (Entity obj :(stateMachine.allObjects)) {
            obj.earlyUpdate();
        }
        for (Entity obj :(stateMachine.allObjects)) {
            obj.update();
        }
        for (Entity obj :(stateMachine.allObjects)) {
            obj.lateUpdate();
        }
    }

    void exit()
    {
        onTimestepSliderChanged.unSubscribe(this::setTimeStep);
    }
}
