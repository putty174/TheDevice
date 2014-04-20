package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.control.Controller;
import com.pressx.managers.DesignHelper;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.enemy.MonsterManager;
import com.pressx.objects.player.Player;
import com.pressx.screens.game.Room;
import com.pressx.screens.game.UI;
import com.pressx.spawner.CustomSpawner;
import com.pressx.thedevice.GameStats;
import com.pressx.thedevice.TheDevice;

public class GameScreen extends BaseState {
	private Draw draw;
	private Textures textures;
	private Sounds sounds;
	
	String levelName;//Temporary until we make a way for the game to choose the map to use
	
	UI gameUI;
	Player player;
	GameStats stats;
	
	private Sprite background;
	
	public GameScreen(TheDevice g, Player player, Sounds s, Textures textures, GameStats stats, UI gameUI, GameObject box, CustomSpawner spawner, Room room, Controller controller) {
		super(g);
		draw = new Draw();
		sounds = s;
		this.textures = textures;
		levelName = "Level1";
		this.player = player;
		this.stats = stats;
		this.room = room;
		this.box = box;
		this.spawner = spawner;
		this.gameUI = gameUI;
		this.controller = controller;
		
		gameUI.setSpawner(spawner);
		
		g.inventory.initializeItemsForGame();
		
		stats.setBox(box);
		
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
		gameUI.setDraw(draw);
		this.background = new Sprite(textures.getArtAsset("game_bg"));
	}
	
	public void dispose() {
	}
	
	public void render(SpriteBatch batch) {
		this.background.draw(batch);
		
		if(stats.placeItem() != 0)
			this.player.pause_touch();
		
		float dt = Gdx.graphics.getDeltaTime();
		
		if (!gameUI.update())
		{
			if(box.getHp() <= 0){
				//helper.dispose();
				game.moveToEnd(stats);
				return;
			}
			
			//boolean gameIsOver = this.room.update(dt);
			stats.updateTimeElapsed();
			
			//spawner.update(dt);
			
			if(stats.nukeState())
			{
				this.room.wipe();
				stats.nukeStateOff();
			}
			this.room.update(dt);
			
		}
		else
			dt = 0;
		
		//Draw
		gameUI.render();
		this.room.render(batch, game.renderInfo);
		draw.draw(batch);

		stats.updateTimeElapsed();
	}
	
	public void update() {
	}
}