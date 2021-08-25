package Physics.Forces.Springs;

import Utilities.Math.CustomMath;

public abstract class DynamicSpring extends Spring
{

    protected float[] springConstant;
    protected float[] equilibriumRatio;

    public void setSpringConstant(float a, float b)
    {
        float min = Math.min(a, b);
        float max = Math.max(a, b);
        springConstant[0] = min;
        springConstant[1] = max;
    }

    public void setSpringConstant(float a)
    {
        springConstant[0] = a;
        springConstant[1] = a;
    }

    public void setEquilibriumRatio(float a, float b)
    {
        float min = Math.min(a, b);
        float max = Math.max(a, b);
        equilibriumRatio[0] = min;
        equilibriumRatio[1] = max;
    }

    public void setEquilibriumRatio(float a)
    {
        equilibriumRatio[0] = a;
        equilibriumRatio[1] = a;
    }

    public float getSpringConstantSimple()
    {
        return CustomMath.avg(springConstant);
    }

    public float getEquilibriumRatioSimple()
    {
        return CustomMath.avg(equilibriumRatio);
    }
}
