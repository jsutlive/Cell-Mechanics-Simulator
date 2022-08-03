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
    int shorteningCellEnd = 18;

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
                    100.5f, 1f,
                    0f, .00001f);
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
                    constructionNodes.addAll(oldNodes);
                    constructionNodes.addAll(nodes);
                    newCell = ApicalConstrictingCell.build(constructionNodes,lateralResolution, 1);
                    constructionNodes.clear();
                    if(i == 1)
                    {
                        Collections.reverse(oldNodes);
                        constructionNodes.addAll(oldNodes);
                        Collections.reverse(oldNodes);
                        Collections.reverse(mirroredNodes);
                        constructionNodes.addAll(mirroredNodes);
                        mirroredCell = ApicalConstrictingCell.build(constructionNodes,lateralResolution, 1);
                        Collections.reverse(mirroredNodes);
                        constructionNodes.clear();
                    }
                    else
                    {
                        constructionNodes.addAll(oldMirroredNodes);
                        Collections.reverse(mirroredNodes);
                        constructionNodes.addAll(mirroredNodes);
                        mirroredCell = ApicalConstrictingCell.build(constructionNodes ,lateralResolution, 1);
                        Collections.reverse(mirroredNodes);
                        constructionNodes.clear();
                    }
                }
                else
                {
                    if(i>= shorteningCellBegin && i <= shorteningCellEnd)
                    {
                        if( i != (numberOfSegmentsInTotalCircle/2)) {
                            oldNodes.addAll(nodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            Collections.reverse(mirroredNodes);
                            constructionNodes.addAll(mirroredNodes);
                            newCell = ShorteningCell.build(oldNodes, lateralResolution, 1);
                            mirroredCell = ShorteningCell.build(constructionNodes, lateralResolution, 1);
                            Collections.reverse(mirroredNodes);
                        }else
                        {
                            constructionNodes.addAll(oldNodes);
                            constructionNodes.addAll(zeroEdgeNodes);
                            newCell = ShorteningCell.build(constructionNodes, lateralResolution, 1);
                            constructionNodes.clear();

                            constructionNodes.addAll(oldMirroredNodes);
                            Collections.reverse(zeroEdgeNodes);
                            constructionNodes.addAll(zeroEdgeNodes);
                            Collections.reverse(zeroEdgeNodes);
                            mirroredCell = ShorteningCell.build(constructionNodes, lateralResolution, 1);
                            constructionNodes.clear();
                        }
                    }
                    else{
                        if( i != (numberOfSegmentsInTotalCircle/2)) {
                            oldNodes.addAll(nodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            Collections.reverse(mirroredNodes);
                            constructionNodes.addAll(mirroredNodes);
                            newCell = BasicCell.build(oldNodes, lateralResolution, 1);
                            mirroredCell = BasicCell.build(constructionNodes, lateralResolution, 1);
                            Collections.reverse(mirroredNodes);
                        }else
                        {
                            oldNodes.addAll(zeroEdgeNodes);
                            constructionNodes.addAll(oldMirroredNodes);
                            Collections.reverse(zeroEdgeNodes);
                            constructionNodes.addAll(zeroEdgeNodes);
                            Collections.reverse(zeroEdgeNodes);
                            newCell = BasicCell.build(oldNodes, lateralResolution, 1);
                            mirroredCell = BasicCell.build(constructionNodes, lateralResolution, 1);
                            constructionNodes.clear();

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


