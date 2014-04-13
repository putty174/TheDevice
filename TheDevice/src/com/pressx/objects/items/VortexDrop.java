package com.pressx.objects.items;

import java.util.ArrayList;

import com.pressx.control.GameTimer;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;
import com.pressx.thedevice.GameStats;

public class VortexDrop extends AnimatedObject {
	GameTimer existTimer = new GameTimer(5), blinkTimer = new GameTimer(.1f);
	GameStats stats;
	private boolean isBlink = false;
	private int blinkCount;
	
	public VortexDrop(Draw d, Sounds s, Textures t, GameStats stats, float posX, float posY) {
		super(d,s,t,"vortexicon",14, posX, posY, 0, 0, 5, 5, 0, 0,
				false, 2.5f, false, 5, 5, t.getArtAsset("vortex_drop"),
				200, 200);
		
		this.stats = stats;
		this.screenBound = true;
		//this.add_animation("vortex_item",0, 0, 4, 8, true);
		//this.set_animation("vortex_item", true);
		this.animationManager = Textures.getAnimManager("VortexPickup").copy();
		this.animationManager.setStdCondition("IdleBounce");
		this.animationManager.changeAnimation("IdleBounce", 60, true);
	}
	
	@Override
	public void behavior_collision(GameObject obj){
		if(obj.getID() == 0){
			if(stats.addAmmo("Vortex")){
				{
					this.terminate();
				}
			}
		}
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		if(existTimer.isDone()){
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
			existTimer.update_timer(dt);	
			super.update(dt, objects);
		}
	}
}