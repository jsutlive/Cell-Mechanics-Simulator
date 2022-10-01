package Morphogenesis.Components.Meshing;

import Framework.Object.Component;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.Gauss;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Mesh extends Component {
    public List<Node2D> nodes = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();

    protected float area;
    private Vector2f centroid;

    public boolean contains(Node2D n){
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
    }

    private void calculateCentroid(){
        float x = 0;
        float y = 0;
        for (Node2D node: nodes){
            x += node.getPosition().x;
            y += node.getPosition().y;
        }
        x = x/nodes.size();
        y = y/nodes.size();
        centroid = new Vector2f(x,y);
    }

    @Override
    public void onValidate()
    {
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
    }

}
