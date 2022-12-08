package Morphogenesis.Meshing;

import Morphogenesis.Physics.CellGroups.GroupSelector;
import Morphogenesis.ReloadComponentOnChange;
import Utilities.Geometry.Vector.Vector2f;

import java.util.Random;

@GroupSelector
@ReloadComponentOnChange
public class VoronoiMesh extends Mesh{
    public int numberOfCells = 20;
    public int modelWidth = 800;
    public int modelHeight = 800;

    private int[] xc, yc;

    @Override
    public void awake() {
        Random random = new Random();
        for(int i = 0; i < numberOfCells; i++){
            xc[i] = random.nextInt(modelWidth);
            yc[i] = random.nextInt(modelHeight);
        }
        for (int x = 0; x < modelWidth; x++) {
            for (int y = 0; y < modelHeight; y++) {
                int n = 0;
                for (byte i = 0; i < numberOfCells; i++) {
                    if (Vector2f.dist(xc[i], x, yc[i], y) < Vector2f.dist(xc[n], x, yc[n], y)) {
                        n = i;
                    }
                }
            }
        }
    }
}
