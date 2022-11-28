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

    public static Color DEFAULT_COLOR = Color.white;

    public static Renderer instance;

    protected List<IRender> batch = new ArrayList<>();

    protected static Dimension bounds;

    protected Camera camera;

    protected boolean applicationIsRunning = false;

    private final Time renderClock = Time.getTime(60f);

    public void clearBatch(){
        batch.clear();
    }

    protected void addGraphicToBatch(IRender rend){
        batch.add(rend);
    }

    protected void removeGraphicFromBatch(IRender rend){
        batch.remove(rend);
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

    public abstract void clearAllEvents();

    // Set the renderer color
    public abstract  void setColor(Color color);
}