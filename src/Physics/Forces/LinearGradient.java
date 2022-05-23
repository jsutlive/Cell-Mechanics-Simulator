package Physics.Forces;

public class LinearGradient extends Gradient{

    private float constantStep;

    public float getConstantStep() {
        return constantStep;
    }

    private float ratioStep;
    public float getRatioStep(){
        return ratioStep;
    }

    @Override
    public void calculate(int gradientCells, float constantCeiling, float ratioCeiling,
                          float constantFloor, float ratioFloor) {
        checkGradientArguments(gradientCells);
        int halfCells = gradientCells/2;
        constantStep = getLinearStep(constantCeiling, constantFloor, halfCells);
        ratioStep = getLinearStep(ratioCeiling, ratioFloor, halfCells);

        float[] constants = new float[halfCells];
        float[] ratios = new float[halfCells];
        for(int i = 0; i < halfCells; i++)
        {
            constants[i] = constantCeiling - (i * constantStep);
            ratios[i] = ratioFloor + (i * ratioStep);
        }
        this.constants = constants;
        this.ratios = ratios;
    }

    public LinearGradient(){}
}
