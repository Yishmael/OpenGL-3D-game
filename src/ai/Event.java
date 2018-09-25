package ai;

public class Event {
    public enum EventType {
        PLAYER_SEEN, PLAYER_UNSEEN, HEALTH_LOW,
    }

    protected EventType type;

    // TODO save info regarding the event
    public Event(EventType type) {
        this.type = type;
    }
}
