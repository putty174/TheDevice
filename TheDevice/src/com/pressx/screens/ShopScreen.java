package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.items.ShopItem;
import com.pressx.items.ShopItem.ShopItemState;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;


public class ShopScreen extends BaseState {
	private Draw draw;
	private Sounds sounds;
	private Textures textures;
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
	static final float ITEMICON_HEIGHT = ITEMUNIT_HEIGHT-ITEMICON_EXTRASPACE*2;
	static final float ITEMICON_WIDTH = ITEMICON_HEIGHT*3/4;//not really used; calculated from screen size to have square icons
	static final float ITEMICON_OFFSETX = ITEMICON_EXTRASPACE;
	static final float ITEMICON_OFFSETY = ITEMICON_EXTRASPACE;
	static final float ITEMTITLE_OFFSETX = ITEMUNIT_OFFSETX+ITEMICON_OFFSETX*2+ITEMICON_WIDTH;
	static final float ITEMTITLE_OFFSETY = ITEMUNIT_HEIGHT/2+.05f;
	static final float ITEMBUTTON_WIDTH = ITEMUNIT_WIDTH/2.3f/1.2f;
	static final float ITEMBUTTON_HEIGHT = ITEMUNIT_HEIGHT/2.5f/1.2f;
	static final float ITEMBUTTON_OFFSETX = ITEMUNIT_OFFSETX+.2f;
	static final float ITEMBUTTON_OFFSETY = 0;//ITEMUNIT_HEIGHT/20;
	
	int numExperience = 1337;//temporary
	Sprite spr_background,spr_backbutton,spr_itemselectback,spr_experience;
	Sprite spr_arrow_up,spr_arrow_down;
	ShopItem[] items;

	float itemscrollvelocity,itemscroll;//the velocity it will scroll at (ITEMUNIT_OFFSETMULTY/second) if the mouse is not held down and how far it's scrolled down (ITEMUNIT_OFFSETMULTY) 
	int animcycle;//incremented by 1 every update (for animations)
	
	ShopItem selectedItem = null;

	public ShopScreen(TheDevice g){
		super(g);
		draw = new Draw();
		sounds = new Sounds();
		textures = new Textures();
		sounds.loadSoundAssets(Sounds.PACKS.SHOP);
		textures.loadArtAssets("Shop");
		spr_background = getspr("shop_background");
		spr_backbutton = getspr("shop_backbutton");
		spr_itemselectback = getspr("itembackground_selected");
		spr_experience = getspr("exp");

		spr_arrow_up = getspr("uparrow");
		spr_arrow_down = getspr("uparrow");
		
		items = new ShopItem[6];//temporary
		items[0] = new ShopItem(textures,"Mine","item0");
		items[0].setState(ShopItemState.EQUIPPED);
		items[1] = new ShopItem(textures,"Poison","item1");
		items[2] = new ShopItem(textures,"Thunderstrike","item2");
		items[3] = new ShopItem(textures,"Healing Bot","item3");
		items[4] = new ShopItem(textures,"Insta-cactus","item4");
		items[5] = new ShopItem(textures,"Smoke Bomb","item5");
	}
	
	public Sprite getspr(String name)
	{
		return new Sprite(textures.getArtAsset(name));
	}//also used by ShopItem
	
	public void create(){}
	public void dispose(){}

	public void render(SpriteBatch batch){
		int screensizex = Gdx.graphics.getWidth();
		int screensizey = Gdx.graphics.getHeight();
		float screenytox = (float)screensizey/screensizex;
		@SuppressWarnings("unused")
		float screenxtoy = (float)screensizex/screensizey;
		animcycle++;
		
		//Draw background and "Back" button
		draw.draw(Draw.TYPES.BACKGROUND,spr_background,0,0,1f,1f);
		draw.draw(Draw.TYPES.BUTTON,spr_backbutton,.05f,.9f,.15f,.075f);
		
		//Draw experience count
		int anim_exp = (animcycle/5)%4;
		spr_experience.setRegion(30*(anim_exp == 0 ? 0 : anim_exp == 2 ? 2 : 1), 0, 30, 30);
		draw.draw(Draw.TYPES.BUTTON,spr_experience,.275f,.9f,1f/20,/*.075f*/1f/15f);
		draw.write(""+numExperience,.33f,.975f);
		
		//Draw items
		for(int i = (int)itemscroll; i < (int)itemscroll+NUMITEMSPERPAGE_MAX; i++){
			if(i >= items.length) break;
			if(i < 0) continue;
			float posy = 1-ITEMUNIT_OFFSETMULTY*(-itemscroll+i+1)-ITEMUNIT_OFFSETMULTY_HEIGHT_DIFF;
			
			if(items[i] == selectedItem){
				float sizex = ITEMUNIT_WIDTH*SELECTEDITEMUNITBACKGROUNDRATIO_X;
				float sizey = ITEMUNIT_HEIGHT*SELECTEDITEMUNITBACKGROUNDRATIO_Y;
				int anim_selection = (animcycle/5)%4;
				spr_itemselectback.setRegion(0,255*anim_selection,680,255);
				draw.draw(Draw.TYPES.EXTRAS,spr_itemselectback,ITEMUNIT_OFFSETX+ITEMUNIT_WIDTH/2-sizex/2,posy+ITEMUNIT_HEIGHT/2-sizey/2,sizex,sizey);
			}
			
			draw.draw(Draw.TYPES.BUTTON,items[i].background,ITEMUNIT_OFFSETX,posy,ITEMUNIT_WIDTH,ITEMUNIT_HEIGHT);
			draw.draw(Draw.TYPES.EXTRAS,items[i].icon,ITEMUNIT_OFFSETX+ITEMICON_OFFSETX,posy+ITEMICON_OFFSETY,ITEMICON_HEIGHT*screenytox,ITEMICON_HEIGHT);
			draw.draw(Draw.TYPES.EXTRAS,items[i].button,ITEMBUTTON_OFFSETX,posy+ITEMBUTTON_OFFSETY,ITEMBUTTON_WIDTH,ITEMBUTTON_HEIGHT);
			draw.write(items[i].name,ITEMTITLE_OFFSETX,posy+ITEMTITLE_OFFSETY);
		}

		if(selectedItem != null){
			draw.write(selectedItem.name, 0f, 0.5f);
			draw.write(selectedItem.description, 0f, 0.4f);
		}
		
		int anim_arrow = (animcycle)%14;
		if(itemscroll > 0){
			draw.draw(Draw.TYPES.BUTTON,spr_arrow_up,.97f,.8f+.002f*anim_arrow,.02f,.15f);
		}
		anim_arrow = (animcycle+7)%14;
		if(itemscroll < ITEMSCROLL_MAX){
			draw.draw(Draw.TYPES.BUTTON,spr_arrow_down,.97f,.2f-.002f*anim_arrow,.02f,-.15f);
		}
		
		draw.draw(batch);
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
					game.moveToMain();
				}
			}
			mousehelddownfor = -1;
		}
	}
}