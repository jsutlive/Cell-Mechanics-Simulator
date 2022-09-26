package Model.Cells;

import Model.Components.Physics.Collision.Spring.ElasticForce;
import Model.Components.Physics.OsmosisForce;


public class BasicCell extends Cell{

    @Override
    public void start() {
        addComponent(new ElasticForce());
        addComponent(new OsmosisForce());
//        addComponent(new InternalElasticForce());
    }
}
