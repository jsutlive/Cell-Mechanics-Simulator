package Component;

import Framework.Object.Entity;
import Framework.Rigidbodies.Node2D;

import java.util.ArrayList;
import java.util.List;

public abstract class Collider extends Force {
    public transient List<Node2D> nodes = new ArrayList<>();
    public transient List<Entity> cells = new ArrayList<>();

    @Override
    public void update() {

    }


}
