package game.sounds;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class Jukebox {	
	/* Dem Beats */
	private Music music = null;
	private HashMap<String, String> jukebox = new HashMap<String, String>();
	
	/* Sounds */
	private HashMap<String, Sound_Packet> megaphone = new HashMap<String, Sound_Packet>();
	
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
	public void load_music(String file_name) {
		/* Load Content */
		FileHandle handle = Gdx.files.internal(file_name);
		String content[] = handle.readString().split("\\r?\\n");
		
		/* Extract Data */
		for(String i : content) {
			if(!i.contains("#")) {
				String tokens[] = i.split(" ");
				this.jukebox.put(tokens[0], tokens[1]);
			}//fi
		}//rof
		
	}//END load_music
	
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
	 * Sets the volume of the music currently playing.
	 * 
	 * @param volume the volume [0,1] where 0 is silent and 1 is max volume.
	 */
	public void music_volume(float volume) {
		if(this.music != null) {
			this.music.setVolume(volume);
		}//fi
		else {
			System.err.println("Trying to set volume of music that does not exist.");
		}//esle
	}//END music_volume
	
	/**
	 * Pauses the music being played.
	 */
	public void music_pause() {
		this.music.pause();
	}//END music_pause
	
	/**
	 * Returns whether or not the music is playing or not.
	 * 
	 * @return whether or not the music is playing.
	 */
	public boolean music_playing() {
		return this.music.isPlaying();
	}//END music_playing
	
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
