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
    //Renderer object singleton instance.
    private static Renderer instance;
    public static Color DEFAULT_COLOR = Color.white;

    protected List<IRender> batch = new ArrayList<>();

    protected boolean applicationIsRunning = false;
    private final Time renderClock = Time.getTime(60f);

    @Override
    // run the renderer in an update loop
    public void run() {
        while(applicationIsRunning)
        {
            renderClock.advance();
            if (renderClock.isReadyForNextFrame()) {
                System.out.print("clear ");
                render();
                System.out.print("finished ");
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