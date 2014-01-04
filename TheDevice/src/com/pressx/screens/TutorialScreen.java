package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Graphics;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;

public class TutorialScreen extends BaseState {
	Sprite nav_left, nav_right, nav_exit;
	Sprite[] screen;
	int currentScreen, screenMax;
	
	public TutorialScreen(){
		super();
		
		currentScreen = 0;
		screenMax = 2 - 1;
		screen = new Sprite[screenMax+1];
		
		screen[0] = new Sprite(Textures.getArtAsset("tut_pg1"));
		screen[1] = new Sprite(Textures.getArtAsset("tut_pg2"));

		nav_left = new Sprite(Textures.getArtAsset("tut_nav_left"));
		nav_right = new Sprite(Textures.getArtAsset("tut_nav_right"));
		nav_exit = new Sprite(Textures.getArtAsset("tut_nav_exit"));
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
				Sounds.play("buttonl");
			}
			else if(currentScreen != screenMax && nav_right.getBoundingRectangle().contains(x, y))
			{
				currentScreen++;
				Sounds.play("buttonh");
			}
			else if(nav_exit.getBoundingRectangle().contains(x, y))
			{
				Sounds.play("buttonh");
				TheDevice.moveToMain();
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
		Graphics.draw(Graphics.TYPES.BACKGROUND, screen[currentScreen], 0, 0, 1f, 1f);
		Graphics.draw(Graphics.TYPES.BUTTON, nav_exit, 0.415f, -0.04f, 0.17f, 0.3f);
		if(currentScreen != 0)
			Graphics.draw(Graphics.TYPES.BUTTON, nav_left, 0.08f, 0.02f, 0.11f, 0.19f);
		else if(currentScreen != screenMax)
			Graphics.draw(Graphics.TYPES.BUTTON, nav_right, 0.81f, 0.02f, 0.11f, 0.19f);
	}
	
	public void create() {
	}
	
	public void dispose() {
	}
}