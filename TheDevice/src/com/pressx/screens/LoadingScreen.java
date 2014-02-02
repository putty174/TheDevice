package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.pressx.control.Controller;
import com.pressx.managers.Graphics;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.device.Device;
import com.pressx.objects.player.Player;
import com.pressx.screens.game.Room;
import com.pressx.screens.game.UI;
import com.pressx.spawner.CustomSpawner;
import com.pressx.thedevice.GameStats;
import com.pressx.thedevice.TheDevice;

public class LoadingScreen extends BaseState{
	private float complete;
	private Sprite play;
	private Sprite bg;
	private boolean done = false;
	
	private UI gameUI;
	private Player player;
	private GameObject box;
	private CustomSpawner spawner;
	public Room room;
	private String levelName;
	
	private Controller controller;
	
	public LoadingScreen()	{
		super();
		bg = new Sprite(Textures.getArtAsset("bg"));
		play = new Sprite(Textures.getArtAsset("play"));
	}
	
	public void create() {
		levelName = "level1";
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
		new GameStats(player);
		
		room = new Room(player);
		box = new Device(25,25, room);
		this.room.add_object(this.box);
		this.room.add_object(player);
		spawner = new CustomSpawner(levelName,box,room);//temporary path
		
		gameUI = new UI(this.room);
		this.controller = new Controller(TheDevice.renderInfo);
		this.controller.add_controllable(room);
		Gdx.input.setInputProcessor(this.controller);
	}
	
	public void update() {
		if(Textures.a_manager.update() && Sounds.s_manager.update() && spawner.getUpdate())
		{
			done = true;
			complete = 1;
			if(Gdx.input.isTouched())
				if(play.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
					TheDevice.moveToGame(player, gameUI, box, spawner, room, controller);
		}
		else
			complete = Interpolation.linear.apply(complete, (Textures.a_manager.getProgress()+Sounds.s_manager.getProgress()+spawner.getProgress())/4, 0.1f);
	}
	
	public void render(SpriteBatch batch) {
		Graphics.draw(Graphics.TYPES.BACKGROUND, bg, 0, 0, 1, 1);
		String progress = Integer.toString(Math.round(complete * 100)) + "%";
		Graphics.write(progress, ((Graphics.screenWidth - Graphics.font.getBounds(progress).width) / 2) / Graphics.screenWidth, 2/3f);
		if(done)
			Graphics.draw(Graphics.TYPES.BUTTON, play, ((Graphics.screenWidth-play.getBoundingRectangle().width)/2f)/Graphics.screenWidth, ((Graphics.screenHeight-play.getBoundingRectangle().height)/3f)/Graphics.screenHeight, 0.15f, 0.25f);
	}
	
	public void dispose() {
	}
}