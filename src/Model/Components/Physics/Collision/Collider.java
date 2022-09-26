package Model.Components.Physics.Collision;

import Model.Cells.Cell;
import Model.Components.Physics.Force;
import Physics.Rigidbodies.Node;

import java.util.ArrayList;
import java.util.List;

public class Collider extends Force {
    public transient List<Node> nodes = new ArrayList<>();
    public transient List<Cell> cells = new ArrayList<>();

    @Override
    public void update() {

    }


}
