package Morphogenesis.Entities;

import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Morphogenesis.Components.Render.MeshRenderer;

public class SimpleCell extends Cell{

    @Override
    public void awake() {
        addComponent(new MeshRenderer());
    }

    @Override
    public void start() {
        addComponent(new ElasticForce());
    }
}
