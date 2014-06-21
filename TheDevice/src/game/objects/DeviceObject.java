package game.objects;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import game.drawable.Animator;
import game.drawable.infomation.PositionObject;
import game.objects.behavior.Behavior;
import game.objects.behavior.tasks.Task;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DeviceObject implements PositionObject {
	/* Room */
	public final Room room; //The room the object is in.
	
	/* State */
	public boolean isTangible = true; //Whether or not the object is tangible or not. 
	
	/* Physics */
	private Vector2 position; //The position of this object.
	private Vector2 velocity; //The velocity of this object.
	private boolean velocityChange = false; //Whether or not the velocity has been changed or not during the cycle.
	private final float friction; //The rate at which the object starts to stop at.
	private float mass; //The mass of the object (affects solid collision)
	
	/* Collision */
	private HashMap<DeviceObject, Boolean> collisionList = new HashMap<DeviceObject, Boolean>(); //Everything this object has collided with this cycle.
	//Collision Information
	protected boolean isSolid; //Whether or not the object is solid or not.
	protected float collision_width, collision_height; //The collision height and width.
	protected float collision_alignmentX, collision_alignmentY; //The offset to determine where the hitbox is in percent.
	protected float collision_offsetX, collision_offsetY; //The offset to determine where the hitbox in game units.
	//Collision Cycle
	private boolean collision_otherCycle = false;
	
	/* Touch */
	protected boolean touchable; //Whether or not the object is touchable.
	protected float touch_width, touch_height; //The touch area width and height.
	protected float touch_alignmentX, touch_alignmentY; //The offset to determine where the touch area is in percent.
	protected float touch_offsetX, touch_offsetY; //The offset to determine where the touch area is in game units.
	
	/* Behavior */
	public final String identity; //The type of object this is.
	protected Behavior behavior; //The behavior of this object.
	private boolean controllable; //Whether or not the object is controllable.
	private Task current_task = null; //The current task of this object.
	private boolean tasks_locked = false; //Whether or not new tasks can be taken.
	
	/* Properties */
	private HashMap<String, Float> attributes = new HashMap<String, Float>(); //The attributes of the object.
	private boolean dead = false; //Whether or not the object is dead.
	
	/* Rendering */
	public Animator animator = null; //The animation of the current object.
	
	/* Construction */
	/**
	 * Constructs a base device object.
	 * 
	 * @param room the room the object is in.
	 * @param object_data the data that makes the object the specified object.
	 */
	@SuppressWarnings("unchecked")
	public DeviceObject(Room room, ObjectData object_data) {
		/* Initialize */
		this.position = new Vector2();
		this.velocity = new Vector2();
		this.room = room;
		
		/* Extract Data */
		//Collision
		this.isSolid = object_data.collision_solid;
		this.collision_width = object_data.collision_hit[0];
		this.collision_height = object_data.collision_hit[1];
		this.collision_alignmentX = object_data.collision_hit[2];
		this.collision_alignmentY = object_data.collision_hit[3];
		this.collision_offsetX = object_data.collision_hit[4];
		this.collision_offsetY = object_data.collision_hit[5];
		//Touch
		this.touchable = object_data.touchable;
		this.touch_width = object_data.touch[0];
		this.touch_height = object_data.touch[1];
		this.touch_alignmentX = object_data.touch[2];
		this.touch_alignmentY = object_data.touch[3];
		this.touch_offsetX = object_data.touch[4];
		this.touch_offsetY = object_data.touch[5];
		//Behavior
		this.identity = object_data.identity; 
		this.behavior = object_data.behavior;
		this.controllable = object_data.behavior_controllable;
		//Properties
		this.friction = object_data.friction;
		this.mass = object_data.mass;
		this.attributes = (HashMap<String, Float>)object_data.attributes.clone();
		
		this.velocity_set(new Vector2(25,0));
	}//END DeviceObject
	
	/**
	 * Initializes the object.
	 */
	public void initialize() {
		this.behavior.event_initialize(this);
	}//END initialize
	
	/* Physics */
	//Friction
	public final float friction_get() {
		return this.friction;
	}//END friction_get
	
	//Position
	/**
	 * Sets the position of this object to the given value.
	 * 
	 * @param p the position vector to set the position as.
	 */
	public final void position_set(Vector2 p) {
		this.position = p.cpy();
	}//END position_set
	
	/**
	 * Returns a copy of the position of this object.
	 * 
	 * @return a position vector.
	 */
	public final Vector2 position_get() {
		return this.position.cpy();
	}//END position_get
	
	//Velocity
	/**
	 * Indicates a change in the velocity even if there isn't any.
	 */
	public final void velocity_toggle() {
		this.velocityChange = true;
	}//END velocity_toggle
	
	/**
	 * Adds a velocity vector to the velocity of this object.
	 * 
	 * @param v the velocity of this object.
	 */
	public final void velocity_add(Vector2 v) {
		this.velocity.add(v);
		this.velocityChange = true;
	}//END velocity_add
	
	/**
	 * Sets the velocity of this object to the given value.
	 * 
	 * @param v the velocity vector to set the velocity as.
	 */
	public final void velocity_set(Vector2 v) {
		this.velocity.set(v);
		this.velocityChange = true;
	}//END velocity_set
	
	/**
	 * Returns the velocity of this object.
	 * 
	 * @return a velocity vector.
	 */
	public final Vector2 velocity_get() {
		return this.velocity.cpy();
	}//END velocity_get
	
	/* Attributes */
	/**
	 * Returns the attribute value given the name.
	 * 
	 * @param attr_name the name of the attribute.
	 * @return a float specifying the value of the indicated attribute.
	 */
	public final float attribute_get(String attr_name) {
		return this.attributes.get(attr_name);
	}//END attribute_get
	
	/**
	 * Sets the value of the attribute given the name and the value.
	 * 
	 * @param attr_name the name of the attribute.
	 * @param value the value of the attribute.
	 */
	public final void attribute_set(String attr_name, float value) {
		this.attributes.put(attr_name, value);
	}//END attribute_set
	
	/**
	 * Adds to the specified attribute with the given value.
	 * 
	 * @param attr_name the name of the attribute.
	 * @param value the value to add.
	 */
	public final void attribute_add(String attr_name, float value) {
		float attr = this.attributes.get(attr_name);
		attr += value;
		this.attributes.put(attr_name, attr);
	}//END attribute_add
	
	/* Collision */
	/**
	 * Checks for collisions and collides with other objects.
	 * 
	 * @param objects the objects to collide with.
	 * @param doBehavior indicates whether or not to do object collision behavior or not.
	 * @return whether or not there was a solid collision.
	 */
	public final boolean collision(Iterable<DeviceObject> objects, boolean doBehavior) {
		boolean solid = false;
		Iterator<DeviceObject> iter = objects.iterator();
		while(iter.hasNext()) {
			DeviceObject obj = iter.next();
			if (obj != this && this.collide(obj, doBehavior)) { // If not the same object and it was a solid collision...
				solid = true; //Indicate solid collision.
			}//fi
		}//elihw
		
		return solid;
	}//END collision
	
	//Collision Information
	/**
	 * Returns this objects hitbox.
	 * 
	 * @return a hitbox
	 */
	public final Rectangle collision_hitbox() {
		return new Rectangle(
				this.position.x - this.collision_width * this.collision_alignmentX - this.collision_offsetX,
				this.position.y - this.collision_height * this.collision_alignmentY - this.collision_offsetY,
				this.collision_width,
				this.collision_height);
	}//END collision_hitbox
	
	/**
	 * Returns whether or not this object is solid.
	 * 
	 * @return a boolean indicating whether or not this object is solid.
	 */
	public final boolean collision_isSolid() {
		return this.isSolid;
	}//END collision_isSolid
	
	/**
	 * Adds a collider object to this object's list.
	 * 
	 * @param o the colliding object.
	 */
	public final void collision_addCollider(DeviceObject o) {
		this.collisionList.put(o, this.collision_otherCycle);
	}//END collision_addCollider
	
	//Collision Event
	/**
	 * Collides this object with another if possible.
	 * 
	 * @param o the colliding object.
	 * @param doBehavior indicates whether or not to do collision behavior or not.
	 * @return whether or not there was a solid collision.
	 */
	protected final boolean collide(DeviceObject o, boolean doBehavior) {
		boolean solid_collision = false;
		//Check if there is a collision.
		if(
				o.isTangible && this.isTangible &&
				this.collision_hitbox().overlaps(o.collision_hitbox())
		) {			
			//Solid Collision
			if(this.isSolid && o.collision_isSolid()) { //Check if both objects are solid.
				solid_collision = true; //Indicate solid collision
			}//fi
			
			//Collision Behavior
			if(doBehavior && !this.collisionList.containsKey(o)) { //Check if objects have not collided before.
				o.collision_addCollider(this);
				this.collision_event(o);
				o.collision_event(this);
				
				if(solid_collision) {
					collision_solid(o);
				}//fi
			}//fi
		}//fi
		return solid_collision; //Indicate the type of collision.
	}//END collide
	
	/**
	 * Does an event based on the collision that has happened.
	 * 
	 * @param o the colliding object.
	 */
	protected void collision_event(DeviceObject o) {
		this.behavior.event_collision(this, o);
	}//END collision_event
	
	/**
	 * Default behavior between objects whose collision is solid.
	 * 
	 * @param o the object being collided with.
	 */
	protected void collision_solid(DeviceObject o) {
		Vector2 v_s = this.velocity_get();
		Vector2 v_o = o.velocity_get();
		Vector2 dir = new Vector2();
		this.collision_hitbox().getCenter(dir).sub(o.collision_hitbox().getCenter(new Vector2())).nor();
		
		/* Set velocity (elastic collision) */
		//Self
		this.velocity_set((v_s.cpy().scl(this.mass - o.mass).add(v_o.cpy().scl(o.mass*2))).div(((this.mass + o.mass)*2)));
		this.velocity_add(dir.cpy().scl(2f*o.mass/this.mass));
		//Other
		o.velocity_set((v_o.cpy().scl(o.mass - this.mass).add(v_s.cpy().scl(this.mass*2))).div(((this.mass + o.mass)*2)));
		o.velocity_add(dir.cpy().scl(-2f*this.mass/o.mass));
	}//END collision_solid
	
	/* Task */
	/**
	 * Sets the current task of this object.
	 * 
	 * @param task the task to do.
	 */
	public final void task_set(Task task) {
		if(!this.tasks_locked) {
			this.current_task = task;
		}//fi
	}//END task_set
	
	/**
	 * Makes it so no tasks can be replace the current task.
	 */
	public final void task_lock() {
		this.tasks_locked = true;
	}//END task_lock
	
	/**
	 * Makes it so that the current task can be replaceds.
	 */
	public final void task_unlock() {
		this.tasks_locked = false;
	}//END task_unlock
	
	/**
	 * Returns if this object has a task or not.
	 * 
	 * @return true if this object has a task, otherwise false.
	 */
	public final boolean task_has() {
		return this.current_task != null;
	}//END task_has
	
	/* Update */
	/**
	 * Updates this object based on the time that has passed (in seconds).
	 * 
	 * @param dt the time that has passed.
	 */
	public final void update(float dt) {
		this.update_start(dt);
		this.update_move(dt);
		this.update_end(dt);
	}//END update
	
	/**
	 * Updates the position of the object.
	 * 
	 * @param dt the time that has passed.
	 */
	protected void update_move(float dt) {
		//Friction if relevant.
		if(!this.velocityChange) { //if the velocity has not been changed during this cycle.
			Vector2 norm_vect = (this.velocity.cpy()).nor();
			float magnitude = this.velocity.len() - (this.friction * dt);
			if (magnitude < 0) {
				magnitude = 0;
			}//fi
			
			this.velocity = norm_vect.scl(magnitude);
		}//fi
		
		Vector2 oldPosition = this.position.cpy();
		//Calculate and set offset.
		Vector2 positionOffset = this.velocity.cpy().scl(dt);
		Vector2 boundsCheck = this.position.cpy().add(positionOffset);
		if(boundsCheck.x < 0 || boundsCheck.x > (100 * Gdx.graphics.getWidth() * 0.8 / Gdx.graphics.getHeight()) - this.touch_width){
			positionOffset.x = 0;
		}
		if(boundsCheck.y < 0 || boundsCheck.y > 100 - this.touch_height){
			positionOffset.y = 0;
		}
		this.position.add(positionOffset); //Add the offset.
		
		//Do collision checking.
		if(this.collision(this.room.objects, true)) { //Collide with other objects.
			Vector2 newPosition = this.position.cpy();
			this.position.set(oldPosition); //Restore to old position if solid collision.
			if(this.collision(this.collisionList.keySet(), false)) {
				this.position.set(newPosition); //Go back to new position since still in an object.
			}//fi
				
		}//fi
	}//END update_move
	
	/**
	 * At the start of the update cycle do things specified by this.
	 * 
	 * @param dt the time that has passed.
	 */
	protected void update_start(float dt) {
		//Update Start
		this.behavior.event_updateStart(this);
		
		//Animation
		if(this.animator != null) {
			this.animator.update(dt);
		}//fi
		
		//Task
		if(this.current_task != null) {
			this.current_task.act(this, dt);
		}//fi
		
		//Collision
		this.collisionList.values().removeAll(Collections.singleton(!this.collision_otherCycle));
	}//END update_start
	
	/**
	 * After all the other updates does the end update.
	 * 
	 * @param dt the time that has passed.
	 */
	protected void update_end(float dt) {
		//Task
		if(this.current_task != null && this.current_task.isDone()) {
			this.current_task = this.current_task.getNext();
		}//fi
		
		//Animation
		if(this.animator != null && this.animator.is_done()) {
			this.behavior.event_animationFinish(this, this.animator.get_currentAnimation());
		}//fi
		
		//Collision
		this.collision_otherCycle = !this.collision_otherCycle;
		
		//Movement
		this.velocityChange = false; //Set the velocity to not be changed.
		
		//Update End
		this.behavior.event_updateEnd(this);
	}//END update_end
	
	/* Touch */
	/**
	 * Returns whether or not this object is touchable.
	 * 
	 * @return a boolean representing whether or not this object is touchable or not.
	 */
	public boolean isTouchable() {
		return this.touchable;
	}//END isTouchable
	
	/**
	 * Returns a array containing the touch information of this object.
	 * 
	 * @return an array with {touch_radius, touch_offsetX, touch_offsetY}.
	 */
	public Rectangle touch_area() {
		return new Rectangle(
				this.position.x - this.touch_width * this.touch_alignmentX - this.touch_offsetX,
				this.position.y - this.touch_height * this.touch_alignmentY - this.touch_offsetY,
				this.touch_width,
				this.touch_height);
	}//END touch_area
	
	/**
	 * Returns whether or not this object is controllable.
	 * 
	 * @return a boolean representing whether or not this is controllable.
	 */
	public boolean isControllable() {
		return this.controllable;
	}//END isControllable
	
	/**
	 * The event that happens when the player clicks.
	 * 
	 * @param touched_object the object that was touched.
	 * @param click_position the position in the play area the player clicked.
	 */
	public void touch_up(DeviceObject touched_object, Vector2 click_position) {
		this.behavior.event_touchUp(this, touched_object, click_position);
	}//END touch_up
	
	/**
	 * The event that happens when the player clicks.
	 * 
	 * @param touched_object the object that was touched.
	 * @param click_position the position in the play area the player clicked.
	 */
	public void touch_down(DeviceObject touched_object, Vector2 click_position) {
		this.behavior.event_touchDown(this, touched_object, click_position);
	}//END touch_up
	
	/**
	 * The event that happens when the player clicks.
	 * 
	 * @param touched_object the object that was touched.
	 * @param click_position the position in the play area the player clicked.
	 */
	public void touch_drag(DeviceObject touched_object, Vector2 click_position) {
		this.behavior.event_touchDrag(this, touched_object, click_position);
	}//END touch_up
	
	/* Other Behavior */
	/**
	 * Triggers an unspecified behavior based on the name.
	 * 
	 * @param trigger_name the trigger name to identify which behavior to play.
	 */
	public void trigger(String trigger_name) {
		this.behavior.event_trigger(this, trigger_name);
	}//END trigger
	
	/**
	 * Triggers an unspecified behavior based on the name and gives the triggering object.
	 * 
	 * @param trigger_name the trigger name to identify which behavior to play.
	 * @param triggering_object the object triggering the trigger.
	 */
	public void trigger_fromObject(String trigger_name, DeviceObject triggering_object) {
		this.behavior.event_trigger(triggering_object, trigger_name);
	}//END trigger
	
	/* Special */
	/**
	 * Gives an indication of how to draw this object.
	 * 
	 * @return a float representing where on the z plane this object is.
	 */
	public float getZ() {
		return this.position.y;
	}//END getZ
	
	/**
	 * Sets the animator of this object.
	 * 
	 * @param animator an animator to use for this object.
	 */
	public void setAnimator(Animator animator) {
		this.animator = animator;
	}//END setAnimator
	
	/**
	 * Sets this object as being dead.
	 */
	public final void kill() {
		this.dead = true;
	}//END kill
	
	/**
	 * Runs the behavior of when this object is finalized.
	 */
	public final void finalize() {
		behavior.event_finalize(this);
	}//END behavior_terminate
	
	/**
	 * Indicates whether or not to destroy this object.
	 * 
	 * @return a boolean representing whether or not this object is dead.
	 */
	public final boolean isDead() {
		return this.dead;
	}//END isDead
}//END class DeviceObject
