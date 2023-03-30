package Component;

import Framework.Timer.Time;
import Utilities.Geometry.Vector.Vector;
import Framework.Rigidbodies.Edge;

import static Framework.States.StateMachine.timer;


public class ApicalConstrictingSpringForce extends SpringForce {
    public float rampTime = 300f;

    @Override
    public void awake() {
        RingCellMesh mesh = parent.getComponent(RingCellMesh.class);
        edges.add(mesh.edges.get(mesh.lateralResolution));
    }

    @Override
    public void update() {
        Vector force;
        for(Edge edge: edges){
            if(timer.elapsedTime <  Time.asNanoseconds(rampTime)) {
                force  = calculateSpringForce(edge, constant * timer.elapsedTime / Time.asNanoseconds(rampTime));
            }
            else {
                force = calculateSpringForce(edge, constant);
            }
            edge.addConstrictionForceVector(getClass().getSimpleName(), force);
        }
    }
}
