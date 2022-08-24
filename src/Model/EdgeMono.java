package Model;

import Engine.Object.MonoBehavior;
import Engine.States.State;
import Model.Components.Physics.ApicalConstrictingSpringForce;
import Model.Components.Physics.ElasticForce;
import Model.Components.Render.EdgeRenderer;
import Physics.Rigidbodies.*;

import java.awt.*;

/**
 * EdgeMono creates a mock edge to behave as part of the system as a cell would.
 * Uses a specialized renderer.
 * Used for debugging purposes.
 */
public class EdgeMono extends MonoBehavior {

    private Edge edge;

    public EdgeMono(){}

    public void setEdge(Edge e){
        this.edge = e;
    }
    public Edge getEdge(){return edge;}

    public Node[] getNodes(){
        return edge.getNodes();
    }

    @Override
    public void awake() {
        EdgeRenderer renderer = new EdgeRenderer();
        this.addComponent(renderer);
    }

    @Override
    public void start() {
        addComponent(new ElasticForce());
        addComponent(new ApicalConstrictingSpringForce());
    }

    public void setColor(Color color){
        EdgeRenderer rend = getComponent(EdgeRenderer.class);
        rend.setColor(color);
    }

    public static EdgeMono build(Edge e) throws IllegalAccessException, InstantiationException {
        EdgeMono mono = (EdgeMono)State.create(EdgeMono.class);
        mono.setEdge(e);
        return mono;
    }
}