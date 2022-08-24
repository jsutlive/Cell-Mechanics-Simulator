package Model;

import Engine.Object.MonoBehavior;
import Engine.States.State;
import GUI.Vector.CircleGraphic;
import Model.Cells.*;
import Model.Components.Meshing.CellMesh;
import Model.Components.Render.CellRenderer;
import Model.Organisms.*;
import Physics.Forces.*;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Boundary;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Model extends MonoBehavior
{
    IOrganism organism = new DrosophilaEmbryo();
    List<Node> allNodes = new ArrayList<>();
    float shellRadius = 302f;
    Cell yolk;
    LJForceType ljType = LJForceType.simple;
    public static Vector2f largestResultantForce = new Vector2f(0);
    public static Gradient apicalGradient;
    List<Node> basalNodes = new ArrayList<>();
    List<Node> apicalNodes = new ArrayList<>();
    /**
     * In the Model Monobehavior object, awake is used to generate the cells and other physical components
     * of the simulation.
     * @throws InstantiationException Cannot instantiate cells in model due to an error
     * @throws IllegalAccessException Problems accessing memory during cell creation
     */
    @Override
    public void awake() throws InstantiationException, IllegalAccessException {
        State.addGraphicToScene(new CircleGraphic(new Vector2i(400), 602, Color.gray));
        apicalGradient = new GaussianGradient(0f, 0.8f);
        organism.generateOrganism();

        setCellColors();
        allNodes = organism.getAllNodes();

        for(Cell cell : organism.getAllCells()){
            CellMesh mesh = cell.getComponent(CellMesh.class);
            for(Edge edge : mesh.edges) {
                if(edge instanceof BasalEdge) {
                    basalNodes.add(edge.getNodes()[0]);
                }else if(edge instanceof ApicalEdge){
                    apicalNodes.add(edge.getNodes()[0]);
                }
            }
        }

    }


    public void printCells(){
        // pick specific cells to print using cell.print() to debug using this function
    }

    @Override
    public void start() {
        for(Cell cell : organism.getAllCells()) {
            cell.start();
        }

    }

    private void checkNodesWithinBoundary(List<Node> allNodes) {
        for(Node node: allNodes)
        {
            if(!Boundary.ContainsNode(node, new Vector2f(400), shellRadius))
            {
                Boundary.clampNodeToBoundary(node, new Vector2f(400), shellRadius);
            }

        }
    }

   /* private void checkCollision(){

        
    }

    private void calculateLennardJonesForces(float maxRadius, float ljConstant, Cell cell) {
        for (Edge edge: cell.getEdges())
        {
            for(Node n: organism.getAllNodes()){
                if(edge.contains(n)) continue;
                float dist = CustomMath.pDistanceSq(n, edge);
                if(Float.isNaN(dist)) continue;
                if(dist < maxRadius){
                    Vector2f forceVector = Force.GetLennardJonesLikeForce(ljConstant, edge, n, ljType);
                    if(!Float.isNaN(forceVector.x) && !Float.isNaN(forceVector.y)) {
                       n.AddForceVector(forceVector);
                   }
                }
            }
            edge.hasActed = false;
        }
    }


    /**
     * Use State.create(Model.class) instead to ensure a proper reference to the state is established.
     * When established, this object immediately runs start functions.
     */
    public Model() {
        this.start();
    }

    private void setCellColors() {
        for(Cell cell: organism.getAllCells())
        {
            CellRenderer renderer = cell.getComponent(CellRenderer.class);
            CellMesh mesh = cell.getComponent(CellMesh.class);

            System.out.println(cell.getId());
            if(cell instanceof ApicalConstrictingCell)
            {
                renderer.setColor(Color.MAGENTA);
                for(Edge edge: mesh.edges){

                    if(edge instanceof ApicalEdge){
                        edge.setColor(Color.RED);
                    }
                }
            }
            if(cell instanceof ShorteningCell)
            {
                renderer.setColor(Color.BLUE);
            }
            if(cell instanceof BasicCell)
            {
                renderer.setColor(Color.WHITE);
            }

        }
    }




}
