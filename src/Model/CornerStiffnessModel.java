package Model;

import GUI.Painter;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Corner;
import Utilities.Geometry.Geometry;
import Utilities.Geometry.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class CornerStiffnessModel extends Model {

    List<Corner> corners = new ArrayList<>();
    List<Node> nodes = new ArrayList<>();
    float cornerAdjustConst = 0.25f;
    @Override
    public void awake() throws InstantiationException, IllegalAccessException {
        Node origin = new Node(400,400);
        Node north = new Node(400,600);
        Node south = new Node(400,200);
        Node east = new Node(600,400);
        Node west = new Node(200,400);

        Corner NE = new Corner(north, origin, east);
        Corner SE = new Corner(south, origin, east);
        Corner NW = new Corner(north, origin, west);
        Corner SW = new Corner(south, origin, west);

        corners.add(NE); corners.add(SE); corners.add(NW); corners.add(SW);
    }

    protected void adjustCorners() {
        int sign = -1;
        for (Corner corner : corners) {
            Node n1 = corner.nodes.get(0);
            Node n2 = corner.nodes.get(1);
            Node n3 = corner.nodes.get(2);
            if (Geometry.calculateAngleBetweenPoints(corner) > (float) Math.PI) {
                if (Geometry.calculateAngleBetweenPoints(corner) > Geometry.ninetyDegreesAsRadians) {
                    Vector2f n1Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n1.getPosition(), -10 * sign);
                    n1Force.mul(cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n1.AddForceVector(n1Force);

                    Vector2f n3Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n3.getPosition(), -10 * sign);
                    n3Force.mul(-cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n3.AddForceVector(n3Force);
                } else if (Geometry.calculateAngleBetweenPoints(corner) < Geometry.ninetyDegreesAsRadians) {
                    Vector2f n1Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n1.getPosition(), 10 * sign);
                    n1Force.mul(cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n1.AddForceVector(n1Force);

                    Vector2f n3Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n3.getPosition(), 10 * sign);
                    n3Force.mul(-cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n3.AddForceVector(n3Force);
                }
            } else {
                if (Geometry.calculateAngleBetweenPoints(corner) > Geometry.ninetyDegreesAsRadians) {
                    Vector2f n1Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n1.getPosition(), 10 * sign);
                    n1Force.mul(cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n1.AddForceVector(n1Force);

                    Vector2f n3Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n3.getPosition(), 10 * sign);
                    n3Force.mul(-cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n3.AddForceVector(n3Force);
                } else if (Geometry.calculateAngleBetweenPoints(corner) < Geometry.ninetyDegreesAsRadians) {
                    Vector2f n1Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n1.getPosition(), -10 * sign);
                    n1Force.mul(cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n1.AddForceVector(n1Force);

                    Vector2f n3Force = Geometry.getForceToMovePointAlongArc(n2.getPosition(), n3.getPosition(), -10 * sign);
                    n3Force.mul(-cornerAdjustConst);
                    n1Force.dot(corner.direction);
                    n3.AddForceVector(n3Force);
                }
            }

        }
    }

    @Override
    public void update() {
        adjustCorners();
        render();
    }

    public void render(){
        for(Node node: nodes)
        {
            Painter.drawPoint(node.getPosition().asInt());
        }
    }

    /**
     * Use State.create(Model.class) instead to ensure a proper reference to the state is established.
     * When established, this object immediately runs start functions.
     */
    public CornerStiffnessModel() throws InstantiationException, IllegalAccessException {
        this.start();
    }
}