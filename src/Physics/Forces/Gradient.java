package Physics.Forces;

public abstract class Gradient {

    public float delayFactor = 1000;
    protected float[] constants;
    public float[] getConstants() {
        return constants;
    }

    protected float[] ratios;
    public float[] getRatios() {
        return ratios;
    }

    public abstract void calculate(int gradientCells, float constanctCeiling, float ratioCeiling,
                                   float constantFloor, float ratioFloor);

    public void calculate(int gradientCells, float constantCeiling, float ratioCeilng){
        calculate(gradientCells, constantCeiling, ratioCeilng, 0f,0f);
    }

    public void calculateNormalized(int gradientCells){
        calculate(gradientCells,1,1,0,0);
    }

    void checkGradientArguments(int gradientCells){
        if(gradientCells%2 != 0){
            throw new IllegalArgumentException("Gradient cells must be divisible by 2");
        }
    }

    public static float getLinearStep(float high, float low, int steps){
        return (high - low)/(steps-1);
    }
}
