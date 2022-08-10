package Model.Components.Physics;

import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;

import java.util.List;

public class SpringForce extends Force{
    public float targetLengthRatio;
    public List<Edge> edges;

    @Override
    public void apply() {
        for(Edge edge: edges){
            Node[] nodes = edge.getNodes();
            Vector2f forceVector = calculateSpringForce(edge, 5f);
        }
    }

    //TODO: FIX THIS FORCE
    @Override
    public void setup() {
        CellMesh cellMesh = (CellMesh) parent.getComponent(CellMesh.class);
        edges = cellMesh.edges;
    }

    public void setTargetLengthRatio(float ratio){
        targetLengthRatio = ratio;
    }

    public Vector2f calculateSpringForce(Edge edge, float constant) {
        // Hooke's law calculation to get force magnitude
        float forceMag = constant * (edge.getLength() - (targetLengthRatio * edge.getInitialLength()));
        // Find a unit vector showing x and y components of this edge
        Vector2f forceVector = new Vector2f(edge.getXUnit(), edge.getYUnit());
        // Multiply magnitude * unit vector
        forceVector.mul(forceMag);
        return forceVector;
    }


}
