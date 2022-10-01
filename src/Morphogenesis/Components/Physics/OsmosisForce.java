package Morphogenesis.Components.Physics;

import Morphogenesis.Components.Meshing.Mesh;
import Utilities.Geometry.Vector.Vector;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Utilities.Math.CustomMath;

import java.util.List;

public class OsmosisForce extends Force {

    private transient List<Edge> edges;
    private transient float initialArea;
    public float osmosisConstant = 0.003f;

    @Override
    public void update() {
        float forceMagnitude = calculateOsmosisForceMagnitude(getComponent(Mesh.class));
        restore(forceMagnitude);
    }

    public void restore(float forceMagnitude){
        Vector force;
        //determine orientation of edges by finding perpendicular, instead of applying force to push from center, we lift each edge outwards
        //calculate normals

        for(Edge edge : edges){
            force = CustomMath.normal(edge);
            force.mul(forceMagnitude);

            //multiplies the edgeNormal by the length
            //logically if an edge is larger, there is more force pushing on it
            force.mul(edge.getLength());

            addForceToBody(edge, force);
        }
    }

    public float calculateOsmosisForceMagnitude(Mesh mesh)
    {
        return osmosisConstant * (mesh.getArea() - initialArea);
    }

    @Override
    public void awake() {
        this.edges = getComponent(Mesh.class).edges;
        initialArea = getComponent(Mesh.class).getArea();
    }
}
