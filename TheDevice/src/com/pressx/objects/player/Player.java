package com.pressx.objects.player;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.pressx.actions.Action;
import com.pressx.actions.Attack;
import com.pressx.actions.Goto;
import com.pressx.actions.Push;
import com.pressx.control.GameTimer;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;
import com.pressx.objects.attributes.AttackAttributes;

public class Player extends AnimatedObject{
	float i = 0;
	/* Stats */
	private AttackAttributes attack = new AttackAttributes();
	private int XPamount = 0;
	private boolean isStunned;
	private Action heldAction;
	private GameTimer stunTime = new GameTimer(1);
	public boolean isKnocked;
	private GameTimer knockTime;
	private boolean isSlowed;
	private GameTimer slowTime;
	
	/* Animation */
	private static final int ANIMATION_IDLE = 0;
	private static final int ANIMATION_WALKING = 1;
	private static final int ANIMATION_ATTACKING = 2;
	
	//Facing Angle
	private float facing_angle = 0;
	private int lastFacingIndex = 0;
	private float heading_X, heading_Y;
	
	/* Touch */
	private GameObject target = null;
	private boolean touch_pause = false;
	
	/* Constructor */
	/**
	 * Constructs a player object for a game.
	 * 
	 * @param objectID the identifier for the object
	 * @param posX the x position of the object in space
	 * @param posY the y position of the object in space
	 * @param mass how massive the object is
	 * @param friction the magnitude of the friction force applied to the object
	 * @param hitWidth the width of the hitbox
	 * @param hitHeight the height of the hitbox
	 * @param hitX the x offset of the hitbox
	 * @param hitY the y offset of the hitbox
	 * @param colliders the things this object will collide and interact with
	 * @param isSolid whether or not the object can be moved through
	 * @param isTouchable if the object is touchable or not
	 * @param drawWidth the width that the object is drawn at
	 * @param drawWidth the width that the object is drawn at
	 * @param drawHeight the height that the object is drawn at
	 * @param sprites the set of sprites to be used in drawing the object
	 * @param srcWidth the width of a sprite
	 * @param srcHeight the height of a sprite
	 */
	public Player(Draw d, Sounds s, Textures t, int objectID, float posX, float posY, float mass, float friction, float hitWidth, float hitHeight, float hitX, float hitY, boolean isSolid, float touchRadius, boolean isTouchable, float drawWidth, float drawHeight,int srcWidth, int srcHeight)
	{
		super(d, s, t, "player", objectID, posX, posY, mass, 300, hitWidth, hitHeight, hitX, hitY, isSolid, touchRadius, isTouchable, 20, 15, t.getArtAsset("hero"), 200, 150);
		
		this.screenBound = true;
		
		/* Draw */
		this.drawOffsetY = 4;
		this.movement.speedcap = 150;
		
		/* Stats */
		this.movement.speed = 30f;
		this.movement.acceleration = 30f;
		this.setAttack();
		this.knockTime = new GameTimer(.55f);
		this.slowTime = new GameTimer(2.5f);
		
		/* Animations */
		//Idle
//		for(int i = 0; i < 8; i++)
//		{
//			this.add_animation("idle_".concat(Integer.toString(i)),5, 4 + i, 1, 6, true);
//			/*
//			 Alright, so for now, the system for idle animations will be idle_(some number). 
//			 System starts at 0, which is at essentially 0 degrees.
//			 For every increment i, increase the angle by 45 degrees in the counterclockwise position.
//			 This will get you the direction that the player is facing.
//			 Don't exceed 7. Just don't do it man. It's not worth it.
//			 */
//		}//rof
		
		//Walking
//		this.add_animation("run_0",0, 4 + 0, 5, 500, true);
//		this.add_animation("run_1",0, 4 + 2, 5, 500, true);
//		this.add_animation("run_2",0, 4 + 2, 5, 500, true);
//		this.add_animation("run_3",0, 4 + 2, 5, 500, true);
//		this.add_animation("run_4",0, 4 + 4, 5, 500, true);
//		this.animator.add_animation("run_5",0, 4 + 5, 500, true, 4,3,1,0);
//		this.add_animation("run_6", 0, 4 + 6, 5, 500, true);
//		this.animator.add_animation("run_7", 0, 4 + 7, 500, true, 4,3,1,0);
//		
		//Attack
		
		/*
		 System works the same as before, only instead of 45 degree increments, 90 degree increments.
		 */
		
//		this.add_animation("attack_0", 0, 2, 5, 50, false);
//		this.add_animation("attack_1", 0, 0, 5, 50, false);
//		this.add_animation("attack_2", 0, 3, 5, 50, false);
//		this.add_animation("attack_3", 0, 1, 5, 50, false);
//		
		this.animationManager = Textures.getAnimManager("Hero");
		this.animationManager.changeAnimation("Idle1", 30, true);
		this.animationManager.setStdCondition("Idle0");
		
		this.animator = null;
		
		this.directionBasedAnimation(ANIMATION_IDLE);
	}//END Player
	
	public void setAttack(){
		this.attack.damage = 1;
		this.attack.attackframe = 4;
		this.attack.power = 50;
		this.attack.range = 9f;
		this.attack.attackrange_limit = 900;
	}
	
	/* Input */
	public void pause_touch()
	{
		this.touch_pause = true;
	}//END pause_touch
	
	public void input_touchDown(float x, float y, int pointer, int button, GameObject target)
	{
		if(!this.touch_pause)
		{
			if(!this.attack.isAttacking)
			{
				this.heading_X = x;
				this.heading_Y = y;
				this.target = target;
				
				if(target == null)
				{
					if(button == 0)
					{
						this.action_queue.clear();
						this.action_queue.add_action(new Goto(x, y, this.movement));
					}//fi
				}//fi
				else
				{
					this.heading_X = this.target.get_positionX();
					this.heading_Y = this.target.get_positionY();
					this.action_queue.clear();
					switch (this.target.getID())
					{
						case 1: //Is the device
							this.action_queue.add_action(new Goto(target.get_positionX(), target.get_positionY(), this.movement));
							break;
						case 97: //Is the device
							this.action_queue.add_action(new Goto(target.get_positionX(), target.get_positionY(), this.movement));
							break;
						default: //Enemy
							this.action_queue.add_action(new Attack(this.target,this.movement, this.attack));
							break;
					}//hctiws
				}//esle
			}//fi
		}//fi
	}//END input_touchDown
	
	public void input_touchDrag(float x, float y, int pointer)
	{
		if(!this.touch_pause)
		{
			if(!this.attack.isAttacking)
			{
				if(this.target == null)
				{
					this.heading_X = x;
					this.heading_Y = y;
					this.action_queue.clear();
					this.action_queue.add_action(new Goto(x, y, this.movement));
				}//fi
			}//fi
		}//fi
	}//END input_touchDrag
	
	public void input_touchUp(float x, float y, int pointer, int button)
	{
		if(!this.touch_pause)
		{
			if(!this.attack.isAttacking)
			{
				if(this.target != null && !this.target.isDying() && this.target.getID() != 3)
				{
					float yComp = y - this.target.get_positionY();
					float xComp = x - this.target.get_positionX();
					if(Math.sqrt(yComp * yComp + xComp *xComp) > 3)
					{
						float direction = (float)(Math.atan2(yComp, xComp));
						this.heading_X = this.target.get_positionX();
						this.heading_Y = this.target.get_positionY();
						this.action_queue.clear();
						switch (this.target.getID())
						{
							case 1: //Is the device
								this.action_queue.add_action(new Push(this.target, this.movement, 80, direction, this.attack.range));
								break;
							case 97: //Is the device
								this.action_queue.add_action(new Push(this.target, this.movement, 80, direction, this.attack.range));
								break;
						}//hctiws
					}//fi
				}//fi
			}//fi
		}//fi
		else
		{
			this.touch_pause = false;
		}//esle
	}//END input_touchUp
	
	/* Targeting */
	public GameObject get_target()
	{
		return this.target;
	}//END get_target
	
	public Vector2 get_heading()
	{
		return new Vector2(this.heading_X, this.heading_Y);
	}//END get_heading
	
	public boolean isIdle()
	{
		if(this.action_queue.get_actionID() == 0)
		{
			return true;
		}//fi
		return false;
	}
	
	/* Collision */
	@Override
	public void behavior_collision(GameObject collider)
	{
		switch (collider.getID())
		{
			case 1: //Collision with device
				double direction = this.get_velocityDirection() * Math.PI / 180;
				collider.impact(this.get_vMagnitude() * this.mass / 10, direction);
				break;
			case 2: //Collision with exp
				collider.terminate();
				XPamount += 5;
				break;
		}//hctiws
	}//END behavior_collision
	
	/* Animation */
	private int get_facingAngleIndex()
	{
		return (int)((this.facing_angle + 22.5)%360)/(45);
	}//END get_facingAngleIndex
	
	/**
	 * Sets the animation based on the direction the object is facing.
	 * 
	 * @param animationID The ID of the set of animations.
	 */
	public void directionBasedAnimation(int animationID)
	{
		this.animation_state = animationID;
		if(animation_state == ANIMATION_ATTACKING)
		{
			float temp = facing_angle % 360;
			Vector2 locVec = target.get_position().sub(this.get_position());
			temp = locVec.angle();
			if(temp <= 45){
				this.animationManager.changeAnimation("AttackEast", 60, false);
				this.animationManager.setStdCondition("Idle".concat("Idle".concat(Integer.toString((this.get_facingAngleIndex() + 4) % 8))));
			}
			else if(facing_angle <= 135){
				this.animationManager.changeAnimation("AttackNorth", 60, false);
				this.animationManager.setStdCondition("Idle".concat("Idle".concat(Integer.toString((this.get_facingAngleIndex() + 4) % 8))));
			}
			else if(temp <= 225){
				this.animationManager.changeAnimation("AttackWest", 60, false);
				this.animationManager.setStdCondition("Idle".concat("Idle".concat(Integer.toString((this.get_facingAngleIndex() + 4) % 8))));
			}
			else if(temp <= 315){
				this.animationManager.changeAnimation("AttackSouth", 60, false);
				this.animationManager.setStdCondition("Idle".concat("Idle".concat(Integer.toString((this.get_facingAngleIndex() + 4) % 8))));
			}
			else{
				this.animationManager.changeAnimation("AttackEast", 60, false);
				this.animationManager.setStdCondition("Idle".concat("Idle".concat(Integer.toString((this.get_facingAngleIndex() + 4) % 8))));
			}
			//this.animationManager.changeAnimation("Attack".concat(Integer.toString(this.get_facingAngleIndex())), 60, false);
			//this.animationManager.setStdCondition("Idle".concat("Idle".concat(Integer.toString((this.get_facingAngleIndex() + 4) % 8))));
			//this.animator.playAnimation("attack_".concat(Integer.toString((int)((this.facing_angle + 45)%360)/(90))), 15, false);
		}
		else if(animation_state == ANIMATION_WALKING)
		{
			this.animationManager.changeAnimation("Move".concat(Integer.toString((this.get_facingAngleIndex() + 4) % 8)), 60, true);
			//this.animator.playAnimation("run_".concat(Integer.toString(this.get_facingAngleIndex())), 15, true);
		}
		else{
			this.animationManager.changeAnimation("Idle".concat(Integer.toString(this.get_facingAngleIndex())), 60, true);
			//this.animator.playAnimation("idle_".concat(Integer.toString(this.get_facingAngleIndex())), 15, true);
		}
	}//END directionBasedAnimation
	
	private void set_currentAnimation()
	{
		if(this.isIdle())
		{
			this.animation_state = ANIMATION_IDLE;
		}//fi
		else
		{
			this.animation_state = ANIMATION_WALKING;
		}//esle
			directionBasedAnimation(this.animation_state);
				
	}//END set_currentAnimation
	
	/* Update */
	@Override
	public void update(float dt, ArrayList<GameObject> objects)
	{
		if(this.isKnocked){
			this.movement.acceleration = 0;
			this.knockTime.update_timer(dt);
			if(this.knockTime.isDone()){
				isKnocked = false;
				this.movement.acceleration = 30f;
				this.knockTime.reset_timer();
			}
		}
		if(this.isSlowed){
			this.movement.acceleration = 5f;
			this.movement.speed = 20f;
			this.movement.speedcap = 20f;
			this.slowTime.update_timer(dt);
			if(this.slowTime.isDone()){
				this.isSlowed = false;
				this.movement.acceleration = 30f;
				this.movement.speed = 30f;
				this.movement.speedcap = 150;
				this.slowTime.reset_timer();
				this.sprite.setColor(1,1,1,1);
			}
		}
		if(this.target != null && (this.target.isGone() || this.target.isDying()))
		{
			this.target = null;
			this.attack = new AttackAttributes();
			this.setAttack();
		}//fi
		
		if(this.attack.isAttacking && this.animation_state != ANIMATION_ATTACKING)
		{
			this.directionBasedAnimation(ANIMATION_ATTACKING);
			int play = (int)(Math.random() * 8);
			sounds.play("hero.smack" + play);
		}//fi
		
		if(!this.attack.isAttacking)
		{
			this.facing_angle = 180 + (float)(Math.atan2(this.get_positionY() - this.heading_Y,
					this.get_positionX() - this.heading_X) * 180 / Math.PI);
			int newFacing = this.get_facingAngleIndex();
			if (newFacing != this.lastFacingIndex)
			{
				this.lastFacingIndex = newFacing;
			}//fi
			
			this.set_currentAnimation();
		
//			if((this.animator.get_currentAnimation().equals("attack_".concat(Integer.toString(this.animation_state * 8 + this.get_facingAngleIndex())))));
//			{
//				this.directionBasedAnimation(this.animation_state);
//			}//fi
			
			if(this.animationManager.getCurrentAnimation().equals("Attack".concat(Integer.toString(this.animation_state * 8 + this.get_facingAngleIndex())))){
				this.directionBasedAnimation(this.animation_state);
			}
		
			Action temp = this.action_queue.get_action();
			if(this.isStunned){
				temp = this.action_queue.get_action();
				if(temp != null)
				{
					this.heldAction = temp;
				}
				this.action_queue.clear();
				stunTime.update_timer(dt);
				if(stunTime.isDone()){
					this.sprite.setColor(1,1,1,1);
					if(this.heldAction != null)
					{
						this.action_queue.add_action(this.heldAction);
					}//fi
					this.isStunned = false;
					this.friction = 1000;
					stunTime.reset_timer();
				}
				return;
			}	
			//this.animationManager.update();
			super.update(dt, objects);
			
			if(this.isStunned && heldAction != null)
			{
				this.action_queue.clear();
				this.action_queue.add_action(heldAction);
			}
			

		}//fi
		else
		{
			//this.animationManager.update();
			super.update(dt, objects);
		}
		this.sprite = this.animationManager.update();
	}//END update
	
	@Override
	protected void update_animationState()
	{
		
	}//END update_animationState
	
	public int getXP(){
		int temp = XPamount;
		XPamount = 0;
		return temp;
	}
	
	public void stun(GameObject obj){
		this.sprite.setColor(0.5f,0.5f,1f,0.8f);
		this.isStunned = true;
		this.friction = 100;
		this.velocity = obj.get_velocity();	
	}
	
	public void slow(){
		this.sprite.setColor(0.5f, 1f, 0.5f, 0.8f);
		this.isSlowed = true;
	}
}