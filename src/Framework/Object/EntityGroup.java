package Framework.Object;

import Renderer.Graphics.IColor;
import Renderer.Graphics.IRender;
import Renderer.Renderer;

import java.awt.*;
import java.util.List;

public class EntityGroup {
    public List<Entity> entities;
    public Color color = Renderer.DEFAULT_COLOR;
    public String name = "";

    public EntityGroup(List<Entity>entities, int index){
        this.entities = entities;
        name = Integer.toString(index);
    }

    public void setName(String name){
        this.name = name.substring(0, Math.min(name.length(), 5));
    }

    public void changeGroupColor(Color color){
        if (this.color == color) return;
        this.color = color;
        for(Entity e: entities){
            for(Component c: e.getComponents()){
                if(c instanceof IColor){
                    IColor rend = (IColor) c;
                    rend.setColor(color);
                }
            }
        }
    }
}
