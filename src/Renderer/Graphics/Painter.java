package Renderer.Graphics;

import Morphogenesis.Components.Meshing.Mesh;
import Morphogenesis.Rigidbodies.Nodes.Node;
import Renderer.Renderer;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;
import Utilities.Math.CustomMath;

import java.awt.*;

public class Painter {
    public static Color DEFAULT_COLOR = Color.white;

    public static void drawMesh(Mesh mesh, Color color) {
        System.out.print("meshStart ");
        for(Node node: mesh.nodes)
        {
            Vector position = node.getPosition();
            System.out.print(position.get(0) + "&" + position.get(1) + " ");
        }
        System.out.print("meshEnd ");
    }
}
