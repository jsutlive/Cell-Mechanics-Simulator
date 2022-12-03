package Morphogenesis.Components.Physics;

import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.ReloadComponentOnChange;
import Utilities.Geometry.Vector.Vector;
import Morphogenesis.Rigidbodies.Edge;
import java.util.List;

import static Utilities.Math.CustomMath.normal;

@ReloadComponentOnChange
public class OsmosisForce extends Force {

    private transient List<Edge> edges;
    private transient float initialArea;
    public transient float desiredArea;
    public float osmosisConstant = 0.025f;
    public float internalPressure = 0f;

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
            force = normal(edge);
            force.mul(forceMagnitude);

            //multiplies the edgeNormal by the length
            //logically if an edge is larger, there is more force pushing on it
            force.mul(edge.getLength());
            addForceToBody(edge, force);
        }
    }

    private float calculateOsmosisForceMagnitude(Mesh mesh) {
        return osmosisConstant * (mesh.getArea() - desiredArea);
    }

    @Override
    public void awake() {
        this.edges = getComponent(Mesh.class).edges;
        initialArea = getComponent(Mesh.class).getArea();
        desiredArea = initialArea + (initialArea*internalPressure);
    }
}
