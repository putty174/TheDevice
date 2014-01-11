package com.pressx.thedevice;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Graphics;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.screens.BaseState;
import com.pressx.screens.CutsceneScreen;
import com.pressx.screens.GameOverScreen;
import com.pressx.screens.GameScreen;
import com.pressx.screens.LoadingScreen;
import com.pressx.screens.MainMenuScreen;
import com.pressx.screens.TutorialScreen;
import com.pressx.social.Social;

public final class TheDevice implements ApplicationListener {
	static int currentState;
	static BaseState[] posStates;
	GameStats stats;
	Textures manager;
	static Social fb;
	
	public static float[] renderInfo = 
		{
		100,				//Game Width
		100, 	//Game Height
		0,					//Scalar
		0,					//Camera X
		0,					//Camera Y
		};

	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		//These MUST be called, even if you do nothing with it;
		new Sounds();
		new Textures();
		new Graphics();
		
		posStates = new BaseState[8];
		
		float width = Gdx.graphics.getWidth();
		renderInfo[1] = Graphics.screenHeight * 100 / Graphics.screenWidth;
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
		Graphics.clearAll();
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		SpriteBatch batch = new SpriteBatch();
		batch.begin();
		posStates[currentState].update();
		posStates[currentState].render(batch);
		Graphics.draw();
		batch.end();
		batch.dispose();
	}

	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}
	
	public static void moveToLoading() {
		Textures.unloadArtAssets();
		Textures.loadArtAssets("Loading");
		posStates[7] = new LoadingScreen();
		posStates[7].create();
		currentState = 7;
	}
	
	public static void moveToMain(){
		Sounds.stopBGM();
		Sounds.stopSound();
		Sounds.unloadSoundAssets();
		Sounds.loadSoundAssets(Sounds.PACKS.MAIN);
		Sounds.playBGM();
		Textures.unloadArtAssets();
		Textures.loadArtAssets("Main");
		posStates[0] = new MainMenuScreen(fb);
		posStates[0].create();
		currentState = 0;
	}

	public static void moveToSequence(String seq){
		Textures.unloadArtAssets();
		if(seq.equals("Intro")){
			Textures.loadArtAssets("Intro");
			posStates[2]=new CutsceneScreen("Intro", 5);
			currentState = 2;
		}
		else if(seq.equals("Outro")){
			Textures.loadArtAssets("Outro");
			posStates[6]=new CutsceneScreen("Outro", 3);
			currentState = 6;
		}
	}
	
	public static void moveToGame(){
		Sounds.stopBGM();
		Sounds.stopSound();
		Sounds.unloadSoundAssets();
		Sounds.loadSoundAssets(Sounds.PACKS.GAME);
		Sounds.playBGM();
		Textures.unloadArtAssets();
		Textures.loadArtAssets("Game");
		posStates[3] = new GameScreen();
		posStates[3].create();
		currentState = 3;
	}
	
	public static void moveToTutorial(){
		Textures.unloadArtAssets();
		Textures.loadArtAssets("Tut");
		posStates[4] = new TutorialScreen();
		posStates[4].create();
		currentState = 4;
	}
	
	public static void moveToEnd(){
		Sounds.stopBGM();
		Sounds.stopSound();
		Sounds.unloadSoundAssets();
		Sounds.loadSoundAssets(Sounds.PACKS.END);
		Sounds.play("laugh");
		Textures.unloadArtAssets();
		Textures.loadArtAssets("End");
		posStates[5] = new GameOverScreen();
		posStates[5].create();
		currentState = 5;
	}
}
