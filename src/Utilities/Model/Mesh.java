package Utilities.Model;

import Engine.States.State;
import GUI.Painter;
import Model.Cells.ApicalConstrictingCell;
import Model.Cells.Cell;

import Model.EdgeMono;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Mesh {

    public static List<Cell> circle(int totalCells, float innerRadius, float outerRadius, Vector2f offset) throws InstantiationException, IllegalAccessException {
        List<Cell> cells = new ArrayList<>();
        List<Node> nodesA = new ArrayList<>();
        List<Node> nodesB = new ArrayList<>();
        List<Node> zeroNodes = new ArrayList<>();

        for(int i = 0; i < totalCells; i++){
            Vector2f unitVector = CustomMath.GetUnitVectorOnCircle(i, totalCells);
            float radiusDifference = (innerRadius - outerRadius);
            Node a = new Node(CustomMath.TransformToWorldSpace(unitVector, outerRadius + 0, offset));
            Node b = new Node(CustomMath.TransformToWorldSpace(unitVector, outerRadius + radiusDifference * .25f, offset));
            Node c = new Node(CustomMath.TransformToWorldSpace(unitVector, outerRadius + radiusDifference * .5f, offset));
            Node d = new Node(CustomMath.TransformToWorldSpace(unitVector, outerRadius + radiusDifference * .75f, offset));
            Node e = new Node(CustomMath.TransformToWorldSpace(unitVector, outerRadius + radiusDifference, offset));

            if (i != 0) {
                nodesA.add(a);
                nodesA.add(b);
                nodesA.add(c);
                nodesA.add(d);
                nodesA.add(e);

                // Generate Cell
                Cell cell;
                if (i > 0 || i <= 1) {
                    cell = makeCellEdgeMonos(nodesA, nodesB, ApicalConstrictingCell.class, i);
                } else {
                    cell = makeCellEdgeMonos(nodesA, nodesB, Cell.class, i);
                }
                cells.add(cell);

                // cleanup and prep for next iteration
                nodesA.clear();
                nodesB.clear();
            }

            if(i == 0)
            {
                zeroNodes.add(a);
                zeroNodes.add(b);
                zeroNodes.add(c);
                zeroNodes.add(d);
                zeroNodes.add(e);
            }

            nodesB.add(e);
            nodesB.add(d);
            nodesB.add(c);
            nodesB.add(b);
            nodesB.add(a);
        }
        Cell cell = makeCellEdgeMonos(zeroNodes, nodesB, ApicalConstrictingCell.class, 0);
        cells.add(cell);

        return cells;
    }

    public static Cell makeCell(List<Node> a, List<Node> b, Class cellClass) throws IllegalAccessException, InstantiationException {
        List<Node> nodes = new ArrayList<>();
        nodes.addAll(b); nodes.addAll(a);

        List<Edge> edges = new ArrayList<>();
        List<Edge> internalEdges = new ArrayList<>();
        // make lateral edges, side 1
        for(int i = 1; i < b.size(); i++){
            Edge e = new LateralEdge(b.get(i-1), b.get(i));
            edges.add(e);
        }
        // make apical edge
        Edge apical = new ApicalEdge(b.get(4) ,a.get(0));
        edges.add(apical);

        // make lateral edges, side 2
        for(int i = 1; i < a.size(); i++){
            Edge e = new LateralEdge(a.get(i-1), a.get(i));
            edges.add(e);
        }

        // make basal edge
        Edge basal = new BasalEdge(a.get(4), b.get(0));
        edges.add(basal);

        // Create internal edges
        internalEdges.add(new BasicEdge(b.get(0), a.get(3)));
        internalEdges.add(new BasicEdge(b.get(1), a.get(2)));
        internalEdges.add(new BasicEdge(b.get(2), a.get(1)));
        internalEdges.add(new BasicEdge(b.get(3), a.get(0)));

        internalEdges.add(new BasicEdge(b.get(1), a.get(4)));
        internalEdges.add(new BasicEdge(b.get(2), a.get(3)));
        internalEdges.add(new BasicEdge(b.get(3), a.get(2)));
        internalEdges.add(new BasicEdge(b.get(4), a.get(1)));

        internalEdges.add(new BasicEdge(b.get(3), a.get(1)));
        internalEdges.add(new BasicEdge(b.get(2), a.get(2)));
        internalEdges.add(new BasicEdge(b.get(1), a.get(3)));




        Cell cell = (Cell) State.create(cellClass);
        cell.setEdges(edges);
        cell.setInternalEdges(internalEdges);
        cell.setNodes(nodes);
        State.setFlagToRender(cell);
        cell.setColor(Painter.DEFAULT_COLOR);
        return cell;
    }

    public static Cell makeCellEdgeMonos(List<Node> a, List<Node> b, Class cellClass, int cellID) throws IllegalAccessException, InstantiationException {
        List<Node> nodes = new ArrayList<>();
        nodes.addAll(b); nodes.addAll(a);

        List<Edge> edges = new ArrayList<>();
        List<Edge> internalEdges = new ArrayList<>();
        // make lateral edges, side 1
        for(int i = 1; i < b.size(); i++){
            Edge e = new LateralEdge(b.get(i-1), b.get(i));
            AddEdgeToState(e, cellID);
            edges.add(e);
        }
        // make apical edge
        Edge apical = new ApicalEdge(b.get(4) ,a.get(0));
        AddEdgeToState(apical, cellID);
        edges.add(apical);

        // make lateral edges, side 2
        for(int i = 1; i < a.size(); i++){
            Edge e = new LateralEdge(a.get(i-1), a.get(i));
            AddEdgeToState(e, cellID);
            edges.add(e);
        }

        // make basal edge
        Edge basal = new BasalEdge(a.get(4), b.get(0));
        AddEdgeToState(basal, cellID);
        edges.add(basal);

        // Create internal edges
        Edge ie1 = new BasicEdge(b.get(0), a.get(3));
        AddEdgeToState(ie1, cellID);
        internalEdges.add(ie1);

        Edge ie2 = new BasicEdge(b.get(1), a.get(2));
        AddEdgeToState(ie2, cellID);
        internalEdges.add(ie2);

        Edge ie3 = new BasicEdge(b.get(2), a.get(1));
        AddEdgeToState(ie3, cellID);
        internalEdges.add(ie3);

        Edge ie4 = new BasicEdge(b.get(3), a.get(0));
        AddEdgeToState(ie4, cellID);
        internalEdges.add(ie4);


        Edge ie5 = new BasicEdge(b.get(1), a.get(4));
        AddEdgeToState(ie5, cellID);
        internalEdges.add(ie5);

        Edge ie6 = new BasicEdge(b.get(2), a.get(3));
        AddEdgeToState(ie6, cellID);
        internalEdges.add(ie6);

        Edge ie7 = new BasicEdge(b.get(3), a.get(2));
        AddEdgeToState(ie7, cellID);
        internalEdges.add(ie7);

        Edge ie8 = new BasicEdge(b.get(4), a.get(1));
        AddEdgeToState(ie8, cellID);
        internalEdges.add(ie8);


        Edge ie9 = new BasicEdge(b.get(3), a.get(1));
        AddEdgeToState(ie9, cellID);
        internalEdges.add(ie8);

        Edge ie10 = new BasicEdge(b.get(2), a.get(2));
        AddEdgeToState(ie10,cellID);
        internalEdges.add(ie8);

        Edge ie11 = new BasicEdge(b.get(1), a.get(3));
        AddEdgeToState(ie11, cellID);
        internalEdges.add(ie8);

        Cell cell;
        if(cellClass == ApicalConstrictingCell.class)
        {
            cell = new ApicalConstrictingCell();
        }
        else {
            cell = new Cell();
        }
        cell.setEdges(edges);
        cell.setInternalEdges(internalEdges);
        cell.setNodes(nodes);

        return cell;
    }

    private static void AddEdgeToState(Edge e, int cellID) throws InstantiationException, IllegalAccessException {
        EdgeMono mono = (EdgeMono)State.create(EdgeMono.class);
        mono.setEdge(e);
        mono.setCellID(cellID);
        if(e instanceof ApicalEdge) mono.setColor(Color.RED);
        if(e instanceof BasalEdge) mono.setColor(Color.BLUE);
        if(e instanceof LateralEdge) mono.setColor(Color.GREEN);
        if(e instanceof BasicEdge) mono.setColor(Color.BLACK);
    }
}
