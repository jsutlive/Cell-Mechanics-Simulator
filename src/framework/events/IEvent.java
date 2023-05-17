package framework.events;

/**
 * This is a C#-style event handler system (with EventHandler), ported to Java.
 * From Danilow's C# Like Event Handlers Pattern in Java
 * Reference: https://www.codeproject.com/Tips/1178828/Csharp-Like-Event-Handlers-Pattern-in-Java
 * Accessed 2022
 */

@FunctionalInterface
public interface IEvent<TEventArgs extends Object> {
    void invoke( TEventArgs eventArgs);
}