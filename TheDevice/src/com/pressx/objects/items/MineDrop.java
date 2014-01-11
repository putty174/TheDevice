package com.pressx.objects.items;

import java.util.ArrayList;

import com.pressx.control.GameTimer;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;
import com.pressx.thedevice.GameStats;

public class MineDrop extends AnimatedObject {
	GameTimer timeToExpire = new GameTimer(10);
	boolean isActive;
	
	public MineDrop(float posX, float posY){
		super("mine",99, posX, posY, 1, 90, 5, 5, 0, 0,
				false, 5, false, 5, 5,
				Textures.getArtAsset("mine_drop"), 200, 200);
		this.movement.speedcap = 100;
		this.isActive = false;
		this.add_animation("mine_item", 0, 0, 4, 8, true);
		this.set_animation("mine_item", true);

	}
	
	@Override
	public void behavior_collision(GameObject obj){
		if(obj.getID() == 0){
			if(GameStats.addMine()){
				this.terminate();
			}
		}	
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		super.update(dt, objects);
		timeToExpire.update_timer(dt);
		if(timeToExpire.isDone() && !isActive){
			this.terminate();
		}		
	}
}