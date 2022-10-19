package Framework.States;

import Framework.Object.Entity;
import Framework.Object.Tag;
import Morphogenesis.Models.BasicCellsModel;
import Renderer.Graphics.IRender;
import Morphogenesis.Models.DrosophilaRingModel;

import java.util.ConcurrentModificationException;

public class EditorState extends State
{
    Entity model;
    @Override
    public void Init() {
        State.reset();
        model = State.create(DrosophilaRingModel.class);
        assert model != null;
        model.addTag(Tag.MODEL);
        for(Entity obj: allObjects){
            obj.start();
        }
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

    @Override
    void OnChangeState() {

    }
}
