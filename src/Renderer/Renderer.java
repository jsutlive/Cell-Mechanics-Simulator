package Renderer;

import Framework.Timer.Time;
import Renderer.Graphics.IRender;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Render object responsible for updating canvas
 */
public abstract class Renderer implements Runnable {

    //Graphics object that our painter class references to draw objects
    public static Graphics graphics;
    public static Color DEFAULT_COLOR = Color.white;

    protected List<IRender> batch = new ArrayList<>();
    protected Dimension bounds;

    protected boolean applicationIsRunning = false;
    private final Time renderClock = Time.getTime(60f);

    @Override
    // run the renderer in an update loop
    public void run() {
        //Create a new camera when render loop starts. This is the main camera.
        while(applicationIsRunning) {
            renderClock.advance();
            if (renderClock.isReadyForNextFrame()) {
                render();
            }
            renderClock.printFrameRateAndResetFrameTimer();
        }
    }

    public void clearBatch(){
        batch.clear();
    }

    protected void addGraphicToBatch(IRender rend){
        batch.add(rend);
    }

    protected void removeGraphicFromBatch(IRender rend){
        batch.remove(rend);
    }

    protected abstract void render();
}