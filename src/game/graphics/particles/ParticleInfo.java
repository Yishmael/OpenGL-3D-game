package game.graphics.particles;

import engine.utils.Vec3;

public enum ParticleInfo {
    EXPLOSION(300, 0.1f, 0f, false), IMPACT(100, 0.1f, 0f, false), FLAME(200, 2f, 0.03f, true);

    private int fragmentCount;
    private float fragmentDuration;
    private float timeBetweenFragments;
    private boolean looping;
    private Vec3[] velocities;

    ParticleInfo(int fragmentCount, float duration, float timeBetweenFragments, boolean looping) {
        this.fragmentCount = fragmentCount;
        this.fragmentDuration = duration;
        this.timeBetweenFragments = timeBetweenFragments;
        this.looping = looping;
        velocities = new Vec3[fragmentCount];
    }

    public int getFragmentCount() {
        return fragmentCount;
    }

    public float getFragmentDuration() {
        return fragmentDuration;
    }

    public Vec3[] getFragmentVelocities() {
        return velocities;
    }

    public float getTimeBetweenFragments() {
        return timeBetweenFragments;
    }

    public boolean isLooping() {
        return looping;
    }
}
