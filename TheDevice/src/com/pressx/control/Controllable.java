package com.pressx.control;

public interface Controllable {
	/**
	 * Function for when a click happens.
	 * 
	 * @param x the x position of the touch
	 * @param y the y position of the touch
	 * @param pointer the address of the touch
	 * @param button the type of mouse click
	 */
	public void input_touchDown(float x, float y, int pointer, int button);
	public void input_touchUp(float x, float y, int pointer, int button);
	public void input_touchDrag(float x, float y, int pointer);
}