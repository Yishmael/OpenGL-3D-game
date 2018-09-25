package ai;

import engine.utils.Color;
import game.Misc;
import game.entities.units.Unit;
import game.graphics.text.WorldText;

public class Agent {
    public enum State {
        ATTACKING, FLEEING, IDLING, PATROLLING,
    }

    private State state;
    private float idleTime;
    private Unit unit;

    public Agent(Unit unit) {
        this.unit = unit;
        state = State.IDLING;
    }

    public State getState() {
        return state;
    }

    public void processEvent(Event event) {
        switch (event.type) {
        case HEALTH_LOW:
            System.out.println("switching to FLEE mode");
            state = State.FLEEING;
            break;
        case PLAYER_SEEN:
            System.out.println("switching to ATK mode");
            state = State.ATTACKING;
            break;
        case PLAYER_UNSEEN:
            System.out.println("going back to idling");
            state = State.IDLING;
            break;
        }
    }

    public void update() {
        switch (state) {
        case IDLING:
            idleTime += Misc.dt;
            if (idleTime > 5f) {
                idleTime = 0;
                state = State.PATROLLING;
            }
            break;
        case PATROLLING:
            // move to the next patrol point
            // if returned to first patrol point
            // state = State.IDLING;
            break;
        case ATTACKING:
            // keep attacking the player
            break;
        case FLEEING:
            // run away from the enemy
            break;
        }

    }

}
