package Model.Components.Meshing;

import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class CellMesh extends Mesh{

    private transient float restingArea;
    @Override
    public void start() {
        calculateArea();
        restingArea = area;
    }

    @Override
    public void earlyUpdate() {
        for(Node n: nodes) n.resetResultantForce();
    }

    @Override
    public void lateUpdate() {
        System.out.println("MOVE");
        for (Node n : nodes) {
            n.Move();
        }
        calculateArea();
    }

    public float getRestingArea(){
        return restingArea;
    }

    public boolean collidesWithNode(Node n){
        //checks whether point is inside polygon by drawing a horizontal ray from the point
        //if the num of intersections is even, then it is outside, else it is inside
        //because if a point crosses the shape a total of a even amount of times, then it must have entered inside then exited again.
        Vector2f nodePos = n.getPosition();
        int intersections = 0;
        for(Edge edge: edges){
            Vector2f[] positions = edge.getPositions();
            Vector2f p1 = positions[0].sub(nodePos);
            Vector2f p2 = positions[1].sub(nodePos);

            //if they are both on same side of the y-axis, it doesn't intersect
            if(Math.signum(p1.y) == Math.signum(p2.y)){continue;}
            //intersection point (not the actual point, which would contain division, but changed in a way that it should still perserve sine)
            float intersectPoint = (p1.y * (p1.x - p2.x) - p1.x * (p1.y - p2.y)) * (p1.y - p2.y);

            if(intersectPoint < 0){continue;}

            intersections++;
        }
        return intersections%2 != 0;
    }
}
