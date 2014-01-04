package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.pressx.managers.Graphics;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;

public class LoadingScreen extends BaseState{
	private float complete;
	private Sprite play;
	private Sprite bg;
	private boolean done = false;
	
	public LoadingScreen()	{
		super();
		bg = new Sprite(Textures.getArtAsset("bg"));
		play = new Sprite(Textures.getArtAsset("play"));
	}
	
	public void create() {
	}
	
	public void update() {
		if(Textures.a_manager.update() && Sounds.s_manager.update())
		{
			done = true;
			complete = 1;
			if(Gdx.input.isTouched())
				if(play.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
					TheDevice.moveToGame();
		}
		else
			complete = Interpolation.linear.apply(complete, (Textures.a_manager.getProgress()+Sounds.s_manager.getProgress())/2, 0.1f);
	}
	
	public void render(SpriteBatch batch) {
		Graphics.draw(Graphics.TYPES.BACKGROUND, bg, 0, 0, 1, 1);
		String progress = Integer.toString(Math.round(complete * 100)) + "%";
		Graphics.write(progress, ((Graphics.screenWidth - Graphics.font.getBounds(progress).width) / 2) / Graphics.screenWidth, 2/3f);
		if(done)
			Graphics.draw(Graphics.TYPES.BUTTON, play, ((Graphics.screenWidth-play.getBoundingRectangle().width)/2f)/Graphics.screenWidth, ((Graphics.screenHeight-play.getBoundingRectangle().height)/3f)/Graphics.screenHeight, 0.15f, 0.25f);
	}
	
	public void dispose() {
	}
}