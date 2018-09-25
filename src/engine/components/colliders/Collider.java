package engine.components.colliders;

import engine.components.Component;
import engine.physics.IntersectInfo;
import engine.physics.IntersectInfo.ColliderType;
import engine.utils.Vec3;
import game.data.spatial.Point;

public interface Collider extends Component {
    public IntersectInfo getIntersectInfo(Collider collider);
    public Vec3 getNormalAtPoint(Point point);
    public ColliderType getColliderType();
    // TODO maybe send the gameobject instead
    public void onCollisionEnter();
    public void onCollision(Collider collider);
    public void onCollisionExit();
    
}
