package Renderer.Graphics;

import Framework.Events.EventHandler;

public interface IRender {
    EventHandler<IRender> onRendererAdded = new EventHandler<>();
    EventHandler<IRender> onRendererRemoved = new EventHandler<>();
    default void add(IRender rend){
        onRendererAdded.invoke(rend);
    }
    default void remove(IRender rend){
        onRendererRemoved.invoke(rend);
    }
    void render();
}
