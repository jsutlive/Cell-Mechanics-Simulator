package Morphogenesis.Physics.Spring;

import Framework.Rigidbodies.Edge;
import Morphogenesis.Meshing.Mesh;
import Utilities.Geometry.Vector.Vector;

import java.util.ArrayList;

import static Framework.States.StateMachine.timer;
import static Framework.Timer.Time.asNanoseconds;
import static Framework.Timer.Time.fromNanoseconds;

public class BasalRigidityLossSpringForce extends SpringForce{
    Edge basalEdge;
    public float onsetTime = 60f;
    public float rampTime = 300f;
    public float basalEdgeConstantInitial;
    public float basalEdgeConstantFinal = 5f;
    public float rampConstantIncrementPerSecond;
    @Override
    public void awake() {
        edges = new ArrayList<>(getComponent(Mesh.class).edges);
        basalEdge = edges.get(edges.size()-1);
        edges.remove(edges.get(edges.size()-1));
        targetLengthRatio = 1;
        constant = 70f;
        basalEdgeConstantInitial = 450f;
        rampConstantIncrementPerSecond = (basalEdgeConstantInitial-basalEdgeConstantFinal)/rampTime;
    }

    @Override
    public void update() {
        Vector force;
        for(Edge edge: edges){
            force = calculateSpringForce(edge, constant);
            edge.addConstrictionForceVector(getClass().getSimpleName(), force);
        }
        if(timer.elapsedTime < asNanoseconds(onsetTime)){
            force = calculateSpringForce(basalEdge, constant);
        }else {
            if (timer.elapsedTime < asNanoseconds(rampTime)) {
                force = calculateSpringForce(basalEdge, basalEdgeConstantInitial - (rampConstantIncrementPerSecond * fromNanoseconds(timer.elapsedTime)));
            } else {
                force = calculateSpringForce(basalEdge, basalEdgeConstantFinal);
            }
        }
        basalEdge.addConstrictionForceVector(getClass().getSimpleName(), force);
    }
}
