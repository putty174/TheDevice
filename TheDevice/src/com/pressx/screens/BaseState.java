package com.pressx.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pressx.control.Controller;
import com.pressx.thedevice.TheDevice;

public abstract class BaseState {
	Controller controller;
	TheDevice game;
	
	public BaseState(TheDevice g){
		this.game = g;
	}
	public abstract void create();
	public abstract void update();
	public abstract void render(SpriteBatch batch);
	public abstract void dispose();
}