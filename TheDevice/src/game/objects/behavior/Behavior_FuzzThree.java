package game.objects.behavior;

import game.objects.DeviceObject;

import com.badlogic.gdx.math.Vector2;

public class Behavior_FuzzThree extends Behavior_Enemy{
	
	
	@Override
	public void event_initialize(DeviceObject self) {
		super.event_initialize(self);
		this.health = 3;
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
	}
	
	@Override
	public void event_updateStart(DeviceObject self){
		super.event_updateStart(self);
	}
}
