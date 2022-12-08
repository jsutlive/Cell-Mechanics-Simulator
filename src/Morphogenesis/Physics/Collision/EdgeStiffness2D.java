package Morphogenesis.Physics.Collision;

import Morphogenesis.Meshing.IBoxMesh;
import Morphogenesis.Meshing.Mesh;
import Morphogenesis.Physics.Force;
import Framework.Rigidbodies.Node;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

import java.util.ArrayList;
import java.util.List;

import static Utilities.Geometry.Geometry.calculateAngleBetweenPoints;

public class EdgeStiffness2D extends Force {

    List<Node> sideA;

    public float constant = 30.0f;

    @Override
    public void awake() {
        sideA = new ArrayList<>();
        IBoxMesh boxMesh = (IBoxMesh) getComponent(Mesh.class);
        assert boxMesh!= null;
        for(int i =0; i <= boxMesh.getLengthResolution(); i++){
            sideA.add(boxMesh.getNodes().get(i));
        }
    }

    @Override
    public void update() {
        for(int i = 1; i<sideA.size() - 1; i++){
            Vector2f p1 = (Vector2f) sideA.get(i-1).getPosition();
            Vector2f p2 = (Vector2f) sideA.get(i).getPosition();
            Vector2f p3 = (Vector2f) sideA.get(i+1).getPosition();

            Vector normal = CustomMath.normal(p1,p3);
            float theta = calculateAngleBetweenPoints(p1, p2, p3);

            if(theta > 180) addForceToBody(sideA.get(i), normal.mul(constant));
            else if(theta < 180) addForceToBody(sideA.get(i), normal.mul(-constant));
            else addForceToBody(sideA.get(i), normal.mul(0));
        }
    }


}
