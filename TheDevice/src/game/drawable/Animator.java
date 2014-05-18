package game.drawable;

public class Animator {
	/* Directions */
	private static final String[] DIRECTIONS = {"Left", "UpLeft", "Up", "UpRight", "Right", "DownRight", "Down", "DownLeft"};
	
	/* Drawable */
	private Drawable_Sprite sprite;
	
	/* Animation */
	private AnimationPacket animations;
	
	/* Current Animation */
	private Animation current_animation;
	private String current_animation_name;
	private float current_frame = 0;
	private float animation_speed = 0;
	private boolean looping = false;
	private boolean done = false;

	/* Construction */
	/**
	 * Constructs an animator object.
	 * 
	 * @param animations the animations to be used by the animator.
	 * @param sprite the sprite linked to the animator.
	 */
	public Animator(AnimationPacket animations, Drawable_Sprite sprite) {
		this.animations = animations;
		this.sprite = sprite;
	}//END Animator
	
	/* Drawable */
	/**
	 * Sets the drawable of this animator and deletes the old drawable.
	 * 
	 * @param drawable the new drawable.
	 */
	public void set_drawable(Drawable_Sprite drawable) {
		this.sprite.kill();
		this.sprite = drawable;
	}//END set_drawable
	
	/**
	 * Returns the drawable currently in use.
	 * 
	 * @return a drawable object.
	 */
	public Drawable_Sprite get_drawable() {
		return this.sprite;
	}//END set_drawable
	
	
	
	/* Animation Management */
	/**
	 * Toggles the animator to play the specified animation.
	 * 
	 * @param animation the name of the animation to play.
	 * @param start_frame the frame to start on.
	 * @param play_speed the speed (frames/second) to play the animation.
	 * @param loops whether or not the animation loops or not.
	 */
	public void play(String animation, float start_frame, float play_speed, boolean loops) {
		this.switch_animation(animation);
		this.current_frame = start_frame;
		this.animation_speed = play_speed;
		this.looping = loops;
		this.done = false;
	}//END play
	
	/**
	 * Toggles the animator to play the specified animation.
	 * 
	 * @param animation the name of the animation to play.
	 * @param direction a indication of which of the 8 directions to animate for.
	 * @param start_frame the frame to start on.
	 * @param play_speed the speed (frames/second) to play the animation.
	 * @param loops whether or not the animation loops or not.
	 */
	public void play(String animation, int direction, float start_frame, float play_speed, boolean loops) {
		this.switch_animation(animation, direction);
		this.current_frame = start_frame;
		this.animation_speed = play_speed;
		this.looping = loops;
		this.done = false;
	}//END play
	
	/**
	 * Switches the animation to the specified animation without affecting any of the other values.
	 * Assumes that the animations are the same in length.
	 * 
	 * @param animation the name of the animation to play.
	 */
	public void switch_animation(String animation) {
		this.current_animation = this.animations.get_animation(animation);
		this.current_animation_name = animation;
	}//END switch_animation
	
	/**
	 * Switches the animation to the specified animation without affecting any of the other values.
	 * 
	 * @param animation the name of the animation to play.
	 * @param direction a indication of which of the 8 directions to animate for.
	 */
	public void switch_animation(String animation, int direction) {
		this.current_animation = this.animations.get_animation(animation+DIRECTIONS[direction]);
		this.current_animation_name = animation;
	}//END switch_animation
	
	/**
	 * Returns an array of 4 (x1,y1,x2,y2) representing the current frame of this animation.
	 * 
	 * @return the current frame of this animation.
	 */
	private int[] get_frame() {
		int new_frame = this.current_animation.animationOrder[(int)this.current_frame];
		return this.current_animation.frames[new_frame];
	}//END get_frame
	
	/**
	 * Returns whether or not the animation is done.
	 * 
	 * @return true if the animation is done, otherwise false.
	 */
	public boolean is_done() {
		return this.done;
	}//END is_ done
	
	/**
	 * Returns the the name of the current animation.
	 * 
	 * @return a string representing the current animation.
	 */
	public String get_currentAnimation() {
		return this.current_animation_name;
	}//END get_currentAnimation
	
	/**
	 * Test whether the specified animation is the animation playing.
	 * 
	 * @param animation the name of the animation to test.
	 * @return true if the animation is the current animation, otherwise false.
	 */
	public boolean isAnimation(String animation) {
		if(this.current_animation_name == null) return this.current_animation_name == animation;
		return this.current_animation_name.equals(animation);
	}//END isAnimation
	
	/**
	 * Test whether or not the specified animation is in the animator.
	 * 
	 * @param animation the animation is question.
	 * @return true if the animator has the specified animation, otherwise false.
	 */
	public boolean containsAnimation(String animation) {
		return this.animations.contains(animation);
	}//END containsAnimation
	
	/* Sprite */
	private void update_sprite() {
		int frame[] = this.get_frame();
		this.sprite.setSrcRect(frame[0], frame[1], frame[2] - frame[0], frame[3] - frame[1]);
	}//END update_sprite
	
	/* Update */
	/**
	 * Update the current animation.
	 * 
	 * @param dt the time that has passed.
	 */
	public void update(float dt) {
		if(this.current_animation != null && !this.done) {
			this.current_frame = this.current_frame + dt * this.animation_speed;
			if(this.current_frame >= this.current_animation.animationOrder.length || this.current_frame <= -1) {
				if(this.looping) {
					this.current_frame = 0;
				}//fi
				else {
					this.done = true;
					return;
				}//esle
			}//fi
			this.update_sprite();
		}//fi
	}//END update
}//END class Animator
