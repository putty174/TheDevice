package com.pressx.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pressx.managers.Textures;
import com.pressx.screens.ShopScreen;

public class ShopItem{
	public enum ShopItemState{NULL,LOCKED,UNLOCKED,EQUIPPED}
	
	private Textures textures;
	public String name,description;
	public Sprite icon,background,button;
	private ShopItemState state = ShopItemState.NULL;
	
	public ShopItem(Textures textures, String name,String icon/*,String description*/){
		this.textures = textures;
		this.name = name;
		this.icon = new Sprite(textures.getArtAsset(icon));
		this.description = name.toUpperCase()+" DESCRIPTION HERE";//description;
		background = new Sprite(textures.getArtAsset("itembackground"));
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
}
