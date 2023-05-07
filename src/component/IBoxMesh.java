package component;

import framework.rigidbodies.Node2D;

import java.util.ArrayList;

public interface IBoxMesh {
    int getLengthResolution();
    int getWidthResolution();
    ArrayList<Node2D> getNodes();
}
