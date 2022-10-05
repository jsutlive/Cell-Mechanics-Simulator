package Framework.Events;

import java.util.ArrayList;

public class EventHandler<TEventArgs>
{
    private ArrayList<IEvent<TEventArgs>> eventDelegateArray = new ArrayList<>();
    public void subscribe(IEvent<TEventArgs> methodReference)
    {
        eventDelegateArray.add(methodReference);
    }
    public void unSubscribe(IEvent<TEventArgs> methodReference)
    {
        eventDelegateArray.remove(methodReference);
    }
   
    public void close()
    {
        if (eventDelegateArray.size()>0)
            eventDelegateArray.clear();
    }

    public void invoke(TEventArgs eventArgs) {
        if (eventDelegateArray.size()>0)
            eventDelegateArray.forEach(p -> p.invoke(eventArgs));
    }
}