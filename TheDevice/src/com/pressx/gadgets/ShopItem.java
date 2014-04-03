package com.pressx.gadgets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.GameStats;

public abstract class ShopItem{
	public enum ShopItemState{NULL,LOCKED,UNLOCKED,EQUIPPED}//UNLOCKEDBUTUNEQUIPPABLE is only used by ShopScreen
	protected GameStats stats;
	protected Sounds sound;
	protected Textures textures;
	public String name,description;
	public Sprite icon,largeicon,background,button;
	private ShopItemState state = ShopItemState.NULL;
	
	String iconname,largeiconname;
	
	public ShopItem(Textures textures, Sounds s, String name,String icon,String largeicon/*,String description*/){
		this.sound = s;
		this.textures = textures;
		this.name = name;
		largeiconname = largeicon;
		iconname = icon;
	}
	
	public void setGameStats(GameStats s) {
		this.stats = s;
	}
	
	public abstract int getCost();

	public void initializeForShop(){
		this.icon = getspr(iconname);
		this.largeicon = getspr(largeiconname);
		this.description = "DESCRIPTION HERE";//description;
		background = getspr("itembackground");
		setState(ShopItemState.LOCKED);
	}
	
	public void setState(ShopItemState s){
		if(state == s) return;
		state = s;
		switch(state){
		case LOCKED:
			button = new Sprite(textures.getArtAsset("itembutton_buy"));
			break;
		case UNLOCKED:
			button = new Sprite(textures.getArtAsset("itembutton_equip"));
			break;
		case EQUIPPED:
			button = new Sprite(textures.getArtAsset("itembutton_unequip"));
			break;
		case NULL:
			//should only be NULL if the item has not been initialized yet
			break;
		}
	}
	public ShopItemState getState(){
		return state;
	}
	
	public Sprite getspr(String name)
	{
		System.out.println(name);
		return new Sprite(textures.getArtAsset(name));
	}//also used by ShopItem
}
