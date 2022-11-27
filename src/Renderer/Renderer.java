package Renderer;

import Framework.Timer.Time;
import Renderer.Graphics.IRender;
import Utilities.Geometry.Vector.Vector2i;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Renderer implements Runnable {
    //Renderer object singleton instance.
    private static Renderer instance;

    protected List<IRender> batch = new ArrayList<>();

    private boolean applicationIsRunning = false;

    private final Time renderClock = Time.getTime(60f);

    /**
     * Used to generate a singleton instance of our Renderer.
     * @return the current Renderer, or create and return a new renderer if it is currently null.
     */
    public static Renderer getInstance(String title) {
        if(instance == null)
        {
            instance = build();
            if(instance!=null) {
                IRender.onRendererAdded.subscribe(instance::addObjectRendererToBatch);
                IRender.onRendererRemoved.subscribe(instance::removeObjectRendererFromBatch);
                instance.applicationIsRunning = true;
            }
        }
        return instance;
    }

    public static Renderer getInstance(){
        return instance;
    }

    public static void clearBatch(){
        instance.batch.clear();
    }

    protected void addObjectRendererToBatch(IRender rend){
        instance.batch.add(rend);
    }

    protected void removeObjectRendererFromBatch(IRender rend){
        instance.batch.remove(rend);
    }

     static <T extends Renderer> T build() {
        try {
            return ((Class<T>) RendererStdout.class).newInstance();
        } catch (IllegalAccessException | InstantiationException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    // run the renderer in an update loop
    public void run() {
        while(applicationIsRunning) {
            renderClock.advance();
            if (renderClock.isReadyForNextFrame()) {
                System.out.print("clear ");
                System.out.flush();
                render();
            }
            renderClock.printFrameRateAndResetFrameTimer();
        }
    }

    protected abstract void render();
}