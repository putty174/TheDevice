package com.pressx.screens;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Graphics;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.social.Social;
import com.pressx.thedevice.TheDevice;

public class MainMenuScreen extends BaseState {
	Sprite bgArt, play, help, post, shop;
	Social fb;
	String key;
	File blah;

	public MainMenuScreen(Social facebook){
		super();
		play = new Sprite(Textures.getArtAsset("main_play"));
		bgArt = new Sprite(Textures.getArtAsset("main_bg"));
		help = new Sprite(Textures.getArtAsset("main_help"));
		post = new Sprite(Textures.getArtAsset("exp"));
		post.setRegion(0, 0, 30, 30);
		shop = new Sprite(Textures.getArtAsset("shopbutton"));
		fb = facebook;
		
		String filename = "../TheDevice-android/assets/data/leveldata/formations/FuzzieArmy.spawnformation";
		
		blah = new File(filename);
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
		Graphics.draw(Graphics.TYPES.BUTTON, shop, .23f, .67f, .1f, .135f);
		Graphics.write("Placeholder Shop Button", .1f, .9f);
		
		if(blah.exists())
			Graphics.write("Yes", 0.3f, 0.3f);
		else
			Graphics.write("No", 0.3f, 0.3f);
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
				fb.switchActivity();
			}else if(shop.getBoundingRectangle().contains(x,y)){
				TheDevice.moveToShop();
			}
		}
	}
}