package Model.Components.Physics;

import Engine.Object.Tag;
import Engine.States.State;
import Engine.Timer.Time;
import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ForceVector.ForceType;
import Model.DrosophilaRingModel;
import Physics.Forces.Gradient;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;


public class ApicalConstrictingSpringForce extends SpringForce{
    float constant;
    float rampTime = 2f;
    @Override
    public void awake() {
        forceVector.setType(ForceType.apicalConstriction);
        CellMesh mesh = parent.getComponent(CellMesh.class);
        for(Edge edge : mesh.edges) if (edge instanceof ApicalEdge) edges.add(edge);

        int ringPosition = getParentAs(Cell.class).getRingLocation();
        DrosophilaRingModel model = (DrosophilaRingModel)State.findObjectWithTag(Tag.MODEL);
        Gradient apicalGradient = model.apicalGradient;
        constant = apicalGradient.getConstants()[ringPosition - 1];
        setTargetLengthRatio(apicalGradient.getRatios()[ringPosition - 1]);
        System.out.println(constant);
    }

    @Override
    public void update() {
        for(Edge edge: edges){
            Node[] nodes = edge.getNodes();
            Cell cell = getParentAs(Cell.class);
            if(Time.elapsedTime <  Time.asNanoseconds(rampTime)) {
                calculateSpringForce(edge, constant * Time.elapsedTime / Time.asNanoseconds(rampTime));
            }
            else {
                calculateSpringForce(edge, constant);
            }
            nodes[0].addForceVector(forceVector);
            nodes[1].addForceVector(forceVector.neg());
        }
    }
}
