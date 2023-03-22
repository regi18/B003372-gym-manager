package utils.observer;

import utils.observer.events.Event;


public interface Observer {
    void update(Event event);
}
