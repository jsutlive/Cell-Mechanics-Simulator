package Model.Cells;

import Model.Components.Physics.Spring.ElasticForce;
import Model.Components.Physics.Spring.LateralShorteningSpringForce;
import Model.Components.Physics.OsmosisForce;
import Model.Components.Render.CellRenderer;

import java.awt.*;

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
