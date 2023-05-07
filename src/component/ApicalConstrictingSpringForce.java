package component;

import utilities.geometry.Vector.Vector;
import framework.rigidbodies.Edge;

import static framework.states.StateMachine.timer;
import static framework.utilities.Time.asNanoseconds;


public class ApicalConstrictingSpringForce extends SpringForce {
    public float onsetTime;
    public float rampTime = 300f;

    @Override
    public void awake() {
        RingCellMesh mesh = parent.getComponent(RingCellMesh.class);
        edges.add(mesh.edges.get(mesh.lateralResolution));
    }

    @Override
    public void update() {
        if(timer.elapsedTime < asNanoseconds(onsetTime)) return;
        Vector force;
        for(Edge edge: edges){
            if(timer.elapsedTime <  asNanoseconds(rampTime)) {
                force  = calculateSpringForce(edge, constant * timer.elapsedTime / asNanoseconds(rampTime));
            }
            else {
                force = calculateSpringForce(edge, constant);
            }
            edge.addConstrictionForceVector(getClass().getSimpleName(), force);
        }
    }
}
