package audio;

import static org.lwjgl.openal.AL10.alListener3f;
import static org.lwjgl.openal.ALC11.ALC_CAPTURE_DEVICE_SPECIFIER;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import audio.Sound.SoundType;
import engine.utils.Vec3;
import game.data.Resources;
import game.utils.Loader;

public abstract class Audio {
    private static ArrayList<Integer> buffers = new ArrayList<>();
    private static ALCCapabilities alcCaps;
    private static long device, context;
    private static Source defaultSource;

    private static float soundVolume = 1f;

    private static float musicVolume = 1f;

    public static void delete() {
        for (int buffer: buffers) {
            AL10.alDeleteBuffers(buffer);
        }
        ALC10.alcCloseDevice(device);
    }

    public static Source getDefaultSource() {
        return defaultSource;
    }

    public static String getDeviceName() {
        return ALC10.alcGetString(device, ALC_CAPTURE_DEVICE_SPECIFIER);
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static void init() {
        device = ALC10.alcOpenDevice((ByteBuffer) null);
        if (device != 0) {
            alcCaps = ALC.createCapabilities(device);
            context = ALC10.alcCreateContext(device, (IntBuffer) null);
            if (context != 0) {
                if (!ALC10.alcMakeContextCurrent(context)) {
                    System.err.println("ALC make context failed");
                }
                AL.createCapabilities(alcCaps);
            }
        }

        for (SoundType type: SoundType.values()) {
            loadSound(type);
        }

        defaultSource = new Source();
    }

    protected static Sound loadSound(SoundType type) {
        if (Resources.sounds.containsKey(type)) {
            return Resources.sounds.get(type);
        }
        String name = type.toString().toLowerCase();
        String fullpath = "/sounds/" + name + ".wav";
        int buffer = -1;
        AudioInputStream ais = null;
        try {
            InputStream is = Loader.class.getResourceAsStream(fullpath);
            InputStream bis = new BufferedInputStream(is);
            ais = AudioSystem.getAudioInputStream(bis);
            // ais = AudioSystem.getAudioInputStream(new File(fullpath));
            byte[] bytes = new byte[1 * 1024 * 1024];
            int bytesRead = ais.read(bytes, 0, bytes.length);
            ByteBuffer data = BufferUtils.createByteBuffer(bytesRead);
            for (int i = 0; i < bytesRead; i++) {
                data.put(bytes[i]);
            }
            data.flip();
            buffer = AL10.alGenBuffers();
            buffers.add(buffer);
            // System.out.println(ais.getFormat());
            AL10.alBufferData(buffer, AL10.AL_FORMAT_MONO16, data, (int) ais.getFormat().getSampleRate());
            ais.close();
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();

        }
        Sound sound = new Sound(type, buffer);
        Resources.sounds.put(type, sound);

        return sound;
    }

    public static void playBackgroundMusic() {
         new Source().play(SoundType.BACKGROUND_MUSIC);
    }

    public static void setListenerPosition(Vec3 position) {
        alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
    }

    public static void setMusicVolume(float gain) {
        musicVolume = gain;
    }

    public static void setSoundVolume(float gain) {
        soundVolume = gain;
    }

    public static void playSoundAt(SoundType type, Vec3 position) {
        defaultSource.setPosition(position);
        defaultSource.play(type);
    }

}
