package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Graphics;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.GameStats;
import com.pressx.thedevice.TheDevice;

public class GameOverScreen extends BaseState {
	Sprite gameOverImage, retry, quit;
	
	String score, monster, time;
	
	public GameOverScreen(){
		gameOverImage = new Sprite(Textures.getArtAsset("end_bg"));
		retry = new Sprite(Textures.getArtAsset("end_retry"));
		quit = new Sprite(Textures.getArtAsset("end_quit"));
		
		//Write Score
		score = "Final Score: " + GameStats.getScore();
		
		//Write Monster
		monster = "Monsters Slain: " + GameStats.getMonsterCount();
		
		//Write Time
		time = "Time Survived: " + GameStats.timeElapsed();
	}
	
	public void update(){
		if(Gdx.input.justTouched()){
			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();
			if(retry.getBoundingRectangle().contains(x,y)){
				Sounds.play("buttonl");
				TheDevice.moveToLoading();
			}
			if(quit.getBoundingRectangle().contains(x,y)){
				Sounds.play("buttonh");
				TheDevice.moveToSequence("Outro");
			}
		}
	}
	
	public void render(SpriteBatch batch){
		Graphics.draw(Graphics.TYPES.BACKGROUND, gameOverImage, 0, 0, 1f, 1f);
		Graphics.draw(Graphics.TYPES.BUTTON, retry, 0.2f, 0.445f, 0.27f, 0.13f);
		Graphics.draw(Graphics.TYPES.BUTTON, quit, 0.55f, 0.445f, 0.27f, 0.13f);
		Graphics.write(score, ((Graphics.screenWidth - Graphics.font.getBounds(score).width) / 2) / Graphics.screenWidth, 1/3f);
		Graphics.write(monster, ((Graphics.screenWidth - Graphics.font.getBounds(monster).width) / 2) / Graphics.screenWidth, 1/3f - ((Graphics.font.getBounds(score).height * 1.2f)/Graphics.screenHeight));
		Graphics.write(time, ((Graphics.screenWidth - Graphics.font.getBounds(time).width) / 2) / Graphics.screenWidth, 1/3f - (((Graphics.font.getBounds(score).height * 1.2f) + (Graphics.font.getBounds(monster).height * 1.2f))/Graphics.screenHeight));
	}

	public void create() {
	}
	
	public void dispose() {
	}
}