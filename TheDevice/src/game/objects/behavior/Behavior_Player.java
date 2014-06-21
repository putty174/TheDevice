package game.objects.behavior;

import com.badlogic.gdx.math.Vector2;

import game.objects.Attachment;
import game.objects.DeviceObject;
import game.objects.behavior.tasks.Task_Die;
import game.objects.behavior.tasks.Task_GotoPoint;

public class Behavior_Player extends Behavior {
	/* Initialization */
	@Override
	public void event_initialize(DeviceObject self) {
		self.animator.play("Attack", 0, 10, false);
		Attachment att = self.room.spawn_attachment("halo", "halo", self, 25, 7.5f, 0.5f, 0, -3, -13, -0.01f);
		att.animator.play("Lulz", 0, 15, true);
	}//END event_initialize
	
	/* Touch */
	@Override
	public void event_touchUp(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {
		self.task_set(new Task_GotoPoint(click_position, 1f));
	}//END event_touchUp
	
	@Override
	public void event_touchDown(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {
		self.task_set(new Task_GotoPoint(click_position, 1f));
	}//END event_click
	
	@Override
	public void event_touchDrag(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {
		self.task_set(new Task_GotoPoint(click_position, 1f));
	}//END event_click
	
	/* Collision */
	@Override
	public void event_collision(DeviceObject self, DeviceObject other) {
	}//END event_collision
	
	/* Animation */
	@Override
	public void event_animationFinish(DeviceObject self, String animation_name) {
		self.animator.play("Movement", 0, 10, true);
	}//END event_animationFinish
	
	/* Update */
	@Override
	public void event_updateStart(DeviceObject self) {
		System.out.println(self.room.jukebox.sound_play("s_0", 1, 1, 0));
//		ArrayList<DeviceObject> objects = self.room.objects;
//		Iterator<DeviceObject> iter = objects.iterator();
//		while(iter.hasNext()) {
//			DeviceObject obj = iter.next();
//
//			if(!self.task_has() && obj != self) {
//				self.task_set(Common_Tasks.attack_object(20, 24, obj));
//			}//fi
//		}//elihw
	}//END event_updateStart
}//END Behavior
