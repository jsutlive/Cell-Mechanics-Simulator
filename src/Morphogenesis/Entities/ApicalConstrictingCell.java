package Morphogenesis.Entities;

import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Components.Physics.Collision.CornerStiffness2D;
import Morphogenesis.Components.Physics.Collision.EdgeStiffness2D;
import Morphogenesis.Components.Physics.Spring.ApicalConstrictingSpringForce;
import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Physics.Spring.InternalElasticForce;
import Morphogenesis.Components.Render.CellRenderer;
import Morphogenesis.Rigidbodies.Edges.BasalEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;

import java.awt.*;
import java.util.ArrayList;

/**
 * An Apical Constricting Cell undergoes the following forces:
 *
 * Active:
 * Apical constriction
 *
 * Passive:
 * Elasticity
 * Osmosis
 */
public class ApicalConstrictingCell extends Cell
{
    /**
     * List forces to be applied to this type of cell here
     */
    @Override
    public void start() {
        addComponent(new ElasticForce());
        addComponent(new ApicalConstrictingSpringForce());
        addComponent(new OsmosisForce());
        addComponent(new InternalElasticForce());
        addComponent(new EdgeStiffness2D());
        addComponent(new CornerStiffness2D());

    }

    @Override
    public void awake() {
        super.awake();
        getComponent(CellRenderer.class).setColor(Color.MAGENTA);
        adjustBasalMembrane();
    }

    private void adjustBasalMembrane(){
        ArrayList<Edge> edgeList = (ArrayList<Edge>) getComponent(Mesh.class).edges;
        for(Edge edge: edgeList){
            if(edge instanceof BasalEdge){
                edge.setElasticConstant(edge.getElasticConstant()/2f);
            }
        }
    }
}
