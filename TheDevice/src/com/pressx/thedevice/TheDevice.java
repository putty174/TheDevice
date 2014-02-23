package com.pressx.thedevice;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.control.Controller;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.player.Player;
import com.pressx.screens.BaseState;
import com.pressx.screens.CutsceneScreen;
import com.pressx.screens.GameOverScreen;
import com.pressx.screens.GameScreen;
import com.pressx.screens.LoadingScreen;
import com.pressx.screens.MainMenuScreen;
import com.pressx.screens.ShopScreen;
import com.pressx.screens.TutorialScreen;
import com.pressx.screens.game.Room;
import com.pressx.screens.game.UI;
import com.pressx.spawner.CustomSpawner;

public class TheDevice implements ApplicationListener {
	private int NUMSTATES = 9;
	
	private Sounds sounds;
	private Textures textures;
	
	int currentState;
	BaseState[] posStates;
	
	
	public float[] renderInfo = 
		{
		100,				//Game Width
		100, 	//Game Height
		0,					//Scalar
		0,					//Camera X
		0,					//Camera Y
		};
	
	public TheDevice() {
	}

	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		posStates = new BaseState[NUMSTATES];
		
		sounds = new Sounds();
		textures = new Textures();
		
		float width = Gdx.graphics.getWidth();
		renderInfo[1] = Gdx.graphics.getHeight() * 100 / Gdx.graphics.getWidth();
		renderInfo[2] = width/renderInfo[0];
		
		moveToMain();
		//posStates[1]=new OptionsScreen(this, sounds, manager);
		
		
		currentState = 0;
		//sounds.playMusicLooping(1);
	}

	@Override
	public void dispose() {
		posStates[currentState].dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		SpriteBatch batch = new SpriteBatch();
		batch.begin();
		posStates[currentState].update();
		posStates[currentState].render(batch);
		batch.end();
		batch.dispose();
	}

	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}
	
	public void moveToMain(){
		sounds.unloadSoundAssets();
		textures.unloadArtAssets();
		sounds.loadSoundAssets(Sounds.PACKS.MAIN);
		textures.loadArtAssets("Main");
		sounds.playBGM();
		posStates[0] = new MainMenuScreen(this, sounds, textures);
		posStates[0].create();
		currentState = 0;
	}
	
	public void moveToTutorial(){
		textures.unloadArtAssets();
		textures.loadArtAssets("Tutorial");
		posStates[4] = new TutorialScreen(this, textures, sounds);
		posStates[4].create();
		currentState = 4;
	}
	
	public void moveToLoading() {
		sounds.stopBGM();
		sounds.unloadSoundAssets();
		textures.unloadArtAssets();
		sounds.loadSoundAssets(Sounds.PACKS.GAME);
		textures.loadArtAssets("Loading");
		posStates[7] = new LoadingScreen(this,sounds,textures);
		posStates[7].create();
		currentState = 7;
	}

	public void moveToSequence(String seq){
		textures.unloadArtAssets();
		textures.loadArtAssets(seq);
		if(seq.equals("Intro")){
			posStates[2]=new CutsceneScreen(this, textures, "Intro");
			currentState = 2;
		}
		else if(seq.equals("Outro")){
			posStates[6]=new CutsceneScreen(this, textures, "Outro");
			currentState = 6;
		}
	}
	
	public void moveToGame(Player player, Textures textures, GameStats stats, UI gameUI, GameObject box, CustomSpawner spawner, Room room, Controller controller){
		sounds.playBGM();
		posStates[3] = new GameScreen(this, player, sounds, textures, stats, gameUI, box, spawner, room, controller);
		posStates[3].create();
		currentState = 3;
	}
	
	public void moveToEnd(GameStats stats){
		sounds.stopBGM();
		sounds.unloadSoundAssets();
		sounds.loadSoundAssets(Sounds.PACKS.END);
		textures.unloadArtAssets();
		textures.loadArtAssets("End");
		posStates[5] = new GameOverScreen(this, sounds, textures, stats);
		posStates[5].create();
		currentState = 5;
	}
	
	public void moveToShop(){
		posStates[8] = new ShopScreen(this);
		posStates[8].create();
		currentState = 8;
	}
}
