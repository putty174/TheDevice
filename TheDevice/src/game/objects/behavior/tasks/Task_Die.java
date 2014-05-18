package game.objects.behavior.tasks;

import game.objects.DeviceObject;

public class Task_Die extends Task {
	private float death_speed;
	
	/**
	 * Creates a new task causing the tasked object to die.
	 * 
	 * @param death_speed the speed at which to play the animation (frames/sec).
	 */
	public Task_Die(float death_speed) {
		this.death_speed = death_speed;
	}//END Task_Die
	
	@Override
	public void act(DeviceObject self, float dt) {
		if (self.animator != null && self.animator.containsAnimation("Death")) {
			if (!self.animator.isAnimation("Death")) {
				self.animator.play("Death", 0, this.death_speed, false);
			}//fi
			else if (self.animator.is_done()) {
				self.kill();
			}//fi esle
		}//fi
		else {
			self.kill();
		}//esle
	}//END act
}//END class Task_Die
