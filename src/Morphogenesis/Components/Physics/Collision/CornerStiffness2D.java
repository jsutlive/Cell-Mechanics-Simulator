package Morphogenesis.Components.Physics.Collision;

import Morphogenesis.Components.Meshing.RingCellMesh;
import Morphogenesis.Components.Physics.Force;
import Morphogenesis.Components.ReloadComponentOnChange;
import Morphogenesis.Rigidbodies.Nodes.Node;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
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
        int apicalResolution = getComponent(RingCellMesh.class).apicalResolution;
        int lateralResolution = getComponent(RingCellMesh.class).lateralResolution;
        List<Node2D> nodes = getComponent(RingCellMesh.class).nodes;

        cornerA.add(nodes.get(nodes.size() - 1)); cornerA.add(nodes.get(0)); cornerA.add(nodes.get(1));

        cornerB.add(nodes.get(lateralResolution - 1)); cornerB.add(nodes.get(lateralResolution));
        cornerB.add(nodes.get(lateralResolution + 1));

        cornerC.add(nodes.get(lateralResolution + apicalResolution -1));
        cornerC.add(nodes.get(lateralResolution + apicalResolution));
        cornerC.add(nodes.get(lateralResolution + apicalResolution + 1));

        cornerD.add(nodes.get(nodes.size() - apicalResolution - 1));
        cornerD.add(nodes.get(nodes.size() - apicalResolution));
        cornerD.add(nodes.get(0));
    }

    @Override
    public void update() {
        calculateCornerStiffness(cornerA);
        calculateCornerStiffness(cornerB);
        calculateCornerStiffness(cornerC);
        calculateCornerStiffness(cornerD);
    }

    private void calculateCornerStiffness(List<Node> corner){
        Vector2f p1 = (Vector2f) corner.get(0).getPosition();
        Vector2f p2 = (Vector2f) corner.get(1).getPosition();
        Vector2f p3 = (Vector2f) corner.get(2).getPosition();

        Vector normal = CustomMath.normal(p1,p3);
        float theta = calculateAngleBetweenPoints(p1, p2, p3);

        if(theta > 90) addForceToBody(corner.get(1), normal.mul(constant));
        else if(theta < 90) addForceToBody(corner.get(1), normal.mul(-constant));
    }
}
