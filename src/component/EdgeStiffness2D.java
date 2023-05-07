package component;

import framework.rigidbodies.Node;
import utilities.geometry.Vector.Vector;
import utilities.geometry.Vector.Vector2f;
import utilities.math.CustomMath;

import java.util.ArrayList;
import java.util.List;

import static utilities.geometry.Geometry.calculateAngleBetweenPoints;

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
