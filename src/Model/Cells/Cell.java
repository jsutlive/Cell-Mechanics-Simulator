package Model.Cells;

import Engine.Object.MonoBehavior;
import Engine.States.State;
import Model.Components.Component;
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
    public int ringLocation;
    public transient int id;
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public void setRingLocation(int i){ringLocation = i;}

    public int getRingLocation(){
        return ringLocation;
    }

    /**
     * Add components here that are needed for all cells. This includes more generic forces
     * and the rendering mechanism.
     * In each unique cell type's awake method, configure type-specific physics and characteristics.
     */
    @Override
    public void awake(){
        addRenderer(new CellRenderer());
        addComponent(new CellMesh());
        State.setFlagToRender(this);
    }

    @Override
    public void start() {
        addComponent(new ElasticForce());
        //addComponent(new ApicalConstrictingSpringForce());
    }

    public void overrideElasticConstants(){
    }


    public void addRenderer(CellRenderer renderer){
        addComponent(renderer);
        State.setFlagToRender(this);
    }
    /*
    public Vector2f getCenter(){

    }

    public void print()
    {
        String cellClass;
        int numberOfApicalEdges = 0;
        int numberOfBasalEdges = 0;
        int numberOfLateralEdges = 0;

        if(this instanceof ApicalConstrictingCell){
            cellClass = "Apical Constricting Cell";
        }else if(this instanceof ShorteningCell){
            cellClass = "Lateral Shortening Cell";
        }else {
            cellClass = "Basic Cell";
        }
        for(Edge edge:edges){
            if(edge instanceof ApicalEdge){
                numberOfApicalEdges++;
            }else if (edge instanceof BasalEdge){
                numberOfBasalEdges++;
            }else if (edge instanceof LateralEdge){
                numberOfLateralEdges++;
            }
        }
        System.out.println("--CELL VARS--");
        System.out.println("CELL ID: " + id);
        System.out.println("CELL TYPE: " + cellClass);
        System.out.println("RING LOCATION: " + getRingLocation());
        System.out.println("APICAL EDGES: " + numberOfApicalEdges);
        System.out.println("BASAL EDGES: " + numberOfBasalEdges);
        System.out.println("LATERAL EDGES: " + numberOfLateralEdges);
    }

    void generateInternalEdges(List<Node> nodes){
        int length = nodes.size();
        int halfLength = length/2;
        for(int i =0; i <halfLength - 1; i++){
            //internalEdges.add(new BasicEdge(nodes.get(i), nodes.get(length - i- 2)));
            //internalEdges.add(new BasicEdge(nodes.get(i + 1), nodes.get(length - i -1)));
        }
    }*/
}

