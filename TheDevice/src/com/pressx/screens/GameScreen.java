package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.control.Controller;
import com.pressx.managers.Graphics;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.device.Device;
import com.pressx.objects.player.Player;
import com.pressx.screens.game.Room;
import com.pressx.screens.game.UI;
import com.pressx.spawner.CustomSpawner;
import com.pressx.thedevice.GameStats;
import com.pressx.thedevice.TheDevice;

public class GameScreen extends BaseState {
	String LEVELPATH;//Temporary until we make a way for the game to choose the map to use
	
	UI gameUI;
	Player player;
	//DesignHelper helper;
	
	private Sprite background;
	
	public GameScreen() {
		LEVELPATH = Gdx.files.internal("./data/leveldata/levels/Level1.devicelevel").toString();
	}
	
	/* Game */
	private GameObject box;
	private CustomSpawner spawner;
	
	public Room room;
	private GameStats g;
	
	//Controller
	Controller controller;
	
	@Override
	public void create()
	{
		this.background = new Sprite(Textures.getArtAsset("game_bg"));
		/* Start Room */
		player = new Player(
				0, //ID
				50, 30, //Position
				1, //Mass
				100, //Friction
				5, 2, //Hitbox
				0, 0, //Hitoffset
				true, //Solid
				8, //Touch Radius
				false, //Touchability
				8, (16 + 2/3) * 0.8f, //Draw width and height
				150, 250
				);	
		
		//UI
		this.g = new GameStats(player);
		//Set XP Fill
		
		
		this.room = new Room(player, this.g);
		
		box = new Device(25,25, room);
		this.room.add_object(this.box);
		this.room.add_object(player);
		
		spawner = new CustomSpawner(LEVELPATH,box,room);//temporary path
		
		//Spawn Management for dev tool
		//helper = new DesignHelper(new MonsterManager(box, this.room), this.room, box, gameUI);
		
		
		gameUI = new UI(g, this.room);
		
		/* Controls */
		this.controller = new Controller(TheDevice.renderInfo);
		this.controller.add_controllable(room);
		Gdx.input.setInputProcessor(this.controller);
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
			g.updateTimeElapsed();
			
			spawner.update(dt);
			if(g.nukeState())
			{
				this.room.wipe();
				g.nukeStateOff();
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
		
		g.updateTimeElapsed();
		TheDevice.moveToEnd();
	}
	
	public void update() {
	}
}