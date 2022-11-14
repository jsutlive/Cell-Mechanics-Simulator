package Morphogenesis.Entities;

import Framework.Object.Entity;
import Morphogenesis.Components.Render.MeshRenderer;

public class Cell extends Entity {
    public transient int ringLocation;

    public void setRingLocation(int i){ringLocation = i;}

    public int getRingLocation(){
        return ringLocation;
    }

    /**
     * Add components here that are needed for all cells. This includes meshing
     * and the rendering mechanism.
     * In each unique cell type's start method, configure type-specific physics and characteristics.
     */
    @Override
    public void awake(){
        addComponent(new MeshRenderer());
    }
}

