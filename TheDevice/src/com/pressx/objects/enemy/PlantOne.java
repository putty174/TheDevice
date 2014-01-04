package com.pressx.objects.enemy;

import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.screens.game.Room;

public class PlantOne extends Enemy {
	float cooldown = 0.6f;
	
	public PlantOne(GameObject device, float posX, float posY, Room room) {
		super("plant1", device, 3, posX, posY, 12, 12, 6, 6, 0,
				0, true, 10, true, 12, 12,
				Textures.getArtAsset("plant1"), 256, 256, room);
		
		this.evolution = 1;
		this.attack.damage = 1;
		this.attack.attackframe = 8;
		this.attack.power = 20;
		this.attack.range = 7;
		this.attack.attackrange_limit = 10;
		this.movement.speed = 15;
		this.movement.speedcap = 20;
		this.movement.acceleration = 1;
		this.health.current = 1;
		this.health.max = 1;
		this.worth = 3;
		
		this.animator.add_animation("death", 0, 0, 5, false, 0, 1, 2, 3, 2, 1, 0);
		this.animator.add_animation("attack", 0, 0, 5, true, 0, 1, 2, 3, 2, 1, 0);
		this.animator.add_animation("walk", 0, 0, 5, true, 0, 1, 2, 3, 2, 1, 0);
		this.set_animation("walk", true);
	}
	
	@Override
	public void playDeath() {
		Sounds.play("plant1.death");
	}
	public void playHit() {
		Sounds.play("fuzzie2.damage");
	}
	@Override
	public void playAttack(){
		Sounds.play("fuzzie1.bite");
	}
	
	
	@Override
	protected void evolve(){
		this.worth = 0;
		this.terminate();
		GameObject monster = new PlantTwo(device, this.get_positionX(), this.get_positionY(), room);
		monster.levelUp = 3;
		room.spawn_object(monster);
		Sounds.play("monster.level");
	}
}