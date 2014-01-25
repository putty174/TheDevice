package main;

import java.io.BufferedReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationManager {
	
	String managerName;
	Sprite activeSprite;
	Animation[] animations;
	Animation currentAnimation;
	
	public AnimationManager(FileHandle f){
		try{
			BufferedReader reader = new BufferedReader(f.reader());
			
			managerName = reader.readLine();
			activeSprite = new Sprite(new Texture(Gdx.files.internal(reader.readLine())));
			int numAnimations = Integer.parseInt(reader.readLine());
			animations = new Animation[numAnimations];
			int[][] store;
			int[] frameOrder;
			for(int i = 0; i < numAnimations; i ++){
				String name = reader.readLine();
				int numFrames = Integer.parseInt(reader.readLine());
				store = new int[4][numFrames];
				for(int j = 0; j < numFrames; j++){				
					String str = reader.readLine();
					String[] tempStrings = str.split(" ");
					for(int k = 0; k < 4; k ++){
						store[k][j] = Integer.parseInt(tempStrings[k]);
					}
				}
				int numFramesTotal = Integer.parseInt(reader.readLine());
				frameOrder = new int[numFramesTotal];
				String[] str = reader.readLine().split(" ");
				for(int j = 0; j < numFramesTotal; j ++){
					frameOrder[j] = Integer.parseInt(str[j]);
				}
				animations[i] = new Animation(store, frameOrder, name);
				animations[i].setLooping(Boolean.parseBoolean(reader.readLine()));
			}
			currentAnimation = animations[2];
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Sprite update(){
		return currentAnimation.update();
	}
	
	public void changeAnimation(String str){
		if(currentAnimation.getName().equals(str)){
			return;
		}
		else{
			for(Animation anim : animations){
				if(anim.getName().equals(str)){
					currentAnimation.interrupt(anim);
				}
			}
		}
	}
	
	private class Animation {
		
		private String name;
		private boolean isLooping = true;
		private int[][] context;
		private int[] frameOrder;
		private int frameX, frameY, width, height, currentFrame;
		private float timer;
		
		private Animation(int[][] context, int[] frameOrder, String name){
			this.context = context;
			this.frameOrder = frameOrder;
			this.name = name;
			frameX = context[0][0];
			frameY = context[1][0];
			width = context[2][0] - context[0][0];
			height = context[3][0] - context[1][0];
		}
		
		private void clearAnimation(){
			frameX = context[0][0];
			frameY = context[1][0];
			width = context[2][0] - context[0][0];
			height = context[3][0] - context[1][0];
			currentFrame = 0;
		}
		
		protected Sprite update(){
			timer += Gdx.graphics.getDeltaTime();
			activeSprite.setBounds(0, 0, 256, 256);
			activeSprite.setRegion(frameX, frameY, width, height);
			if(timer > ((float)frameOrder.length) / 120){
				timer = 0;
				currentFrame ++;
				if(currentFrame == frameOrder.length){
					
					if(isLooping){
						currentFrame = 0;
					}
					else{
						
						if(this.name.equals("Death")){
							System.out.println(this.name + " " + frameOrder.length);
							return null;
						}
					}
				}
				frameX = context[0][frameOrder[currentFrame]];
				frameY = context[1][frameOrder[currentFrame]];
				width = context[2][frameOrder[currentFrame]] - context[0][frameOrder[currentFrame]];
				height = context[3][frameOrder[currentFrame]] - context[1][frameOrder[currentFrame]];
			}
			//System.out.println(activeSprite == null);
			return activeSprite;
		}
		
		protected void interrupt(Animation anim){
			clearAnimation();
			currentAnimation = anim;
		}
		
		protected void setLooping(boolean bool){
			isLooping = bool;
		}
		
		public String getName(){
			return this.name;
		}
		
	}
	
}
