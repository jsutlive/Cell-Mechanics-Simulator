package Morphogenesis.Components.Render;

import Framework.Object.DoNotExposeInGUI;
import Morphogenesis.Components.ReloadComponentOnChange;
import Renderer.Graphics.IColor;
import Renderer.Graphics.IRender;
import Framework.Object.Component;
import Renderer.Graphics.Painter;

import java.awt.*;

public abstract class ObjectRenderer extends Component implements IRender, IColor {
    @ReloadComponentOnChange
    public Color color = Painter.DEFAULT_COLOR;
    protected Color defaultColor = Painter.DEFAULT_COLOR;
}