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
    public void run()
    {
        try {
            if(State.GetState() != null)
            {
                System.out.print("clear ");
                System.out.flush();
                State.GetState().Render();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
