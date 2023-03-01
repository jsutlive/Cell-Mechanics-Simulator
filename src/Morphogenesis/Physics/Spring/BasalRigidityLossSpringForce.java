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
    public float onsetTime = 0f;
    public float rampTime = 25f;
    public float basalEdgeConstant;
    public float rampConstantIncrementPerSecond;
    @Override
    public void awake() {
        edges = new ArrayList<>(getComponent(Mesh.class).edges);
        basalEdge = edges.get(edges.size()-1);
        edges.remove(edges.get(edges.size()-1));
        targetLengthRatio = 1;
        constant = 25f;
        basalEdgeConstant = 5f;
        rampConstantIncrementPerSecond = (constant-basalEdgeConstant)/rampTime;
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
                force = calculateSpringForce(basalEdge, basalEdgeConstant - (rampConstantIncrementPerSecond * fromNanoseconds(timer.elapsedTime)));
            } else {
                force = calculateSpringForce(basalEdge, basalEdgeConstant);
            }
        }
        basalEdge.addConstrictionForceVector(getClass().getSimpleName(), force);
    }
}
