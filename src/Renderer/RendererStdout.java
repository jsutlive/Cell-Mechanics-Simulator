package Renderer;

import Framework.States.State;


public class RendererStdout extends Renderer
{
    public RendererStdout()
    {

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
