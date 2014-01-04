package com.pressx.draw;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Animator {
	protected float animationSpeed; //The speed at which the animation animates.
	protected boolean looping; //Indicates whether the animation loops or not.
	
	/* Animation */
	protected String animatorName;
	private String animation_indicator;
	private Animation current_animation;
	private HashMap<String, Animation> animations = new HashMap<String, Animation>(); //A list of animations.
	
	/* Sprite */
	protected Sprite sprite;
	protected int srcWidth, srcHeight; //The size of the sprite to draw.
	
	/* Constructor */
	/**
	 * Creates an Animator object which helps control and manage animations of other objects.
	 * 
	 * @param sprite the sprites that are used for animation
	 * @param srcWidth the width of the sprites to cut out
	 * @param srcHeight the height of the sprites to cut out
	 */
	public Animator(String name, Sprite sprite, int srcWidth, int srcHeight)
	{
		this.animatorName = name;
		this.sprite = sprite;
		this.srcWidth = srcWidth;
		this.srcHeight = srcHeight;
	}//END Animator
	
	public Animator(String name, String sprite){
		this.animatorName = name;
		this.sprite = new Sprite(new Texture(sprite));
	}
	
	public void playAnimation(String animationID, float animationSpeed, boolean loops){
		this.current_animation = this.animations.get(animationID);		
		this.current_animation.changeSpeed(animationSpeed);
		if(!loops){
			this.current_animation.reset_animation();
		}
		
		this.animation_indicator = animationID;
		this.looping = loops;
	}
	
	/* Animation */
	private class Animation
	{
		/* Frames */
		private int[] animationFrames; //Number of frames.
		private int[][] frameDimensions; //Used for defining the dimensions for each frame.
		private int startX, startY; //Where the animation starts.
		private int curFrame = 0; //The current frame in the animation.
		protected boolean isDone = false; //Indicates whether or not the animation is looping or not.
		private float animationSpeed = 10;
		
		private float animationIndicator = 0; //Indicates when to go to the next frame.
		
		
		/* Looping */
		
		
		/* Constructor */
		public Animation(int startX, int startY, int numFrames, float animationSpeed, boolean loops)
		{
			this.startX = startX;
			this.startY = startY;
			
			this.animationFrames = new int[numFrames];
			for(int i = 0; i < this.numFrames(); i++)
			{
				this.animationFrames[i] = i;
			}//rof
			
			frameDimensions = new int[numFrames][4];
			
		}//END Animation
		

		public Animation(int startX, int startY, float animationSpeed, boolean loops, int... frames)
		{
			this.startX = startX;
			this.startY = startY;
			
			this.animationFrames = frames;
			
			
		}//END Animation
		
		public Animation(int[][] dimensions, int[] frameOrder){
			this.frameDimensions = dimensions;
			this.animationFrames = frameOrder;
		}
		
		/* Animation */
		
		private int numFrames()
		{
			return this.animationFrames.length;
		}//END numFrames
		
		public int getFrameX()
		{
			return this.startX + this.animationFrames[this.curFrame];
		}//END getFrameX
		
		public int getFrameY()
		{
			return this.startY;
		}//END getFrameY
		
		public void reset_animation()
		{
			this.curFrame = 0;
			this.isDone = false;
		}//END reset_animation
		
		private void nextFrame()
		{
			this.curFrame++;
			
			if(this.curFrame >= this.numFrames())
			{
				if(looping)
				{
					this.curFrame = 0;
				}//fi
				else
				{
					this.curFrame--;
					this.isDone = true;
				}//esle
			}//fi
		}//END nextFrame
		
		public int getCurrentFrame()
		{
			return this.curFrame;
		}//END getCurrentFrame
		
		public void setCurrentFrame(int frame)
		{
			this.curFrame = frame;
		}//END setCurrentFrame
		
		public boolean isDone()
		{
			return this.isDone;
		}//END isDone
		
		/* Update */
		public void update(float delta)
		{
			this.animationIndicator += delta;
			while(this.animationIndicator >= 1/this.animationSpeed)
			{
				this.animationIndicator -= 1/this.animationSpeed;
				this.nextFrame();
			}//elihw
		}//END update
		
		public void changeSpeed(float speed)
		{
			this.animationSpeed = speed;
		}
	}//END class Animation
	
	/**
	 * Adds an animations to a list of animations.
	 * 
	 * @param startX the column the starting sprite is on
	 * @param startY the row the sprites are on
	 * @param numFrames the number of frames total
	 * @param animationSpeed the number of frames per second/interattr
	 * @param loops whether the animation is looping or not
	 */
	public void add_animation(String name, int startX, int startY, int numFrames, float animationSpeed, boolean loops)
	{
		this.animations.put(name, new Animation(startX, startY, numFrames, animationSpeed, loops));
	}//END add_animation
	
	/**
	 * Adds an animations to a list of animations.
	 * 
	 * @param startX the column the starting sprite is on
	 * @param startY the row the sprites are on
	 * @param animationSpeed the number of frames per second/interattr
	 * @param loops whether the animation is looping or not
	 * @param frames the frames to animate
	 */
	public void add_animation(String name, int startX, int startY, float animationSpeed, boolean loops, int... frames)
	{
		this.animations.put(name, new Animation(startX, startY, animationSpeed, loops, frames));
	}//END add_animation
	
	public void add_animation(String name, int[][] frames, int[] frameOrder){
		this.animations.put(name, new Animation(frames, frameOrder));
	}
	
	public void change_animationSpeed(int animation_id, float animationSpeed)
	{
		Animation animation = this.animations.get(animation_id);
		animation.changeSpeed(animationSpeed);
	}//END add_animation
	
	/**
	 * Sets the animation to animate.
	 * 
	 * @param animationID the ID of the animation.
	 */
	public void set_animation(String id, boolean loops)
	{
		if(this.current_animation != null)
		{
			this.current_animation.reset_animation();
		}//fi
		
		this.animation_indicator = id;
		this.current_animation = this.animations.get(id);
		this.animationSpeed = 10;
		this.looping = loops;
	}//END set_animation
	
	/**
	 * Returns the current animation ID being used.
	 * 
	 * @return the ID of the animation.
	 */
	public String get_currentAnimation()
	{
		return this.animation_indicator;
	}//END get_currentAnimation
	
	/**
	 * @return the current frame of the current animation
	 */
	public int getCurrentFrame()
	{
		return this.current_animation.getCurrentFrame();
	}//END getCurrentFrame
	
	/**
	 * Sets the current frame to the indicated attrue.
	 * 
	 * @param frame the frame number
	 */
	public void setCurrentFrame(int frame)
	{
		this.current_animation.setCurrentFrame(frame);
	}//END setCurrentFrame
	
	/**
	 * @return whether the animation is done or not
	 */
	public boolean isDone()
	{
		return this.current_animation.isDone();
	}//END isDone
	
	/* Update */
	/**
	 * Updates the current animation and perhaps what it is displaying.
	 * 
	 * @param delta a value that helps determine how to animate.
	 */
	public void update(float delta)
	{
		if(this.current_animation != null)
		{
			//Update Animation
			this.current_animation.update(delta);
			//Update Sprite
			this.sprite.setRegion(this.current_animation.getFrameX() * this.srcWidth, this.current_animation.getFrameY() * this.srcHeight, this.srcWidth, this.srcHeight);
		}//fi
		else
		{
			//Set to first frame.
			this.sprite.setBounds(0, 0, this.srcWidth, this.srcHeight);
		}//esle
	}//END update
	
	public void update(float delta, boolean useCustomSize){
		//To add after int->string ID system is in.
		if(useCustomSize){
			if(this.current_animation != null){
				this.current_animation.update(delta);				
			}
			this.setSpriteBounds(current_animation.frameDimensions[current_animation.getFrameX()]);
		}
	}
	
	private void setSpriteBounds(int[] dimensions){
		this.sprite.setRegion(dimensions[0], dimensions[1], dimensions[2], dimensions[3]);
	}
}