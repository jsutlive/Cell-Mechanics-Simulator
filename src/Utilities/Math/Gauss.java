package Utilities.Math;

import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Gauss {

    public static float nShoelace(List<Node> nodes)
    {
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
        float area = 0f;
        // Loop over each pair of coordinates, applying the matrix math
        for (int i = 0; i < n - 1; i++) {
            area += coords.get(i).x * coords.get(i + 1).y - coords.get(i + 1).x * coords.get(i).y;
        }
        // Last pair is the final coordinates + the first coordinates
        return Math.abs(area + coords.get(n - 1).x * coords.get(0).y - coords.get(0).x * coords.get(n - 1).y) / 2.0f;
    }

    // return pdf(x) = standard Gaussian pdf
    public static float pdf(float x) {
        return (float) (Math.exp(-x*x / 2) / Math.sqrt(2 * Math.PI));
    }

    // return pdf(x, mu, signma) = Gaussian pdf with mean mu and stddev sigma
    public static float pdf(float x, float mu, float sigma) {
        return pdf((x - mu) / sigma) / sigma;
    }



}
