package game.objects.behavior;

import game.objects.DeviceObject;
import game.objects.behavior.tasks.Task_Die;

public class Behavior_Common extends Behavior {
	private boolean isDying = false;
	protected float animation_deathSpeed = 10;
	
	@Override
	public void event_updateEnd(DeviceObject self) {
		if(!this.isDying) {
			float health = self.attribute_get("health");
			float health_max = self.attribute_get("health_max");
			
			if (health > health_max) {
				self.attribute_set("health", health_max);
			}//fi
			else if (health <= 0) {
				self.isTangible = false;
				self.task_set(new Task_Die(this.animation_deathSpeed));
				self.task_lock();
				this.isDying = true;
			}//esle
		}//fi
	}//END self
}//END Behavior_Common
