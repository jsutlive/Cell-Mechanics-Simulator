package Renderer.Graphics;

import Morphogenesis.Components.Meshing.Mesh;
import Renderer.Renderer;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;
import Utilities.Math.CustomMath;

import java.awt.*;

public class Painter {
    public static Color DEFAULT_COLOR = Color.white;

    public static void drawMesh(Mesh mesh, Color color) {
        for(Edge edge: mesh.edges)
        {
            Vector2f[] positions = edge.getPositions();
            System.out.println(positions);
        }
    }
}
