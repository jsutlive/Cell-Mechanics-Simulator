package Model;

import Engine.Object.MonoBehavior;
import Engine.States.State;
import Model.Components.EdgeRenderer;
import Physics.Forces.Force;
import Physics.Rigidbodies.*;

import java.awt.*;

/**
 * EdgeMono creates a mock edge to behave as part of the system as a cell would.
 * Uses a specialized renderer.
 * Used for debugging purposes.
 */
public class EdgeMono extends MonoBehavior {

    private Edge edge;
    private int cellID;

    public void setCellID(int id){
        cellID = id;
    }

    public EdgeMono(){}

    public void setEdge(Edge e){
        this.edge = e;
    }
    public Edge getEdge(){return edge;}

    public Node[] getNodes(){
        return edge.getNodes();
    }

    @Override
    public void run(){}

    @Override
    public void awake() throws InstantiationException, IllegalAccessException {
        EdgeRenderer renderer = new EdgeRenderer();
        this.addComponent(renderer);
        State.setFlagToRender(this);
    }

    public void setColor(Color color){
        EdgeRenderer rend = (EdgeRenderer) getComponent(EdgeRenderer.class);
        rend.setColor(color);
    }

    @Override
    public void update() {
        if(edge instanceof BasicEdge)
        {
            Force.elastic(edge, .25f);

        }
        else if(edge instanceof ApicalEdge) {
            if((cellID > 71 || cellID <= 8)) {
                edge.setColor(Color.WHITE);
                if(cellID > 0){
                    Force.constrict(edge, -.4f, 0.001f);
                }
                else Force.constrict(edge, .4f, .001f);

            }
            Force.elastic(edge,.25f);
        }
        else if (edge instanceof BasalEdge){
            if((cellID > 0 || cellID <= 1)) {
                Force.elastic(edge, .55f);
            }
            Force.elastic(edge, .35f);
        }
        else {
            Force.elastic(edge, .35f);
        }
        for (Node node: getNodes()) node.Move();
    }

    public static EdgeMono build(Edge e) throws IllegalAccessException, InstantiationException {
        EdgeMono mono = (EdgeMono)State.create(EdgeMono.class);
        mono.setEdge(e);
        return mono;
    }
}