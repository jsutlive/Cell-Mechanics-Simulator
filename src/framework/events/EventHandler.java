package framework.events;

import java.util.ArrayList;

/**
 * This is a C#-style event handler system (with IEvent), ported to Java.
 * Slightly altered (Joseph Sutlive) version of Danilow's C# Like Event Handlers Pattern in Java
 * Reference: https://www.codeproject.com/Tips/1178828/Csharp-Like-Event-Handlers-Pattern-in-Java
 * Accessed 2022
 *
 * @param <TEventArgs> Java class that is sent via this event
 */

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