package Model.Cells;

import Model.Components.Physics.ElasticForce;
import Model.Components.Physics.OsmosisForce;
import Model.Components.Render.CellRenderer;

import java.awt.*;


public class BasicCell extends Cell{

    @Override
    public void start() {
        addComponent(new ElasticForce());
        addComponent(new OsmosisForce());
//        addComponent(new InternalElasticForce());
    }
}
