package com.pressx.thedevice;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "TheDevice";
		cfg.useGL20 = false;
		cfg.width = 1280;//800;
		cfg.height = 800;//600;
		
		new LwjglApplication(new TheDevice(), cfg);
	}//END main
}
