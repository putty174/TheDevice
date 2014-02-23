package com.pressx.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pressx.items.PlayerInventory;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.GameStats;

public class UI {
	private Draw draw;
	private Sounds sound;
	private Textures textures;
	private GameStats stats;
	
	private Sprite UIBase;
	
	private Sprite pause;
	
	private Sprite nukeCount1;
	private Sprite nukeCount2;
	private Sprite nukeCount3;
	private Sprite nuke;
	
	private Room room;
	
	private String time;
	private String score;
	
	public UI(Draw draw, Sounds sounds, Textures textures, GameStats stats, Room room,PlayerInventory inventory){
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
		
		stats.item0 = inventory.item0;		
		stats.item1 = inventory.item1;
		stats.item0.setGameStats(stats);
		stats.item1.setGameStats(stats);
		stats.item0.setDraw(draw);
		stats.item1.setDraw(draw);
		stats.item0.setRoom(room);
		stats.item1.setRoom(room);
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
				
				//update the two items
				stats.item0.update();
				stats.item1.update();

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
		
		//Draw Items
<<<<<<< HEAD
		stats.item0.drawButton(draw,0);
		stats.item1.drawButton(draw,1);
=======
		stats.item0.drawButton(draw, 0);
		stats.item1.drawButton(draw, 1);
>>>>>>> 619ef0f56ca11613393556499301f564a86045df
		
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
	}
	
	public void setDraw(Draw d) {
		this.draw = d;
	}
}
