package Morphogenesis.Entities;

import Morphogenesis.Components.Physics.Collision.CornerStiffness2D;
import Morphogenesis.Components.Physics.Collision.EdgeStiffness2D;
import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Render.MeshRenderer;


public class BasicRingCell extends Cell{

    @Override
    public void awake() {
        addComponent(new MeshRenderer());
        addComponent(new ElasticForce());
        addComponent(new OsmosisForce());
        addComponent(new EdgeStiffness2D());
        addComponent(new CornerStiffness2D());
    }
}
