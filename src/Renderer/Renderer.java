package Renderer;

import Framework.Data.ImageHandler;
import Framework.Utilities.Time;
import Renderer.Graphics.IRender;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static Framework.Data.FileBuilder.fullPathName;

/**
 * Render object responsible for updating canvas
 */
public abstract class Renderer implements Runnable {

    public static Color DEFAULT_COLOR = Color.white;
    //Graphics object that our painter class references to draw objects
    protected Graphics graphics;

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
            //renderClock.printFrameRateAndResetFrameTimer();
        }
    }

    protected void exportImage(String name){
        BufferedImage screenshot = renderImage();
        ImageHandler writer = new ImageHandler(screenshot,
                new File(fullPathName +
                        name + "_seconds.jpg"));
        writer.write();
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
    public abstract BufferedImage renderImage();
}