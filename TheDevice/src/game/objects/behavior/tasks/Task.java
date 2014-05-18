package game.objects.behavior.tasks;

import game.objects.DeviceObject;

public abstract class Task {
	/* Finished? */
	private boolean done = false;
	
	/**
	 * Returns whether or not this task is done.
	 * 
	 * @return a boolean specifying whether this task is done or not.
	 */
	public final boolean isDone() {
		return this.done;
	}//END isDone
	
	/**
	 * Sets this task as being done.
	 */
	protected void terminate() {
		this.done = true;
	}//END terminate
	
	/* Action */
	/**
	 * Acts upon the given task.
	 * 
	 * @param self the object doing the action.
	 * @param dt the difference in time between this update and the last update.
	 */
	public void act(DeviceObject self, float dt) {
		this.terminate();
		//Override this...
	}//END act
	
	/* Next Task */
	private Task next = null; //The next task.
	
	/**
	 * Returns the next task for the acting object to do.
	 * 
	 * @return the next task.
	 */
	public final Task getNext() {
		return this.next;
	}//END getNext
	
	/**
	 * Sets the next task to do.
	 * 
	 * @param next the next task.
	 */
	public final void setNext(Task next) {
		this.next = next;
	}//END setNext
	
	/* Task Linking */
	/**
	 * Links the specified tasks, with the first task being the first specified.
	 * 
	 * @param tasks the tasks to link.
	 * @return the first task.
	 */
	public static final Task link_tasks(Task... tasks) {
		Task first = null;
		Task last = null;
		for(int i = 0; i < tasks.length; i++) {
			if(first == null) {
				first = tasks[i];
			}//fi
			else {
				last.setNext(tasks[i]);
			}//esle
			last = tasks[i];
		}//rof
		return first;
	}//END 
}//END abstract class Task
