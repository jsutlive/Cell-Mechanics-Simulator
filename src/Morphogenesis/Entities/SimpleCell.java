package Morphogenesis.Entities;

import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Components.Render.CellRenderer;

public class SimpleCell extends Cell{

    @Override
    public void awake() {
        addComponent(new CellRenderer());
    }

    @Override
    public void start() {
        addComponent(new ElasticForce());
    }
}
