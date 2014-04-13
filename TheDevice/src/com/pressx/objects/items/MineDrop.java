package com.pressx.objects.items;

import java.util.ArrayList;

import com.pressx.control.GameTimer;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;
import com.pressx.thedevice.GameStats;

public class MineDrop extends AnimatedObject {
	GameStats stats;
	GameTimer timeToExpire = new GameTimer(10), blinkTimer = new GameTimer(0.1f);
	boolean isActive, isBlink;
	private int blinkCount;
	
	public MineDrop(Draw d, Sounds s, Textures t, GameStats stats, float posX, float posY){
		super(d,s,t,"mine",99, posX, posY, 1, 90, 5, 5, 0, 0,
				false, 5, false, 5, 5,
				t.getArtAsset("mine_drop"), 200, 200);
		this.stats = stats;
		this.movement.speedcap = 100;
		this.isActive = false;
		this.screenBound = true;
		//this.add_animation("mine_item", 0, 0, 4, 8, true);
		//this.set_animation("mine_item", true);
		this.animationManager = Textures.getAnimManager("MinePickup").copy();
		this.animationManager.setStdCondition("IdleBounce");
		this.animationManager.changeAnimation("IdleBounce", 60, true);
		this.stats = stats;
	}
	
	@Override
	public void behavior_collision(GameObject obj){
		if(obj.getID() == 0){
			if(stats.addAmmo("Mine")){
				this.terminate();
			}
		}	
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		//super.update(dt, objects);
		if(timeToExpire.isDone()){
			if(blinkCount > 15){
				this.terminate();
				return;
			}
			if(blinkTimer.isDone()){
				blinkTimer.reset_timer();
				isBlink = !isBlink;
				blinkCount += 1;
			}
			else{
				blinkTimer.update_timer(dt);				
				super.update(dt, objects);
				if(isBlink){
					this.sprite.setAlpha(0);					
				}
				else{
					this.sprite.setAlpha(1);
				}
			}
		}
		else{
			timeToExpire.update_timer(dt);	
			super.update(dt, objects);
		}	
	}
}