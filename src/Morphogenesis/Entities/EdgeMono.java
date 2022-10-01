package Morphogenesis.Entities;

import Framework.Object.Entity;
import Framework.States.State;
import Morphogenesis.Components.Physics.Spring.ApicalConstrictingSpringForce;
import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Components.Render.EdgeRenderer;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node;

import java.awt.*;

/**
 * EdgeMono creates a mock edge to behave as part of the system as a cell would.
 * Uses a specialized renderer.
 * Used for debugging purposes.
 */
public class EdgeMono extends Entity {

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

    public static EdgeMono build(Edge e) {
        EdgeMono mono = State.create(EdgeMono.class);
        mono.setEdge(e);
        return mono;
    }
}