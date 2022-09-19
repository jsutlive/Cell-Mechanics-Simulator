package Model.Components.Physics;

import Model.Components.Meshing.CellMesh;
import Model.Components.Meshing.Mesh;
import Model.Components.Physics.ForceVector.ForceType;
import Physics.Rigidbodies.Edge;
import Utilities.Math.CustomMath;

import java.util.List;

public class OsmosisForce extends Force {

    private transient List<Edge> edges;
    private transient float initialArea;
    public float osmosisConstant = 0.01f;

    @Override
    public void update() {
        restore();
    }

    public void restore(){
        //determine orientation of edges by finding perpendicular, instead of applying force to push from center, we lift each edge outwards
        //calculate normals

        float forceMagnitude =calculateOsmosisForceMagnitude(getComponent(Mesh.class));
        System.out.println(forceMagnitude);
        for(Edge edge : edges){
            forceVector.set(CustomMath.normal(edge));
            //forceVector.mul(-forceMagnitude);

            //multiplies the edgeNormal by the length
            //logically if an edge is larger, there is more force pushing on it
            //forceVector.mul(edge.getLength());

            edge.addForceVector(forceVector);
        }
    }

    public float calculateOsmosisForceMagnitude(Mesh mesh)
    {
        return osmosisConstant * (mesh.getArea() - mesh.getRestingArea());
    }

    @Override
    public void awake() {
        this.edges = getComponent(Mesh.class).edges;
        initialArea = getComponent(Mesh.class).getRestingArea();
        forceVector.setType(ForceType.osmosis);
    }
}
