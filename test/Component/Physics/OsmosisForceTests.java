package Component.Physics;

import Model.Cells.BasicCell;
import Model.Cells.Cell;
import Model.Components.Meshing.CellMesh;
import Model.Components.Meshing.Mesh;
import Model.Components.Physics.OsmosisForce;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OsmosisForceTests {

    static Cell cell;
    static OsmosisForce osmosisForce;
    static Mesh mesh;
    @BeforeAll
    static void build_base_components(){
        List<Node> nodes  = new ArrayList<>();
        nodes.add(new Node(0,0)); nodes.add(new Node(0,1)); nodes.add(new Node(1,1)); nodes.add(new Node(1, 0));
        cell = new BasicCell().withComponent(new CellMesh().build(nodes, 1, 1));
        mesh = cell.getComponent(CellMesh.class);
        osmosisForce = cell.addComponent(new OsmosisForce());
    }

    @Test
    void check_osmosis_force_calculates_positive_force_for_mesh_smaller_than_rest(){
        mesh.nodes.get(0).MoveTo(new Vector2f(0.1f,0.1f));
        osmosisForce.restore();
        assertTrue(osmosisForce.calculateOsmosisForceMagnitude(mesh) > 0);
    }

    @Test
    void check_osmosis_force_calculates_negative_force_for_mesh_larger_than_rest(){
        mesh.nodes.get(0).MoveTo(new Vector2f(-0.1f,-0.1f));
        osmosisForce.restore();
        assertTrue(osmosisForce.calculateOsmosisForceMagnitude(mesh) < 0);
    }
}
