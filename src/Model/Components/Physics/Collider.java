package Model.Components.Physics;

import Model.Cells.Cell;
import Model.Components.Component;
import Model.Components.Meshing.CellMesh;
import Model.Components.Physics.ForceVector.ForceType;
import Model.DrosophilaRingModel;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;

import java.util.ArrayList;
import java.util.List;

public class Collider extends Force {
    public transient List<Node> nodes = new ArrayList<>();
    public transient List<Cell> cells = new ArrayList<>();

    @Override
    public void update() {

    }


}
