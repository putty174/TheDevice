package game.objects.behavior.tasks;

import game.objects.DeviceObject;

public final class Common_Tasks {
	private Common_Tasks() {}
	
	/* Common Task Generator */
	/**
	 * Generates a task where an object will move to another and attack it.
	 * 
	 * @param attempt_range the range where the tasked object will attempt to hit the victim.
	 * @param attack_range the range needed where the tasked object will actually hit the victim.
	 * @param victim the object being attacked.
	 * @return a task where the tasked object will goto and try to attack an object.
	 */
	public static final Task attack_object(float attempt_range, float attack_range, DeviceObject victim) {
		Task_GotoObject goto_obj = new Task_GotoObject(victim, attempt_range);
		Task_Attack atta_obj = new Task_Attack(victim, attack_range);
		goto_obj.setNext(atta_obj);
		return goto_obj;
	}//END attack_object
}//END class Common_Tasks
