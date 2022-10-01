package Morphogenesis.Components.Physics.Spring;

import Framework.Object.Tag;
import Framework.States.State;
import Framework.Timer.Time;
import Morphogenesis.Entities.Cell;
import Morphogenesis.Components.Meshing.Mesh;
import Utilities.Geometry.Vector.Vector;
import Morphogenesis.Models.DrosophilaRingModel;
import Morphogenesis.Components.Physics.Forces.Gradient;
import Morphogenesis.Rigidbodies.Edges.ApicalEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;


public class ApicalConstrictingSpringForce extends SpringForce {
    float rampTime = 5f;
    @Override
    public void awake() {
        Mesh mesh = parent.getComponent(Mesh.class);
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
            edge.addConstrictionForceVector(getClass().getSimpleName(), force);
        }
    }
}
