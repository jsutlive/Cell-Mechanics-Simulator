package Morphogenesis.Components.Render;

import Framework.Object.DoNotExposeInGUI;
import Renderer.Graphics.IColor;
import Renderer.Graphics.IRender;
import Framework.Object.Component;

import java.awt.*;

public abstract class ObjectRenderer extends Component implements IRender, IColor {
    public Color color;
    protected Color defaultColor;
}