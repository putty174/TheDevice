package game.objects.behavior.tasks;

import com.badlogic.gdx.math.Vector2;

import game.objects.DeviceObject;

public class Task_GotoPoint extends Task_Goto {
	private Vector2 point; //Object to be tracked.
	
	/* Constructor */
	/**
	 * Task to goto other object.
	 * 
	 * @param point the point to goto.
	 * @param termination_range the range at which this task terminates.
	 */
	public Task_GotoPoint(Vector2 point, float termination_range) {
		super(termination_range);
		this.point = point;
	}//END Task_GotoObject
	
	/* Action */
	@Override
	public void act(DeviceObject self, float dt) {
		this.goto_point(self, this.point, dt);
	}//END act
}//END class Task_GotoObject
