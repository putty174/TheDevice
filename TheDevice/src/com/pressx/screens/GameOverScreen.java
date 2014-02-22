package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.GameStats;
import com.pressx.thedevice.TheDevice;

public class GameOverScreen extends BaseState {
	private Draw draw;
	private Sounds sounds;
	private Textures textures;
	
	Sprite gameOverImage, retry, quit;
	
	String score, monster, time;
	
	public GameOverScreen(TheDevice g, GameStats stats){
		super(g);
		draw = new Draw();
		sounds = new Sounds();
		textures = new Textures();
		sounds.loadSoundAssets(Sounds.PACKS.END);
		textures.loadArtAssets("End");
		gameOverImage = new Sprite(textures.getArtAsset("end_bg"));
		retry = new Sprite(textures.getArtAsset("end_retry"));
		quit = new Sprite(textures.getArtAsset("end_quit"));
		
		//Write Score
		score = "Final Score: " + stats.getScore();
		
		//Write Monster
		monster = "Monsters Slain: " + stats.getMonsterCount();
		
		//Write Time
		time = "Time Survived: " + stats.timeElapsed();
	}
	
	public void update(){
		if(Gdx.input.justTouched()){
			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();
			if(retry.getBoundingRectangle().contains(x,y)){
				sounds.play("buttonl");
				game.moveToLoading();
			}
			if(quit.getBoundingRectangle().contains(x,y)){
				sounds.play("buttonh");
				game.moveToSequence("Outro");
			}
		}
	}
	
	public void render(SpriteBatch batch){
		draw.draw(Draw.TYPES.BACKGROUND, gameOverImage, 0, 0, 1f, 1f);
		draw.draw(Draw.TYPES.BUTTON, retry, 0.2f, 0.445f, 0.27f, 0.13f);
		draw.draw(Draw.TYPES.BUTTON, quit, 0.55f, 0.445f, 0.27f, 0.13f);
		draw.write(score, ((draw.screenWidth - draw.font.getBounds(score).width) / 2) / draw.screenWidth, 1/3f);
		draw.write(monster, ((draw.screenWidth - draw.font.getBounds(monster).width) / 2) / draw.screenWidth, 1/3f - ((draw.font.getBounds(score).height * 1.2f)/draw.screenHeight));
		draw.write(time, ((draw.screenWidth - draw.font.getBounds(time).width) / 2) / draw.screenWidth, 1/3f - (((draw.font.getBounds(score).height * 1.2f) + (draw.font.getBounds(monster).height * 1.2f))/draw.screenHeight));
		
		draw.draw(batch);
	}

	public void create() {
	}
	
	public void dispose() {
	}
}