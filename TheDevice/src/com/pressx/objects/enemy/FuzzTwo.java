package com.pressx.objects.enemy;

import java.util.ArrayList;

import com.pressx.control.GameTimer;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.player.Player;
import com.pressx.screens.game.Room;

public class FuzzTwo extends Enemy {
	GameTimer timer = new GameTimer(5);
	GameTimer prepTime = new GameTimer(1);
	GameTimer runTime = new GameTimer(0.5f);
	
	float xRun, yRun;
	boolean isPrepping, isRunning;
	
	public FuzzTwo(GameObject device, float posX, float posY, Room room) {
		super("fuzz2",device, 3, posX, posY, 2, 5, 6, 6, 0,
				0, true, 10, true, 9, 9,
				Textures.getArtAsset("fuzz2"), 128, 128, room);
		
		this.device = device;
		this.evolution = 2;
		this.attack.attackframe = 1;
		this.drawOffsetY = 1.25f;
		this.attack.damage = 2;
		this.attack.power = 20;
		this.attack.range = 7;
		this.attack.attackrange_limit = 11;
		this.movement.speed = 15;
		this.movement.speedcap = 40;
		this.movement.acceleration = 1;
		this.health.current = 2;
		this.health.max = 2;
		this.worth = 7;
		
		this.add_animation("death",0, 0, 7, 12, false);
		this.add_animation("attack",0, 1, 2, 5, true);
		this.animator.add_animation("walk",0, 2, 5, true, 0,1,0,2);
		this.add_animation("charge",0, 3, 3, 5, true);

		this.set_animation("walk", true);
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		if(this.getHp() <= 0){
			super.update(dt, objects);
			return;
		}
		if((!isPrepping && !isRunning) && !this.animator.get_currentAnimation().equals("walk")){
			this.animator.set_animation("walk", true);
			return;
		}
		if(timer.isDone()){
			this.action_queue.clear();
			if(!this.animator.get_currentAnimation().equals("charge")){
				this.animator.set_animation("charge", true);
				isPrepping = true;
			}
			if(isPrepping){
				prepTime.update_timer(dt);
				if(prepTime.isDone()){
					for(GameObject obj : objects){
						if(obj.getID() == 0){
							charge(obj);
							break;
						}
					}
					isRunning = true;
					Sounds.play("fuzzie2.charge");
					isPrepping = false;
					prepTime.reset_timer();
					this.animator.set_animation("attack", true);
					timer.reset_timer();
				}
				else{
					this.animator.update(dt);
				}				
			}
			return;				
		}
		if(isRunning){
			
			runTime.update_timer(dt);
			this.action_queue.clear();
			
			this.set_velocity(xRun,yRun);
			
			if(runTime.isDone()){
				clearAction();
				this.isRunning = false;
			}
			
		}
		timer.update_timer(dt);
		super.update(dt, objects);
	}	
	
	@Override
	public void behavior_collision(GameObject obj){
		super.behavior_collision(obj);
		if(obj.getID() == 0 && isRunning){
			((Player) obj).stun(this);
			Sounds.play("fuzzie2.collide");
			clearAction();
		}
	}
	
	private void clearAction(){
		this.movement.speed = 15;
		this.action_queue.clear();
		isRunning = false;
		this.mass = 2;
		runTime.reset_timer();
		this.animator.set_animation("walk", true);
	}
	
	private void charge(GameObject obj){
		this.mass = 7;
		this.action_queue.clear();
		double angle = (Math.atan2(obj.get_positionY() - this.get_positionY(), obj.get_positionX() - this.get_positionX()));
		this.xRun = (float)(40 * Math.cos(angle));
		this.yRun = (float)(40 * Math.sin(angle));
		this.set_velocity(xRun,yRun);
		
		//(360 * (float)Math.atan2(this.get_positionY() - obj.get_positionY(), this.get_positionX() - obj.get_positionX()))
	}
	
	public void playSound(){
		Sounds.play("fuzzie2.bite");
	}
	
	public void playHit(){
		Sounds.play("fuzzie2.damage");
	}
	
	@Override
	public void playDeath() {
		Sounds.play("fuzzie2.death");
	}
	
	@Override
	protected void evolve(){
		this.worth = 0;
		this.terminate();
		GameObject monster = new FuzzThree(device, this.get_positionX(), this.get_positionY(), room);
		room.spawn_object(monster);
		monster.levelUp = 3;
		Sounds.play("monster.level");
	}
}