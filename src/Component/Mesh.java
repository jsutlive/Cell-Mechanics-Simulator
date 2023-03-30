package Component;

import Framework.Events.EventHandler;
import Framework.Object.Entity;
import Framework.Object.Transform;
import Annotations.Builder;
import Annotations.DoNotEditInGUI;
import Annotations.VirtualRenderer;
import Framework.Rigidbodies.Edge;
import Framework.Rigidbodies.Node;
import Framework.Rigidbodies.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;
import Utilities.Math.Gauss;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class Mesh extends Component {
    public transient ArrayList<Node2D> nodes = new ArrayList<>();
    public transient ArrayList<Edge> edges = new ArrayList<>();

    public static EventHandler<Mesh> onMeshRebuilt = new EventHandler<>();

    @DoNotEditInGUI
    public float area;
    private Vector2f centroid;
    @DoNotEditInGUI
    public float restingArea;

    public float getPerimeter(){
        float perimeter = 0;
        for(Edge e: edges) perimeter += e.getLength();
        return perimeter;
    }

    public boolean contains(Node n){
        for(Node2D node : nodes){
            if(node.getPosition().equals(n.getPosition()))
                return true;
        }
        return false;
    }
    public boolean contains(Edge e){
        return edges.contains(e);
    }

    public void reset(){
        for(Node n: nodes){
            n.reset();
        }
    }

    public float getArea(){
        calculateArea();
        return area;
    }

    public float getMaximumDistance(){return 0f;}

    public float getMinimumDistance(){return 0f;}

    protected void calculateArea(){
        area = CustomMath.round(Gauss.nShoelace(nodes),1);
        if(restingArea == 0) restingArea = area;
    }

    public Vector2f calculateCentroid(){
        float x = 0;
        float y = 0;
        for (Node2D node: nodes){
            x += node.getPosition().x;
            y += node.getPosition().y;
        }
        x = x/nodes.size();
        y = y/nodes.size();
        centroid = new Vector2f(x,y);
        return centroid;
    }

    // REFACTOR THIS METHOD
    public float getDistanceToBoundary(){
        if(parent.parent!= null){
            RigidBoundary boundary = parent.parent.getComponent(RigidBoundary.class);
            if(boundary!= null) {
                return getDistanceToBoundary(boundary);
            }
        }
        return 0;
    }

    public float getDistanceToBoundary(RigidBoundary boundary) {
        float shortestDistance = Float.POSITIVE_INFINITY;
        float radius = boundary.outerRadius;
        for (Node node : nodes) {
            Vector2f position = (Vector2f) node.getPosition();
            float dist = CustomMath.abs(Vector2f.dist(position, Vector2f.zero)- radius);
            shortestDistance = Math.min(shortestDistance, dist);
        }
        return shortestDistance;
    }


    @Override
    public void onValidate()
    {
        if(getComponent(Mesh.class)!= this) {
            removeSelf();
            return;
        }
        parent.removeComponent(MeshRenderer.class);
        Transform t = getComponent(Transform.class);
        if(t!= null) t.position = calculateCentroid();
        for(Method method: getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(Builder.class))
            {
                try {
                    method.invoke(this);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if(parent.getClass().isAnnotationPresent(VirtualRenderer.class)){
            parent.addComponent(new MeshRenderer(false));
        }
        else parent.addComponent(new MeshRenderer(true));
    }

    public Entity returnCellContainingPoint(Vector2f vector2f){
        if(collidesWithPoint(vector2f)) return parent;
        return null;
    }

    public boolean collidesWithNode(Node2D n) {
        Vector2f nodePos = n.getPosition().copy();
        return collidesWithPoint(nodePos);
    }

    public boolean collidesWithPoint(Vector2f vec){
        boolean collision = false;

        // go through each of the vertices, plus
        // the next vertex in the list
        int next;
        for (int current=0; current<nodes.size(); current++) {

            // get next vertex in list
            // if we've hit the end, wrap around to 0
            next = current + 1;
            if (next == nodes.size()) next = 0;

            // get the PVectors at our current position
            // this makes our if statement a little cleaner
            Vector2f vc = nodes.get(current).getPosition();    // c for "current"
            Vector2f vn = nodes.get(next).getPosition();       // n for "next"

            // compare position, flip 'collision' variable
            // back and forth
            if (((vc.y >= vec.y && vn.y < vec.y) || (vc.y < vec.y && vn.y >= vec.y)) &&
                    (vec.x < (vn.x-vc.x)*(vec.y-vc.y) / (vn.y-vc.y)+vc.x)) {
                collision = !collision;
            }
        }
        return collision;
    }

}
