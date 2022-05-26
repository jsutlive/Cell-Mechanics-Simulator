package Model.Organisms;

import Model.*;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
import Utilities.Math.CustomMath;
import Utilities.Model.Builder;
import Utilities.Model.Mesh;

import java.util.ArrayList;
import java.util.List;

public class DrosophilaEmbryo implements  IOrganism {

    int lateralResolution = 4;


    int numberOfSegmentsInTotalCircle = 80;
    int numberOfConstrictingSegmentsInCircle = 18;

    int shorteningCellBegin = 16;
    int shorteningCellEnd = 30;

    float outerRadius = 300;
    float innerRadius = 200;
    List<Cell> allCells = new ArrayList<>();
    List<Node> allNodes = new ArrayList<>();

    //bounding box dimensions that determine where and how large the image will be drawn.
    final Vector2i boundingBox;

    public DrosophilaEmbryo()
    {
        boundingBox = new Vector2i(800);
    }

    @Override
    public void generateOrganism() throws InstantiationException, IllegalAccessException {
        generateTissueRing();
        Model.apicalGradient.calculate(numberOfConstrictingSegmentsInCircle,
                1.55f, .3f,
                0.15f, .0001f);
        for(Cell cell: allCells)
        {
            for(Node node: cell.getNodes()){
                if(!allNodes.contains(node)) allNodes.add(node);
            }
        }
    }

    private void generateTissueRing() throws InstantiationException, IllegalAccessException {
        Vector2f position, unitVector;
        ArrayList<Cell> mirroredCells = new ArrayList<>();
        ArrayList<Edge> oldEdges = new ArrayList<>();
        ArrayList<Edge> oldMirroredEdges = new ArrayList<>();


        ArrayList<Edge> zeroEdge = new ArrayList<>();

        //make lateral edges
        for (int i = 0; i < (numberOfSegmentsInTotalCircle/2)+1; i++) {
            ArrayList<Edge> edges = new ArrayList<>();
            ArrayList<Edge> mirroredEdges = new ArrayList<>();
            unitVector = CustomMath.GetUnitVectorOnCircle(i, numberOfSegmentsInTotalCircle);
            Node lastNode = new Node();   // Create null vertex to be used to create edges later.
            Node lastMirroredNode = new Node();
            Node lastMirroredNodeX = new Node();
            Node lastZeroEdgeNode = new Node();
            for (int j = 0; j <= lateralResolution; j++) {
                float radiusToNode = getRadiusToNode(j);
                // Transform polar to world coordinates
                position = CustomMath.TransformToWorldSpace(unitVector, radiusToNode, boundingBox.asFloat());
                Node currentNode = new Node(position);
                Node mirroredNode = currentNode.clone();
                mirroredNode.mirrorAcrossYAxis();
                if(i == numberOfSegmentsInTotalCircle/2)
                {
                    unitVector = CustomMath.GetUnitVectorOnCircle(0, numberOfSegmentsInTotalCircle);
                    unitVector.y = - unitVector.y;
                    position = CustomMath.TransformToWorldSpace(unitVector,radiusToNode, boundingBox.asFloat());
                    Node currentZeroEdgeNode = new Node(position);

                    if(j >= 1){
                        zeroEdge.add(new LateralEdge(currentZeroEdgeNode, lastZeroEdgeNode));
                    }
                    lastZeroEdgeNode = currentZeroEdgeNode;
                }
                if (j >= 1) {
                    edges.add(new LateralEdge(currentNode, lastNode));
                    mirroredEdges.add(new LateralEdge(mirroredNode, lastMirroredNode));
                }
                allNodes.add(currentNode);
                allNodes.add(mirroredNode);
                lastNode = currentNode;
                lastMirroredNode = mirroredNode;
            }

            if (i > 0) {
                Cell newCell;
                Cell mirroredCell;
                // Build the first set of cells in the cell ring
                if(i<=numberOfConstrictingSegmentsInCircle/2)
                {
                    newCell = Builder.createCell(oldEdges, edges, ApicalConstrictingCell.class);
                    if(i == 1)
                    {
                        mirroredCell = Builder.createCell(mirroredEdges, oldEdges, ApicalConstrictingCell.class);
                    }
                    else
                    {
                        mirroredCell = Builder.createCell(mirroredEdges, oldMirroredEdges, ApicalConstrictingCell.class);
                    }
                }
                else
                {
                    Class cellClass = Cell.class;
                    if(i>= shorteningCellBegin && i <= shorteningCellEnd)
                    {
                        cellClass = ShorteningCell.class;
                    }
                    if( i != (numberOfSegmentsInTotalCircle/2)) {

                        newCell = Builder.createCell(oldEdges, edges, cellClass);
                        mirroredCell = Builder.createCell(mirroredEdges, oldMirroredEdges, cellClass);
                    }else
                    {
                        newCell = Builder.createCell(oldEdges, zeroEdge, cellClass);
                        mirroredCell = Builder.createCell(zeroEdge, oldMirroredEdges, cellClass);

                    }

                }
                newCell.setId(i-1);
                mirroredCell.setId(numberOfSegmentsInTotalCircle-i);
                addCellToList(mirroredCells, mirroredCell, i);
                addCellToList(allCells, newCell, i);

            } else if (i == 0){
                //zeroEdge = edges;
            }
            oldMirroredEdges = mirroredEdges;
            oldEdges = edges;
        }
        allCells.addAll(mirroredCells);

    }

    @Override
    public void addCellToList(List<Cell> cellList, Cell cell)
    {
        addCellToList(cellList, cell, 0);
    }

    public void addCellToList(List<Cell> cellList, Cell cell, int ringLocation) {
        if(cell !=null) {
            cell.setRingLocation(ringLocation);
            cellList.add(cell);
        }else
        {
            throw new NullPointerException("New cell object not instantiated successfully");
        }
    }

    private float getRadiusToNode(int j) {
        float radiusStep = (innerRadius - outerRadius) / lateralResolution;
        float radiusToNode = outerRadius +  radiusStep * j;
        return radiusToNode;
    }

    @Override
    public List<Cell> getAllCells() {
        return allCells;
    }

    @Override
    public List<Node> getAllNodes(){
        return allNodes;
    }
}


