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

    public float constant = 10f;


    @Override
    public void awake() {
        List<Node2D> nodes = getComponent(Mesh.class).nodes;
        int size = nodes.size();
        for(int i = 0; i < size; i++){
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
            Vector normal = getNormalForNodeSet(key);
            float theta = calculateAngleBetweenNodes(key);
            float restingTheta = edgeAngleHashMap.get(key);
            if(theta > restingTheta) addForceToBody(key.get(1), normal.mul(constant));
            else if(theta < restingTheta) addForceToBody(key.get(1), normal.mul(-constant));
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
