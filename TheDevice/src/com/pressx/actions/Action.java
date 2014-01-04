package com.pressx.actions;

import com.pressx.objects.GameObject;

public abstract class Action {
	private boolean isDone = false;
	private int actionID;
	
	/* Constructor */
	/**
	 * Creates an action which is an instruction for an object to do something.
	 * 
	 * @param actionID the identifier of the action
	 */
	public Action(int actionID)
	{
		this.actionID = actionID;
	}//END Action
	
	/* Identification */
	/**
	 * @return the actions ID
	 */
	public int getID()
	{
		return this.actionID;
	}
	
	/* Action */
	/**
	 * Makes an object perform an action.
	 * 
	 * @param self the object doing the acting
	 */
	public abstract void act(GameObject self);
	
	/**
	 * Indicates that the action is done.
	 */
	protected final void terminate()
	{
		this.isDone = true;
	}//END terminate
	
	/**
	 * @return true if is done, otherwise false.
	 */
	public final boolean isDone()
	{
		return this.isDone;
	}//END isDone
}