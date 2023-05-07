package component;

import utilities.math.Gauss;

public class GaussianGradient extends Gradient{

    private float mu;

    public float getMu() {
        return mu;
    }

    private float sigma;
    public float getSigma(){
        return sigma;
    }

    @Override
    public void calculate(int gradientCells, float constantCeiling, float ratioCeiling,
                          float constantFloor, float ratioFloor) {
        checkGradientArguments(gradientCells);
        int halfCells = gradientCells/2;
        constants = new float[halfCells];
        ratios = new float[halfCells];

        float xStep = getLinearStep(3,0, halfCells);
        for(int i = 0; i < halfCells; i++)
        {
            float x = xStep*i;
            float y = Gauss.pdf(x, mu, sigma);
            constants[i] = y*constantCeiling;
            ratios[i] = ratioCeiling;//y * ratioCeiling;

        }
        float constOffset = constantFloor - constants[halfCells-1];
        float constMultiplier = constantCeiling / (constants[0] + constOffset);
        float ratioOffset = ratioFloor - ratios[halfCells-1];
        float ratioMultiplier = ratioCeiling / (ratios[0] + ratioOffset);
        for(int i = 0; i < halfCells; i++)
        {
            constants[i] += constOffset;
            constants[i] *= constMultiplier;
            ratios[i] += ratioOffset;
            ratios[i] *= ratioMultiplier;
        }


    }

    public GaussianGradient(float mu, float sigma){
        this.mu = mu;
        this.sigma = sigma;
    }


}
