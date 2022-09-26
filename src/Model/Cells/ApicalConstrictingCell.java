package Model.Cells;

import Model.Components.Physics.Spring.ApicalConstrictingSpringForce;
import Model.Components.Physics.Spring.ElasticForce;
import Model.Components.Physics.OsmosisForce;
import Model.Components.Render.CellRenderer;

import java.awt.*;

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
//        addComponent(new InternalElasticForce());
    }

    @Override
    public void awake() {
        super.awake();
        getComponent(CellRenderer.class).setColor(Color.MAGENTA);
    }
}
