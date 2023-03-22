package utils.observer;

import utils.observer.events.Event;
import java.util.ArrayList;


public abstract class Subject {
   private final ArrayList<Observer> subscribers = new ArrayList<>();

   public void subscribe(Observer o) {
       this.subscribers.add(o);
   }

   public boolean unsubscribe(Observer o) {
       return this.subscribers.remove(o);
   }

   public void notifyAll(Event event) {
       this.subscribers.forEach(observer -> observer.update(event));
   }
}
