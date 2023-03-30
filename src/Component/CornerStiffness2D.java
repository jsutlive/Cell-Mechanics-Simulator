package Component;

import Framework.Rigidbodies.Node;
import Framework.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

import java.util.ArrayList;
import java.util.List;

import static Utilities.Geometry.Geometry.calculateAngleBetweenPoints;

public class CornerStiffness2D extends Force {

    public float constant = 15.0f;

    private List<Node> cornerA = new ArrayList<>();
    private List<Node> cornerB = new ArrayList<>();
    private List<Node> cornerC = new ArrayList<>();
    private List<Node> cornerD = new ArrayList<>();

    @Override
    public void awake() {
        IBoxMesh boxMesh = (IBoxMesh) getComponent(Mesh.class);
        assert boxMesh!= null;
        int apicalResolution = boxMesh.getWidthResolution();
        int lateralResolution = boxMesh.getLengthResolution();
        List<Node2D> nodes = boxMesh.getNodes();

        cornerA.add(nodes.get(nodes.size() - 1)); cornerA.add(nodes.get(0)); cornerA.add(nodes.get(1));

        cornerB.add(nodes.get(lateralResolution - 2)); cornerB.add(nodes.get(lateralResolution-1));
        cornerB.add(nodes.get(lateralResolution));

        cornerC.add(nodes.get(lateralResolution + apicalResolution -2));
        cornerC.add(nodes.get(lateralResolution + apicalResolution -1));
        cornerC.add(nodes.get(lateralResolution + apicalResolution));

        cornerD.add(nodes.get(nodes.size() - apicalResolution - 1));
        cornerD.add(nodes.get(nodes.size() - apicalResolution));
        cornerD.add(nodes.get(0));
    }

    @Override
    public void update() {
        addForceToBody(cornerA.get(1), calculateCornerStiffness(cornerA));
        addForceToBody(cornerD.get(1), calculateCornerStiffness(cornerD));
        addForceToBody(cornerC.get(1), calculateCornerStiffness(cornerC));
        addForceToBody(cornerB.get(1), calculateCornerStiffness(cornerB));

    }

    /**
     * Calculate the force that needs to be added to corner nodes to retain corner stiffness
     * @param corner list of nodes (a,b,c) in order that make up a corner
     *                  p3
     *                 /
     *       p1----p2/
     *
     *   illegal argument exception thrown in the case that less/greater than three nodes are given.
     */
    public Vector calculateCornerStiffness(List<Node> corner){
        if(corner.size() != 3 ){
            throw new IllegalArgumentException("Corners must consist of three nodes");
        }
        Vector2f p1 = (Vector2f) corner.get(0).getPosition();
        Vector2f p2 = (Vector2f) corner.get(1).getPosition();
        Vector2f p3 = (Vector2f) corner.get(2).getPosition();

        Vector normal = CustomMath.normal(p1,p3);
        float theta = calculateAngleBetweenPoints(p1, p2, p3);
        if(theta >= 180f) theta -= 180f;

        if(theta > 90) return(normal.mul(constant));
        else if(theta < 90) return(normal.mul(-constant));
        else return normal.mul(0);
    }
}
