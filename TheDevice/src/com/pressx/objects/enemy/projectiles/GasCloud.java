package com.pressx.objects.enemy.projectiles;

import java.util.ArrayList;

import com.pressx.control.GameTimer;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;
import com.pressx.objects.player.Player;

public class GasCloud extends AnimatedObject {
	private GameTimer timer;
	private boolean isDone = false;
	
	public GasCloud(float posX, float posY) {
		super("gasCloud", 18, posX, posY, 1, 1, 0, 0, 0, 0,
				false, 0, false, 10, 10, Textures.getArtAsset("gas_cloud"),
				264, 151);
		
		
		timer = new GameTimer(5f);
		this.animator.add_animation("gas_start", 0, 0, 10, false, 0,1,2,3);
		this.animator.add_animation("gas_loop", 0, 0, 10, true, 0,1,2,3);
		this.animator.add_animation("gas_end", 0, 0, 10, false, 3);
		this.animator.set_animation("gas_start", false);
	}
	
	@Override
	public void behavior_collision(GameObject obj){
		if(obj.getID() == 0){
			((Player) obj).slow();
		}
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){

		this.sprite.setColor(1.f, 1.f, 1.f, 0.75f + 0.25f*(float)Math.sin(timer.get_time()));
		if(this.animator.get_currentAnimation().equals("gas_start")){
			if(this.animator.isDone()) {
				this.animator.set_animation("gas_loop", true);
			}
		}
		else if(this.animator.get_currentAnimation().equals("gas_loop")){
			if(!isDone) {
				timer.update_timer(dt);
				isDone = timer.isDone();
			}
			else {
				this.set_animation("gas_end", false);
			}
		}
		else if(this.animator.get_currentAnimation().equals("gas_end")){
			if(isDone){
				this.terminate();
			}
			else{
				super.update(dt, objects);
				return;
			}
		}
		super.update(dt, objects);
	}
}