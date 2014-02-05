package com.pressx.screens;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Graphics;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.social.Social;
import com.pressx.thedevice.TheDevice;

class ShopItem{
	public String name;
	public Sprite icon,background;
	public ShopItem(String name,Sprite icon){
		this.name = name;
		this.icon = icon;
		background = new Sprite(Textures.getArtAsset("itembackground"));
	}
}

public class ShopScreen extends BaseState {
	static final int NUMITEMS = 5;
	static final int NUMITEMSPERPAGE = 5;
	static final float ITEMUNIT_OFFSETX = .5f;
	static final float ITEMUNIT_OFFSETMULTY = 1f/NUMITEMSPERPAGE;
	static final float ITEMUNIT_WIDTH = .45f;
	static final float ITEMUNIT_HEIGHT = ITEMUNIT_OFFSETMULTY-.02f;
	static final float ITEMICON_EXTRASPACE = .015f;
	static final float ITEMICON_WIDTH = ITEMUNIT_HEIGHT-ITEMICON_EXTRASPACE*2;
	static final float ITEMICON_HEIGHT= ITEMUNIT_HEIGHT-ITEMICON_EXTRASPACE*2;
	static final float ITEMICON_OFFSETX = ITEMICON_EXTRASPACE;
	static final float ITEMICON_OFFSETY = ITEMICON_EXTRASPACE;
	static final float ITEMTITLE_OFFSETX = ITEMUNIT_OFFSETX+ITEMICON_OFFSETX*2+ITEMICON_WIDTH;
	static final float ITEMTITLE_OFFSETY = ITEMUNIT_HEIGHT/2+.05f;
	
	Sprite spr_background;
	ShopItem[] items;
	
	ShopItem selectedItem = null;
	
	static Sprite getspr(String name){return new Sprite(Textures.getArtAsset(name));}

	public ShopScreen(){
		super();
		spr_background = getspr("shop_background");
		items = new ShopItem[5];//temporary
		items[0] = new ShopItem("Aero Dash",getspr("item0"));
		items[1] = new ShopItem("Incinerate",getspr("item1"));
		items[2] = new ShopItem("Electro-blast",getspr("item2"));
		items[3] = new ShopItem("Insect Swarm",getspr("item3"));
		items[4] = new ShopItem("Telekinesis",getspr("item4"));
	}
	
	public void create(){}
	public void dispose(){}

	public void render(SpriteBatch batch){
		Graphics.draw(Graphics.TYPES.BACKGROUND,spr_background,0,0,1f,1f);
		for(int i = 0; i < items.length; i++){
			float posy = 1-ITEMUNIT_OFFSETMULTY*(i+1);
			Graphics.draw(Graphics.TYPES.BUTTON,items[i].background,ITEMUNIT_OFFSETX,posy,ITEMUNIT_WIDTH,ITEMUNIT_HEIGHT);
			Graphics.draw(Graphics.TYPES.EXTRAS,items[i].icon,ITEMUNIT_OFFSETX+ITEMICON_OFFSETX,posy+ITEMICON_OFFSETY,ITEMICON_WIDTH,ITEMICON_HEIGHT);
			Graphics.write(items[i].name,ITEMTITLE_OFFSETX,posy+ITEMTITLE_OFFSETY);
		}

		if(selectedItem != null)
			Graphics.write(selectedItem.name, 0.1f, 0.5f);
	}
	
	public void update(){
		if(Gdx.input.justTouched()){
			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();
			for(int i = 0; i < items.length; i++){
				if(items[i].background.getBoundingRectangle().contains(x,y)){
					selectedItem = items[i];
					break;
				}
			}
			/*
			if(play.getBoundingRectangle().contains(x, y))
			{
				Sounds.play("buttonl");
				TheDevice.moveToSequence("Intro");
			}
			else if(help.getBoundingRectangle().contains(x,y))
			{
				Sounds.play("buttonl");
				TheDevice.moveToTutorial();
			}
			else if(post.getBoundingRectangle().contains(x,y))
			{
				fb.switchActivity();
			}*/
		}
	}
}