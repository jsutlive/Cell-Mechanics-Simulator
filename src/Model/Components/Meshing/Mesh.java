package Model.Components.Meshing;

import Model.Components.Component;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Math.Gauss;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public abstract class Mesh extends Component {
    public List<Node> nodes = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();

    protected transient float restingArea;
    protected float area;
    private Vector2f centroid;

    public boolean contains(Node n){
        return nodes.contains(n);
    }
    public boolean contains(Edge e){
        return edges.contains(e);
    }

    public float getArea(){
        calculateArea();
        return area;
    }

    protected void calculateArea(){
        area = Gauss.nShoelace(nodes);
        if(restingArea == 0)restingArea = area;
    }
    public float getRestingArea(){
        return restingArea;
    }

    private void calculateCentroid(){
        float x = 0;
        float y = 0;
        for (Node node: nodes){
            x += node.getPosition().x;
            y += node.getPosition().y;
        }
        x = x/nodes.size();
        y = y/nodes.size();
        centroid = new Vector2f(x,y);
    }


}
