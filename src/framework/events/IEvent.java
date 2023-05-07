package framework.events;

@FunctionalInterface
public interface IEvent<TEventArgs extends Object> {
    void invoke( TEventArgs eventArgs);
}