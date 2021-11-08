package Model;

import Engine.Object.MonoBehavior;
import Engine.States.State;
import Model.Components.EdgeRenderer;
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
            edge.constrict(.25f, 1f);

        }
        else if(edge instanceof ApicalEdge) {
            if((cellID > 71 || cellID <= 8)) {
                edge.setColor(Color.WHITE);
                edge.constrict(.4f, .001f);
                System.out.println(edge.getNodes()[0].getResultantForce().x + "::" + edge.getNodes()[0].getResultantForce().y);

            }
            edge.constrict(.25f, 1f);
        }
        else if (edge instanceof BasalEdge){
            if((cellID > 71 || cellID <= 8)) {
                edge.constrict(.55f, 1f);
            }
            edge.constrict(.35f, 1f);
        }
        else {
            edge.constrict(.35f, 1f);
        }
        for (Node node: getNodes()) node.Move();
    }
}