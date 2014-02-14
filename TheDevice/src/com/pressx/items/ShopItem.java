package com.pressx.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pressx.screens.ShopScreen;

public class ShopItem{
	public enum ShopItemState{NULL,LOCKED,UNLOCKED,EQUIPPED}
	
	public String name,description;
	public Sprite icon,background,button;
	private ShopItemState state = ShopItemState.NULL;
	
	String iconname;
	
	public ShopItem(String name,String icon/*,String description*/){
		this.name = name;
		iconname = icon;
	}

	public void initializeForShop(){
		this.icon = ShopScreen.getspr(iconname);
		this.description = "DESCRIPTION HERE";//description;
		background = ShopScreen.getspr("itembackground");
		setState(ShopItemState.LOCKED);
	}
	
	public void setState(ShopItemState s){
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
		case NULL:
			//should only be NULL if the item has not been initialized yet
			break;
		}
	}
	public ShopItemState getState(){
		return state;
	}
}
