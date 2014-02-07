package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.managers.Graphics;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;

enum ItemState{NULL,LOCKED,UNLOCKED,EQUIPPED}

class ShopItem{
	public String name,description;
	public Sprite icon,background,button;
	private ItemState state = ItemState.NULL;
	public ShopItem(String name,String icon/*,String description*/){
		this.name = name;
		this.icon = ShopScreen.getspr(icon);
		this.description = name.toUpperCase()+" DESCRIPTION HERE";//description;
		background = ShopScreen.getspr("itembackground");
		setState(ItemState.LOCKED);
	}
	
	public void setState(ItemState s){
		if(state == s) return;
		state = s;
		switch(state){
		case LOCKED:
			button = ShopScreen.getspr("itembutton_buy");
			break;
		case UNLOCKED:
			button = ShopScreen.getspr("itembutton_equip");
			break;
		case EQUIPPED:
			button = ShopScreen.getspr("itembutton_unequip");
			break;
		}
	}
	public ItemState getState(){
		return state;
	}
}

public class ShopScreen extends BaseState {
	static final int NUMITEMS = 6;
	static final int NUMITEMSPERPAGE = 5;
	static final int NUMITEMSPERPAGE_MAX = NUMITEMSPERPAGE+1;
	
	static final float ITEMSCROLL_MAX = NUMITEMS-NUMITEMSPERPAGE;
	static final float ITEMSCROLL_SLIPPERINESS = .1f;//velocity multiplier per second
	static final float ITEMSCROLL_BOUNCINESSONMAX = .002f;//how much it "stretches" when hitting the top or bottom
	
	static final float ITEMUNIT_OFFSETX = .5f;
	static final float ITEMUNIT_OFFSETMULTY = 1f/NUMITEMSPERPAGE;
	static final float ITEMUNIT_WIDTH = .45f;
	static final float ITEMUNIT_HEIGHT = ITEMUNIT_OFFSETMULTY-.02f;
	static final float ITEMUNIT_OFFSETMULTY_HEIGHT_DIFF = ITEMUNIT_OFFSETMULTY-ITEMUNIT_HEIGHT;
	static final float SELECTEDITEMUNITBACKGROUNDRATIO_X = 680f/634;
	static final float SELECTEDITEMUNITBACKGROUNDRATIO_Y = 255f/209;
	static final float ITEMICON_EXTRASPACE = .015f;
	static final float ITEMICON_WIDTH = ITEMUNIT_HEIGHT-ITEMICON_EXTRASPACE*2;
	static final float ITEMICON_HEIGHT= ITEMUNIT_HEIGHT-ITEMICON_EXTRASPACE*2;
	static final float ITEMICON_OFFSETX = ITEMICON_EXTRASPACE;
	static final float ITEMICON_OFFSETY = ITEMICON_EXTRASPACE;
	static final float ITEMTITLE_OFFSETX = ITEMUNIT_OFFSETX+ITEMICON_OFFSETX*2+ITEMICON_WIDTH;
	static final float ITEMTITLE_OFFSETY = ITEMUNIT_HEIGHT/2+.05f;
	static final float ITEMBUTTON_WIDTH = ITEMUNIT_WIDTH/2;
	static final float ITEMBUTTON_HEIGHT = ITEMUNIT_HEIGHT/5;
	static final float ITEMBUTTON_OFFSETX = ITEMUNIT_OFFSETX+.2f;
	static final float ITEMBUTTON_OFFSETY = ITEMUNIT_HEIGHT/20;
	
	Sprite spr_background,spr_backbutton,spr_itemselectback;
	ShopItem[] items;
	
	ShopItem selectedItem = null;
	
	public static Sprite getspr(String name){return new Sprite(Textures.getArtAsset(name));}//also used by ShopItem

	public ShopScreen(){
		super();
		spr_background = getspr("shop_background");
		spr_backbutton = getspr("shop_backbutton");
		spr_itemselectback = getspr("itembackground_selected");
		items = new ShopItem[6];//temporary
		items[0] = new ShopItem("Mine","item0");
		items[0].setState(ItemState.EQUIPPED);
		items[1] = new ShopItem("Poison","item1");
		items[2] = new ShopItem("Thunderstrike","item2");
		items[3] = new ShopItem("Healing Bot","item3");
		items[4] = new ShopItem("Insta-cactus","item4");
		items[5] = new ShopItem("Smoke Bomb","item5");
	}
	
	public void create(){}
	public void dispose(){}

	float itemscrollvelocity = 5;
	float itemscroll = 0;
	public void render(SpriteBatch batch){
		Graphics.draw(Graphics.TYPES.BACKGROUND,spr_background,0,0,1f,1f);
		Graphics.draw(Graphics.TYPES.BUTTON,spr_backbutton,.05f,.9f,.15f,.075f);
		//Draw items
		for(int i = (int)itemscroll; i < (int)itemscroll+NUMITEMSPERPAGE_MAX; i++){
			if(i >= items.length) break;
			if(i < 0) continue;
			float posy = 1-ITEMUNIT_OFFSETMULTY*(-itemscroll+i+1)-ITEMUNIT_OFFSETMULTY_HEIGHT_DIFF;
			if(items[i] == selectedItem){
				float sizex = ITEMUNIT_WIDTH*SELECTEDITEMUNITBACKGROUNDRATIO_X;
				float sizey = ITEMUNIT_HEIGHT*SELECTEDITEMUNITBACKGROUNDRATIO_Y;
				Graphics.draw(Graphics.TYPES.UI,spr_itemselectback,ITEMUNIT_OFFSETX+ITEMUNIT_WIDTH/2-sizex/2,posy+ITEMUNIT_HEIGHT/2-sizey/2,sizex,sizey);
			}
			Graphics.draw(Graphics.TYPES.BUTTON,items[i].background,ITEMUNIT_OFFSETX,posy,ITEMUNIT_WIDTH,ITEMUNIT_HEIGHT);
			Graphics.draw(Graphics.TYPES.EXTRAS,items[i].icon,ITEMUNIT_OFFSETX+ITEMICON_OFFSETX,posy+ITEMICON_OFFSETY,ITEMICON_WIDTH,ITEMICON_HEIGHT);
			Graphics.draw(Graphics.TYPES.EXTRAS,items[i].button,ITEMBUTTON_OFFSETX,posy+ITEMBUTTON_OFFSETY,ITEMBUTTON_WIDTH,ITEMBUTTON_HEIGHT);
			Graphics.write(items[i].name,ITEMTITLE_OFFSETX,posy+ITEMTITLE_OFFSETY);
		}

		if(selectedItem != null){
			Graphics.write(selectedItem.name, 0.1f, 0.5f);
			Graphics.write(selectedItem.description, 0.1f, 0.4f);
		}
	}
	
	float mousehelddownfor = -1;
	float lastmouseposx,lastmouseposy;
	public void update(){
		float dt = Gdx.graphics.getDeltaTime();
		
		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();
		float relativemousex = (float)x/Gdx.graphics.getWidth();
		float relativemousey = (float)y/Gdx.graphics.getHeight();
		
		if(Gdx.input.isTouched()){
			if(mousehelddownfor < 0)
				mousehelddownfor = 0;
			else{
				mousehelddownfor += dt;
				if(relativemousex > ITEMUNIT_OFFSETX && relativemousex < ITEMUNIT_OFFSETX+ITEMUNIT_WIDTH){
					float b = itemscroll < 0 ? -itemscroll+1 : itemscroll > ITEMSCROLL_MAX ? itemscroll-ITEMSCROLL_MAX+1 : 1;
					float a = (relativemousey-lastmouseposy)*NUMITEMSPERPAGE/b;
					itemscrollvelocity = a/dt;
					itemscroll += a;
				}
			}
			lastmouseposx = relativemousex;
			lastmouseposy = relativemousey;
		}else{
			itemscroll += itemscrollvelocity*dt;
			if(itemscroll < 0 || itemscroll > ITEMSCROLL_MAX){
				itemscrollvelocity = 0;
				float lerp = (float)Math.pow(ITEMSCROLL_BOUNCINESSONMAX,dt);
				itemscroll = itemscroll*(lerp)+(itemscroll < 0 ? 0 : ITEMSCROLL_MAX)*(1-lerp);
			}//else
				itemscrollvelocity *= Math.pow(ITEMSCROLL_SLIPPERINESS,dt);
			
			if(mousehelddownfor >= 0 && mousehelddownfor < .5f){
				for(int i = 0; i < items.length; i++){
					if(items[i].background.getBoundingRectangle().contains(x,y)){
						selectedItem = items[i];
						break;
					}
				}
				if(spr_backbutton.getBoundingRectangle().contains(x,y)){
					TheDevice.moveToMain();
				}
			}
			mousehelddownfor = -1;
		}
	}
}