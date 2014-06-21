package game.sounds;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class Jukebox {	
	/* Dem Beats */
	private Music music = null;
	private HashMap<String, String> jukebox = new HashMap<String, String>();
	
	/* Sounds */
	private HashMap<String, Sound_Packet> megaphone = new HashMap<String, Sound_Packet>();
	
	private class Sound_Packet {
		private Sound sound = null;
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
		/**
		 * Plays the sound.
		 * 
		 * @param volume the volume [0,1] of the sound, where 0 is silent and 1 is max.
		 * @param pitch the pitch where 1 is default pitch, >1 is faster, and <1 is slower.
		 * @param pan the pan where the -1 is full left, 0 is center, and 1 is full right.
		 * @return the id of the sound being played.
		 */
		public long play(float volume, float pitch, float pan) {
			long sound_id;
			if (this.sound == null) {
				this.load();
				System.err.println("Playing sound, \"" + this.file_path +"\", without loading it.");
			}//fi
			sound_id = this.sound.play(volume, pitch, pan);
			return sound_id;
		}//END play
		
		/**
		 * Stops all the sounds of this type from being played.
		 */
		public void stop_all() {
			this.sound.stop();
		}//END stop_all
		
		/**
		 * Stops a specific sound from being played.
		 * 
		 * @param sound_pointer the pointer of the sound to modify.
		 */
		public void stop(long sound_pointer) {
			this.sound.stop(sound_pointer);
		}//END stop
		
		/**
		 * Pauses the specific sound.
		 * 
		 * @param sound_pointer the pointer of the sound to modify.
		 */
		public void pause(long sound_pointer) {
			this.sound.pause(sound_pointer);
		}//END pause
		
		/**
		 * Resumes the specific paused sound.
		 * 
		 * @param sound_pointer the pointer of the sound to modify.
		 */
		public void resume(long sound_pointer) {
			this.sound.resume(sound_pointer);
		}//END resume
		
		/**
		 * Sets the volume of a specific sound.
		 * 
		 * @param sound_pointer the pointer of the sound to modify.
		 * @param volume the volume [0,1] of the sound, where 0 is silent and 1 is max.
		 */
		public void volume(long sound_pointer, float volume) {
			this.sound.setVolume(sound_pointer, volume);
		}//END volume
		
		/**
		 * Sets the volume of a specific sound.
		 * 
		 * @param sound_pointer the pointer of the sound to modify.
		 * @param pitch the pitch where 1 is default pitch, >1 is faster, and <1 is slower.
		 */
		public void pitch(long sound_pointer, float pitch) {
			this.sound.setPitch(sound_pointer, pitch);
		}//END pitch
		
		/**
		 * Sets the volume of a specific sound.
		 * 
		 * @param sound_pointer the pointer of the sound to modify.
		 * @param pan the pan where the -1 is full left, 0 is center, and 1 is full right.
		 */
		public void pan(long sound_pointer, float pan) {
			this.sound.setVolume(sound_pointer, pan);
		}//END pan
		
		/**
		 * Loads the sound.
		 */
		public void load() {
			this.sound = Gdx.audio.newSound(Gdx.files.internal(this.file_path));
		}//END load
		
		/**
		 * Unloads and disposes of the sound.
		 */
		public void unload() {
			this.sound.dispose();
		}//END unload
		
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
	
	public void load_sound(String file_name) {
		/* Load Content */
		FileHandle handle = Gdx.files.internal(file_name);
		String content[] = handle.readString().split("\\r?\\n");
		
		/* Extract Data */
		for(String i : content) {
			if(!i.contains("#")) {
				String tokens[] = i.split(" ");
				this.megaphone.put(tokens[0], new Sound_Packet(tokens[1]));
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
			this.music.dispose();
			this.music = null;
		}//fi
	}//END music_unload
	
	/* Sound Management */
	/**
	 * Unloads all of the sounds that are currently loaded.
	 */
	public void sound_unload() {
		Iterator<Sound_Packet> iter = this.megaphone.values().iterator();
		while(iter.hasNext()) {
			iter.next().unload();
		}//elihw
	}//END sound_unload
	
	/**
	 * Loads the sound to be played later.
	 * 
	 * @param sound_id the sound to be played.
	 */
	public void sound_load(String sound_id) {
		this.megaphone.get(sound_id).load();
	}//END sound_load
	
	/**
	 * Plays the sound.
	 * 
	 * @param sound_id the sound to be played.
	 * @param volume the volume [0,1] of the sound, where 0 is silent and 1 is max.
	 * @param pitch the pitch where 1 is default pitch, >1 is faster, and <1 is slower.
	 * @param pan the pan where the -1 is full left, 0 is center, and 1 is full right.
	 * @return the id of the sound being played.
	 */
	public long sound_play(String sound_id, float volume, float pitch, float pan) {
		long sound_pointer;
		sound_pointer = this.megaphone.get(sound_id).play(volume, pitch, pan);
		
		return sound_pointer;
	}//END sound_play
	
	/**
	 * Pauses the indicated sound.
	 * 
	 * @param sound_id the name of the sound.
	 * @param sound_pointer the pointer of the sound to pause.
	 */
	public void sound_pause(String sound_id, long sound_pointer) {
		this.megaphone.get(sound_id).pause(sound_pointer);
	}//END sound_pause
	
	/**
	 * Resumes the indicated sound.
	 * 
	 * @param sound_id the name of the sound.
	 * @param sound_pointer the pointer of the sound to resume.
	 */
	public void sound_resume(String sound_id, long sound_pointer) {
		this.megaphone.get(sound_id).resume(sound_pointer);
	}//END sound_pause
	
	/**
	 * Sets the volume of the indicated sound.
	 * 
	 * @param sound_id the name of the sound.
	 * @param sound_pointer the pointer of the sound to modify.
	 * @param volume the volume of the sound.
	 */
	public void sound_volume(String sound_id, long sound_pointer, float volume) {
		this.megaphone.get(sound_id).volume(sound_pointer, volume);
	}//END sound_volume
	
	/**
	 * Sets the pan of the indicated sound.
	 * 
	 * @param sound_id the name of the sound.
	 * @param sound_pointer the pointer of the sound to modify.
	 * @param pan the pan of the sound.
	 */
	public void sound_pan(String sound_id, long sound_pointer, float pan) {
		this.megaphone.get(sound_id).pan(sound_pointer, pan);
	}//END sound_pan
	
	/**
	 * Sets the pitch of the indicated sound.
	 * 
	 * @param sound_id the name of the sound.
	 * @param sound_pointer the pointer of the sound to modify.
	 * @param pitch the pitch of the sound.
	 */
	public void sound_pitch(String sound_id, long sound_pointer, float pitch) {
		this.megaphone.get(sound_id).pitch(sound_pointer, pitch);
	}//END sound_pitch
	
	/**
	 * Stops the indicated sound from planing.
	 * 
	 * @param sound_id the name of the sound.
	 * @param sound_pointer the pointer of the sound to modify.
	 */
	public void sound_stop(String sound_id, long sound_pointer) {
		this.megaphone.get(sound_id).stop(sound_pointer);
	}//END sound_stop
	
	/**
	 * Stops all sounds of the specified name.
	 * 
	 * @param sound_id the name of the sound.
	 */
	public void sound_stopAll(String sound_id) {
		this.megaphone.get(sound_id).stop_all();
	}//END sound_stopAll
}//END class Jukebox