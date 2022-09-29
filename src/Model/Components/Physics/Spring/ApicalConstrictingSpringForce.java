package Model.Components.Physics.Spring;

import Engine.Object.Tag;
import Engine.States.State;
import Engine.Timer.Time;
import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Utilities.Physics.ForceType;
import Model.DrosophilaRingModel;
import Physics.Forces.Gradient;
import Physics.Rigidbodies.Edges.ApicalEdge;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Nodes.Node2D;


public class ApicalConstrictingSpringForce extends SpringForce {
    float constant;
    float rampTime = 5f;
    @Override
    public void awake() {
        forceVector.setType(ForceType.apicalConstriction);
        CellMesh mesh = parent.getComponent(CellMesh.class);
        for(Edge edge : mesh.edges) if (edge instanceof ApicalEdge) edges.add(edge);

        int ringPosition = getParentAs(Cell.class).getRingLocation();
        DrosophilaRingModel model = (DrosophilaRingModel)State.findObjectWithTag(Tag.MODEL);
        assert model != null;
        Gradient apicalGradient = model.apicalGradient;
        constant = apicalGradient.getConstants()[ringPosition - 1];
        setTargetLengthRatio(apicalGradient.getRatios()[ringPosition - 1]);
    }

    @Override
    public void update() {
        for(Edge edge: edges){
            Node2D[] nodes = edge.getNodes();
            if(Time.elapsedTime <  Time.asNanoseconds(rampTime)) {
                forceVector.set(calculateSpringForce(edge, constant * Time.elapsedTime / Time.asNanoseconds(rampTime)));
            }
            else {
                forceVector.set(calculateSpringForce(edge, constant));
            }
            nodes[0].addForceVector(forceVector);
            nodes[1].addForceVector(forceVector.neg());
        }
    }
}
