package Model.Cells;

import Data.LogData;
import Engine.States.State;
import Model.Components.Lattice.Lattice;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ElasticForce;
import Model.Components.Physics.InternalElasticForce;
import Model.Components.Physics.LateralShorteningSpringForce;
import Model.Components.Physics.OsmosisForce;
import Model.Components.Render.CellRenderer;
import Physics.Rigidbodies.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShorteningCell extends Cell{

    @Override
    public void start() {
        addComponent(new ElasticForce());
        addComponent(new LateralShorteningSpringForce());
        addComponent(new OsmosisForce());
//        addComponent(new InternalElasticForce());
    }

    @Override
    public void awake() {
        super.awake();
        getComponent(CellRenderer.class).setColor(Color.BLUE);
    }

}
