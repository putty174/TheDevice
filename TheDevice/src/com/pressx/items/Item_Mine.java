package com.pressx.items;

import com.badlogic.gdx.Gdx;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.items.Mine;
import com.pressx.objects.items.MineDrop;

public class Item_Mine extends Item{
	public Item_Mine(){
		super("Mine","ui_mine");
	}

	@Override
	public String getButtonSpriteName(){
		return "ui_mine";
	}
	
	@Override
	public String getCountSpriteName(){
		return "ui_minecount";
	}

	@Override
	protected void onActivated(){
		spr_button.setRegion(124,0,124,95);
	}
	@Override
	protected void onDeactivated(){
		spr_button.setRegion(0,0,124,95);
	}

	@Override
	public int getMaxAmmo(){
		return 3;
	}
	@Override
	public int getMinLevelForDrop(){
		return 2;
	}
	
	@Override
	public float getDropChance(){
		return .3f;
	}
	
	@Override
	public AnimatedObject dropAmmo(float posx,float posy){
		return new MineDrop(posx,posy);
	}
	
	@Override
	public void fieldPressed(float touchX,float touchY){
		room.add_object(new Mine((float)touchX / Gdx.graphics.getWidth() * 100, (float)touchY / Gdx.graphics.getHeight() * 66));	
	}
}
