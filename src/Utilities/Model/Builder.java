package Utilities.Model;

import Engine.Renderer;
import Engine.States.State;
import Model.ApicalConstrictingCell;
import Model.Cell;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Vector2f;

import java.util.*;

public class Builder {
    /**
     * Makes four cells with simple box structure where the cells have four edges, each with a length of 1.
     * Three are Basic Edges, one is an Apical Edge.
     * @return list of cells in the structure.
     */
    public static List<Cell> getSimpleFourCellBox() throws InstantiationException, IllegalAccessException {
        int numberOfCells = 5;
        int lateralResolution = 4;
        Vector2f nullPosition = new Vector2f(5000);

        List<Edge> sideB = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        for(int i =0; i < numberOfCells; i++)
        {
            List<Edge> sideA = new ArrayList<>();
            Node oldNode = new Node(nullPosition);
            for(int j = 0; j <= lateralResolution; j++)
            {
                Node n = new Node((i*100) + 200, (j*100)+ 200);
                if(oldNode.getPosition()!= nullPosition) {
                    sideA.add(new LateralEdge(oldNode, n));
                }
                oldNode = n;
            }
            if(i != 0){
                Cell c = createCell(sideB, sideA);
                cells.add(c);
            }
            sideB = sideA;

        }
        return cells;
    }


    public static Cell createCell(List<Edge> sideA, List<Edge> sideB) throws InstantiationException, IllegalAccessException {
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
        Node apicalA = sideA.get(0).getNodes()[0];
        Node apicalB = sideB.get(0).getNodes()[0];
        Edge apicalEdge = new ApicalEdge(apicalA, apicalB);
        edges.add(apicalEdge);

        // Create the basal edges of the cell
        int n = 4;
        Node basalB = sideA.get(n-1).getNodes()[1];
        Node basalA = sideB.get(n-1).getNodes()[1];
        Edge basalEdge = new BasalEdge(basalA, basalB);
        edges.add(basalEdge);

        // compile and create the cell object
        Cell cell = (Cell)State.create(ApicalConstrictingCell.class);
        cell.setNodes(nodes);
        cell.setEdges(edges);
        cell.setInternalEdges(internalEdges);
        State.setFlagToRender(cell);
        cell.setColor(Renderer.defaultColor);
        return cell;
    }

    private static HashSet<Node> addVerticesFromEdgeList(List<Edge> edgeList) {
        HashSet<Node> vertices = new HashSet<>();
        for (Edge edge: edgeList)
        {
            Node[] vtxArray = edge.getNodes();
            vertices.add(vtxArray[0]);
            vertices.add(vtxArray[1]);
        }
        return vertices;
    }

}
