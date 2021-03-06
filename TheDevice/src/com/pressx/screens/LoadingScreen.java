package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.pressx.control.Controller;
import com.pressx.managers.Draw;
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
	private Draw draw;
	private Sounds sounds;
	private Textures textures;
	
	private float complete;
	private Sprite play;
	private Sprite bg;
	private Sprite gear;
	private boolean done = false;
	
	private UI gameUI;
	private Player player;
	private GameStats stats;
	private GameObject box;
	private CustomSpawner spawner;
	public Room room;
	private String levelName;
	
	private Controller controller;
	
	public LoadingScreen(TheDevice g, Sounds s, Textures t)	{
		super(g);
		draw = new Draw();
		sounds = s;
		textures = t;
		bg = new Sprite(textures.getArtAsset("bg"));
		gear = new Sprite(textures.getArtAsset("gear"));
		play = new Sprite(textures.getArtAsset("play"));
	}
	
	public void create() {
		levelName = "level1";
		
	}
	
	public void update() {
		textures.loadArtAssets("Game");
		if(textures.a_manager.update() && sounds.s_manager.update())
		{
			if(!done){
				done = true;
				complete = 1;
				player = new Player(draw, sounds, textures,
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
				stats = new GameStats(draw, textures, sounds, player);
				
				room = new Room(textures, draw, sounds, stats, player);
				box = new Device(draw, sounds, textures, 25,25, room);
				this.room.add_object(this.box);
				this.room.add_object(player);
				spawner = new CustomSpawner(draw, sounds, textures, levelName,box,room);//temporary path
				
				gameUI = new UI(draw, sounds, textures, stats, room, game.inventory);
				this.controller = new Controller(stats, game.renderInfo);
				this.controller.add_controllable(room);
				Gdx.input.setInputProcessor(this.controller);
			}
			if(Gdx.input.isTouched())
				if(spawner.getUpdate() && play.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
					game.moveToGame(player, textures, stats, gameUI, box, spawner, room, controller);
				}
		}
		else
			complete = Interpolation.linear.apply(complete, (textures.a_manager.getProgress()+sounds.s_manager.getProgress()+spawner.getProgress())/4, 0.1f);
	}
	
	public void render(SpriteBatch batch) {
		draw.draw(Draw.TYPES.BACKGROUND, bg, 0, 0, 1, 1);
		if(done) {
			draw.clear();
			draw.draw(Draw.TYPES.BUTTON, play, ((draw.screenWidth-play.getBoundingRectangle().width)/2f)/draw.screenWidth, ((draw.screenHeight-play.getBoundingRectangle().height)/2f)/draw.screenHeight, 0.15f, 0.25f);
		}
		else
			draw.draw(Draw.TYPES.BUTTON, gear, ((draw.screenWidth-gear.getBoundingRectangle().width)/2f)/draw.screenWidth, ((draw.screenHeight-gear.getBoundingRectangle().height)/2f)/draw.screenHeight, 0.1f, 0.1f);
		draw.draw(batch);
	}
	
	public void dispose() {
	}
}