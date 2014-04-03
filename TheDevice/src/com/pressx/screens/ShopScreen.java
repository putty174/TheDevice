package com.pressx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.gadgets.Item;
import com.pressx.gadgets.ShopItem;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.TheDevice;


public class ShopScreen extends BaseState {
	private Draw draw;
	private Sounds sounds;
	private Textures textures;
	
	final float NUMITEMSPERPAGE = 4.4f;//an exact value can look confusing
	final float NUMITEMSPERPAGE_MAX = NUMITEMSPERPAGE+1;

	final float ITEMSCROLL_SLIPPERINESS = .1f;//multiplied to velocity per second
	final float ITEMSCROLL_BOUNCINESSONMAX = .002f;//how much it "stretches" when hitting the top or bottom
	
	final float ITEMUNIT_OFFSETX = .5f;//ITEMUNIT: The list of items
	final float ITEMUNIT_OFFSETMULTY = 1f/NUMITEMSPERPAGE;
	final float ITEMUNIT_WIDTH = .45f;
	final float ITEMUNIT_HEIGHT = ITEMUNIT_OFFSETMULTY-.02f;
	final float ITEMUNIT_OFFSETMULTY_HEIGHT_DIFF = ITEMUNIT_OFFSETMULTY-ITEMUNIT_HEIGHT;
	final float SELECTEDITEMUNITBACKGROUNDRATIO_X = 680f/634;//SELECTEDITEMUNITBACKGROUNDRATIO: The animated flashing effect for the currently-selected item
	final float SELECTEDITEMUNITBACKGROUNDRATIO_Y = 255f/209;
	final float ITEMICON_EXTRASPACE = .015f;//Space between the top side of the itemunit and the top of the item icon 
	final float ITEMICON_HEIGHT = ITEMUNIT_HEIGHT-ITEMICON_EXTRASPACE*2;
	//final float ITEMICON_WIDTH = ITEMICON_HEIGHT*3/4;//not used; instead calculated from screen size to make square icons
	final float ITEMICON_OFFSETX = ITEMICON_EXTRASPACE;
	final float ITEMICON_OFFSETY = ITEMICON_EXTRASPACE;
	final float ITEMTITLE_OFFSETX = ITEMUNIT_OFFSETX+ITEMICON_OFFSETX*2+ITEMICON_HEIGHT*3/4;//ITEMTITLE: Name of item on the itemunits
	final float ITEMTITLE_OFFSETY = ITEMUNIT_HEIGHT/2+.05f;
	final float ITEMBUTTON_WIDTH = ITEMUNIT_WIDTH/2.1f/1.2f;//ITEMBUTTON: Small nonfunctional "button" on every itemunit
	final float ITEMBUTTON_HEIGHT = ITEMUNIT_HEIGHT/2.5f/1.2f;
	final float ITEMBUTTON_OFFSETX = ITEMUNIT_OFFSETX+.2f;
	final float ITEMBUTTON_OFFSETY = 0;
	final float ITEMDESC_OFFSETX = .01f;//ITEMDESC: Selected item's description box
	final float ITEMDESC_OFFSETY = 0f;
	final float ITEMDESC_WIDTH = .46f;
	final float ITEMDESC_HEIGHT = .9f;
	final float ITEMDESCBUTTON_WIDTH = ITEMDESC_WIDTH/2.5f;//ITEMDESCBUTTON: The "Build", "Equip", "Unequip", or "Loadout Full" button above the description of the selected item
	final float ITEMDESCBUTTON_HEIGHT = ITEMDESC_HEIGHT/9;
	final float ITEMDESCBUTTON_OFFSETX = ITEMDESC_OFFSETX+ITEMDESC_WIDTH/2;
	final float ITEMDESCBUTTON_OFFSETY = ITEMDESC_OFFSETY+ITEMDESC_HEIGHT-ITEMDESC_HEIGHT/3;
	final float ITEMLARGEICON_OFFSETX = ITEMDESC_OFFSETX+.06f;//ITEMLARGEICON: The icon above the description of the selected item
	final float ITEMLARGEICON_OFFSETY = ITEMDESC_OFFSETY+.628f;
	final float ITEMLARGEICON_WIDTH = 2f/20;
	final float ITEMLARGEICON_HEIGHT = 2f/15;
	final float ITEMLARGEUPGRADE_OFFSETX = ITEMDESCBUTTON_OFFSETX;//ITEMLARGEUPGRADE: The "Upgrade" button below the description
	final float ITEMLARGEUPGRADE_OFFSETY = ITEMDESC_OFFSETY+.05f;
	final float ITEMLARGEUPGRADE_WIDTH = ITEMDESCBUTTON_WIDTH;
	final float ITEMLARGEUPGRADE_HEIGHT = ITEMDESCBUTTON_HEIGHT;
	final float LOADOUT_WIDTH = .55f;//LOADOUT: The bar at the bottom with the two currently-equipped items
	final float LOADOUT_HEIGHT = ITEMICON_HEIGHT;//+.05f;
	final float LOADOUT_OFFSETX = 1-LOADOUT_WIDTH;
	final float LOADOUT_OFFSETY = 0;
	final float LOADOUTICON0_OFFSETX_CENTER = LOADOUT_OFFSETX+LOADOUT_WIDTH/4+.025f;
	final float LOADOUTICON1_OFFSETX_CENTER = LOADOUT_OFFSETX+LOADOUT_WIDTH*3/4+.025f;
	final float LOADOUTICON_OFFSETY = LOADOUT_HEIGHT-ITEMICON_HEIGHT-.0125f;
	
	float ITEMSCROLL_MAX;
	
	Sprite spr_background,spr_backbutton,spr_itemselectback,spr_experience;
	Sprite spr_arrow_up,spr_arrow_down;
	Sprite spr_desc_background,spr_desc_icon;
	Sprite[] spr_desc_bigbuttons;
	Sprite spr_desc_bigbutton_current;
	Sprite spr_bigbutton_upgrade;
	Sprite spr_loadoutbar;
	Sprite spr_loadouticon0,spr_loadouticon1;
	ShopItem[] items;

	float itemscrollvelocity,itemscroll;//the velocity it will scroll at (ITEMUNIT_OFFSETMULTY/second) if the mouse is not held down and how far it's scrolled down (ITEMUNIT_OFFSETMULTY) 
	int animcycle;//incremented by 1 every update (for animations)
	boolean draggingitemlist;
	
	ShopItem selectedItem = null;
	
	boolean upgradeScreenOpen;

	public ShopScreen(TheDevice g){
		super(g);
		ITEMSCROLL_MAX = g.inventory.allItems.length-NUMITEMSPERPAGE+LOADOUT_HEIGHT/ITEMUNIT_HEIGHT;
		
		draw = new Draw();
		sounds = g.sounds;//new Sounds();
		textures = g.textures;//new Textures();
		sounds.loadSoundAssets(Sounds.PACKS.SHOP);
		textures.loadArtAssets("Shop");
		spr_background = getspr("shop_background");
		spr_backbutton = getspr("shop_backbutton");
		spr_itemselectback = getspr("itembackground_selected");
		spr_experience = getspr("exp");

		spr_arrow_up = getspr("uparrow");
		spr_arrow_down = getspr("uparrow");
		
		spr_desc_background = getspr("itemdescbackground");
		
		spr_loadoutbar = getspr("loadoutbar");
		
		spr_desc_bigbuttons = new Sprite[4];
		spr_desc_bigbuttons[0] = getspr("itembutton_loadoutfull_big");
		spr_desc_bigbuttons[1] = getspr("itembutton_buy_big");
		spr_desc_bigbuttons[2] = getspr("itembutton_equip_big");
		spr_desc_bigbuttons[3] = getspr("itembutton_unequip_big");
		spr_bigbutton_upgrade = getspr("itembutton_upgrade_big");
		
		items = game.inventory.allItems;
		
		for(ShopItem item : items)
			item.initializeForShop();
		game.inventory.updateAllItemShopButtons();
		updateLoadoutSprites();
	}
	
	void updateLoadoutSprites(){
		if(game.inventory.item0 != null)
			spr_loadouticon0 = new Sprite(game.inventory.item0.icon);
		if(game.inventory.item1 != null)
			spr_loadouticon1 = new Sprite(game.inventory.item1.icon); 
	}
	
	public Sprite getspr(String name)
	{
		return new Sprite(textures.getArtAsset(name));
	}//also used by ShopItem
	
	public void create(){}
	public void dispose(){}

	public void render(SpriteBatch batch){
		draw.clear();
		
		int screensizex = Gdx.graphics.getWidth();
		int screensizey = Gdx.graphics.getHeight();
		float screenytox = (float)screensizey/screensizex;
		float screenxtoy = 1/screenytox;
		float itemiconwidth = ITEMICON_HEIGHT*screenytox;
		animcycle++;
		
		//Draw background and "Back" button
		draw.draw(Draw.TYPES.BACKGROUND,spr_background,0,0,1f,1f);
		draw.draw(Draw.TYPES.BUTTON,spr_backbutton,.05f,.9f,.15f,.075f);
		
		//Draw experience count
		int anim_exp = (animcycle/5)%4;
		spr_experience.setRegion(128*(anim_exp == 0 ? 0 : anim_exp == 2 ? 2 : 1), 0, 128, 128);
		draw.draw(Draw.TYPES.BUTTON,spr_experience,.275f,.9f,1f/20,/*.075f*/1f/15f);
		draw.write(""+game.inventory.numExperience,.33f,.975f);
		
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
				int anim = (animcycle/4)%6;
				anim = anim == 5 ? 1 : anim == 4 ? 2 : anim;
				items[i].icon.setRegion(128*anim,0,128,128);
			}else{
				items[i].icon.setRegion(0,0,128,128);
			}
			draw.draw(Draw.TYPES.EXTRAS,items[i].icon,ITEMUNIT_OFFSETX+ITEMICON_OFFSETX,posy+ITEMICON_OFFSETY,itemiconwidth,ITEMICON_HEIGHT);
			draw.draw(Draw.TYPES.BUTTON,items[i].background,ITEMUNIT_OFFSETX,posy,ITEMUNIT_WIDTH,ITEMUNIT_HEIGHT);
			draw.draw(Draw.TYPES.EXTRAS,items[i].button,ITEMBUTTON_OFFSETX,posy+ITEMBUTTON_OFFSETY,ITEMBUTTON_WIDTH,ITEMBUTTON_HEIGHT);
			draw.write(items[i].name,ITEMTITLE_OFFSETX,posy+ITEMTITLE_OFFSETY);
		}
		
		//Draw animated arrows on the right
		int anim_arrow = (animcycle)%14;
		if(itemscroll > 0){
			draw.draw(Draw.TYPES.BUTTON,spr_arrow_up,.97f,.8f+.002f*anim_arrow,.02f,.15f);
		}
		anim_arrow = (animcycle+7)%14;
		if(itemscroll < ITEMSCROLL_MAX){
			draw.draw(Draw.TYPES.BUTTON,spr_arrow_down,.97f,.2f-.002f*anim_arrow+LOADOUT_HEIGHT+LOADOUT_OFFSETY,.02f,-.15f);
		}
		
		//Draw description
		if(selectedItem != null){
			draw.draw(Draw.TYPES.UI,spr_desc_background,ITEMDESC_OFFSETX,ITEMDESC_OFFSETY,ITEMDESC_WIDTH,ITEMDESC_HEIGHT);
			draw.write(selectedItem.name, .1f, 0.85f);
			draw.write(selectedItem.description, 0f, 0.4f);
			ShopItem.ShopItemState blah = selectedItem.getState();
			if(blah == ShopItem.ShopItemState.UNLOCKED && game.inventory.checkLoadoutFull())
				blah = ShopItem.ShopItemState.NULL;
			spr_desc_bigbutton_current = spr_desc_bigbuttons[blah.ordinal()];
			if(blah == ShopItem.ShopItemState.LOCKED && game.inventory.numExperience < selectedItem.getCost())
				spr_desc_bigbutton_current.setColor(new Color(.5f,.5f,1,1));
			else
				spr_desc_bigbutton_current.setColor(new Color(1,1,1,1));
			draw.draw(Draw.TYPES.BUTTON,spr_desc_bigbutton_current,ITEMDESCBUTTON_OFFSETX,ITEMDESCBUTTON_OFFSETY,ITEMDESCBUTTON_WIDTH,ITEMDESCBUTTON_HEIGHT);
			draw.draw(Draw.TYPES.BUTTON,selectedItem.largeicon,ITEMLARGEICON_OFFSETX,ITEMLARGEICON_OFFSETY,ITEMLARGEICON_WIDTH,ITEMLARGEICON_HEIGHT);
			draw.draw(Draw.TYPES.BUTTON,spr_bigbutton_upgrade,ITEMLARGEUPGRADE_OFFSETX,ITEMLARGEUPGRADE_OFFSETY,ITEMLARGEUPGRADE_WIDTH,ITEMLARGEUPGRADE_HEIGHT);
		}

		//Draw loadout bar and two equipped items
		draw.draw(Draw.TYPES.SUPEREXTRAS,spr_loadoutbar,LOADOUT_OFFSETX,LOADOUT_OFFSETY,LOADOUT_WIDTH,LOADOUT_HEIGHT);
		draw.write("1",LOADOUTICON0_OFFSETX_CENTER-.1f,LOADOUTICON_OFFSETY+ITEMICON_HEIGHT/2+.01f);
		if(game.inventory.item0 != null){
			spr_loadouticon0.setRegion(0,0,128,128);
			draw.draw(Draw.TYPES.MEGAEXTRAS,spr_loadouticon0,LOADOUTICON0_OFFSETX_CENTER-itemiconwidth/2,LOADOUTICON_OFFSETY,itemiconwidth,ITEMICON_HEIGHT);
		}
		draw.write("2",LOADOUTICON1_OFFSETX_CENTER-.1f,LOADOUTICON_OFFSETY+ITEMICON_HEIGHT/2+.01f);
		if(game.inventory.item1 != null){
			spr_loadouticon1.setRegion(0,0,128,128);
			draw.draw(Draw.TYPES.MEGAEXTRAS,spr_loadouticon1,LOADOUTICON1_OFFSETX_CENTER-itemiconwidth/2,LOADOUTICON_OFFSETY,itemiconwidth,ITEMICON_HEIGHT);
		}
		
		draw.draw(batch);
	}
	
	ShopItem.ShopItemState getCurrentState(){
		return game.inventory.checkItemIsEquipped(selectedItem) ? ShopItem.ShopItemState.EQUIPPED : !game.inventory.checkItemIsOwned(selectedItem) ? ShopItem.ShopItemState.LOCKED : game.inventory.checkLoadoutFull() ? ShopItem.ShopItemState.NULL : ShopItem.ShopItemState.UNLOCKED; 
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
			if(mousehelddownfor < 0){
				mousehelddownfor = 0;
				if(relativemousex > ITEMUNIT_OFFSETX && relativemousex < ITEMUNIT_OFFSETX+ITEMUNIT_WIDTH && relativemousey > LOADOUT_OFFSETY+LOADOUT_HEIGHT){
					draggingitemlist = true;					
				}
			}else if(draggingitemlist){
				//if(!upgradeScreenOpen){
				mousehelddownfor += dt;
				float b = itemscroll < 0 ? -itemscroll+1 : itemscroll > ITEMSCROLL_MAX ? itemscroll-ITEMSCROLL_MAX+1 : 1;
				float a = (relativemousey-lastmouseposy)*NUMITEMSPERPAGE/b;
				itemscrollvelocity = a/dt;
				itemscroll += a;
			}
			lastmouseposx = relativemousex;
			lastmouseposy = relativemousey;
		}else{
			draggingitemlist = false;
			itemscroll += itemscrollvelocity*dt;
			if(itemscroll < 0 || itemscroll > ITEMSCROLL_MAX){
				itemscrollvelocity = 0;
				float lerp = (float)Math.pow(ITEMSCROLL_BOUNCINESSONMAX,dt);
				itemscroll = itemscroll*(lerp)+(itemscroll < 0 ? 0 : ITEMSCROLL_MAX)*(1-lerp);
			}
			itemscrollvelocity *= Math.pow(ITEMSCROLL_SLIPPERINESS,dt);
			
			if(mousehelddownfor >= 0 && mousehelddownfor < .5f){//if the mouse has clicked quickly instead of being held down (for button presses)
				if(relativemousey > LOADOUT_OFFSETY+LOADOUT_HEIGHT){//if the mouse isn't on the loadout bar
					for(int i = 0; i < items.length; i++){
						if(items[i].background.getBoundingRectangle().contains(x,y)){
							selectedItem = items[i];
							break;
						}
					}
				}
				//if the mouse is on the loadout bar
				else if(game.inventory.item0 != null && spr_loadouticon0.getBoundingRectangle().contains(x,y))
					selectedItem = game.inventory.item0;
				else if(game.inventory.item1 != null && spr_loadouticon1.getBoundingRectangle().contains(x,y))
					selectedItem = game.inventory.item1;

				if(spr_backbutton.getBoundingRectangle().contains(x,y)){
					game.moveToMain();
				}
				if(spr_desc_bigbutton_current != null && spr_desc_bigbutton_current.getBoundingRectangle().contains(x,y)){
					switch(getCurrentState()){
					case LOCKED://purchase a locked item (if possible)
						game.inventory.tryPurchaseItem(selectedItem);
						break;
					case UNLOCKED://equip an unlocked item
						game.inventory.equipItem((Item)selectedItem);
						updateLoadoutSprites();
						break;
					case EQUIPPED://unequip an equipped item
						game.inventory.unequipItem(selectedItem);
						updateLoadoutSprites();
						break;
					default://do nothing if the loadout is full
						break;
					}
					game.inventory.updateAllItemShopButtons();
				}
			}
			mousehelddownfor = -1;
		}
	}
}
