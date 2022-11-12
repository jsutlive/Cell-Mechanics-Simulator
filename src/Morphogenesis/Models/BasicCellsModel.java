package Morphogenesis.Models;

import Framework.States.State;
import Morphogenesis.Components.Meshing.SimpleCellMesh;
import Morphogenesis.Entities.SimpleCell;
import Utilities.Geometry.Vector.Vector2f;

/**
 * Example model for building some simple cells
 */
public class BasicCellsModel extends Model{

    @Override
    public void awake() {
        State.create(SimpleCell.class, new SimpleCellMesh().build(30, 8, new Vector2f(200)));
        State.create(SimpleCell.class, new SimpleCellMesh().build(30, 8, new Vector2f(-300)));
    }
}
