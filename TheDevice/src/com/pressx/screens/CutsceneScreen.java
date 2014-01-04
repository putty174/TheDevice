package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Graphics;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;

public class CutsceneScreen extends BaseState {
	int currentScene, maxScene;
	Sprite scene;
	String cinematicType;
	
	public CutsceneScreen(String cinematicType, int numScenes){
		super();
		this.currentScene = 0;
		this.cinematicType = cinematicType;
		maxScene=numScenes;
		scene = new Sprite(Textures.getArtAsset("sc" + Integer.toString(currentScene)));
	}
	
	public void update()
	{
		if(currentScene == maxScene)
		{
			if(cinematicType == "Intro")
				TheDevice.moveToLoading();
			else if(cinematicType == "Outro")
				TheDevice.moveToMain();
		}
		else
			scene = new Sprite(Textures.getArtAsset("sc" + Integer.toString(currentScene)));
		
		if(Gdx.input.justTouched())
			currentScene ++;
	}

	public void render(SpriteBatch batch)
	{
		Graphics.draw(Graphics.TYPES.BACKGROUND, scene, 0, 0, 1f, 1f);
	}

	public void create(){
	}
	
	public void dispose(){
	}
}