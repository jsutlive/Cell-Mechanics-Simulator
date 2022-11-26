package Renderer;

import Framework.Timer.Time;
import Renderer.Graphics.IRender;
import Utilities.Geometry.Vector.Vector2i;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Renderer implements Runnable {
    //Renderer.Graphics object that our painter class references to draw objects
    public static Graphics g;

    protected static String windowTitle;

    //Renderer object singleton instance.
    private static Renderer instance;

    protected List<IRender> batch = new ArrayList<>();

    protected static Dimension bounds;

    protected Camera camera;

    private boolean applicationIsRunning = false;

    private Time renderClock = Time.getTime(60f);

    /**
     * Used to generate a singleton instance of our Renderer.
     * @return the current Renderer, or create and return a new renderer if it is currently null.
     */
    public static Renderer getInstance(String title) {
        if(instance == null)
        {
            windowTitle = title;
            instance = build(Renderer2D.class);
            IRender.onRendererAdded.subscribe(instance::addObjectRendererToBatch);
            IRender.onRendererRemoved.subscribe(instance::removeObjectRendererFromBatch);
            instance.applicationIsRunning = true;
        }
        return instance;
    }

    public static Renderer getInstance(){
        return instance;
    }

    public static Camera getCamera(){
        if(instance == null) return null;
        else return instance.camera;
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

    /**
     * Add graphical representation of object that does not have attached physics
     * @param rend object that implements the IRender interface
     */
    public static void addGraphicToScene(IRender rend){
        instance.batch.add(rend);
    }

    public static void removeGraphicFromScene(IRender rend){
        instance.batch.removeIf(r -> instance.batch.contains(rend));
    }

     static <T extends Renderer> T build(Class<T> type) {
        try {
            return type.newInstance();
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
                render();
            }
            renderClock.printFrameRateAndResetFrameTimer();
        }
    }

    protected abstract void render();

    // Draw any circular objects or points
    public abstract void drawCircle(Vector2i center, int diameter);

    // Draw any lines
    public abstract void drawLine(Vector2i pointA, Vector2i pointB);

    // Set the renderer color
    public abstract  void setColor(Color color);

    public void clearAllEvents(){}
}