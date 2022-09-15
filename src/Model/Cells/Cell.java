package Model.Cells;

import Data.LogData;
import Engine.Object.MonoBehavior;
import Engine.States.State;
import Model.Components.Component;
import Model.Components.Lattice.Lattice;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ApicalConstrictingSpringForce;
import Model.Components.Physics.ElasticForce;
import Model.Components.Render.CellRenderer;
import Physics.Forces.Force;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Corner;
import Utilities.Geometry.Geometry;
import Utilities.Geometry.Vector2f;
import Utilities.Math.Gauss;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Cell extends MonoBehavior {
    public transient int ringLocation;
    public transient int id;
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

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
        addComponent(new CellMesh());
        addComponent(new CellRenderer());
        addComponent(new Lattice());
    }
}

