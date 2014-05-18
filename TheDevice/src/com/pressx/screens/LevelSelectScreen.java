package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Draw;
import com.pressx.managers.Levels;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;

public class LevelSelectScreen extends BaseState{
	static final int NUMBUTTONS = 8;
	static final int LBUTTON_BUTTONSPERROW = 4;
	static final float LBUTTON_OFFSETY_BASE = .4f;
	static final float LBUTTON_OFFSETY_MULT = -.3f;
	static final float LBUTTON_WIDTH = 3f/20;
	static final float LBUTTON_SCREENSPREADX = .7f;
	
	Draw draw;
	Textures textures;
	Levels level;
	
	Sprite spr_background;
	Sprite[] spr_buttons;
	
	public LevelSelectScreen(TheDevice g, Levels l){
		super(g);
		textures = g.textures;
		level = l;
		draw = new Draw();
		
		spr_background = getspr("levelselect_background");
		spr_buttons = new Sprite[NUMBUTTONS];
		for(int i = 0; i < NUMBUTTONS; i++)
			spr_buttons[i] = getspr("levelselect_button");
	}

	public Sprite getspr(String name){return new Sprite(textures.getArtAsset(name));}
	
	@Override
	public void create(){
	}
	
	@Override
	public void update(){
		float dt = Gdx.graphics.getDeltaTime();
		
		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();
		float relativemousex = (float)x/Gdx.graphics.getWidth();
		float relativemousey = (float)y/Gdx.graphics.getHeight();
		
		if(Gdx.input.isTouched()){
			if(spr_buttons[0].getBoundingRectangle().contains(x,y)){
				level.loadLevelAssets("level1");
				game.moveToSequence("Intro");
			}
		}
	}
	
	@Override
	public void render(SpriteBatch batch){
		/////Sizes and conversions
		int screensizex = Gdx.graphics.getWidth();
		int screensizey = Gdx.graphics.getHeight();
		float screenytox = (float)screensizey/screensizex;
		float screenxtoy = 1/screenytox;
		
		/////Draw background
		draw.draw(Draw.TYPES.BACKGROUND,spr_background,0,0,1f,1f);

		/////Draw buttons
		for(int i = 0; i < NUMBUTTONS; i++){
			float posx = (1-LBUTTON_SCREENSPREADX)/2+((float)(i%LBUTTON_BUTTONSPERROW))/(LBUTTON_BUTTONSPERROW-1)*LBUTTON_SCREENSPREADX-LBUTTON_WIDTH/2;
			float posy = LBUTTON_OFFSETY_BASE+LBUTTON_OFFSETY_MULT*(i/LBUTTON_BUTTONSPERROW); 
			draw.draw(Draw.TYPES.BUTTON,spr_buttons[i],posx,posy,LBUTTON_WIDTH,LBUTTON_WIDTH*screenxtoy);
			draw.write(""+(i+1),posx+LBUTTON_WIDTH/2-.01f,posy+LBUTTON_WIDTH/2*screenxtoy+.03f);
		}
		
		draw.draw(batch);
	}
	
	@Override
	public void dispose(){
	}
}
