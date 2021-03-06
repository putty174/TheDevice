package com.pressx.screens;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Draw;
import com.pressx.managers.Levels;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;

public class MainMenuScreen extends BaseState {
	Draw draw;
	Sounds sounds;
	Textures textures;
	Levels levels;
	Sprite bgArt, play, help, post, shop;
	String key;
	File blah;

	public MainMenuScreen(TheDevice g, Sounds s, Textures t){
		super(g);
		draw = new Draw();
		sounds = s;
		textures = t;
		play = new Sprite(textures.getArtAsset("main_play"));
		bgArt = new Sprite(textures.getArtAsset("main_bg"));
		help = new Sprite(textures.getArtAsset("main_help"));
		post = new Sprite(textures.getArtAsset("exp"));
		post.setRegion(0, 0, 30, 30);
		shop = new Sprite(textures.getArtAsset("shopbutton"));
		
		String filename = "../TheDevice-android/assets/data/leveldata/formations/FuzzieArmy.spawnformation";
		
		blah = new File(filename);
	}
	
	public void create() {
	}

	public void dispose() {
	}

	public void render(SpriteBatch batch) {
		draw.draw(Draw.TYPES.BACKGROUND, bgArt, 0, 0, 1f, 1f);
		draw.draw(Draw.TYPES.BUTTON, play, 0.61f, 0.06f, 0.15f, 0.25f);
		draw.draw(Draw.TYPES.BUTTON, help, 0.79f, 0.06f, 0.15f, 0.25f);
		draw.draw(Draw.TYPES.EXTRAS, post, 0.1f, 0.1f, 0.1f, 0.1f);
		draw.draw(Draw.TYPES.BUTTON, shop, .23f, .67f, .1f, .135f);
		draw.write("Shop is open!", .1f, .9f);
		draw.write("A", .5f, 0.5f);
		
//		if(blah.exists())
//			draw.write("Yes", 0.3f, 0.3f);
//		else
//			draw.write("No", 0.3f, 0.3f);
		draw.draw(batch);
	}
	
	public void update()
	{
		if(Gdx.input.justTouched())
		{
			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();
			if(play.getBoundingRectangle().contains(x, y))
			{
				sounds.play("buttonl");
				game.moveToLevelSelect();
			}
			else if(help.getBoundingRectangle().contains(x,y))
			{
				sounds.play("buttonl");
				game.moveToTutorial();
			}
			else if(shop.getBoundingRectangle().contains(x,y)){
				game.moveToShop();
			}
		}
	}
}