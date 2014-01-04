package com.pressx.objects.enemy;

import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.screens.game.Room;

public class FuzzOne extends Enemy {
	public FuzzOne(GameObject device, float posX, float posY, Room room) {
		super("fuzz1", device, 3, posX, posY, 12, 12, 6, 6, 0,
				0, true, 10, true, 12, 12,
				Textures.getArtAsset("fuzz1"), 128, 128, room);
		// TODO Auto-generated constructor stub
		
		this.evolution = 1;
		this.attack.damage = 1;
		this.attack.attackframe = 8;
		this.attack.power = 20;
		this.attack.range = 7;
		this.attack.attackrange_limit = 10;
		this.movement.speed = 10;
		this.movement.speedcap = 15;
		this.movement.acceleration = 1;
		this.health.current = 1;
		this.health.max = 1;
		this.worth = 3;
		
		this.add_animation("death", 0, 0, 7, 12, false);
		this.animator.add_animation("attack", 0, 1, 16, false, 0,1,2,1,0,3,4,0,2);
		this.animator.add_animation("walk", 0, 2, 5, true, 0, 1, 2, 3, 4, 3, 2, 1, 0);
		this.set_animation("walk", true);
	}
	
	@Override
	public void playDeath() {
		Sounds.play("fuzzie1.death");
	}
	public void playHit()
	{
		playDeath();
	}
	@Override
	public void playAttack(){
		Sounds.play("fuzzie1.bite");
	}
	
	@Override
	protected void evolve(){
		this.worth = 0;
		this.terminate();
		GameObject monster = new FuzzTwo(device, this.get_positionX(), this.get_positionY(), room);
		monster.levelUp = 3;
		room.spawn_object(monster);
		Sounds.play("monster.level");
	}
}