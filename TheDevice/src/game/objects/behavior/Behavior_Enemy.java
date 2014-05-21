package game.objects.behavior;

import game.objects.DeviceObject;
import game.objects.behavior.tasks.Task_Attack;
import game.objects.behavior.tasks.Task_Die;
import game.objects.behavior.tasks.Task_GotoObject;
import game.objects.Enemy;

import com.badlogic.gdx.math.Vector2;

public class Behavior_Enemy extends Behavior{
	
	protected DeviceObject target;
	private boolean canDie = false, isClicked = false, isDying = false;
	private int health = 2;

	public void setTarget(DeviceObject obj){
		this.target = obj;
	}
	
	@Override
	public void event_initialize(DeviceObject self) {
		self.animator.play("Movement", 0, 10, true);
	}
	
	@Override
	public void event_touchUp(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {

	}//END event_touchUp
	
	@Override
	public void event_touchDown(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {
		if(self.touch_area().contains(click_position)){
			isClicked = true;
		}
		else{
			isClicked = false;
			canDie = false;
		}
	}
	
	@Override
	public void event_touchDrag(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {

	}//END event_click
	
	@Override
	public void event_collision(DeviceObject self, DeviceObject other){
		if(other.identity.equals("player")){
			if(isClicked){
				isDying = true;
			}
		}
		if(other.identity.equals("box")){
			self.task_set(new Task_Attack(other, 2f));
		}
	}
	
	@Override
	public void event_updateStart(DeviceObject self){
		if(isDying){
			self.task_set(new Task_Die(10f));
		}
		else{
			if(target != null && !self.task_has()){
				self.task_set(new Task_GotoObject(target, 1f));
			}
		}
	}
	
}
