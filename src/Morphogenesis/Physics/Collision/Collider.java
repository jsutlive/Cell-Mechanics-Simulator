package Morphogenesis.Physics.Collision;

import Framework.Object.Entity;
import Morphogenesis.Physics.Force;
import Framework.Rigidbodies.Node2D;

import java.util.ArrayList;
import java.util.List;

public class Collider extends Force {
    public transient List<Node2D> nodes = new ArrayList<>();
    public transient List<Entity> cells = new ArrayList<>();

    @Override
    public void update() {

    }


}
