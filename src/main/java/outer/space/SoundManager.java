package outer.space;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundManager {
    HashMap<String, Clip> clips;

	private static SoundManager instance = null;

	private SoundManager () {
		clips = new HashMap<String, Clip>();

		Clip clip = loadClip("/sounds/damage.wav");
		clips.put("damage", clip);

        Clip clip2 = loadClip("/sounds/game.wav");
		clips.put("game", clip2);

		Clip clip3 = loadClip("/sounds/collectAmmunition.wav");
		clips.put("collect", clip3);

		Clip clip4 = loadClip("/sounds/start.wav");
		clips.put("btnPress", clip4);

		Clip clip5 = loadClip("/sounds/lightning.wav");
		clips.put("lightning", clip5);

		Clip clip6 = loadClip("/sounds/shootFire.wav");
		clips.put("shootFire", clip6);

		Clip clip7 = loadClip("/sounds/heal.wav");
		clips.put("heal", clip7);

	}

    public static SoundManager getInstance() {
		if (instance == null)
			instance = new SoundManager();
		
		return instance;
	}		


    public Clip loadClip(String resourcePath) {
		Clip clip = null;
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(
				getClass().getResource(resourcePath)
			);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (Exception e) {
			System.out.println("Error loading sound: " + resourcePath + " -> " + e);
		}
		return clip;
	}


	public Clip getClip (String title) {

		return clips.get(title);
	}


    public void playClip(String title, boolean looping) {
        Clip clip = getClip(title);
        if (clip != null) {
            clip.setFramePosition(0);
            if (looping)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.start();
        }
    }

	public void stopClip(String title) {
		Clip clip = getClip(title);
		if (clip != null) {
			clip.stop();
		}
	}

	public void setVolume (String title, float volume) {
		Clip clip = getClip(title);

		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();

		gainControl.setValue(gain);
	}
}
