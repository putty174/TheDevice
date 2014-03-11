package com.pressx.objects.enemy;

<<<<<<< HEAD
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.pressx.control.GameTimer;
=======
import com.pressx.managers.Draw;
>>>>>>> e994d722661d7bf24cb2732f372f0d1b48ec50b0
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.player.Player;
import com.pressx.screens.game.Room;

public class PlantThree extends Enemy {
<<<<<<< HEAD
	
	GameTimer deployTimer;
	GameTimer shotTimer;
	int shotCount;
	float oldFriction;
	boolean deployed, undeployed;
	
	public PlantThree(GameObject device, float posX, float posY, Room room) {
		super("plant3",device, 3, posX, posY, 30, 50, 12, 12, 0,
=======
	public PlantThree(Draw d, Sounds s, Textures t, GameObject device, float posX, float posY, Room room) {
		super(d,s,t,"plant3",device, 3, posX, posY, 30, 50, 12, 12, 0,
>>>>>>> e994d722661d7bf24cb2732f372f0d1b48ec50b0
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
		deployed = false;
		undeployed = false;
		
//		this.add_animation("death", 0, 0, 7, 7, false);
//		this.animator.add_animation("attack", 0, 1, 13, false, 0,1,2,3,4,5,6);
//		this.animator.add_animation("walk", 0, 2, 5, true,0,1,2,1,0,3,4,3,0);
//		
//		this.set_animation("walk", true);
		
		this.animationManager = Textures.getAnimManager("Plant3").copy();
		this.animationManager.changeAnimation("Movement", 30, true);
		this.animationManager.setEndCondition("Death");
		this.animationManager.setStdCondition("Movement");
		
		this.animator = null;
		
	}
	
	private void spawnProjectile(GameObject obj){
		this.room.addSeed(this, obj);
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		if(undeployed){
			if(!this.animationManager.getCurrentAnimation().equals("Undeployment")){
				this.animationManager.changeAnimation("Undeployment", 30, false);
			}
			this.sprite = this.animationManager.update();
			if(this.animationManager.isDone()){
				this.friction = oldFriction;
				undeployed = false;
				this.shotCount = 0;
				this.animationManager.changeAnimation("Movement", 30, true);
			}
			return;
		}
		if(!deployed){
			if(deployTimer.isDone()){
				oldFriction = this.friction;
				this.friction = 99999;
				if(!this.animationManager.getCurrentAnimation().equals("Deployment")){
					this.animationManager.changeAnimation("Deployment", 30, false);
				}
				this.sprite = this.animationManager.update();
				if(this.animationManager.isDone()){
					deployed = true;
					deployTimer.reset_timer();	
				}
				//super.update(dt, objects);
				return;			
			}
				
			deployTimer.update_timer(dt);
		}
		else{
			GameObject target = null;
			for(GameObject obj : objects){
				try{
					Player p = (Player) obj;
					target = p;
				}
				catch(Exception e){
					
				}
			}
			if(target == null){
				return;
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
			if(shotTimer.isDone()){
				spawnProjectile(target);
				shotTimer.reset_timer();
				shotCount ++;
			}
			else{
				shotTimer.update_timer(dt);
			}
			if(shotCount >= 3){
				undeployed = true;
				deployed = false;
			}
		}
		super.update(dt, objects);
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