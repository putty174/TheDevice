package com.pressx.objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.pressx.control.GameTimer;
import com.pressx.draw.Animator;
<<<<<<< HEAD
import com.pressx.managers.AnimationManager;
=======
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
>>>>>>> e994d722661d7bf24cb2732f372f0d1b48ec50b0
import com.pressx.managers.Textures;

public class AnimatedObject extends GameObject {
	/* Animation */
	protected Animator animator; //List of animations.
	protected AnimationManager animationManager;
	protected int animation_state; //The current animation that should be playing.
	protected boolean animationSet; //Whether the animation 
	protected GameTimer deathTimer;
	
	protected boolean isDying;
	
	/* Constructor */
	/**
	 * Constructs an animated object for a game.
	 * 
	 *@param objectID the identifier for the object
	 * @param posX the x position of the object in space
	 * @param posY the y position of the object in space
	 * @param mass how massive the object is
	 * @param friction the magnitude of the friction force applied to the object
	 * @param hitWidth the width of the hitbox
	 * @param hitHeight the height of the hitbox
	 * @param hitX the x offset of the hitbox
	 * @param hitY the y offset of the hitbox
	 * @param isSolid whether or not the object can be moved through
	 * @param isTouchable if the object is touchable or not
	 * @param drawWidth the width that the object is drawn at
	 * @param drawWidth the width that the object is drawn at
	 * @param drawHeight the height that the object is drawn at
	 * @param sprites the set of sprites to be used in drawing the object
	 * @param srcWidth the width of a sprite
	 * @param srcHeight the height of a sprite
	 */
<<<<<<< HEAD
	public AnimatedObject(String animatorName, int objectID, float posX, float posY, float mass, float friction, float hitWidth,
			float hitHeight, float hitX, float hitY, boolean isSolid, float touchRadius, boolean isTouchable, float drawWidth, float drawHeight, Texture sprites, int srcWidth, int srcHeight)
	{
		super(objectID, posX, posY, mass, friction, hitWidth, hitHeight, hitX, hitY, isSolid, touchRadius, isTouchable, drawWidth, drawHeight, sprites, srcWidth, srcHeight);
		//this.animationManager = animManager;
=======
	public AnimatedObject(Draw d, Sounds s, Textures t, String animatorName, int objectID, float posX, float posY, float mass, float friction, float hitWidth, float hitHeight, float hitX, float hitY, boolean isSolid, float touchRadius, boolean isTouchable, float drawWidth, float drawHeight, Texture sprites, int srcWidth, int srcHeight)
	{
		super(d, s, t, objectID, posX, posY, mass, friction, hitWidth, hitHeight, hitX, hitY, isSolid, touchRadius, isTouchable, drawWidth, drawHeight, sprites, srcWidth, srcHeight);
		
>>>>>>> e994d722661d7bf24cb2732f372f0d1b48ec50b0
		this.animator = new Animator(animatorName, this.sprite, srcWidth, srcHeight);
		this.deathTimer = new GameTimer(30);
	}//END AnimatedObject
	
	/* Animation */
	public int get_frame()
	{
		if(this.animator == null){
			return this.animationManager.getCurrentFrame();
		}
		return this.animator.getCurrentFrame();
	}//END get_frame
	
	/**
	 * Adds an animations to a list of animations.
	 * 
	 * @param startX the column the starting sprite is on
	 * @param startY the row the sprites are on
	 * @param numFrames the number of frames total
	 * @param animationSpeed the number of frames per second/interval
	 * @param loops whether the animation is looping or not
	 */
	public void add_animation(String name, int startX, int startY, int numFrames, float animationSpeed, boolean loops)
	{
		this.animator.add_animation(name, startX, startY, numFrames, animationSpeed, loops);
	}//END add_animation
	
	/**
	 * Will cancel the current animation and set it the the indicated state.
	 * 
	 * @param animationID the ID of the animation.
	 */
	public void set_animationState(int animationID)
	{
		this.animation_state =  animationID;
		this.animationSet = false;
	}//END set_animation
	
	/**
	 * Sets the animation to animate, if there is no animation currently going on.
	 * 
	 * @param animationID the ID of the animation.
	 */
	public void set_animation(String animationID, boolean loops)
	{
		this.animator.set_animation(animationID, loops);
		this.animationSet = true;
	}//END set_animationStart
	
	/**
	 * Ends the current animation
	 */
	public void end_animation()
	{
		this.animationSet = false;
	}//END end_animation
	
	/* Update */
	@Override
	public void update(float dt, ArrayList<GameObject> objects)
	{
		if(this.animator == null){
			this.sprite = this.animationManager.update();
			if(this.isDying){
				if(this.animationManager.isDone()){
					this.terminate();
				}
				return;
			}
			
			super.update(dt, objects);
		}
		else{
			this.animator.update(dt);
			if(this.isDying){
				if(this.animator.isDone()){
					this.terminate();
				}
				return;
			}
			update_animation();
			super.update(dt, objects);
		}
		
	}//END update
	
	protected void update_animation()
	{
		if(this.animator.isDone())
		{
			this.end_animation();
			this.update_animationState();
		}//fi
	}//END update_animationState
	
	/**
	 * Updates the current animation of the object.
	 */
	protected void update_animationState()
	{
		//Override this function.
	}//END update_animationState
}