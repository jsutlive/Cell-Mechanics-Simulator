package Renderer;

import Framework.States.State;
import Renderer.Graphics.IRender;


public class RendererStdout extends Renderer
{
    public RendererStdout()
    {
        applicationIsRunning = true;
        IRender.onRendererAdded.subscribe(this::addGraphicToBatch);
        IRender.onRendererRemoved.subscribe(this::removeGraphicFromBatch);
    }
    /**
     * Renders graphics to the screen. Should only be accessed from the Engine object.
     */

    @Override
    public void render()
    {
        for(int i = batch.size() -1; i >= 0; i--){
            if(i >= batch.size())return;
            batch.get(i).render();
        }
    }
}
