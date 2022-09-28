package Component.Physics;

import Model.Cells.BasicCell;
import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Model.Components.Meshing.Mesh;
import Model.Components.Physics.OsmosisForce;
import Physics.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OsmosisForceTests {

    @BeforeAll
    static void build_base_components(){

    }

    @Test
    void check_osmosis_force_calculates_positive_force_for_mesh_smaller_than_rest(){

    }

    @Test
    void check_osmosis_force_calculates_negative_force_for_mesh_larger_than_rest(){

    }
}
