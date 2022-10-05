package Morphogenesis.Entities;

import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Components.Physics.OsmosisForce;


public class BasicRingCell extends Cell{

    @Override
    public void start() {
        addComponent(new ElasticForce());
        addComponent(new OsmosisForce());
//        addComponent(new InternalElasticForce());
    }
}
