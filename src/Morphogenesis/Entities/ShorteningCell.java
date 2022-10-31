package Morphogenesis.Entities;

import Morphogenesis.Components.Physics.Collision.CornerStiffness2D;
import Morphogenesis.Components.Physics.Collision.EdgeStiffness2D;
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
        //addComponent(new InternalElasticForce());
        addComponent(new EdgeStiffness2D());
        addComponent(new CornerStiffness2D());

    }

    @Override
    public void awake() {
        super.awake();
        getComponent(CellRenderer.class).setColor(Color.BLUE);
    }

}
