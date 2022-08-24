package Model.Components.Physics;

import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ForceVector.ForceType;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;

import java.util.List;

public class OsmosisForce extends Force {

    private transient List<Edge> edges;
    private transient float initialArea;
    private float osmosisConstant = .005f;

    @Override
    public void update() {
        restore();
    }

    public void restore(){
        //determine orientation of edges by finding perpendicular, instead of applying force to push from center, we lift each edge outwards
        //calculate normals

        float forceMagnitude = osmosisConstant * (parent.getComponent(CellMesh.class).getArea() - initialArea);

        for(Edge edge : edges){
            forceVector.set(CustomMath.normal(edge));
            forceVector.mul(-forceMagnitude);

            //multiplies the edgeNormal by the length
            //logically if an edge is larger, there is more force pushing on it
            forceVector.mul(edge.getLength());

            edge.addForceVector(forceVector);
        }
    }

    @Override
    public void setup() {
        this.edges = parent.getComponent(CellMesh.class).edges;
        initialArea = parent.getComponent(CellMesh.class).getRestingArea();
        forceVector.setType(ForceType.osmosis);
    }
}
