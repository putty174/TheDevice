package game.objects.behavior.tasks;

import game.objects.DeviceObject;

public class Task_GotoObject extends Task_Goto {
	private DeviceObject obj; //Object to be tracked.
	
	/* Constructor */
	/**
	 * Task to goto other object.
	 * 
	 * @param obj the object to track.
	 * @param termination_range the range at which this task terminates.
	 */
	public Task_GotoObject(DeviceObject obj, float termination_range) {
		super(termination_range);
		this.obj = obj;
	}//END Task_GotoObject
	
	/* Action */
	@Override
	public void act(DeviceObject self, float dt) {
		this.goto_point(self, this.obj.position_get(), dt);
	}//END act
}//END class Task_GotoObject
