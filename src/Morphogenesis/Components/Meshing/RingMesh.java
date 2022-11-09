package Morphogenesis.Components.Meshing;

import Framework.Object.DoNotExposeInGUI;
import Framework.Object.Entity;
import Morphogenesis.Entities.Cell;
import Morphogenesis.Rigidbodies.Edges.ApicalEdge;
import Morphogenesis.Rigidbodies.Edges.BasalEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;
import Utilities.Math.CustomMath;
import Utilities.Math.Gauss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DoNotExposeInGUI
public class RingMesh extends Mesh{

    int lateralResolution = 4;


    public int segments = 80;

    float outerRadius = 300;
    float innerRadius = 200;

    public List<Node2D> outerNodes = new ArrayList<>();
    public List<Node2D> innerNodes = new ArrayList<>();
    public List<Cell> cellList = new ArrayList<>();
    private transient List<Edge> basalEdges = new ArrayList<>();
    private transient List<Edge> apicalEdges = new ArrayList<>();

    @Override
    protected void calculateArea(){
        area = Gauss.nShoelace(outerNodes) - Gauss.nShoelace(innerNodes);
    }

    @Override
    public Entity returnCellContainingPoint(Vector2f vector2f){
        for(Cell cell: cellList){
            if(cell.getComponent(RingCellMesh.class).collidesWithPoint(vector2f)){
                return cell;
            }
        }
        return parent;
    }

    public void addCellToList(List<Cell> cellList, Cell cell, int ringLocation) {
        if(cell !=null) {
            cell.setRingLocation(ringLocation);
            cellList.add(cell);
            cell.name = "Cell " + cellList.size();
        }else
        {
            throw new NullPointerException("New cell object not instantiated successfully");
        }
        if(cell.getComponent(RingCellMesh.class).nodes.size() == 0){
            throw new IllegalStateException("Nodes list not found at ring location " + ringLocation);
        }
    }

    public float getRadiusToNode(int j) {
        float radiusStep = (innerRadius - outerRadius) / lateralResolution;
        return outerRadius +  radiusStep * j;
    }

}
