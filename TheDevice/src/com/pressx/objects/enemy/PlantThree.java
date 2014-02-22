package com.pressx.objects.enemy;

import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.screens.game.Room;

public class PlantThree extends Enemy {
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
		
		this.add_animation("death", 0, 0, 7, 7, false);
		this.animator.add_animation("attack", 0, 1, 13, false, 0,1,2,3,4,5,6);
		this.animator.add_animation("walk", 0, 2, 5, true,0,1,2,1,0,3,4,3,0);
		
		this.set_animation("walk", true);
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