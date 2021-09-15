package Model;

import Engine.Renderer;
import Engine.States.State;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
import Utilities.Math.CustomMath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A simple four-cell model which can be used for debugging and testing.
 * Cells arranged in the same manner like they appear at the "seam" of the Drosophila ring model:
 * i.e.,
 *       _____ _____ _____ ______
 *      |     |     |     |     |
 *      |  1  |  0  |  3  |  2  |
 *      |_____|_____|_____|_____|
 */
public class SimpleFourCell implements IOrganism{
    int lateralResolution = 4;


     //if the model were to be extrapolated out to make an entire ring, how many segments would it have
    int numberOfSegmentsInTotalCircle = 80;

    float outerRadius = 300;
    float innerRadius = 200;
    List<Cell> allCells = new ArrayList<>();
    HashSet<Node> allNodes = new HashSet<>();

    //bounding box dimensions that determine where and how large the image will be drawn.
    final Vector2i boundingBox;

    /**
     * Simple constructor wherein the boundaries of the simulation object are spoofed in order to maintain simplicity.
     * Will not scale if the simulation window size is changed. If using this object for debugging purposes, please
     * check the size of the simulation prior to altering the boundingBox size, and vice-versa.
     */
    public SimpleFourCell()
    {
        boundingBox = new Vector2i(800);
    }

    @Override
    public void generateOrganism() throws InstantiationException, IllegalAccessException {
        generateTissueRing();
    }

    private void generateTissueRing() throws InstantiationException, IllegalAccessException {
        Vector2f position, unitVector;
        ArrayList<Edge> oldEdges = new ArrayList<>();
        ArrayList<Edge> zeroEdge = new ArrayList<>();

        //make lateral edges
        for (int i = 0; i < numberOfSegmentsInTotalCircle; i++) {
            ArrayList<Edge> edges = new ArrayList<>();
            unitVector = CustomMath.GetUnitVectorOnCircle(i, numberOfSegmentsInTotalCircle, lateralResolution);
            Node lastNode = new Node();   // Create null vertex to be used to create edges later.
            for (int j = 0; j <= lateralResolution; j++) {
                float nodeRadius = outerRadius + (innerRadius - outerRadius) / lateralResolution * j;
                // Transform polar to world coordinates
                position = CustomMath.TransformToWorldSpace(unitVector, nodeRadius, boundingBox.asFloat());
                Node currentNode = new Node(position);
                if (j >= 1) {
                    edges.add(new LateralEdge(currentNode, lastNode));
                }
                allNodes.add(currentNode);
                lastNode = currentNode;
            }

            if (i == 1 || i == 2 || i == 79 || i == 80) {
                Cell newCell = createCell(oldEdges, edges);
                if (i ==79) {
                    newCell.setRingLocation(80 - (i - 1));
                } else {
                    newCell.setRingLocation(i);
                }
                allCells.add(newCell);

            } else if (i == 0){
                zeroEdge = edges;
            }

            oldEdges = edges;
        }

        Cell newCell = createCell(oldEdges, zeroEdge);
        newCell.setRingLocation(1);
        allCells.add(newCell);

    }

    private Cell createCell(ArrayList<Edge> sideA, ArrayList<Edge> sideB) throws InstantiationException, IllegalAccessException {
        // Get vertices from edge segments, which make up the lateral edges
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        nodes.addAll(addVerticesFromEdgeList(sideB));
        nodes.addAll(addVerticesFromEdgeList(sideA));

        edges.addAll(sideA);
        edges.addAll(sideB);

        // Create internal lattice:
        //  0   0
        //  1   1
        //  2   2
        //  3   3
        //  4   4
        Edge edgeA;
        Edge edgeB;
        List<Edge> internalEdges = new ArrayList<>();
        for(int i = 0; i < sideA.size(); i++)
        {
            edgeA = sideA.get(i);
            edgeB = sideB.get(i);
            Node a = edgeA.getNodes()[0];
            Node b = edgeA.getNodes()[1];
            Node c = edgeB.getNodes()[0];
            Node d = edgeB.getNodes()[1];

            internalEdges.add(new BasicEdge(a,d));
            internalEdges.add(new BasicEdge(b,c));
        }

        // Create the apical edges of the cell
        Node apicalA = sideA.get(0).getNodes()[1];
        Node apicalB = sideB.get(0).getNodes()[1];
        Edge apicalEdge = new ApicalEdge(apicalA, apicalB);
        edges.add(apicalEdge);

        // Create the basal edges of the cell
        int n = lateralResolution;
        Node basalB = sideA.get(n-1).getNodes()[0];
        Node basalA = sideB.get(n-1).getNodes()[0];
        Edge basalEdge = new BasalEdge(basalA, basalB);
        edges.add(basalEdge);

        // compile and create the cell object
        Cell cell = (Cell)State.create(Cell.class);
        cell.setNodes(nodes);
        cell.setEdges(edges);
        cell.setInternalEdges(internalEdges);
        State.setFlagToRender(cell);
        cell.setColor(Renderer.defaultColor);
        return cell;
    }

    private HashSet<Node> addVerticesFromEdgeList(ArrayList<Edge> edgeList) {
        HashSet<Node> vertices = new HashSet<>();
        for (Edge edge: edgeList)
        {
            Node[] vtxArray = edge.getNodes();
            vertices.add(vtxArray[0]);
            vertices.add(vtxArray[1]);
        }
        return vertices;
    }

    @Override
    public List<Cell> getAllCells() {
        return allCells;
    }

    @Override
    public HashSet<Node> getAllNodes(){
        return allNodes;
    }
}
