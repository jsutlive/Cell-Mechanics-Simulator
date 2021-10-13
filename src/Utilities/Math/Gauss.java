package Utilities.Math;

import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Gauss {

    public static float nShoelace(List<Node> nodes)
    {
        System.out.println(nodes);
        List<Vector2f>coords = new ArrayList<>();
        for(Node node: nodes)
        {
            coords.add(node.getPosition());
        }
        return shoelace(coords);
    }
    public static float shoelace(List<Vector2f> coords)
    {
        int n = coords.size();
        System.out.println(n);
        float area = 0f;
        for (int i = 0; i < n - 1; i++) {
            area += coords.get(i).x * coords.get(i + 1).y - coords.get(i + 1).x * coords.get(i).y;
        }
        return Math.abs(area + coords.get(n - 1).x * coords.get(0).y - coords.get(0).x * coords.get(n - 1).y) / 2.0f;
    }


}
