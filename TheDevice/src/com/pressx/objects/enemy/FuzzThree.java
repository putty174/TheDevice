package com.pressx.objects.enemy;

import java.util.ArrayList;

import com.pressx.actions.Goto;
import com.pressx.control.GameTimer;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.player.Player;
import com.pressx.screens.game.Room;

public class FuzzThree extends Enemy {
	GameTimer hitTimer = new GameTimer(5);
	
	public FuzzThree(GameObject device, float posX, float posY, Room room) {
		super("fuzz3",device, 3, posX, posY, 30, 50, 12, 12, 0,
				0, true, 15, true, 12, 12,
				Textures.getArtAsset("fuzz3"), 128, 128, room);
		
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
		
		this.add_animation("death",0, 0, 7, 7, false);
		this.animator.add_animation("attack",0, 1, 13, false, 0,1,2,3,4,5,6);
		this.animator.add_animation("walk",0, 2, 5, true,0,1,2,1,0,3,4,3,0);
		
		this.set_animation("walk", true);
	}
	
	private void specAttack(ArrayList<GameObject> objects){
		this.action_queue.clear();		
		for(GameObject obj : objects){
			if(obj != this)
			{
				float xDist = this.get_positionX() - obj.get_positionX();
				float yDist = this.get_positionY() - obj.get_positionY();
				float dist = (float)Math.pow((xDist * xDist) + (yDist * yDist), .5);
				if(dist < 20){
					if(obj.getID() == 0){
						((Player) obj).movement.acceleration = 0;
						((Player) obj).isKnocked = true;
					}
					else if(obj.getID() == 1){
						obj.setHp(obj.getHp() - this.attack.damage);
					}
					obj.impact(-150, Math.atan2(yDist, xDist));
				}
			}
		}
		room.addShock(this);
		Sounds.play("fuzzie3.roar");
		this.set_animation("walk", true);
		this.isAttacking = false;
		hitTimer.reset_timer();
		
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		
		if(this.getHp() <= 0){
			super.update(dt, objects);
			return;
		}
		if(this.isAttacking){
			this.animator.update(dt);
			if(this.animator.isDone()){
				this.specAttack(objects);
			}
			return;
		}
		else{
			hitTimer.update_timer(dt);
			this.action_queue.clear();
			this.action_queue.add_action(new Goto(device.get_positionX(), device.get_positionY(), movement));
		}
		
		if(this.hitTimer.isDone()){
			for(GameObject obj : objects){
				if(obj.getID() == 0 || obj.getID() == 1){
					float xDist = this.get_positionX() - obj.get_positionX();
					float yDist = this.get_positionY() - obj.get_positionY();
					float dist = (float)Math.pow((xDist * xDist) + (yDist * yDist), .5);
					if(dist < 15){
						if(!this.animator.get_currentAnimation().equals("attack")){
							this.set_animation("attack", false);
							isAttacking = true;
							return;
						}	
					}
				}
			}
		}
		
		this.hitTimer.update_timer(dt);
		
		super.update(dt, objects);
	}
	
	public void playSound(){
		Sounds.play("fuzzie3.attack");
	}
	
	public void playHit(){
		Sounds.play("fuzzie3.damage");
	}
	
	@Override
	public void playDeath() {
		Sounds.play("fuzzie3.death");
	}
}