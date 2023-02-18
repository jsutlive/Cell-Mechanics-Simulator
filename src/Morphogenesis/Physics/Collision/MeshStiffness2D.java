package Morphogenesis.Physics.Collision;

import Morphogenesis.Meshing.Mesh;
import Morphogenesis.Physics.Force;
import Framework.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Utilities.Geometry.Geometry.calculateAngleBetweenPoints;

public class MeshStiffness2D extends Force {

    private HashMap<ArrayList<Node2D>, Float> edgeAngleHashMap = new HashMap<>();
    public float constant = 15f;
    public float cornerFactor = 1f;

    @Override
    public void awake() {
        List<Node2D> nodes = getComponent(Mesh.class).nodes;
        int size = nodes.size();
        for(int i = 0; i < size-1; i++){
            int a, b, c;
            if(i == 0){
                a = size-1; b = i; c = i + 1;
            }else if (i == nodes.size()-1){
                a = size-2; b = size-1; c = 0;
            }else{
                a = i-1; b = i; c = i+1;
            }
            ArrayList<Node2D> angles = new ArrayList<>();
            angles.add(nodes.get(a)); angles.add(nodes.get(b)); angles.add(nodes.get(c));
            edgeAngleHashMap.put(angles, calculateAngleBetweenNodes(angles));
        }
    }

    @Override
    public void update() {
        for(ArrayList<Node2D> key : edgeAngleHashMap.keySet()){
            Vector2f p1 = key.get(0).getPosition();
            Vector2f p2 = key.get(1).getPosition();
            Vector2f p3 = key.get(2).getPosition();
            Vector normal1 = CustomMath.normal(p1,p2);
            Vector normal2 = CustomMath.normal(p2,p3);
            Vector normal = getNormalForNodeSet(key);
            float theta = calculateAngleBetweenNodes(key);
            float restingTheta = edgeAngleHashMap.get(key);

            //the longer the edge, the lesser the force is applied to the edge, makes sense when thinking about force
            //scaling the force linearly based on distance (longer edge, stronger force) makes the mesh unstable
            float dist1 = 1/p1.distanceTo(p2);
            float dist2 = 1/p2.distanceTo(p3);

            if(theta!= restingTheta) {
                float distFromTheta = theta - restingTheta;
                if (restingTheta > 130 || restingTheta < 45) {
                    Vector edge1Force = normal1.mul(constant * distFromTheta * dist1).neg();
                    Vector edge2Force = normal2.mul(constant * distFromTheta * dist2).neg();
                    //by adding the opposite of whatever is added to the surrounding edges, the total force added is 0, so it shouldn't drift
                    addForceToBody(key.get(1), edge1Force.neg());
                    addForceToBody(key.get(1), edge2Force.neg());

                    addForceToBody(key.get(0), edge1Force);
                    addForceToBody(key.get(2), edge2Force );
                }else{
                    Vector edge1Force = normal1.mul(constant * cornerFactor * distFromTheta * dist1).neg();
                    Vector edge2Force = normal2.mul(constant * cornerFactor * distFromTheta * dist2).neg();

                    addForceToBody(key.get(1), edge1Force.neg());
                    addForceToBody(key.get(1), edge2Force.neg());

                    addForceToBody(key.get(0), edge1Force);
                    addForceToBody(key.get(2), edge2Force );
                }
            }
        }
    }

    // get positions from node list, use external method to calculate angle between points
    private float calculateAngleBetweenNodes(ArrayList<Node2D> nodes){
        Vector2f p1 = nodes.get(0).getPosition();
        Vector2f p2 = nodes.get(1).getPosition();
        Vector2f p3 = nodes.get(2).getPosition();
        return calculateAngleBetweenPoints(p1, p2, p3);
    }

    // get positions from node list, use external method to calculate normal
    private Vector getNormalForNodeSet(ArrayList<Node2D> nodes){
        Vector2f p1 = nodes.get(0).getPosition();
        Vector2f p2 = nodes.get(2).getPosition();
        return CustomMath.normal(p1, p2);
    }
}
