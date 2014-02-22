package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;

public class TutorialScreen extends BaseState {
	Draw draw;
	Sounds sounds;
	Textures textures;
	Sprite nav_left, nav_right, nav_exit;
	Sprite[] screen;
	int currentScreen, screenMax;
	
	public TutorialScreen(TheDevice g, Draw d, Textures t, Sounds s){
		super(g);
		
		draw = new Draw();
		sounds = new Sounds();
		textures = new Textures();
		sounds.loadSoundAssets(Sounds.PACKS.TUTORIAL);
		textures.loadArtAssets("Tutorial");
		
		currentScreen = 0;
		screenMax = 2 - 1;
		screen = new Sprite[screenMax+1];
		
		screen[0] = new Sprite(textures.getArtAsset("tut_pg1"));
		screen[1] = new Sprite(textures.getArtAsset("tut_pg2"));

		nav_left = new Sprite(textures.getArtAsset("tut_nav_left"));
		nav_right = new Sprite(textures.getArtAsset("tut_nav_right"));
		nav_exit = new Sprite(textures.getArtAsset("tut_nav_exit"));
	}

	public void update()
	{
		if(Gdx.input.justTouched())
		{
			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();
			if(currentScreen != 0 && nav_left.getBoundingRectangle().contains(x, y))
			{
				currentScreen--;
				sounds.play("buttonl");
			}
			else if(currentScreen != screenMax && nav_right.getBoundingRectangle().contains(x, y))
			{
				currentScreen++;
				sounds.play("buttonh");
			}
			else if(nav_exit.getBoundingRectangle().contains(x, y))
			{
				sounds.play("buttonh");
				game.moveToMain();
				return;
			}
			if(currentScreen < 0)
				currentScreen = 0;
			else if(currentScreen > screenMax)
				currentScreen = screenMax;
		}
	}
	
	public void render(SpriteBatch batch) {
		//Graphics.buttonRefresh();
		draw.draw(Draw.TYPES.BACKGROUND, screen[currentScreen], 0, 0, 1f, 1f);
		draw.draw(Draw.TYPES.BUTTON, nav_exit, 0.415f, -0.04f, 0.17f, 0.3f);
		if(currentScreen != 0)
			draw.draw(Draw.TYPES.BUTTON, nav_left, 0.08f, 0.02f, 0.11f, 0.19f);
		else if(currentScreen != screenMax)
			draw.draw(Draw.TYPES.BUTTON, nav_right, 0.81f, 0.02f, 0.11f, 0.19f);
		
		draw.draw(batch);
	}
	
	public void create() {
	}
	
	public void dispose() {
	}
}