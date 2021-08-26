package Model;

import Engine.Simulation;
import Physics.Bodies.Cell.*;
import Physics.Bodies.Edge;
import Physics.Bodies.Vertex;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;

import java.util.ArrayList;
import java.util.HashSet;

public class DrosophilaEmbryo implements IOrganism
{
    public CellGroup apicalConstrictingCells = new CellGroup();
    public CellGroup lateralConstrictingCells = new CellGroup();
    public CellGroup allCells;

    /**
     * Number of cells that the embryo will have in the tissue ring, default = 80
     */
    public int numberOfSegments = 80;

    /**
     * Number of cells which undergo apical constriction(one side), default = 8 (16 total cells)
     */
    public int numberOfConstrictingCells = 8;

    /**
     * Inner radius of tissue ring, default = 200
     */
    public int innerRadius = 200;

    /**
     * Outer radius of tissue ring, default = 300;
     */
    public int outerRadius = 300;

    /**
     * How many nodes make up the lateral edge of each cell, default is 4
     */
    public int lateralResolution = 4;

    private Cell activeCell;

    @Override
    public CellGroup getAllCells() {
        return allCells;
    }

    public DrosophilaEmbryo()
    {
        allCells = new CellGroup();
    }


    @Override
    public void generateOrganism() {
        generateTissueRing();
        int numberOfCells = allCells.getCells().size();
        for(int i = 0; i < numberOfCells; i++)
        {
            if(i < numberOfConstrictingCells || i > numberOfSegments - numberOfConstrictingCells - 1)
            {
                apicalConstrictingCells.addCell(allCells.getCell(i));

            }
            else
            {
                lateralConstrictingCells.addCell(allCells.getCell(i));
            }
        }
        for(Cell cell: apicalConstrictingCells.getCells())
        {
            System.out.println(cell.getID());
        }
    }

    private void generateTissueRing() {
        Vector2f position, unitVector;
        ArrayList<Edge> oldEdges = new ArrayList<>();
        ArrayList<Edge> zeroEdge = new ArrayList<>();

        //make lateral edges
        for (int i = 0; i < numberOfSegments; i++) {

            ArrayList<Edge> edges = new ArrayList<>();
            unitVector = CustomMath.GetUnitVectorOnCircle(i, numberOfSegments, lateralResolution);
            Vertex lastVertex = new CellNode();   // Create null vertex to be used to create edges later.
            for (int j = 0; j <= lateralResolution; j++) {
                float nodeRadius = outerRadius + (innerRadius - outerRadius) / lateralResolution * j;
                position = CustomMath.TransformToWorldSpace(unitVector, nodeRadius, Simulation.bounds.asFloat());
                Vertex thisVertex = new CellNode(position);
                if (lastVertex.getPosition() != null) {
                    edges.add(new LateralEdge(thisVertex, lastVertex));
                }
                lastVertex = thisVertex;
            }

            if (i >= 1) {
                if (i > numberOfSegments / 2) {
                    Cell newCell = createCell(oldEdges, edges);
                    allCells.addCell(newCell);

                } else {
                    Cell newCell = createCell(oldEdges, edges);
                    allCells.addCell(newCell);
                }

            } else {
                zeroEdge = edges;
            }

            if(i == numberOfSegments - 1)
            {
                Cell newCell = createCell(zeroEdge, edges);
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
}
