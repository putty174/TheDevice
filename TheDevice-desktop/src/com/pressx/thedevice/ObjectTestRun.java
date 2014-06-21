package com.pressx.thedevice;

import game.ObjectTest;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class ObjectTestRun {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "TheDevice";
		cfg.width = 800;
		cfg.height = 600;
		cfg.fullscreen = false;
		cfg.useGL20 = true;
		cfg.vSyncEnabled = false;
		cfg.resizable = false;
		
		new LwjglApplication(new ObjectTest(), cfg);
	}
}
