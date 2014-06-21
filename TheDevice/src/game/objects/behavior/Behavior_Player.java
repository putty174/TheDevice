package game.objects.behavior;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import game.objects.Attachment;
import game.objects.DeviceObject;
import game.objects.Enemy;
import game.objects.behavior.tasks.Task_Die;
import game.objects.behavior.tasks.Task_GotoPoint;

public class Behavior_Player extends Behavior {
	/* Initialization */
	
	private int curIndex = 0;
	private String curAction = "Idle";
	private Enemy target;
	private ArrayList<String> monsterID = new ArrayList<String>();
	
	@Override
	public void event_initialize(DeviceObject self) {
		self.animator.play("IdleDown", 0, 10, true);
		//Attachment att = self.room.spawn_attachment("halo", "halo", self, 25, 7.5f, 0.5f, 0, -3, -13, -0.01f);
		//att.animator.play("Lulz", 0, 15, true);
		monsterID.add("fuzz1");
		monsterID.add("fuzz2");
		monsterID.add("fuzz3");
		monsterID.add("plant1");
		monsterID.add("plant2");
		monsterID.add("plant3");
	}//END event_initialize
	
	/* Touch */
	@Override
	public void event_touchUp(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {
		if(curAction.equals("Attack")){
			return;
		}
		self.task_set(new Task_GotoPoint(click_position, 1f));
		setDirectionBasedAnimation(self, click_position, "Move");
	
	}//END event_touchUp
	
	@Override
	public void event_touchDown(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {
		if(curAction.equals("Attack")){
			return;
		}
		if(touched_object != null){
			if(monsterID.contains(touched_object.identity)){
				//setDirectionBasedAnimation(self, touched_object.position_get(), "Attack");
				target = (Enemy)touched_object;
			}
		}
		
		self.task_set(new Task_GotoPoint(click_position, 1f));
		setDirectionBasedAnimation(self, click_position, "Move");
		
		
		
		
	}//END event_click
	
	@Override
	public void event_touchDrag(DeviceObject self, DeviceObject touched_object, Vector2 click_position) {
		if(curAction.equals("Attack")){
			return;
		}
		self.task_set(new Task_GotoPoint(click_position, 1f));
		setDirectionBasedAnimation(self, click_position, "Move");
	
	}//END event_click
	
	/* Collision */
	@Override
	public void event_collision(DeviceObject self, DeviceObject other) {
		if(monsterID.contains(other.identity)){
			if(target != null && target == other){
				self.task_set(null);
				System.out.println("Wat");
				setDirectionBasedAnimation(self, other.position_get(), "Attack");
				target = null;
			}
		}
	}//END event_collision
	
	/* Animation */
	@Override
	public void event_animationFinish(DeviceObject self, String animation_name) {
		//self.animator.play("Movement", 0, 10, true);
		self.animator.play("Idle", curIndex, 0, 10, true);
	}//END event_animationFinish
	
	/* Update */
	@Override
	public void event_updateStart(DeviceObject self) {
		if(curAction.equals("Idle")){
			return;
		}
		if(!self.task_has()){
			if(curAction.equals("Attack")){
				if(self.animator.is_done()){
					curAction = "Idle";
					self.animator.play(curAction, curIndex, 0, 10, true);
				}
			}
			else{
				curAction = "Idle";
				self.animator.play(curAction, curIndex, 0, 10, true);
			}
		}

	}//END event_updateStart
	
	private void setDirectionBasedAnimation(DeviceObject self, Vector2 target, String action){
		int dir;
		if(action.equals("Attack")){
			dir = (((int)(target.cpy().sub(self.position_get()).angle() + 45) / 90) % 4) * 2;
			curAction = "Attack";
			self.animator.play(curAction, dir, 0, 10, false);
		}
		else{
			dir = (int)((target.cpy().sub(self.position_get()).angle() + 22.5) / 45) % 8;
			curAction = action;
			self.animator.switch_animation(curAction, curIndex);
		}
		curIndex = dir;
	}
	
}//END Behavior
