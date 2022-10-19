package Morphogenesis.Entities;

import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Components.Physics.Spring.InternalElasticForce;
import Morphogenesis.Components.Physics.Spring.LateralShorteningSpringForce;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Render.CellRenderer;

import java.awt.*;

public class ShorteningCell extends Cell{

    @Override
    public void start() {
        addComponent(new ElasticForce());
        addComponent(new LateralShorteningSpringForce());
        addComponent(new OsmosisForce());
        addComponent(new InternalElasticForce());
    }

    @Override
    public void awake() {
        super.awake();
        getComponent(CellRenderer.class).setColor(Color.BLUE);
    }

}
