package Model.Cells;

import Data.LogData;
import Engine.States.State;

import Model.Components.Lattice.Lattice;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ApicalConstrictingSpringForce;
import Model.Components.Physics.ElasticForce;
import Model.Components.Physics.InternalElasticForce;
import Model.Components.Physics.OsmosisForce;
import Model.Components.Render.CellRenderer;
import Physics.Rigidbodies.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
