package alarmaInteligente;

import java.time.LocalTime;

/**
 * Clase que representa un perfil de sonido para alarmas.
 */
public class SoundProfile {
    private String soundType;
    private int volume;
    private boolean vibrationEnabled;

    public static final String SOUND_DEFAULT = "default";
    public static final String SOUND_NATURE = "nature";
    public static final String SOUND_MUSIC = "music";
    public static final String SOUND_GENTLE = "gentle";

    public SoundProfile() {
        this.soundType = SOUND_DEFAULT;
        this.volume = 50;
        this.vibrationEnabled = true;
    }

    public SoundProfile(String soundType, int volume, boolean vibrationEnabled) {
        setSoundType(soundType);
        setVolume(volume);
        this.vibrationEnabled = vibrationEnabled;
    }

    public String getSoundType() {
        return soundType;
    }

    public void setSoundType(String soundType) {
        if (soundType == null || soundType.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de sonido no puede estar vacío");
        }
        this.soundType = soundType.toLowerCase();
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("El volumen debe estar entre 0 y 100");
        }
        this.volume = volume;
    }

    public boolean isVibrationEnabled() {
        return vibrationEnabled;
    }

    public void setVibration(boolean enabled) {
        this.vibrationEnabled = enabled;
    }

    @Override
    public String toString() {
        return String.format("SoundProfile{tipo='%s', volumen=%d%%, vibración=%s}",
                soundType, volume, vibrationEnabled ? "sí" : "no");
    }
}
