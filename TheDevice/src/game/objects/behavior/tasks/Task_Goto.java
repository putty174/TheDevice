package game.objects.behavior.tasks;

import game.objects.DeviceObject;

import com.badlogic.gdx.math.Vector2;

public abstract class Task_Goto extends Task {
	private float termination_range; //The range at which this action terminates.
	
	/* Construction */
	/**
	 * Creates a task object with a goto point.
	 * 
	 * @param goto_point the goto point.
	 * @param termination_range how close to get before this task terminates.
	 */
	public Task_Goto(float termination_range) {
		this.termination_range = termination_range;
	}//END Task_Goto
	
	/* Action */	
	protected final void goto_point(DeviceObject self, Vector2 goto_point, float dt) {
		goto_point = goto_point.cpy();
		if(goto_point.dst(self.position_get()) <= this.termination_range) {
			this.terminate();
		}//fi
		else {
			//Extract information...
			Vector2 vel = self.velocity_get();
			float speed = self.attribute_get("movement_speed"); //Get speed attribute of the object.
			float acceleration = self.attribute_get("movement_acceleration"); //Get the acceleration of the object.
			
			//Calculate the velocity vector.
			Vector2 norm = goto_point.cpy().sub(self.position_get()).nor();
			Vector2 error = norm.cpy().sub(self.velocity_get().nor());
			Vector2 comp = norm.cpy().scl(acceleration*dt).add(error.cpy().scl(acceleration*dt));

			//Add velocity to object.
			vel.add(comp);
			if(vel.len() < speed || vel.len() < self.velocity_get().len()) {
				self.velocity_add(comp);
			}//fi
			else if (self.velocity_get().len() < speed) {
				self.velocity_set(norm.cpy().scl(speed));
			}//fi esle
			else if (Math.abs(self.velocity_get().angle() - norm.angle()) < 3) {
				self.velocity_toggle();
			}//fi esle
		}//esle
	}//END goto_point
}//END class Task_Goto
