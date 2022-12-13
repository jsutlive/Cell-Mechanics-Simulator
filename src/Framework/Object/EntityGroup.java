package Framework.Object;

import Renderer.Graphics.IColor;
import Renderer.Renderer;

import java.awt.*;
import java.util.List;

/**
 * Entity Group: grouped assortment of entities, i.e., an extension of a list/ array of cells. Designed to improve GUI
 * functionality
 */
public class EntityGroup {
    public List<Entity> entities;
    public Color color = Renderer.DEFAULT_COLOR;
    public String name;
    public int groupID;

    public EntityGroup(List<Entity>entities, int index){
        this.entities = entities;
        name = Integer.toString(index);
    }

    public EntityGroup(List<Entity> entities, String name, Color color){
        this.entities = entities;
        this.name = name;
        this.color = color;
    }

    public void setName(String name){
        this.name = name.substring(0, Math.min(name.length(), 5));
    }

    public void add(Entity entity){
        entities.add(entity);
    }

    public void recolor(){
        for(Entity e: entities){
            for(Component c: e.getComponents()){
                if(c instanceof IColor){
                    IColor rend = (IColor) c;
                    rend.setColor(color);
                }
            }
        }
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
