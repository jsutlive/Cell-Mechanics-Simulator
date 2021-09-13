package Model;

import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
import Utilities.Math.CustomMath;

import java.util.ArrayList;
import java.util.HashSet;

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

    /**
     * if the model were to be extrapolated out to make an entire ring, how many segments would it have
     */
    int numberOfSegmentsInTotalCircle = 80;

    float outerRadius = 300;
    float innerRadius = 200;

    final Vector2i boundingBox;

    public SimpleFourCell()
    {
        boundingBox = new Vector2i(800);
    }

    @Override
    public void generateOrganism()
    {
        generateTissueRing();
        Cell.resetID();
    }

    private void generateTissueRing() {
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
                position = CustomMath.TransformToWorldSpace(unitVector, nodeRadius, boundingBox.asFloat());
                Node currentNode = new Node(position);
                if (lastNode.getPosition() != null) {
                    edges.add(new LateralEdge(currentNode, lastNode));
                }
                lastNode = currentNode;
            }

            if (i == 1 || i == 2 || i == numberOfSegmentsInTotalCircle - 1) {
                if (i > numberOfSegmentsInTotalCircle / 2) {
                    Cell newCell = createCell(oldEdges, edges);
                    allCells.addCell(newCell);

                } else {
                    Cell newCell = createCell(oldEdges, edges);
                    allCells.addCell(newCell);
                }

            } else if (i == 0){
                zeroEdge = edges;
            }

            if(i == numberOfSegmentsInTotalCircle - 1)
            {
                Cell newCell = createCell(edges, zeroEdge);
                allCells.addCell(newCell);
            }

            oldEdges = edges;
        }



    }


    private Cell createCell(ArrayList<Edge> sideA, ArrayList<Edge> sideB)
    {
        // Get vertices from edge segments, which make up the lateral edges
        HashSet<Node> nodes = new HashSet<>();
        HashSet<Edge> edges = new HashSet<>();
        nodes.addAll(addVerticesFromEdgeList(sideB));
        nodes.addAll(addVerticesFromEdgeList(sideA));

        edges.addAll(sideA);
        edges.addAll(sideB);

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
        Cell cell = Cell.createCellStructure(nodes, edges);
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
    public CellGroup getAllCells() {
        return allCells;
    }
}
