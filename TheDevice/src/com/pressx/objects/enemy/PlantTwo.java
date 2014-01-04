package com.pressx.objects.enemy;

import java.util.ArrayList;

import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.screens.game.Room;

public class PlantTwo extends Enemy {
	private float lastHP;
	public PlantTwo(GameObject device, float posX, float posY, Room room) {
		super("plant2",device, 3, posX, posY, 2, 5, 6, 6, 0,
				0, true, 10, true, 9, 9,
				Textures.getArtAsset("plant2"), 256, 256, room);
		
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
		
		this.animator.add_animation("death", 0, 0, 5, false, 0,1,2,3,4,3,2,1,0);
		this.animator.add_animation("attack", 0, 0, 5, true, 0,1,2,3,4,3,2,1,0);
		this.animator.add_animation("walk", 0, 0, 5, true, 0,1,2,3,4,3,2,1,0);

		this.set_animation("walk", true);
	}
	
	public void playSound(){
		Sounds.play("fuzzie2.bite");
	}
	
	public void playHit(){
		Sounds.play("fuzzie2.damage");
	}
	
	@Override
	public void playDeath() {
		Sounds.play("plant2.death");
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		if(this.lastHP > this.getHp()){
			//Spawn gas
			room.addGas(this);
			this.lastHP = this.getHp();
		}
		super.update(dt, objects);
	}
}