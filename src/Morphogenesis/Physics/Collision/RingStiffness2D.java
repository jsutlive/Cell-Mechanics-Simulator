package Morphogenesis.Physics.Collision;

import Morphogenesis.Meshing.RingMesh;
import Morphogenesis.Physics.Force;
import Framework.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

import java.util.HashMap;
import java.util.ArrayList;

import static Utilities.Geometry.Geometry.calculateAngleBetweenPoints;

public class RingStiffness2D extends Force {
    HashMap<ArrayList<Node2D>, Float> outerEdgeAngleHashMap = new HashMap<>();
    HashMap<ArrayList<Node2D>, Float> innerEdgeAngleHashMap = new HashMap<>();

    public float innerConstant = 5f;
    public float outerConstant = 5f;

    @Override
    public void awake() {
        RingMesh ring = getComponent(RingMesh.class);
        ArrayList<Node2D> outerNodes = ring.outerNodes;
        ArrayList<Node2D> innerNodes = ring.innerNodes;

        for(int i = 0; i < innerNodes.size(); i++){
            int a, b, c;
            if(i == 0){
                a = innerNodes.size()-1; b = i; c = i + 1;
            }else if (i == innerNodes.size()-1){
                a = innerNodes.size()-2; b = innerNodes.size()-1; c = 0;
            }else{
                a = i-1; b = i; c = i+1;
            }
            ArrayList<Node2D> outerAngle = new ArrayList<>();
            outerAngle.add(outerNodes.get(a)); outerAngle.add(outerNodes.get(b)); outerAngle.add(outerNodes.get(c));
            ArrayList<Node2D> innerAngle = new ArrayList<>();
            innerAngle.add(innerNodes.get(a)); innerAngle.add(innerNodes.get(b)); innerAngle.add(innerNodes.get(c));
            outerEdgeAngleHashMap.put(outerAngle, calculateAngleBetweenNodes(outerAngle));
            innerEdgeAngleHashMap.put(innerAngle, calculateAngleBetweenNodes(innerAngle));
        }
    }

    @Override
    public void update() {
        for(ArrayList<Node2D> key : outerEdgeAngleHashMap.keySet()){
            Vector normal = getNormalForNodeSet(key);
            float theta = calculateAngleBetweenNodes(key);
            float restingTheta = outerEdgeAngleHashMap.get(key);
            if(theta > restingTheta) addForceToBody(key.get(1), normal.mul(outerConstant));
            else if(theta < restingTheta) addForceToBody(key.get(1), normal.mul(-outerConstant));
            else addForceToBody(key.get(1), normal.mul(0));
        }
        for(ArrayList<Node2D> key : innerEdgeAngleHashMap.keySet()){
            Vector normal = getNormalForNodeSet(key);
            float theta = calculateAngleBetweenNodes(key);
            float restingTheta = innerEdgeAngleHashMap.get(key);
            if(theta > restingTheta) addForceToBody(key.get(1), normal.mul(innerConstant));
            else if(theta < restingTheta) addForceToBody(key.get(1), normal.mul(-innerConstant));
            else addForceToBody(key.get(1), normal.mul(0));
        }
    }

    private float calculateAngleBetweenNodes(ArrayList<Node2D> nodes){
        Vector2f p1 = nodes.get(0).getPosition();
        Vector2f p2 = nodes.get(1).getPosition();
        Vector2f p3 = nodes.get(2).getPosition();

        return calculateAngleBetweenPoints(p1, p2, p3);
    }

    private Vector getNormalForNodeSet(ArrayList<Node2D> nodes){
        Vector2f p1 = nodes.get(0).getPosition();
        Vector2f p2 = nodes.get(2).getPosition();
        return CustomMath.normal(p1, p2);
    }
}
