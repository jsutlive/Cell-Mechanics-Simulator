package Model.Components.Physics.Spring;

import Engine.Object.Tag;
import Engine.States.State;
import Engine.Timer.Time;
import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Utilities.Geometry.Vector.Vector;
import Model.DrosophilaRingModel;
import Physics.Forces.Gradient;
import Physics.Rigidbodies.Edges.ApicalEdge;
import Physics.Rigidbodies.Edges.Edge;


public class ApicalConstrictingSpringForce extends SpringForce {
    float constant;
    float rampTime = 5f;
    @Override
    public void awake() {
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
        Vector force;
        for(Edge edge: edges){
            if(Time.elapsedTime <  Time.asNanoseconds(rampTime)) {
                force  = calculateSpringForce(edge, constant * Time.elapsedTime / Time.asNanoseconds(rampTime));
            }
            else {
                force = calculateSpringForce(edge, constant);
            }
            addConstrictionForceToEdge(edge, force);
        }
    }
}
