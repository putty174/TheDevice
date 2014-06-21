package game.drawable;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Manager_Animation {
	private HashMap<String, AnimationPacket> animations = new HashMap<String, AnimationPacket>();
	
	/* Animation Management */
	/**
	 * Returns the animations corresponding to the given identifier.
	 * 
	 * @param animations a string identifying which animations to get.
	 * @return the animations specified.
	 */
	public AnimationPacket get_animations(String animations) {
		return this.animations.get(animations);
	}//END load_animations
	
	/**
	 * Loads all the animations in a file and stores them.
	 * 
	 * @param file_name the file to load data from.
	 */
	public void load_animations(String file_name) {
		/* Load Content */
		FileHandle handle = Gdx.files.internal(file_name);
		String content[] = handle.readString().split("\\r?\\n");
		
		/* Extract Data */
		int i = 0, j = 0;
		while(true) {
			try {
				String animation_group = content[i+j];
				//Add animations group to list of animations if not already in.
				if(!this.animations.containsKey(animation_group)) {
					this.animations.put(animation_group, new AnimationPacket());
				}//fi
				AnimationPacket packet = this.animations.get(animation_group);
				
				//Parse Animations
				//System.out.println(content[i+ j]);
				int num_animations = Integer.parseInt(content[i + j + 1]);
				for(int k = 0; k < num_animations; k++) {	
					String animation_name = content[i + j + k + 2];
					//System.out.println(animation_name);
					//Frames
					int num_frames = Integer.parseInt(content[i + j + k + 3]);
					int frames[][] = new int[num_frames][4];
					//Frame Order
					String order_tokens[] = content[i + j + k + 4].split(" ");
					int frame_order[] = new int[order_tokens.length];
					for(int l = 0; l < order_tokens.length; l++) {
						frame_order[l] = Integer.parseInt(order_tokens[l]);
					}//rof
					//Animation Frames
					for(int l = 0; l < num_frames; l++) {
						String tokens[] = content[i + j + k + l + 5].split(" ");
						for(int m = 0; m < 4; m++) {
							frames[l][m] = Integer.parseInt(tokens[m]);
						}//rof
					}//rof
					packet.add_animation(animation_name, frames, frame_order);
					j += num_frames + 2;
				}//rof
				i += num_animations + 2;
			}//yrt
			catch (IndexOutOfBoundsException e) {
				break;
			}//hctac
		}//elihw
	}//END load_animations
}//END class Manager_Animation
