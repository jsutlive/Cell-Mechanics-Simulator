package Physics.Forces;

import Utilities.Math.CustomMath;
import Utilities.Math.Gauss;

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
        constants[0] = constantCeiling;
        ratios[0] = ratioCeiling;
        for(int i = 1; i < halfCells; i++)
        {
            float x = xStep*i;
            float y = Gauss.pdf(x, mu, sigma);
            constants[i] = y*constantCeiling;
            ratios[i] = ratioCeiling;//y * ratioCeiling;
            //constants[i] = CustomMath.floor(y * constantCeiling, constantFloor);
            //ratios[i] = CustomMath.floor(y * ratioCeiling, ratioFloor);
        }

    }

    public GaussianGradient(float mu, float sigma){
        this.mu = mu;
        this.sigma = sigma;
    }


}
