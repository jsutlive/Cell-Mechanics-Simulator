package Model;

import Engine.Simulation;
import Physics.Bodies.Cell.*;
import Physics.Bodies.Edge;
import Physics.Bodies.Vertex;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
import Utilities.Math.CustomMath;

import java.util.ArrayList;
import java.util.HashSet;

public class SimpleFourCell implements IOrganism{
    int lateralResolution = 4;

    // if the model were to be extrapolated out to make an entire ring, how many segements would it have
    int numberOfSegmentsInTotalCircle = 80;

    float outerRadius = 300;
    float innerRadius = 200;

    public CellGroup allCells = new CellGroup();

    final Vector2i boundingBox;

    public int numberOfCells;

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
            Vertex lastVertex = new CellNode();   // Create null vertex to be used to create edges later.
            for (int j = 0; j <= lateralResolution; j++) {
                float nodeRadius = outerRadius + (innerRadius - outerRadius) / lateralResolution * j;
                position = CustomMath.TransformToWorldSpace(unitVector, nodeRadius, boundingBox.asFloat());
                Vertex thisVertex = new CellNode(position);
                if (lastVertex.getPosition() != null) {
                    edges.add(new LateralEdge(thisVertex, lastVertex));
                }
                lastVertex = thisVertex;
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
        HashSet<Vertex> vertices = new HashSet<>();
        HashSet<Edge> edges = new HashSet<>();
        vertices.addAll(addVerticesFromEdgeList(sideB));
        vertices.addAll(addVerticesFromEdgeList(sideA));

        edges.addAll(sideA);
        edges.addAll(sideB);

        // Create the apical edges of the cell
        Vertex apicalA = sideA.get(0).getVertices()[1];
        Vertex apicalB = sideB.get(0).getVertices()[1];
        Edge apicalEdge = new ApicalEdge(apicalA, apicalB);
        edges.add(apicalEdge);

        // Create the basal edges of the cell
        int n = lateralResolution;
        Vertex basalB = sideA.get(n-1).getVertices()[0];
        Vertex basalA = sideB.get(n-1).getVertices()[0];
        Edge basalEdge = new BasalEdge(basalA, basalB);
        edges.add(basalEdge);

        // compile and create the cell object
        Cell cell = Cell.createCellStructure(vertices, edges);
        return cell;
    }

    private HashSet<Vertex> addVerticesFromEdgeList(ArrayList<Edge> edgeList) {
        HashSet<Vertex> vertices = new HashSet<>();
        for (Edge edge: edgeList)
        {
            Vertex[] vtxArray = edge.getVertices();
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
