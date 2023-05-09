package renderer;

import framework.data.ImageHandler;
import framework.utilities.Time;
import renderer.graphics.IRender;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static framework.data.FileBuilder.fullPathName;

/**
 * Render object responsible for updating canvas
 *
 * Copyright (c) 2023 Joseph Sutlive
 * All rights reserved
 */
public abstract class Renderer implements Runnable {

    public static final Color DEFAULT_COLOR = Color.white;
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
        }
    }

    /**
     * Exports an image of the simulation view area to file
     * @param name file name given by user
     */
    protected void exportImage(String name){
        BufferedImage screenshot = renderImage();
        ImageHandler writer = new ImageHandler(screenshot,
                new File(fullPathName +
                        name + "_seconds.jpg"));
        writer.write();
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