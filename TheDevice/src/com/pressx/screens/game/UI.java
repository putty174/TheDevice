package com.pressx.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pressx.gadgets.PlayerInventory;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.spawner.CustomSpawner;
import com.pressx.thedevice.GameStats;

public class UI {
	private Draw draw;
	private Sounds sound;
	private Textures textures;
	private GameStats stats;
	
	private Sprite UIBase;
	
	private Sprite pause;
	
	private Sprite XPBar;
	private Sprite emtpy;
	private Sprite fill;
	
	private Sprite nukeCount1;
	private Sprite nukeCount2;
	private Sprite nukeCount3;
	private Sprite nuke;
	
	private Sprite waveUI;
	
	private Room room;
	
	private String time;
	private String score;
	
	private CustomSpawner spawner;
	
	public UI(Draw draw, Sounds sounds, Textures textures, GameStats stats, Room room,PlayerInventory inventory){
		this.draw = draw;
		this.sound = sounds;
		this.textures = textures;
		this.stats = stats;
		this.room = room;
		UIBase = new Sprite(textures.getArtAsset("ui"));
		UIBase.setRegion(0, 400, 152, 640);
		XPBar = new Sprite(textures.getArtAsset("ui"));
		XPBar.setRegion(180, 265, 55, 115);
		XPBar.setRotation(90);
		emtpy = new Sprite(textures.getArtAsset("ui"));
		emtpy.setRegion(180, 380, 55, 1);
		emtpy.setRotation(90);
		fill = new Sprite(textures.getArtAsset("ui"));
		fill.setRegion(180, 381, 55, 1);
		fill.setRotation(90);
		
		pause = new Sprite(textures.getArtAsset("ui"));
		nuke = new Sprite(textures.getArtAsset("ui"));
		nukeCount1 = new Sprite(textures.getArtAsset("ui"));
		nukeCount2 = new Sprite(textures.getArtAsset("ui"));
		nukeCount3 = new Sprite(textures.getArtAsset("ui"));
		
		waveUI = new Sprite(textures.getArtAsset("ui_wavebar"));
		
		stats.item0 = inventory.item0;		
		stats.item1 = inventory.item1;
		stats.item0.setGameStats(stats);
		stats.item1.setGameStats(stats);
		stats.item0.setDraw(draw);
		stats.item1.setDraw(draw);
		stats.item0.setRoom(room);
		stats.item1.setRoom(room);
	}
	
	public void setSpawner(CustomSpawner spawner){
		this.spawner = spawner;
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
			nuke.setRegion(0,117,128,139);
		else
			nuke.setRegion(128,117,128,139);
		
		//Update NukeCount
		if(stats.nukeCount() > 0)
			nukeCount1.setRegion(156,915,45,47);
		else
			nukeCount1.setRegion(209,915,45,47);
		if(stats.nukeCount() > 1)
			nukeCount2.setRegion(156,915,45,47);
		else
			nukeCount2.setRegion(209,915,45,47);
		if(stats.nukeCount() > 2)
			nukeCount3.setRegion(156,915,45,47);
		else
			nukeCount3.setRegion(209,915,45,47);
		
		//Update Pause
		if (!stats.pauseState())
			pause.setRegion(159,976,41,46);
		else
			pause.setRegion(210,976,41,46);
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
		
		//Draw XPBar
		draw.draw(Draw.TYPES.BUTTON, emtpy, 0.983f, 0.708f, 0.055f, 0.2026f);
		draw.draw(Draw.TYPES.EXTRAS, fill, 0.835f, 0.708f, 0.055f, 0.19f * -stats.getXPpercent());
		draw.draw(Draw.TYPES.SUPEREXTRAS, XPBar, 0.983f, 0.708f, 0.055f, 0.2026f);
		
		
		//Draw Pause
		draw.draw(Draw.TYPES.BUTTON, pause, 0.90f, 0.90f, 0.05f, 0.09f);
		
		//Draw Items
		stats.item0.drawButton(draw,0);
		stats.item1.drawButton(draw,1);
		
		//Draw Nuke
		draw.draw(Draw.TYPES.BUTTON, nuke, 0.84f, 0.06f,0.15f,0.18f);
		
		//Draw NukeCount
		draw.draw(Draw.TYPES.BUTTON, nukeCount1, 0.825f, 0.20f,0.08f,0.095f);
		draw.draw(Draw.TYPES.BUTTON, nukeCount2, 0.875f, 0.22f,0.08f,0.095f);
		draw.draw(Draw.TYPES.BUTTON, nukeCount3, 0.925f, 0.20f,0.08f,0.095f);
		
		//Draw time
		draw.write(time, draw.getPositionToCenterText(time,.4f,1f/1700), 0.995f,1f/1700);
		
		//Draw Score
		draw.write(score, ((draw.screenWidth * 0.99f) - draw.font.getBounds(score).width) / draw.screenWidth, 0.825f);
		
		final float WAVEUI_CENTER = .5f-.1f;//.1f because of the side UI
		if(room.monsterCount == 0 && spawner.nextwavedelaytimer > 0){
			String nextwavetext ="Next wave in "+(int)spawner.nextwavedelaytimer+" seconds"; 
			draw.write(nextwavetext,draw.getPositionToCenterText(nextwavetext,.4f,1f/1000),.85f,1f/1000);
		}
		final float WAVEUI_SIZEX = .25f;
		final float WAVEUI_SIZEY = WAVEUI_SIZEX*.47f;
		final float WAVEUI_POSX = WAVEUI_CENTER-WAVEUI_SIZEX/2;
		final float WAVEUI_POSY = 1-WAVEUI_SIZEY;
		final float WAVEUI_FONTSCALE = 1f/1500;
		draw.draw(Draw.TYPES.BUTTON,waveUI,WAVEUI_POSX,WAVEUI_POSY,WAVEUI_SIZEX,WAVEUI_SIZEY);//682x232 (.340,2.94)
		draw.write(""+spawner.ui_enemiesLeftInWave,WAVEUI_POSX+WAVEUI_SIZEX*.85f,WAVEUI_POSY+WAVEUI_SIZEY*.6f,WAVEUI_FONTSCALE);
		draw.write(""+spawner.ui_currentWaveNumber,WAVEUI_POSX+WAVEUI_SIZEX*.035f,WAVEUI_POSY+WAVEUI_SIZEY*.67f,WAVEUI_FONTSCALE);
		draw.write(""+spawner.ui_totalWaveCount,WAVEUI_POSX+WAVEUI_SIZEX*.135f,WAVEUI_POSY+WAVEUI_SIZEY*.58f,WAVEUI_FONTSCALE);
		draw.write("",0,0);
		//Draw wave info (temporary)
		//draw.write("Wave "+spawner.ui_currentWaveNumber+'/'+spawner.ui_totalWaveCount,.45f,.9f);
		//Draw enemies left (temporary)
		//draw.write("Enemies left: "+spawner.ui_enemiesLeftInWave,.35f,.8f);
	}
	
	public void setDraw(Draw d) {
		this.draw = d;
	}
}
