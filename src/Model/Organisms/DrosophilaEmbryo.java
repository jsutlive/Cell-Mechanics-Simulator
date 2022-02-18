package Model.Organisms;

import Model.*;
import Engine.Renderer;
import Engine.States.State;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
import Utilities.Math.CustomMath;
import Utilities.Model.Builder;
import Utilities.Model.Mesh;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DrosophilaEmbryo implements  IOrganism {

    int lateralResolution = 4;


    int numberOfSegmentsInTotalCircle = 80;

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
        //allCells = Mesh.circle(80, innerRadius, outerRadius, boundingBox.asFloat());
        for(Cell cell: allCells)
        {
            for(Node node: cell.getNodes()){
                if(!allNodes.contains(node)) allNodes.add(node);
            }
        }
    }

    private void generateTissueRing() throws InstantiationException, IllegalAccessException {
        Vector2f position, unitVector;
        ArrayList<Edge> oldEdges = new ArrayList<>();
        ArrayList<Edge> zeroEdge = new ArrayList<>();

        //make lateral edges
        for (int i = 0; i < numberOfSegmentsInTotalCircle; i++) {
            ArrayList<Edge> edges = new ArrayList<>();
            unitVector = CustomMath.GetUnitVectorOnCircle(i, numberOfSegmentsInTotalCircle);
            Node lastNode = new Node();   // Create null vertex to be used to create edges later.
            for (int j = 0; j <= lateralResolution; j++) {
                float radiusToNode = getRadiusToNode(j);
                // Transform polar to world coordinates
                position = CustomMath.TransformToWorldSpace(unitVector, radiusToNode, boundingBox.asFloat());
                Node currentNode = new Node(position);
                if (j >= 1) {
                    edges.add(new LateralEdge(currentNode, lastNode));
                }
                allNodes.add(currentNode);
                lastNode = currentNode;
            }

            if (i > 0) {
                Cell newCell;
                if (i >=40) {
                    if(i>=71) newCell = Builder.createCell(oldEdges, edges, ApicalConstrictingCell.class);
                    else newCell = Builder.createCell(oldEdges, edges, Cell.class);
                    newCell.setRingLocation(80 - (i - 1));

                } else {
                    if(i<=10)newCell = Builder.createCell(oldEdges, edges, ApicalConstrictingCell.class);
                    else newCell = Builder.createCell(oldEdges, edges, Cell.class);
                    newCell.setRingLocation(i);
                }
                allCells.add(newCell);

            } else if (i == 0){
                zeroEdge = edges;
            }

            oldEdges = edges;
        }

        Cell newCell = Builder.createCell(oldEdges, zeroEdge, ApicalConstrictingCell.class);
        newCell.setRingLocation(1);
        allCells.add(newCell);

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


