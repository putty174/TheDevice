package com.pressx.objects.enemy;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.pressx.actions.Goto;
import com.pressx.control.GameTimer;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.player.Player;
import com.pressx.screens.game.Room;

public class PlantThree extends Enemy {
	
	GameTimer deployTimer;
	GameTimer shotTimer;
	int shotCount;
	float oldFriction;
	int state;
	

	public PlantThree(Draw d, Sounds s, Textures t, GameObject device, float posX, float posY, Room room) {
		super(d,s,t,"plant3",device, 3, posX, posY, 30, 50, 12, 12, 0,
				0, true, 15, true, 12, 12,
				t.getArtAsset("plant3"), 128, 128, room);
		
		this.device = device;
		
		this.attack.damage = 2;
		this.attack.power = 20;
		this.attack.attackframe = 6;
		this.attack.range = 10;
		this.attack.attackrange_limit = 14;
		this.movement.speed = 10;
		this.movement.speedcap = 35;
		this.movement.acceleration = 1;
		this.health.current = 6;
		this.health.max = 6;
		this.worth = 13;
		
		this.drawOffsetY = 2;
		
		deployTimer = new GameTimer(2);
		shotTimer = new GameTimer(2);
		
		//State 0 represents Movement
		//State 1 represents Deployment
		//State 2 represents Undeployment
		
		state = 0;
		
//		this.add_animation("death", 0, 0, 7, 7, false);
//		this.animator.add_animation("attack", 0, 1, 13, false, 0,1,2,3,4,5,6);
//		this.animator.add_animation("walk", 0, 2, 5, true,0,1,2,1,0,3,4,3,0);
//		
//		this.set_animation("walk", true);
		
		this.animationManager = Textures.getAnimManager("Plant3").copy();
		this.animationManager.changeAnimation("Movement", 30, true);
		this.animationManager.setEndCondition("Death");
		this.animationManager.setStdCondition("Movement");
		
		//this.animator = null;
		
	}
	
	private void spawnProjectile(GameObject obj){
		this.room.addSeed(this, obj);
		this.shotCount ++;
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		if(isDying){
			super.update(dt, objects);
			return;
		}
		if(state == 0){
			this.action_queue.add_action(new Goto(this.device.get_positionX(), this.device.get_positionY(), this.movement));
			if(this.animationManager.getCurrentAnimation() != "Movement"){
				this.animationManager.changeAnimation("Movement", 60, true);
			}
			if(deployTimer.isDone()){
				state = 1;
				deployTimer.reset_timer();
			}
			else{
				deployTimer.update_timer(dt);
			}
		}
		else if(state == 1){
			oldFriction = this.friction;
			this.friction = Integer.MAX_VALUE;
			if(this.animationManager.getCurrentAnimation() != "Deployment"){
				this.animationManager.changeAnimation("Deployment", 60, false);
			}
			this.sprite = this.animationManager.update();
			if(this.animationManager.isDone()){
				state = 2;
			}
			return;
		}
		else if(state == 2){
			if(shotCount == 3){
				shotCount = 0;
				state = 3;
				return;
			}
			GameObject obj = directionBasedTracking(objects);
			if(shotTimer.isDone()){
				shotTimer.reset_timer();
				spawnProjectile(obj);
			}
			else{
				shotTimer.update_timer(dt);
			}
			
			this.sprite = this.animationManager.update();
			return;
		}
		else if(state == 3){
			if(this.animationManager.getCurrentAnimation() != "Undeployment"){
				this.animationManager.changeAnimation("Undeployment", 60, false);
			}
			this.sprite = this.animationManager.update();
			if(this.animationManager.isDone()){
				state = 0;
				this.animationManager.changeAnimation("Movement", 60, true);
				this.friction = 20;
			}
			return;
		}
		super.update(dt, objects);
	}
	
	
	private GameObject directionBasedTracking(ArrayList<GameObject> objs){
		GameObject target = null;
		for(GameObject obj : objs){
			if(obj.getID() == 0){
				target = obj;
			}
		}
		if(target == null){
			return null;
		}
		Vector2 tempDir = target.get_position().sub(this.get_position());
		float tempAngle = tempDir.angle();
		if(tempAngle <= 45){
			this.animationManager.changeAnimation("AttackEast", 30, true);
		}
		else if(tempAngle <= 135){
			this.animationManager.changeAnimation("AttackNorth", 30, true);
		}
		else if(tempAngle <= 225){
			this.animationManager.changeAnimation("AttackWest", 30, true);
		}
		else if(tempAngle <= 315){
			this.animationManager.changeAnimation("AttackSouth", 30, true);
		}
		else{
			this.animationManager.changeAnimation("AttackEast", 30 ,true);
		}
		return target;
	}
	
	public void playSound(){
		sounds.play("fuzzie3.attack");
	}
	
	public void playHit(){
		sounds.play("fuzzie3.damage");
	}
	
	@Override
	public void playDeath() {
		sounds.play("plant3.death");
	}
}