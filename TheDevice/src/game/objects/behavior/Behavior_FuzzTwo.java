package game.objects.behavior;

import game.objects.DeviceObject;
import game.objects.behavior.tasks.Task_BumRush;
import game.objects.behavior.tasks.Task_GotoObject;
import game.objects.behavior.tasks.Task_Stun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Behavior_FuzzTwo extends Behavior_Enemy{
	
	private float timeElapsed;
	private boolean isBumming = false;
	
	@Override
	public void event_initialize(DeviceObject self) {
		super.event_initialize(self);
		this.health = 2;
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
		if(other.equals(playerTarget) && isBumming){
			self.animator.play("Movement", 0, 10, true);
			self.task_set(new Task_GotoObject(deviceTarget, 2f));
			isBumming = false;
			playerTarget.task_set(new Task_Stun());
		}
		if(other.equals(deviceTarget)){
			
		}
	}
	
	@Override
	public void event_updateStart(DeviceObject self){
		super.event_updateStart(self);
		if(!isBumming){
			timeElapsed += Gdx.graphics.getDeltaTime();
		}
		if(deviceTarget == null || playerTarget == null){
			return;
		}
		if(!self.task_has()){
			isBumming = false;
			self.animator.play("Movement", 0, 10, true);
			self.task_set(new Task_GotoObject(deviceTarget, 2f));
		}
		if(timeElapsed >= 5){
			self.task_set(new Task_BumRush(playerTarget));
			isBumming = true;
			timeElapsed = 0;
		}
	}
}
