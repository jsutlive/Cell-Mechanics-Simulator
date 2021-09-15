package Physics;

import GUI.IColor;
import GUI.IRender;

import java.awt.*;

public class Force implements IColor, IRender
{
    protected Color color;
    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void render() {

    }
}
