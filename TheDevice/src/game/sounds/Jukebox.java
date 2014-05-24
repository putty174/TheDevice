package game.sounds;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Jukebox {	
	/* Dem Beats */
	private Music music = null;
	private HashMap<String, String> jukebox = new HashMap<String, String>();
	
	/* Sounds */
	
	private class Sound_Packet {
		private String file_path;

		
		/* Construction */
		/**
		 * Sets up a sound packet for later loading.
		 * 
		 * @param file_path the path of the file to load.
		 */
		public Sound_Packet(String file_path) {
			this.file_path = file_path;
		}//END Music_Packet
		
		/* Sound */
		
	}//END class Music_Packet
	
	/* Manager Loading */
	
	/* Music Management */
	/**
	 * Loads and prepares play the specified music.  Also disposes of the other music.
	 * 
	 * @param music_name the identifier of the music to be played.
	 */
	public void music_setup(String music_name) {
		this.music_unload();
		
		this.music = Gdx.audio.newMusic(Gdx.files.internal(jukebox.get(music_name)));
	}//END play_music
	
	/**
	 * Plays the music that is currently loaded in.
	 * 
	 * @param volume the volume [0,1] where 0 is silent and 1 is max volume.
	 * @param isLooping whether the track is looping or not.
	 */
	public void music_play(float volume, boolean isLooping) {
		this.music.play();
		this.music.setLooping(isLooping);
		this.music.setVolume(volume);
	}//END music_play
	
	/**
	 * Pauses the music being played.
	 */
	public void music_pause() {
		this.music.pause();
	}//END music_pause
	
	/**
	 * Stops and disposes of the music that is currently being played.
	 */
	public void music_unload() {
		if(this.music != null) {
			this.music.stop();
			this.music.dispose();
			this.music = null;
		}//fi
	}//END music_unload
}//END class Jukebox
