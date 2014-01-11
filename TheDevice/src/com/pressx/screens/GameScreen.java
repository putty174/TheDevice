package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.control.Controller;
import com.pressx.managers.Graphics;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.player.Player;
import com.pressx.screens.game.Room;
import com.pressx.screens.game.UI;
import com.pressx.spawner.CustomSpawner;
import com.pressx.thedevice.GameStats;
import com.pressx.thedevice.TheDevice;

public class GameScreen extends BaseState {
	String levelName;//Temporary until we make a way for the game to choose the map to use
	
	
	UI gameUI;
	Player player;
	//DesignHelper helper;
	
	private Sprite background;
	
	public GameScreen(Player player, UI gameUI, GameObject box, CustomSpawner spawner, Room room, Controller controller) {
		levelName = "Level1";
		this.player = player;
		this.room = room;
		this.box = box;
		this.spawner = spawner;
		this.gameUI = gameUI;
		this.controller = controller;
	}
	
	/* Game */
	private GameObject box;
	private CustomSpawner spawner;
	
	private Room room;
	
	//Controller
	Controller controller;
	
	@Override
	public void create()
	{
		this.background = new Sprite(Textures.getArtAsset("game_bg"));
	}
	
	public void dispose() {
	}
	
	public void render(SpriteBatch batch) {
		this.background.draw(batch);
		
		if(GameStats.placeItem() != 0)
		{
			this.player.pause_touch();
		}//fi
		
		float dt = Gdx.graphics.getDeltaTime();
		
		if (!gameUI.update())
		{
			
			if(box.getHp() <= 0){
				//helper.dispose();
				TheDevice.moveToEnd();
				return;
			}
			
			//boolean gameIsOver = this.room.update(dt);
			GameStats.updateTimeElapsed();
			
			spawner.update(dt);
			if(GameStats.nukeState())
			{
				this.room.wipe();
				GameStats.nukeStateOff();
			}
			this.room.update(dt);
		}
		else
		{
			dt = 0;
			
		}
		
		//Draw
		gameUI.render();
		this.room.render(batch, TheDevice.renderInfo);
		
		Graphics.draw(batch);
		
		GameStats.updateTimeElapsed();
		BitmapFont test = new BitmapFont();
		test.draw(batch, "HA", 0.1f, 0.1f);
	}
	
	public void update() {
	}
}