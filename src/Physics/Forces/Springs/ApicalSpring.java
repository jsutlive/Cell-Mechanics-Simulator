package Physics.Forces.Springs;

import Model.Model;
import Physics.Bodies.Cell.ApicalEdge;
import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.Cell.CellGroup;
import Physics.Bodies.Edge;
import Physics.Bodies.PhysicsBody;
import Utilities.Math.CustomMath;

import java.util.HashMap;
import java.util.HashSet;

public class ApicalSpring extends DynamicSpring
{
    private HashMap<ApicalEdge, ApicalConstrictionParameters> params = new HashMap<>();

    public static ApicalSpring configureNew(float[] constant, float[] ratio)
    {
        ApicalSpring spring = new ApicalSpring();
        spring.springConstant = constant;
        spring.equilibriumRatio = ratio;
        spring.listeners = new HashSet<>();
        return spring;
    }

    private ApicalSpring(){}

    @Override
    public void start()
    {
        setEquationFactors();
    }

    @Override
    public void attach(CellGroup cellGroup)
    {
        for(Cell cell: cellGroup.getCells())
        {
            for(Edge edge: cell.getAllEdges())
            {
                if(edge instanceof ApicalEdge)
                {
                    listeners.add(edge);
                }
            }
        }
    }

    public void setEquationFactors()
    {
        int halfListeners = this.listeners.size()/2;
        float cutoff = getForceCutoff(halfListeners);
        for(PhysicsBody p: listeners)
        {
            ApicalEdge apicalEdge = (ApicalEdge)p;
            int id = apicalEdge.getCell().getID();
            params.put(apicalEdge, new ApicalConstrictionParameters(cutoff, halfListeners, id));

        }
    }

    private float getForceCutoff(int numberOfListeners) {
        float exp = (float) Math.exp(-0.5f * CustomMath.sq(springConstant[1]) * (numberOfListeners -1)/ numberOfListeners);
        return (springConstant[0] * exp);
    }

    @Override
    public void calculateForce(PhysicsBody body)
    {
        ApicalEdge edge = (ApicalEdge) body;
        constant = getConstant(edge);
        ratio = getRatio(edge);
        length = edge.getLength();
        super.calculateForce(edge);
    }

    public float getCutoff(ApicalEdge edge)
    {
        ApicalConstrictionParameters apicalConstrictionParameters = params.get(edge);
        return apicalConstrictionParameters.getCutoff();
    }

    public float getConstant(ApicalEdge edge)
    {
        ApicalConstrictionParameters apicalConstrictionParameters = params.get(edge);
        return apicalConstrictionParameters.getConstrictConstant();
    }

    public float getRatio(ApicalEdge edge)
    {
        ApicalConstrictionParameters apicalConstrictionParameters = params.get(edge);
        return apicalConstrictionParameters.getConstrictRatio();
    }

    public int getLoc(ApicalEdge edge)
    {
        ApicalConstrictionParameters apicalConstrictionParameters = params.get(edge);
        return apicalConstrictionParameters.getLocValue();
    }


    private class ApicalConstrictionParameters
    {
        private float constrictConstant;
        private float constrictRatio;
        private int loc;
        private float cutoff;
        private int sign;

        public ApicalConstrictionParameters(float cutoff, float halfListeners, int id)
        {
            loc = getLoc(id);
            this.cutoff = cutoff;
            calculateConstrictConstant(cutoff, halfListeners, loc);
            calculateConstrictRatio(loc);
        }

        private void calculateConstrictRatio(int loc)
        {
            float equilibriumRatioDifference = equilibriumRatio[1] - equilibriumRatio[0];
            constrictRatio = equilibriumRatioDifference * loc + equilibriumRatio[0];

        }

        private void calculateConstrictConstant(float cutoff, float halfListeners, int loc) {
            float locationFactorConstriction = springConstant[1] / halfListeners * loc;
            constrictConstant = (float) (sign * springConstant[0] * Math.exp( -0.5f * CustomMath.sq(locationFactorConstriction)) - cutoff);
        }

        public float getCutoff()
        {
            return cutoff;
        }

        public float getConstrictConstant()
        {
            return constrictConstant;
        }

        public float getConstrictRatio()
        {
            return constrictRatio;
        }

        public int getLocValue(){return loc;}

        private int getLoc(int id) {
            int loc = 0;
            if(id < 80/ 2) //Model.TOTAL_CELLS
            {
                loc = id + 1;
                sign = 1;
                return loc;
            }
            else
            {
                sign = -1;
                loc = 80-id; //Model.TOTAL_CELLS
                return loc;
            }
        }
    }
}
