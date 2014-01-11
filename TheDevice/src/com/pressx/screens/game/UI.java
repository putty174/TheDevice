package com.pressx.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pressx.managers.Graphics;
import com.pressx.managers.Textures;
import com.pressx.objects.items.Mine;
import com.pressx.objects.items.Vortex;
import com.pressx.thedevice.GameStats;

public class UI {
	private Sprite UIBase;
	
	private Sprite pause;
	
	private Sprite mine;
	private Sprite mineCount;
	
	private Sprite vortex;
	private Sprite vortexCount;
	
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
		mine = new Sprite(Textures.getArtAsset("ui_mine"));
		mineCount = new Sprite(Textures.getArtAsset("ui_minecount"));
		vortex = new Sprite(Textures.getArtAsset("ui_vortex"));
		vortexCount = new Sprite(Textures.getArtAsset("ui_minecount"));
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
		
		//Update Mine
		if(GameStats.mineReady())
			mine.setRegion(0,0,124,95);
		else
			mine.setRegion(124,0,124,95);
		mineCount.setRegion(39*(3-GameStats.getMineCount()),0,39,38);
		
		//Update Vortex
		if(GameStats.vortexReady())
			vortex.setRegion(0,0,124,95);
		else
			vortex.setRegion(124,0,124,95);
		vortexCount.setRegion(39*(3-GameStats.getVortCount()),0,39,38);
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
				if(GameStats.placeItem() == 0)
				{
					if(mine.getBoundingRectangle().contains(touchX, touchY)&& GameStats.mineReady())
						GameStats.useMine();
					else if(vortex.getBoundingRectangle().contains(touchX, touchY) && GameStats.vortexReady())
						GameStats.useVort();
				}
				else
				{
					if(touchX < Gdx.graphics.getWidth() * 0.8f)
					{
						if(GameStats.placeItem() == 1)
						{
							GameStats.placeMine();
							room.add_object(new Mine(touchX / Gdx.graphics.getWidth() * 100, touchY / Gdx.graphics.getHeight() * 66));
						}
						else if(GameStats.placeItem() == 2)
						{
							GameStats.placeVort();
							room.add_object(new Vortex(touchX / Gdx.graphics.getWidth() * 100, touchY / Gdx.graphics.getHeight() * 66));
						}
					}
				}
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
		
		//Draw Mine
		Graphics.draw(Graphics.TYPES.BUTTON, mine, 0.83f, 0.57f, 0.17f, 0.165f);
		
		//Draw Vortex
		Graphics.draw(Graphics.TYPES.BUTTON, vortex, 0.83f, 0.35f, 0.17f, 0.165f);
		
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
		
		//Draw Mine Button with Count
		if(GameStats.mineReady())
			Graphics.draw(Graphics.TYPES.EXTRAS, mineCount, 0.935f, 0.62f, 0.04f, 0.06f);
		else
			Graphics.draw(Graphics.TYPES.EXTRAS, mineCount, 0.935f, 0.60f, 0.04f, 0.06f);
		
		//Draw Vortex Button with Count
		if(GameStats.vortexReady())
			Graphics.draw(Graphics.TYPES.EXTRAS, vortexCount, 0.935f, 0.40f, 0.04f, 0.06f);
		else
			Graphics.draw(Graphics.TYPES.EXTRAS, vortexCount, 0.935f, 0.38f, 0.04f, 0.06f);
	}
}