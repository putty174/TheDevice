package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.facebook.FacebookInterface;
import com.pressx.managers.Graphics;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;

public class MainMenuScreen extends BaseState {
	Sprite bgArt, play, help, post;
	FacebookInterface fb;
	String key;
	boolean touch;

	public MainMenuScreen(FacebookInterface facebook){
		super();
		play = new Sprite(Textures.getArtAsset("main_play"));
		bgArt = new Sprite(Textures.getArtAsset("main_bg"));
		help = new Sprite(Textures.getArtAsset("main_help"));
		post = new Sprite(Textures.getArtAsset("exp"));
		post.setRegion(0, 0, 30, 30);
		fb = facebook;
	}
	
	public void create() {
	}

	public void dispose() {
	}

	public void render(SpriteBatch batch) {
		Graphics.draw(Graphics.TYPES.BACKGROUND, bgArt, 0, 0, 1f, 1f);
		Graphics.draw(Graphics.TYPES.BUTTON, play, 0.61f, 0.06f, 0.15f, 0.25f);
		Graphics.draw(Graphics.TYPES.BUTTON, help, 0.79f, 0.06f, 0.15f, 0.25f);
		Graphics.draw(Graphics.TYPES.EXTRAS, post, 0.1f, 0.1f, 0.1f, 0.1f);
		if(touch)
			Graphics.write("HA HA", 0.1f, 0.1f);
	}
	
	public void update()
	{
		if(Gdx.input.justTouched())
		{
			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();
			if(play.getBoundingRectangle().contains(x, y))
			{
				Sounds.play("buttonl");
				TheDevice.moveToSequence("Intro");
			}
			else if(help.getBoundingRectangle().contains(x,y))
			{
				Sounds.play("buttonl");
				TheDevice.moveToTutorial();
			}
			else if(post.getBoundingRectangle().contains(x,y))
			{
				touch = !touch;
				fb.switchActivity();
			}
		}
	}
}