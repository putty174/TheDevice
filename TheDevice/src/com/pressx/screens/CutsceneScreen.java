package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Draw;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;

public class CutsceneScreen extends BaseState {
	Textures textures;
	Draw draw;
	int currentScene, maxScene;
	Sprite scene;
	String cinematicType;
	
	public CutsceneScreen(TheDevice g, Textures t, String cinematicType){
		super(g);
		textures = t;
		draw = new Draw();
		this.currentScene = 0;
		this.cinematicType = cinematicType;
		scene = new Sprite(textures.getArtAsset("sc" + Integer.toString(currentScene)));
		if(cinematicType.equals("Intro"))
			maxScene = 5;
		else if (cinematicType.equals("Outro"))
			maxScene = 3;
	}
	
	public void update()
	{
		if(currentScene == maxScene)
		{
			if(cinematicType == "Intro")
				game.moveToLoading();
			else if(cinematicType == "Outro")
				game.moveToMain();
		}
		else
			scene = new Sprite(textures.getArtAsset("sc" + Integer.toString(currentScene)));
		
		if(Gdx.input.justTouched())
			currentScene ++;
	}

	public void render(SpriteBatch batch)
	{
		draw.draw(Draw.TYPES.BACKGROUND, scene, 0, 0, 1f, 1f);
		draw.draw(batch);
	}

	public void create(){
	}
	
	public void dispose(){
	}
}