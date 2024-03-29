package component;

import framework.object.EntityGroup;
import input.SelectionEvents;

import java.awt.*;

public abstract class CellGradient extends Component{
    protected transient EntityGroup cellGroup;
    public int numberOfConstrictingCells;
    public Color groupColor;
    protected Gradient gradient;

    protected abstract void addCellsToGroup();

    protected void selectAllInGroup(Component c){
        if(c == this) {
            SelectionEvents.selectGroup(cellGroup);
        }
    }
}

