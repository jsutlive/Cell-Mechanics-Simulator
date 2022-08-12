package Model.Organisms;

import Model.Model;
import Model.Cells.*;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
import Utilities.Math.CustomMath;
import Utilities.Model.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrosophilaEmbryo implements  IOrganism {

    int lateralResolution = 4;


    int numberOfSegmentsInTotalCircle = 80;
    int numberOfConstrictingSegmentsInCircle = 18;

    int shorteningCellBegin = 16;
    int shorteningCellEnd = 28;

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
        if(Model.apicalGradient!=null) {
            Model.apicalGradient.calculate(numberOfConstrictingSegmentsInCircle,
                    100.5f, 0.001f,
                    40.1f, .3f);
        }
        allNodes.clear();
        for(Cell cell: allCells)
        {
            for(Node node: cell.getNodes()){
                if(!node.getPosition().isNull()) {
                    if (!allNodes.contains(node)) allNodes.add(node);
                }
            }
        }
        if(allNodes.size() > 400){
            System.out.println(allNodes.size());
            throw new InstantiationException("Should be only 400 nodes");
        }
        System.out.println("ALLNODES:" + allNodes.size());


    }

    private void generateTissueRing() throws InstantiationException, IllegalAccessException {
        Vector2f position, unitVector;
        ArrayList<Cell> mirroredCells = new ArrayList<>();

        ArrayList<Node> oldNodes = new ArrayList<>();
        ArrayList<Node> oldMirroredNodes = new ArrayList<>();

        ArrayList<Node> zeroEdgeNodes = new ArrayList<>();

        //make lateral edges
        for (int i = 0; i < (numberOfSegmentsInTotalCircle/2)+1; i++) {

            ArrayList<Node> nodes= new ArrayList<>();
            ArrayList<Node> mirroredNodes = new ArrayList<>();
            ArrayList<Node> constructionNodes = new ArrayList<>();

            unitVector = CustomMath.GetUnitVectorOnCircle(i, numberOfSegmentsInTotalCircle);

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
                    zeroEdgeNodes.add(new Node(position));
                }
                nodes.add(currentNode);
                mirroredNodes.add(mirroredNode);
                allNodes.add(currentNode);
                allNodes.add(mirroredNode);
            }

            if (i > 0) {
                Cell newCell;
                Cell mirroredCell;
                // Build the first set of cells in the cell ring
                if(i<=numberOfConstrictingSegmentsInCircle/2)
                {
                    constructionNodes.clear();
                    List<Node> oldNodesZ = new ArrayList<>(); // copy list to prevent assignment issues between collections
                    oldNodesZ.addAll(oldNodes);
                    oldNodes.addAll(nodes);
                    newCell = ApicalConstrictingCell.build(oldNodes,lateralResolution, 1);
                    constructionNodes.clear();
                    Collections.reverse(mirroredNodes);
                    constructionNodes.addAll(mirroredNodes);
                    if(i == 1)
                    {
                        Collections.reverse(oldNodesZ);
                        constructionNodes.addAll(oldNodesZ);
                    }
                    else
                    {
                        constructionNodes.addAll(oldMirroredNodes);
                    }
                    mirroredCell = ApicalConstrictingCell.build(constructionNodes,lateralResolution, 1);
                    Collections.reverse(mirroredNodes);
                }
                else
                {
                    if(i>= shorteningCellBegin && i <= shorteningCellEnd)
                    {
                        if( i != (numberOfSegmentsInTotalCircle/2)) {
                            oldNodes.addAll(nodes);
                            Collections.reverse(mirroredNodes);
                            constructionNodes.addAll(mirroredNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            newCell = ShorteningCell.build(oldNodes, lateralResolution, 1);
                            mirroredCell = ShorteningCell.build(constructionNodes, lateralResolution, 1);
                            Collections.reverse(mirroredNodes);
                        }else
                        {
                            constructionNodes.clear();
                            oldNodes.addAll(zeroEdgeNodes);
                            newCell = ShorteningCell.build(oldNodes, lateralResolution, 1);

                            Collections.reverse(zeroEdgeNodes);
                            constructionNodes.addAll(zeroEdgeNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            Collections.reverse(zeroEdgeNodes);
                            mirroredCell = ShorteningCell.build(constructionNodes, lateralResolution, 1);

                        }
                    }
                    else{
                        if( i != (numberOfSegmentsInTotalCircle/2)) {
                            oldNodes.addAll(nodes);
                            Collections.reverse(mirroredNodes);
                            constructionNodes.addAll(mirroredNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            newCell = BasicCell.build(oldNodes, lateralResolution, 1);
                            mirroredCell = BasicCell.build(constructionNodes, lateralResolution, 1);
                            Collections.reverse(mirroredNodes);
                        }else
                        {
                            constructionNodes.clear();
                            oldNodes.addAll(zeroEdgeNodes);
                            Collections.reverse(zeroEdgeNodes);
                            constructionNodes.addAll(zeroEdgeNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            Collections.reverse(zeroEdgeNodes);
                            newCell = BasicCell.build(oldNodes, lateralResolution, 1);
                            mirroredCell = BasicCell.build(constructionNodes, lateralResolution, 1);

                        }
                    }

                }
                newCell.setId(i-1);
                mirroredCell.setId(numberOfSegmentsInTotalCircle-i);
                addCellToList(mirroredCells, mirroredCell, i);
                addCellToList(allCells, newCell, i);

            }
            Collections.reverse(nodes);
            oldMirroredNodes = mirroredNodes;
            oldNodes = nodes;
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
        if(cell.getNodes().size() == 0){
            throw new IllegalStateException("Nodes list not found at ring location " + ringLocation);
        }
    }

    private float getRadiusToNode(int j) {
        float radiusStep = (innerRadius - outerRadius) / lateralResolution;
        return outerRadius +  radiusStep * j;
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


