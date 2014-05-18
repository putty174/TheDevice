package game.objects.behavior;

import com.badlogic.gdx.math.Vector2;

import game.objects.DeviceObject;

public class Behavior {
	/* Start */
	/**
	 * Behavior of the object when it is created.
	 * 
	 * @param self the main object.
	 */
	public void event_initialize(DeviceObject self) {}
	
	/* Collision */
	/**
	 * Behavior of when an object collides.
	 * 
	 * @param self the main object that is colliding.
	 * @param other the object being collided with.
	 */
	public void event_collision(DeviceObject self, DeviceObject other) {}

	/* Animation */
	/**
	 * Behavior of when the object ends its animation.
	 * 
	 * @param self the object whose animation has ended.
	 * @param animation_name the name of the animation.
	 */
	public void event_animationFinish(DeviceObject self, String animation_name) {}
	
	/* Touch */
	/**
	 * Behavior when player touches up.
	 * 
	 * @param self the position of where the player touched.
	 * @param click_position the position of where the player clicked.
	 */
	public void event_touchUp(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {}
	
	/**
	 * Behavior when player touch down.
	 * 
	 * @param self the position of where the player touched.
	 * @param click_position the position of where the player clicked.
	 */
	public void event_touchDown(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {}
	
	/**
	 * Behavior when player touch drags.
	 * 
	 * @param self the position of where the player touched.
	 * @param click_position the position of where the player clicked.
	 */
	public void event_touchDrag(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {}
	
	/* Event Triggers */
	/**
	 * An unspecified behavior where if certain triggers happen, an event will happen.
	 * 
	 * @param self the object doing the behaving.
	 * @param trigger_name the name of the trigger.
	 */
	public void event_trigger(DeviceObject self, String trigger_name) {}
	

	/**
	 * An unspecified behavior where if certain triggers happen, an event will happen.
	 * 
	 * @param self the object doing the behaving.
	 * @param trigger_name the name of the trigger.
	 * @param triggering_object the object triggering the trigger.
	 */
	public void event_trigger_fromObject(DeviceObject self, DeviceObject triggering_object, String trigger_name) {}
	
	/**
	 * Behavior that happens at the start of every update.
	 * 
	 * @param sel the object doing the behaving.
	 */
	public void event_updateStart(DeviceObject self) {}
	
	/**
	 * Behavior that happens at the end of every update.
	 * 
	 * @param self the object doing the behaving.
	 */
	public void event_updateEnd(DeviceObject self) {}
	
	/**
	 * Behavior when the object is removed from the game.
	 * 
	 * @param self the object doing the behaving.
	 */
	public void event_finalize(DeviceObject self) {}

}//END class Behavior
