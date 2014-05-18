package game.drawable;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class Manager_Texture {
	private HashMap<String, Texture_Packet> drawable_map = new HashMap<String, Texture_Packet>();
	
	/* Drawable Management */
	private class Texture_Packet {
		private String file_path;
		private Texture texture = null;
		public boolean preloaded = false;
		
		/* Construction */
		public Texture_Packet(String file_path) {
			this.file_path = file_path;
		}//END Texture_Packet
		
		/* Texture */
		public Texture getTexture() {
			if(!this.preloaded) {
				System.err.println("File, " + this.file_path + ", is being loaded despite not being preloaded.");
			}//fi
			
			if(this.texture == null) {
				this.texture = new Texture(file_path);
			}//fi
			
			return this.texture;
		}
		
		/**
		 * Unloads the texture loaded in this object.
		 */
		public void unload() {
			this.preloaded = false;
			if(this.texture != null) {
				this.texture.dispose();
				this.texture = null;
			}//fi
		}//END unload
	}//END class Texture_Packet
	
	/**
	 * Unloads all textures stored in this manager.
	 */
	public void unload() {
		Iterator<Texture_Packet> iter = this.drawable_map.values().iterator();
		while(iter.hasNext()) {
			iter.next().unload();
		}//elihw
	}//END unload
	
	/**
	 * Loads the ID's of the textures.
	 * 
	 * @param file_name the file containing ID's and the file paths corresponding to the ID's.
	 */
	public void load_ids(String file_name) {
		/* Load Content */
		FileHandle handle = Gdx.files.internal(file_name);
		String content[] = handle.readString().split("\\r?\\n");
		
		/* Extract Data */
		for(String i : content) {
			if(!i.contains("#")) {
				String tokens[] = i.split(" ");
				this.drawable_map.put(tokens[0], new Texture_Packet(tokens[1]));
			}//fi
		}//rof
	}//END load_ids
	
	/**
	 * Given the key, returns the corresponding texture.
	 * @param texture_id the identifier of the texture.
	 * 
	 * @return a texture corresponding to the given key.
	 */
	public Texture get_texture(String texture_id) {
		Texture_Packet packet = this.drawable_map.get(texture_id);
		return packet.getTexture();
	}//END get_texture
	
	/**
	 * Given the key, returns the corresponding texture. Also sets the packet to being preloaded. 
	 * @param texture_id the identifier of the texture.
	 * 
	 * @return a texture corresponding to the given key.
	 */
	public Texture preload_texture(String texture_id) {
		Texture_Packet packet = this.drawable_map.get(texture_id);
		packet.preloaded = true;
		return packet.getTexture();
	}//END get_texture
}//END class Manager_Texture
