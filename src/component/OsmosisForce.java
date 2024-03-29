package component;

import annotations.DoNotEditInGUI;
import utilities.geometry.Vector.Vector;
import framework.rigidbodies.Edge;
import java.util.List;

import static utilities.math.CustomMath.normal;

public class OsmosisForce extends Force {

    private transient List<Edge> edges;
    private transient float initialArea;
    @DoNotEditInGUI
    public transient float desiredArea;
    public float osmosisConstant = 0.1f;
    public float internalPressure = 0f;

    @Override
    public void onValidate() {
        this.edges = getComponent(Mesh.class).edges;
        initialArea = getComponent(Mesh.class).getArea();
        desiredArea = initialArea + (initialArea*internalPressure);
    }

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

}
