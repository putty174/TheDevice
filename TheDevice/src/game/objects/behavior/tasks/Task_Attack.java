package game.objects.behavior.tasks;

import game.objects.DeviceObject;

public class Task_Attack extends Task{
	private DeviceObject victim; //The object being attacked.
	private float range; //The distance the object attacks otherwise failure.
	
	/**
	 * Creates a task that causes the tasked object to attack a victim.
	 * 
	 * @param victim the object being attacked.
	 * @param range the range the tasked object must be in to actually hit the victim.
	 */
	public Task_Attack(DeviceObject victim, float range) {
		this.victim = victim;
		this.range = range;
	}//END Task_Attack
	
	@Override
	public void act(DeviceObject self, float dt) {
		if (!self.animator.isAnimation("Attack")) {
			self.animator.play("Attack", 0, self.attribute_get("attack_speed"), false);
		}//fi
		else if (self.animator.is_done()) {
			// Play "AttackEnd" animation if the behaving object has one.
			if(self.animator.containsAnimation("AttackEnd")) {
				self.animator.play("AttackEnd", 0, self.attribute_get("attack_speed"), false);
			}//fi
			
			//Attack!
			if(self.position_get().dst(this.victim.position_get()) <= this.range) { //If in range...
				victim.trigger_fromObject("attacked_hit", self);
				self.trigger("attack_connected"); //To bad... It's a miss.
				//victim.attribute_add("health", -self.attribute_get("attack_damage"));
			}//fi
			else {
				self.trigger("attack_whiff"); //To bad... It's a miss.
			}//esle
			
			this.terminate();
		}//fi esle
	}//END act

}//END Task_Attack
