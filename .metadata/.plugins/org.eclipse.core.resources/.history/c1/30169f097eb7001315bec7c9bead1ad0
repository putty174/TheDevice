package com.pressx.objects.items;

import java.util.ArrayList;
import java.util.Iterator;

import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;

public class Mine extends AnimatedObject {
	private boolean isExploding = false;
	private boolean isExploding2 = false;
	
	public Mine(Draw d, Sounds s, Textures t, float posX, float posY) {
		super(d,s,t, "activemine",97, posX, posY, 1, 90, 8, 8, 0, 0,
				false, 5, true, 18, 18, t.getArtAsset("mine"),
				200, 200);
		
		this.screenBound = true;
		this.drawOffsetY = 6.5f;
//		this.add_animation("mine_active", 1, 0, 5, 10, false);
//		this.add_animation("mine_passive", 0, 0, 1, 1, true);
//		this.animator.set_animation("mine_passive", true);
		this.animationManager = Textures.getAnimManager("Landmine");
		this.animationManager.changeAnimation("Passive", 10, true);
		this.animationManager.setEndCondition("Explode");
		this.animator = null;
	}
	
	@Override
	public void behavior_collision(GameObject obj){
		if(obj.getID() == 3 && !this.isExploding){
			sounds.play("hero.mine");
			isExploding = true;
			isExploding2 = true;
			isTouchable = false;
			this.drawOffsetY = 10;
			this.drawWidth = 30;
			this.drawHeight = 30;
			//this.animator.set_animation("mine_active", false);
			this.animationManager.changeAnimation("Explode", 60, false);
			this.set_velocity(0, 0);
			this.isTouchable = false;
		}
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		super.update(dt, objects);
		this.sprite = this.animationManager.update();
		if(isExploding2){
			Iterator<GameObject> iter = objects.iterator();
			while(iter.hasNext())
			{
				GameObject obj = iter.next();
				float dist = (float)Math.sqrt((this.get_positionX() - obj.get_positionX()) * (this.get_positionX() - obj.get_positionX()) + (this.get_positionY() - obj.get_positionY()) * (this.get_positionY() - obj.get_positionY()));
				if(obj.getID() == 3 && dist < 20)
				{
					obj.get_healthAttribute().current -= 3;
				}
			}
			this.isExploding2 = false;
		}
		if(isExploding && this.animationManager.isDone()){
			this.terminate();
		}
		
	}
}