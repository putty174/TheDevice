package com.pressx.objects.enemy;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.pressx.actions.Attack;
import com.pressx.control.GameTimer;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;
import com.pressx.objects.attributes.AttackAttributes;
import com.pressx.objects.device.XP;
import com.pressx.screens.game.Room;

public class Enemy extends AnimatedObject {
	Room room;
	int xpcounter = 0;
	int evolution = 0;
	AttackAttributes attack = new AttackAttributes();
	private float cooldown = 0;
	GameObject device;
	public boolean stunned, slowed;
	byte track;
	boolean isWalking, isAttacking;
	float speedCopy = this.get_speed();
	GameTimer atkTimer;
	
	public Enemy(Draw d, Sounds s, Textures t, String name, GameObject device, int objectID, float posX, float posY, float mass,
			float friction, float hitWidth, float hitHeight, float hitX,
			float hitY, boolean isSolid, float touchRadius,
			boolean isTouchable, float drawWidth, float drawHeight,
			Texture sprites, int srcWidth, int srcHeight, Room room) {
		super(d,s,t,name, objectID, posX, posY, mass, friction, hitWidth, hitHeight, hitX, hitY,
				isSolid, touchRadius, isTouchable, drawWidth, drawHeight,
				sprites, srcWidth, srcHeight);
		
		this.room = room;
		this.device = device;
		/* Stats */
		isWalking = true;
		atkTimer = new GameTimer(30);
		//this.animator = null;
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects)
	{
		//this.sprite = this.animationManager.update();
		if(this.isDying){
			if(!this.animationManager.getCurrentAnimation().equals("Death")){
				this.animationManager.changeAnimation("Death", 60, true);
				this.isTouchable = false;
				this.isSolid = false;
				playDeath();
			}
			super.update(dt, objects);
			return;
		}
			
		if(this.getHp() <= 0){
			this.isDying = true;
			this.friction = 5000000;
			return;
		}
		
		if(this.attack.isAttacking){
			//this.atkBehavior(dt);
			super.update(dt, objects);
		}
		else{
			this.animationManager.changeAnimation("Movement");
		}
		
		if(!stunned)
		{
			if(this.action_queue.get_actionID() == 0)
			{
				if(this.cooldown <= 0)
				{
						this.cooldown = 0.6f;
						if(this.action_queue.get_actionID() == 0)
						{
							this.action_queue.clear();
							this.action_queue.add_action(new Attack(device, movement, attack));
						}//fi
				}//fi
				else
				{
					this.cooldown -= dt;
				}//esle
			}//fi
			
		}//fi
		else
		{
			this.unstun();
		}//esle
		
		super.update(dt, objects);
	}//END update
	
	protected void playAttack() {
	}

	protected void atkBehavior(float dt){
		if(!this.animationManager.getCurrentAnimation().equals("Attack")){
			this.animationManager.changeAnimation("Attack");
		}
		atkTimer.update_timer(dt);
		if(atkTimer.isDone()){
			this.action_queue.clear();
			atkTimer.reset_timer();
			this.attack.isAttacking = false;
		}
		return;
	}
	
	public void stun()
	{
		this.isSolid = false;
		this.stunned = true;
		this.action_queue.clear();
	}
	
	public void unstun()
	{
		this.isSolid = true;
		this.stunned = false;
	}
	
	@Override
	public void behavior_collision(GameObject collider)
	{
		if(collider.isSolid())
		{
			double direction = Math.atan2(this.get_positionY() - collider.get_positionY(),
					this.get_positionX() - collider.get_positionX());
			float xComp = (float)(3 * collider.get_mass() / this.get_mass() * Math.cos(direction));
			float yComp = (float)(3 * collider.get_mass() / this.get_mass() *  Math.sin(direction));
			this.add_velocity(xComp, yComp);
			
			if(this.isColliding(collider, this.get_position()))
			{
				if(collider.isSolid())
				{
					this.stun();
				}//fi
			}//fi
		}//fi
		
		if(collider.getID() == 2 && !this.isDying)
		{
			try{
				XP temp = (XP) collider;
				if(temp.cannotTouch() == null || !temp.cannotTouch().equals(this)){
					collider.worth = 0;
					collider.terminate();
					xpcounter++;
					if(this.xpcounter >= this.evolution){
						this.evolve();
					}
				}
			}
			catch(Exception e){
				
			}
//			collider.worth = 0;
//			collider.terminate();
//			xpcounter++;
//			if(this.xpcounter >= this.evolution)
//			{
//				this.evolve();
//			}
		}
	}//END behavior_collision
	
	/* Sound */
	protected void playDeath()
	{
		//Override
	}
	
	public void playHit(){
	}
	
	/* Evolution */
	protected void evolve(){
	}
	
	//Get attack att's for dev tool
	public AttackAttributes getAttack(){
		return attack;
	}
}