package com.pressx.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.items.Mine;
import com.pressx.objects.items.Vortex;
import com.pressx.thedevice.GameStats;

public class UI {
	private Draw draw;
	private Sounds sound;
	private Textures textures;
	private GameStats stats;
	
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
	
	public UI(Draw draw, Sounds sounds, Textures textures, GameStats stats, Room room){
		this.draw = draw;
		this.sound = sounds;
		this.textures = textures;
		this.stats = stats;
		this.room = room;
		UIBase = new Sprite(textures.getArtAsset("ui_base"));
		pause = new Sprite(textures.getArtAsset("ui_pause"));
		nuke = new Sprite(textures.getArtAsset("ui_bomb"));
		nukeCount1 = new Sprite(textures.getArtAsset("ui_bombcount"));
		nukeCount2 = new Sprite(textures.getArtAsset("ui_bombcount"));
		nukeCount3 = new Sprite(textures.getArtAsset("ui_bombcount"));
		mine = new Sprite(textures.getArtAsset("ui_mine"));
		mineCount = new Sprite(textures.getArtAsset("ui_minecount"));
		vortex = new Sprite(textures.getArtAsset("ui_vortex"));
		vortexCount = new Sprite(textures.getArtAsset("ui_minecount"));
	}
	
	public void create()
	{
	}
	
	public boolean update()
	{
		checkInput();
		updateButtons();
		updateStats();
		
		return stats.pauseState();
	}
	
	private void updateStats()
	{
		//Update Time
		time = Integer.toString(stats.timeElapsed());
		
		//Update Score
		score = Integer.toString(stats.getScore());
	}
	
	private void updateButtons()
	{
		//Update Nuke Button
		if(stats.nukeReady())
			nuke.setRegion(0,0,134,105);
		else
			nuke.setRegion(135,0,134,105);
		
		//Update NukeCount
		if(stats.nukeCount() > 0)
			nukeCount1.setRegion(0,0,93,64);
		else
			nukeCount1.setRegion(94,0,93,64);
		if(stats.nukeCount() > 1)
			nukeCount2.setRegion(0,0,93,64);
		else
			nukeCount2.setRegion(94,0,93,64);
		if(stats.nukeCount() > 2)
			nukeCount3.setRegion(0,0,93,64);
		else
			nukeCount3.setRegion(94,0,93,64);
		
		//Update Pause
		if (!stats.pauseState())
			pause.setRegion(0,0,57,57);
		else
			pause.setRegion(58,0,57,57);
		
		//Update Mine
		if(stats.mineReady())
			mine.setRegion(0,0,124,95);
		else
			mine.setRegion(124,0,124,95);
		mineCount.setRegion(39*(3-stats.getMineCount()),0,39,38);
		
		//Update Vortex
		if(stats.vortexReady())
			vortex.setRegion(0,0,124,95);
		else
			vortex.setRegion(124,0,124,95);
		vortexCount.setRegion(39*(3-stats.getVortCount()),0,39,38);
	}
	
	private void checkInput()
	{
		if(Gdx.input.justTouched())
		{
			float touchX = Gdx.input.getX();
			float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
			if(pause.getBoundingRectangle().contains(touchX, touchY))
				stats.pauseToggle();
			if(!stats.pauseState())
			{
				if(stats.placeItem() == 0)
				{
					if(mine.getBoundingRectangle().contains(touchX, touchY)&& stats.mineReady())
						stats.useMine();
					else if(vortex.getBoundingRectangle().contains(touchX, touchY) && stats.vortexReady())
						stats.useVort();
				}
				else
				{
					if(touchX < Gdx.graphics.getWidth() * 0.8f)
					{
						if(stats.placeItem() == 1)
						{
							stats.placeMine();
							room.add_object(new Mine(draw, sound, textures, touchX / Gdx.graphics.getWidth() * 100, touchY / Gdx.graphics.getHeight() * 66));
						}
						else if(stats.placeItem() == 2)
						{
							stats.placeVort();
							room.add_object(new Vortex(draw, sound, textures, touchX / Gdx.graphics.getWidth() * 100, touchY / Gdx.graphics.getHeight() * 66));
						}
					}
				}
				if(nuke.getBoundingRectangle().contains(touchX, touchY) && stats.nukeReady())
					stats.useNuke();
			}
		}
	}
	
	public void render()
	{
		//Draw UIBase
		draw.draw(Draw.TYPES.UI, UIBase,0.8f,0,0.2f,1);
		
		//Draw Pause
		draw.draw(Draw.TYPES.BUTTON, pause, 0.90f, 0.90f, 0.05f, 0.09f);
		
		//Draw Mine
		draw.draw(Draw.TYPES.BUTTON, mine, 0.83f, 0.57f, 0.17f, 0.165f);
		
		//Draw Vortex
		draw.draw(Draw.TYPES.BUTTON, vortex, 0.83f, 0.35f, 0.17f, 0.165f);
		
		//Draw Nuke
		draw.draw(Draw.TYPES.BUTTON, nuke, 0.84f, 0.06f,0.15f,0.18f);
		
		//Draw NukeCount
		draw.draw(Draw.TYPES.BUTTON, nukeCount1, 0.825f, 0.20f,0.08f,0.095f);
		draw.draw(Draw.TYPES.BUTTON, nukeCount2, 0.875f, 0.22f,0.08f,0.095f);
		draw.draw(Draw.TYPES.BUTTON, nukeCount3, 0.925f, 0.20f,0.08f,0.095f);
		
		//Draw time
		draw.write(time, (((draw.screenWidth * 0.8f) - draw.font.getBounds(time).width) / 2) / draw.screenWidth, 0.98f);
		
		//Draw Score
		draw.write(score, ((draw.screenWidth * 0.99f) - draw.font.getBounds(score).width) / draw.screenWidth, 0.825f);
		
		//Draw Mine Button with Count
		if(stats.mineReady())
			draw.draw(Draw.TYPES.EXTRAS, mineCount, 0.935f, 0.62f, 0.04f, 0.06f);
		else
			draw.draw(Draw.TYPES.EXTRAS, mineCount, 0.935f, 0.60f, 0.04f, 0.06f);
		
		//Draw Vortex Button with Count
		if(stats.vortexReady())
			draw.draw(Draw.TYPES.EXTRAS, vortexCount, 0.935f, 0.40f, 0.04f, 0.06f);
		else
			draw.draw(Draw.TYPES.EXTRAS, vortexCount, 0.935f, 0.38f, 0.04f, 0.06f);
	}
}