package com.pressx.objects.items;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.pressx.control.GameTimer;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;
import com.pressx.thedevice.GameStats;

public class WrenchDrop extends AnimatedObject{
	private GameStats stats;
	private GameTimer ttl = new GameTimer(4.5f);
	private int lvl, blinkCount;
	private GameTimer betweenTimer = new GameTimer(0.1f), fadeTimer = new GameTimer(0.1f);
	private boolean isBlink, fastMode;
	
	public WrenchDrop(Draw d, Sounds s, Textures t, GameStats st, float posX, float posY) {
		super(d,s,t,"wrench",1005, posX, posY, 0, 0, 5, 5, 0, 0,
				false, 5, false, 13, 13,
				t.getArtAsset("wrench"), 200, 200);
		
		this.stats = st;
		this.lvl = 1;
		this.screenBound = true;
		//this.add_animation("wrench_item",0, 0, 1, 0, false);
		//this.set_animation("wrench_item", false);
		
		this.animationManager = Textures.getAnimManager("WrenchPickup").copy();
		this.animationManager.changeAnimation("IdleBounce", 60, true);
		this.animationManager.setStdCondition("IdleBounce");
		this.isBlink = false;
		this.fastMode = false;
	}
	
	@Override
	public void behavior_collision(GameObject obj){
		if(obj.getID() == 0){
			if(stats.getboxHP() < stats.getBoxMaxHP()){
				float newHealth = stats.getboxHP() + (stats.getBoxMaxHP() * (0.2f + (0.15f * (lvl - 1))));
				System.out.println("Old Health: " + stats.getboxHP());
				System.out.println("New Health: " + newHealth);
				stats.setBoxHP(newHealth);
				if(stats.getboxHP() > stats.getBoxMaxHP())
					stats.setBoxHP(stats.getBoxMaxHP());
				this.terminate();
			}
		}
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		
		if(ttl.isDone()){
			if(blinkCount > 15){
				this.terminate();				
			}
			if(betweenTimer.isDone()){
				betweenTimer.reset_timer();
				isBlink = !isBlink;
				blinkCount += 1;
			}
			else{
				betweenTimer.update_timer(dt);				
				super.update(dt, objects);
				this.sprite.setSize(this.sprite.getWidth()/2, this.sprite.getHeight()/2);
				if(isBlink){
					this.sprite.setAlpha(0);
					
				}
				else{
					this.sprite.setAlpha(1);
				}
			}
		}
		else{
			ttl.update_timer(dt);	
			super.update(dt, objects);
		}		
		
	}
}