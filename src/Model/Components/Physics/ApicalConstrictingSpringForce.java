package Model.Components.Physics;

import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ForceVector.ForceType;
import Model.Model;
import Physics.Forces.GaussianGradient;
import Physics.Forces.Gradient;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

public class ApicalConstrictingSpringForce extends SpringForce{
    private Gradient apicalGradient = new GaussianGradient(0f, 0.8f);

    @Override
    public void awake() {
        forceVector.setType(ForceType.apicalConstriction);
        CellMesh mesh = parent.getComponent(CellMesh.class);
        for(Edge edge : mesh.edges) if (edge instanceof ApicalEdge) edges.add(edge);

        Cell cell = getParentAs(Cell.class);
        apicalGradient.calculate(10,
                .4f, .01f,
                .1f, .05f);
        setTargetLengthRatio(apicalGradient.getRatios()[1]);

    }

    @Override
    public void update() {
        for(Edge edge: edges){
            Node[] nodes = edge.getNodes();
            Cell cell = getParentAs(Cell.class);
            calculateSpringForce(edge, apicalGradient.getConstants()[0]);
            nodes[0].addForceVector(forceVector);
            nodes[1].addForceVector(forceVector.neg());
        }
    }
}
