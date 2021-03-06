package com.pressx.objects.enemy;

import java.util.ArrayList;

import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.screens.game.Room;

public class PlantTwo extends Enemy {
	private float lastHP;
	public PlantTwo(Draw d, Sounds s, Textures t, GameObject device, float posX, float posY, Room room) {
		super(d,s,t,"plant2",device, 3, posX, posY, 2, 5, 6, 6, 0,
				0, true, 10, true, 9, 9,
				t.getArtAsset("plant2"), 256, 256, room);
		
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
		this.lastHP = this.getHp();
		
//		this.animator.add_animation("death", 0, 0, 5, false, 0,1,2,3,4,3,2,1,0);
//		this.animator.add_animation("attack", 0, 0, 5, true, 0,1,2,3,4,3,2,1,0);
//		this.animator.add_animation("walk", 0, 0, 5, true, 0,1,2,3,4,3,2,1,0);
//
//		this.set_animation("walk", true);
		this.animationManager = Textures.getAnimManager("Plant2").copy();
		this.animationManager.changeAnimation("Movement", 60, true);
		this.animationManager.setEndCondition("Death");
		this.animationManager.setStdCondition("Movement");
		//this.animator = null;
	}
	
	public void playSound(){
		sounds.play("fuzzie2.bite");
	}
	
	public void playHit(){
		sounds.play("fuzzie2.damage");
	}
	
	@Override
	public void playDeath() {
		sounds.play("plant2.death");
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		if(this.lastHP > this.getHp()){
			//Spawn gas
			room.addGas(this);
			this.lastHP = this.getHp();
		}
		if(this.attack.isAttacking){
			this.atkBehavior(dt);
			super.update(dt, objects);
			return;
		}
		super.update(dt, objects);
	}
	
	@Override
	protected void atkBehavior(float dt){
		atkTimer.update_timer(dt);
		if(atkTimer.isDone()){
			this.action_queue.clear();
		}
		return;
	}
	
	@Override
	protected void evolve(){
		this.worth = 0;
		GameObject monster = new PlantThree(this.draw, this.sounds, this.textures, device, this.get_positionX(), this.get_positionY(), room);
		this.terminate();
		monster.levelUp = 3;
		room.spawn_object(monster);
		sounds.play("monster.level");
	}
}