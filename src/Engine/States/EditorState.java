package Engine.States;

import Engine.Object.Tag;
import GUI.IRender;
import GUI.Painter;
import Model.*;
import Utilities.Geometry.Vector2i;

import java.awt.*;
import java.util.ConcurrentModificationException;

public class EditorState extends State
{
    Model model;
    @Override
    public void Init() throws InstantiationException, IllegalAccessException {
        State.reset();
        model = (Model) State.create(CellTestModel.class);
        model.addTag(Tag.MODEL);
    }

    @Override
    public void Tick() {
    }

    @Override
    public void Render() {
        try {
            for(IRender rend: renderBatch)
            {
                rend.render();
            }
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
    }


}
