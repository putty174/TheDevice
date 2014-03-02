package com.pressx.items;

import com.badlogic.gdx.Gdx;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.items.Vortex;
import com.pressx.objects.items.VortexDrop;
import com.pressx.thedevice.GameStats;

public class Item_Control extends Item{
	public Item_Control(Sounds sounds, Textures textures){
		super(textures, sounds, "Lime Control","vortex_drop","largeicon_control");
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
		room.add_object(new Vortex(draw, sound, textures, (float)touchX / Gdx.graphics.getWidth() * 100, (float)touchY / Gdx.graphics.getHeight() * 66));	
	}

	@Override
	public int getMinLevelForDrop(){
		return 5;
	}

	@Override
	public float getDropChance(){
		return .2f;
	}

	@Override
	public AnimatedObject dropAmmo(float posx, float posy) {
		return new VortexDrop(draw, sound, textures, stats, posx, posy);
	}
	
	@Override
	public int getCost(){return 200;}
}
