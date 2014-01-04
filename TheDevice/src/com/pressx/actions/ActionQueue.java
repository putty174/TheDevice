package com.pressx.actions;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.pressx.objects.GameObject;

public class ActionQueue {
	private ConcurrentLinkedQueue<Action> actions = new ConcurrentLinkedQueue<Action>();

	/* Action Management */
	/**
	 * Adds an action to a queue.
	 * 
	 * @param action
	 */
	public final void add_action(Action action)
	{
		this.actions.add(action);
	}//END add_action
	
	/**
	 * @return the first action
	 */
	public final Action get_action()
	{
		return this.actions.peek();
	}//END get_action
	
	/**
	 * Clears the ActionQueue.
	 */
	public final void clear()
	{
		this.actions.clear();
	}//END clear
	
	/**
	 * @return the ID of the current action, or 0 if there are no actions
	 */
	public final int get_actionID()
	{
		Action curAction = this.actions.peek();
		
		if(curAction != null)
		{
			return curAction.getID();
		}//fi
		//esle...
		return 0; //0 is an indicator for nothing.
	}
	
	/**
	 * Makes an object act based on first thing in the queue.
	 * 
	 * @param self the object that is acting
	 */
	public void act(GameObject self)
	{
		Action curAction = this.actions.peek();
		
		if(curAction != null)
		{
			if(curAction.isDone())
			{
				//Delete current and get next element.
				this.actions.poll();
				this.act(self);
			}//fi
			else
			{
				curAction.act(self);
			}//esle
		}//fi
	}//END act
}