package Model.Components.Physics;

import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ForceVector.ForceVector;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class SpringForce extends Force{
    protected float targetLengthRatio;
    public List<Edge> edges = new ArrayList<>();


    public void setTargetLengthRatio(float ratio){
        targetLengthRatio = ratio;
    }

    protected void calculateSpringForce(Edge edge, float constant) {
        // Hooke's law calculation to get force magnitude
        float forceMag = constant * (edge.getLength() - (targetLengthRatio * edge.getInitialLength()));
        // Find a unit vector showing x and y components of this edge
        Vector2f unit = new Vector2f(edge.getXUnit(), edge.getYUnit());
        // Multiply magnitude * unit vector
        forceVector.set(unit.mul(forceMag));
    }


}
