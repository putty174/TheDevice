package game.objects.behavior;

import game.objects.DeviceObject;
import game.objects.behavior.tasks.Task_Attack;
import game.objects.behavior.tasks.Task_GotoObject;

import com.badlogic.gdx.math.Vector2;

public class Behavior_FuzzOne extends Behavior_Enemy {
	@Override
	public void event_initialize(DeviceObject self) {
		super.event_initialize(self);
	}
	
	@Override
	public void event_touchUp(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {

	}//END event_touchUp
	
	@Override
	public void event_touchDown(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {
		super.event_touchDown(self, touched_object, click_position);
	}
	
	@Override
	public void event_touchDrag(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {

	}//END event_click
	
	@Override
	public void event_collision(DeviceObject self, DeviceObject other){
		super.event_collision(self, other);
		if(other.identity.equals("box")){
			if(!self.animator.isAnimation("Attack")){
				self.task_set(new Task_Attack(other, 5f));
			}
		}
	}
	
	@Override
	public void event_updateStart(DeviceObject self){
		super.event_updateStart(self);
		if(self.isDead()){
			return;
		}
		if(deviceTarget != null){
			if(self.animator.isAnimation("Attack")){
				if(!self.animator.is_done()){
					return;
				}
				else{
					self.animator.play("Movement", 0, 10, true);
					self.task_set(new Task_GotoObject(deviceTarget, 2f));
				}
			}
			else if(!self.task_has()){
				self.animator.play("Movement", 0, 10, true);
				self.task_set(new Task_GotoObject(deviceTarget, 2f));
			}
		}
	}
}
