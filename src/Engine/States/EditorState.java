package Engine.States;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import GUI.IRender;
import Model.*;
import java.util.ConcurrentModificationException;

public class EditorState extends State
{
    MonoBehavior model;
    @Override
    public void Init() throws InstantiationException, IllegalAccessException {
        State.reset();
        model = State.create(DrosophilaRingModel.class);
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
