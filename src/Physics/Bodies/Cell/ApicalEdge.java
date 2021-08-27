package Physics.Bodies.Cell;

import Physics.Bodies.Vertex;

public class ApicalEdge<T extends CellEdge<T>> extends CellEdge<T> {
    public ApicalEdge(Vertex a, Vertex b) {
        super(a, b);
    }
}
