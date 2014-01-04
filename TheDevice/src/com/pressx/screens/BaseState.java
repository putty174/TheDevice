package com.pressx.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.control.Controller;

public abstract class BaseState {
	Controller controller;
	
	public BaseState(){
	}
	public abstract void create();
	public abstract void update();
	public abstract void render(SpriteBatch batch);
	public abstract void dispose();
}