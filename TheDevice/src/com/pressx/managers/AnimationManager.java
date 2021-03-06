package com.pressx.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimationManager {
	
	Sprite activeSprite;
	HashMap<String, Animation> animations = new HashMap<String, Animation>();
	String texturesAccessor, currentName, endCondition, stdInterrupt;
	Animation currentAnimation;
	
	public AnimationManager(String accessor){
		texturesAccessor = accessor;
		currentName = "None";
	}
	
	public void setSprite(Sprite spr){
		this.activeSprite = spr;
	}
	
	public void setEndCondition(String end){
		endCondition = end;
	}
	
	public void setStdCondition(String end){
		stdInterrupt = end;
	}
	
	public String getCurrentAnimation(){
		if(currentAnimation == null){
			return "None";
		}
		return currentAnimation.name;
	}
	
	public void clearSprite(){
		this.activeSprite = null;
	}
	
	public void createAnimation(int[][] context, int[] frameOrder, String name, boolean willLoop){
		animations.put(name, new Animation(context, frameOrder, name));
		animations.get(name).isLooping = willLoop;
		currentAnimation = animations.get(name);
	}
	
	public boolean isTextureLoaded(){
		return activeSprite == null;
	}
	
	public Sprite update(){
		return currentAnimation.update();
	}
	
	public void changeAnimation(String str){
		if(this.getCurrentAnimation().equals(str)){
			return;
		}
		else{
			if(animations.containsKey(str)){
				if(currentAnimation != null){
					currentAnimation.interrupt(str);
				}
				currentAnimation = animations.get(str);	
				currentName = str;
				currentAnimation.animSpeed = 60;
			}	
		}
	}
	
	public int getCurrentFrame(){
		return currentAnimation.currentFrame;
	}
	
	public int getMaxFrame(){
		return currentAnimation.frameOrder.length - 1;
	}
	
	public boolean isDone(){
		return currentAnimation.currentFrame == currentAnimation.frameOrder.length - 1; 
	}
	
	public void changeAnimation(String str, float animSpeed, boolean loops){
		changeAnimation(str);
		currentAnimation.animSpeed = animSpeed;
		currentAnimation.isLooping = loops;
	}
	
	public AnimationManager copy(){
		AnimationManager m = new AnimationManager(texturesAccessor);
		m.activeSprite = this.activeSprite;
		m.animations = new HashMap<String, Animation>();
		for(String str : animations.keySet()){
			m.animations.put(str, animations.get(str).copy());
		}
		return m;
	}
	
	private class Animation {
		
		private boolean isLooping = true;
		private int[][] context;
		private int[] frameOrder;
		private int frameX, frameY, width, height, currentFrame;
		private float timer, animSpeed;
		private String name;
		
		private Animation(int[][] context, int[] frameOrder, String name){
			
			this.context = context;
			this.frameOrder = frameOrder;
			this.name = name;
			frameX = context[0][0];
			frameY = context[0][1];
			width = context[0][2] - context[0][0];
			height = context[0][3] - context[0][1];
			animSpeed = 20;
			
		}
		
		private Animation copy(){
			Animation anim = new Animation(new int[context.length][4], new int[frameOrder.length], this.name);
			for(int i = 0; i < context.length; i ++){
				for(int j = 0; j < 4; j ++){
					anim.context[i][j] = context[i][j];
				}
			}
			for(int i = 0; i < frameOrder.length; i ++){
				anim.frameOrder[i] = frameOrder[i];
			}
			anim.frameX = this.frameX;
			anim.frameY = this.frameY;
			anim.width = this.width;
			anim.height = this.height;
			anim.animSpeed = this.animSpeed;
			anim.clearAnimation();
			return anim;
		}
		
		private void clearAnimation(){
			frameX = context[0][0];
			frameY = context[0][1];
			width = context[0][2] - context[0][0];
			height = context[0][3] - context[0][1];
			currentFrame = 0;
		}
		
		protected Sprite update(){
			timer += Gdx.graphics.getDeltaTime();
			Sprite tempSprite = new Sprite(activeSprite);		
			tempSprite.setRegion(frameX, frameY, width, height);
			tempSprite.setBounds(0, 0, width, height);
			if(timer > ((float)frameOrder.length) / animSpeed){
				timer = 0;
				currentFrame ++;
				if(currentFrame == frameOrder.length){
					
					if(isLooping){
						currentFrame = 0;
					}
					else{
						if(name.equals(endCondition)){
							return null;
						}
						else{
							interrupt(stdInterrupt);
						}
					}
					
				}
				else{
					frameX = context[frameOrder[currentFrame]][0];
					frameY = context[frameOrder[currentFrame]][1];
					width = context[frameOrder[currentFrame]][2] - context[frameOrder[currentFrame]][0];
					height = context[frameOrder[currentFrame]][3] - context[frameOrder[currentFrame]][1];
				}
				
			}
			//System.out.println(activeSprite == null);
			return tempSprite;
		}
		
		protected void interrupt(String str){
			this.clearAnimation();
			currentName = str;
			currentAnimation = animations.get(str);
		}
	}
	
}
