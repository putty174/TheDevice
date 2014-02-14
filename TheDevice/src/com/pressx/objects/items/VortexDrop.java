package com.pressx.objects.items;

import java.util.ArrayList;

import com.pressx.control.GameTimer;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;
import com.pressx.thedevice.GameStats;

public class VortexDrop extends AnimatedObject {
	GameTimer existTimer = new GameTimer(5);
	
	public VortexDrop(float posX, float posY) {
		super("vortexicon",14, posX, posY, 0, 0, 5, 5, 0, 0,
				false, 2.5f, false, 5, 5, Textures.getArtAsset("vortex_drop"),
				200, 200);
		
		this.add_animation("vortex_item",0, 0, 4, 8, true);
		this.set_animation("vortex_item", true);
	}
	
	@Override
	public void behavior_collision(GameObject obj){
		if(obj.getID() == 0){
			if(GameStats.addAmmo("Vortex")){
				{
					this.terminate();
				}
			}
		}
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		if(existTimer.isDone()){
			this.terminate();
		}
		existTimer.update_timer(dt);
		super.update(dt, objects);
	}
}