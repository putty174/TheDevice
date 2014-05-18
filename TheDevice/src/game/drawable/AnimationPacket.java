package game.drawable;

import java.util.HashMap;

public class AnimationPacket {
	private HashMap<String, Animation> animations = new HashMap<String, Animation>();
	
	/* Animation Management */
	/**
	 * Adds an animation to the animation packet.
	 * @param animationName the identifier for the animation.
	 * @param frames the frames of the animation.
	 * @param animationOrder the order the frames are played.
	 */
	public void add_animation(String animationName, int[][] frames, int[] animationOrder) {
		this.animations.put(animationName, new Animation(frames, animationOrder));
	}//END create_animation
	
	/**
	 * Retrieves an animation given the name.
	 * 
	 * @param animationName the identifier corresponding to the animation. 
	 * @return an animation object.
	 */
	public Animation get_animation(String animationName) {
		return this.animations.get(animationName);
	}//END get_animation
	
	/**
	 * Tests whether or not the specified animation is in stored or not.
	 * 
	 * @param animation the specified animation to test.
	 * @return true if the animation is stored in this packet, otherwise false.
	 */
	public boolean contains(String animation) {
		return this.animations.containsKey(animation);
	}//END contains
}//END class AnimationPacket
