
package audio;

import org.lwjgl.openal.AL10;

import audio.Sound.SoundType;
import engine.utils.Vec3;

public class Source {
    private int source;

    public Source() {
        source = AL10.alGenSources();
        setGain(1);
        setPitch(1);
        setPosition(new Vec3());
    }

    public void delete() {
        stop();
        AL10.alDeleteSources(source);
    }

    @Override
    protected void finalize() {
        delete();
        try {
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void pause() {
        AL10.alSourcePause(source);
    }

    public void play(SoundType sound) {
        stop();
        if (sound == SoundType.BACKGROUND_MUSIC) {
            setGain(Audio.getMusicVolume());
        } else {
            setGain(Audio.getSoundVolume());
        }
        AL10.alSourcei(source, AL10.AL_BUFFER, Audio.loadSound(sound).getBuffer());
        AL10.alSourcePlay(source);
    }

    public void resume() {
        AL10.alSourcePlay(source);
    }

    public void setPosition(Vec3 position) {
        AL10.alSource3f(source, AL10.AL_POSITION, position.x, position.y, position.z);
    }

    public void setLooping(boolean looping) {
        AL10.alSourcei(source, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    public void setGain(float gain) {
        AL10.alSourcef(source, AL10.AL_GAIN, gain);
    }

    public void setPitch(float pitch) {
        AL10.alSourcef(source, AL10.AL_PITCH, pitch);
    }

    public void stop() {
        AL10.alSourceStop(source);
    }
}
