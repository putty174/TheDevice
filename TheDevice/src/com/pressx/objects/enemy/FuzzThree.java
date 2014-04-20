package com.pressx.objects.enemy;

import java.util.ArrayList;

import com.pressx.actions.Goto;
import com.pressx.control.GameTimer;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.player.Player;
import com.pressx.screens.game.Room;

public class FuzzThree extends Enemy {
	GameTimer hitTimer = new GameTimer(5);
	
	public FuzzThree(Draw d, Sounds s, Textures t, GameObject device, float posX, float posY, Room room) {
		super(d,s,t,"fuzz3",device, 3, posX, posY, 30, 50, 12, 12, 0,
				0, true, 15, true, 12, 12,
				t.getArtAsset("fuzz3"), 128, 128, room);
		
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
		
		this.animationManager = Textures.getAnimManager("Fuzzy3").copy();
		this.animationManager.changeAnimation("Movement", 60, true);
		this.animationManager.setEndCondition("Death");
		this.animationManager.setStdCondition("Movement");

		//this.animator = null;
		
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
					else{
						continue;
					}
					obj.impact(-150, Math.atan2(yDist, xDist));
				}
			}
		}
		room.addShock(this);
		//Sounds.play("fuzzie3.roar");
		this.animationManager.changeAnimation("Movement");
		sounds.play("fuzzie3.roar");
		//this.set_animation("walk", true);
		this.isAttacking = false;
		hitTimer.reset_timer();
		
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		
		if(this.getHp() <= 0){
			super.update(dt, objects);
			return;
		}
		if(this.attack.isAttacking){
			this.atkBehavior(dt);
			super.update(dt, objects);
		}
		if(this.isAttacking){
			if(!this.animationManager.getCurrentAnimation().equals("Attack")){
				this.animationManager.changeAnimation("Attack", 15, false);
			}
			this.sprite = this.animationManager.update();
			if(this.animationManager.isDone()){
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
						if(!this.animationManager.getCurrentAnimation().equals("Attack")){
							this.animationManager.changeAnimation("Attack");
							isAttacking = true;
							return;
						}
					}
				}
			}
		}
		else{
			this.hitTimer.update_timer(dt);
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
		sounds.play("fuzzie3.death");
	}
}