package component;

import renderer.graphics.IColor;
import renderer.graphics.IRender;

import java.awt.*;

import static renderer.Renderer.DEFAULT_COLOR;


public abstract class ObjectRenderer extends Component implements IRender, IColor {
    protected Color color = DEFAULT_COLOR;
    public Color defaultColor = DEFAULT_COLOR;

}