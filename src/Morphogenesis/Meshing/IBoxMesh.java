package Morphogenesis.Meshing;

import Framework.Rigidbodies.Node2D;

import java.util.ArrayList;

public interface IBoxMesh {
    int getLengthResolution();
    int getWidthResolution();
    ArrayList<Node2D> getNodes();
}
