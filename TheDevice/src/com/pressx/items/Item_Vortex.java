package com.pressx.items;

import com.badlogic.gdx.Gdx;
import com.pressx.objects.items.Vortex;

public class Item_Vortex extends Item{
	public Item_Vortex(){
		super("Vortex","ui_vortex");
	}

	@Override
	public String getButtonSpriteName(){
		return "ui_vortex";
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
	public void fieldPressed(float touchX,float touchY){
		room.add_object(new Vortex((float)touchX / Gdx.graphics.getWidth() * 100, (float)touchY / Gdx.graphics.getHeight() * 66));	
	}
}
