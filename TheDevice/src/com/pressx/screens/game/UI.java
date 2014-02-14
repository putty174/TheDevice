package com.pressx.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pressx.items.*;
import com.pressx.managers.Graphics;
import com.pressx.managers.Textures;
import com.pressx.thedevice.GameStats;

public class UI {
	private Sprite UIBase;
	
	private Sprite pause;
	
	private Sprite nukeCount1;
	private Sprite nukeCount2;
	private Sprite nukeCount3;
	private Sprite nuke;
	
	private Room room;
	
	private String time;
	private String score;
	
	public UI(Room room){
		this.room = room;
		UIBase = new Sprite(Textures.getArtAsset("ui_base"));
		pause = new Sprite(Textures.getArtAsset("ui_pause"));
		nuke = new Sprite(Textures.getArtAsset("ui_bomb"));
		nukeCount1 = new Sprite(Textures.getArtAsset("ui_bombcount"));
		nukeCount2 = new Sprite(Textures.getArtAsset("ui_bombcount"));
		nukeCount3 = new Sprite(Textures.getArtAsset("ui_bombcount"));
		
		GameStats.item0.setRoom(room);		
		GameStats.item1.setRoom(room);
	}
	
	public void create()
	{
	}
	
	public boolean update()
	{
		checkInput();
		updateButtons();
		updateStats();
		
		return GameStats.pauseState();
	}
	
	private void updateStats()
	{
		//Update Time
		time = Integer.toString(GameStats.timeElapsed());
		
		//Update Score
		score = Integer.toString(GameStats.getScore());
	}
	
	private void updateButtons()
	{	
		//Update Nuke Button
		if(GameStats.nukeReady())
			nuke.setRegion(0,0,134,105);
		else
			nuke.setRegion(135,0,134,105);
		
		//Update NukeCount
		if(GameStats.nukeCount() > 0)
			nukeCount1.setRegion(0,0,93,64);
		else
			nukeCount1.setRegion(94,0,93,64);
		if(GameStats.nukeCount() > 1)
			nukeCount2.setRegion(0,0,93,64);
		else
			nukeCount2.setRegion(94,0,93,64);
		if(GameStats.nukeCount() > 2)
			nukeCount3.setRegion(0,0,93,64);
		else
			nukeCount3.setRegion(94,0,93,64);
		
		//Update Pause
		if (!GameStats.pauseState())
			pause.setRegion(0,0,57,57);
		else
			pause.setRegion(58,0,57,57);
	}
	
	private void checkInput()
	{
		if(Gdx.input.justTouched())
		{
			float touchX = Gdx.input.getX();
			float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
			if(pause.getBoundingRectangle().contains(touchX, touchY))
				GameStats.pauseToggle();
			if(!GameStats.pauseState())
			{
				//update the two items
				GameStats.item0.update();
				GameStats.item1.update();

				if(nuke.getBoundingRectangle().contains(touchX, touchY) && GameStats.nukeReady())
					GameStats.useNuke();
			}
		}
	}
	
	public void render()
	{
		//Draw UIBase
		Graphics.draw(Graphics.TYPES.UI, UIBase,0.8f,0,0.2f,1);
		
		//Draw Pause
		Graphics.draw(Graphics.TYPES.BUTTON, pause, 0.90f, 0.90f, 0.05f, 0.09f);
		
		//Draw Items
		GameStats.item0.drawButton(0);
		GameStats.item1.drawButton(1);
		
		//Draw Nuke
		Graphics.draw(Graphics.TYPES.BUTTON, nuke, 0.84f, 0.06f,0.15f,0.18f);
		
		//Draw NukeCount
		Graphics.draw(Graphics.TYPES.BUTTON, nukeCount1, 0.825f, 0.20f,0.08f,0.095f);
		Graphics.draw(Graphics.TYPES.BUTTON, nukeCount2, 0.875f, 0.22f,0.08f,0.095f);
		Graphics.draw(Graphics.TYPES.BUTTON, nukeCount3, 0.925f, 0.20f,0.08f,0.095f);
		
		//Draw time
		Graphics.write(time, (((Graphics.screenWidth * 0.8f) - Graphics.font.getBounds(time).width) / 2) / Graphics.screenWidth, 0.98f);
		
		//Draw Score
		Graphics.write(score, ((Graphics.screenWidth * 0.99f) - Graphics.font.getBounds(score).width) / Graphics.screenWidth, 0.825f);
	}
}
