package Morphogenesis.Components.Meshing;

import Framework.Data.*;
import Framework.Object.DoNotDestroyInGUI;
import Framework.Object.Entity;
import Morphogenesis.Components.Render.DoNotEditInGUI;
import Morphogenesis.Rigidbodies.Edges.ApicalEdge;
import Morphogenesis.Rigidbodies.Edges.BasalEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Edges.LateralEdge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;

import java.util.List;

/**
 * RingCellMesh retains the mesh information about a given cell, in particular its nodes, edges, and boundaries.
 * Can be used to determine collision boundaries, etc.
 */
@LogData
@DoNotDestroyInGUI
public class RingCellMesh extends Mesh{

    @DoNotEditInGUI
    public int lateralResolution = 4;
    @DoNotEditInGUI
    public int apicalResolution = 1;


    @Override
    public void start() {
        calculateArea();
    }

    @Override
    public void earlyUpdate() {
        for(Node2D n: nodes) n.resetResultantForce();
    }

    @Override
    public void lateUpdate() {
        for (Node2D n : nodes) {
            n.move();
        }
        calculateArea();
    }

    @Override
    public Entity returnCellContainingPoint(Vector2f vector2f){
        if(collidesWithPoint(vector2f)) return parent;
        return null;
    }

    public boolean collidesWithNode(Node2D n) {
        Vector2f nodePos = n.getPosition().copy();
        return collidesWithPoint(nodePos);
    }

    public boolean collidesWithPoint(Vector2f vec){
        boolean collision = false;

        // go through each of the vertices, plus
        // the next vertex in the list
        int next;
        for (int current=0; current<nodes.size(); current++) {

            // get next vertex in list
            // if we've hit the end, wrap around to 0
            next = current + 1;
            if (next == nodes.size()) next = 0;

            // get the PVectors at our current position
            // this makes our if statement a little cleaner
            Vector2f vc = nodes.get(current).getPosition();    // c for "current"
            Vector2f vn = nodes.get(next).getPosition();       // n for "next"

            // compare position, flip 'collision' variable
            // back and forth
            if (((vc.y >= vec.y && vn.y < vec.y) || (vc.y < vec.y && vn.y >= vec.y)) &&
                    (vec.x < (vn.x-vc.x)*(vec.y-vc.y) / (vn.y-vc.y)+vc.x)) {
                collision = !collision;
            }
        }
        return collision;
    }

    public RingCellMesh build(List<Node2D> builderNodes){
        return build(builderNodes, apicalResolution, lateralResolution);
    }

    public RingCellMesh build(List<Node2D> builderNodes, int xRes, int yRes){
        // Start from top left, move along til end of lateral resolution
        nodes = builderNodes;
        int nodeCount = 0;
        while (nodeCount < yRes){
            nodeCount++;
            Edge e = new LateralEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while (nodeCount < yRes + xRes){
            nodeCount++;
            Edge e = new ApicalEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while(nodeCount < (2*yRes) + xRes){
            nodeCount++;
            Edge e = new LateralEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
            e.setNodesReference(nodeCount-1, nodeCount);
            edges.add(e);
        }
        while (nodeCount < nodes.size()){
            nodeCount++;
            if(nodeCount == nodes.size()){
                Edge e = new BasalEdge(nodes.get(nodeCount-1), nodes.get(0));
                e.setNodesReference(nodeCount-1, 0);
                edges.add(e);
            }
            else{
                Edge e = new BasalEdge(nodes.get(nodeCount-1), nodes.get(nodeCount));
                e.setNodesReference(nodeCount-1, nodeCount);
                edges.add(e);
            }
        }
        return this;
    }
}
