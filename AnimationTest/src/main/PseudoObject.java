package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PseudoObject {
	
	AnimationManager manager;
	
	public PseudoObject(){
		manager = new AnimationManager(Gdx.files.internal("data/fuzz1.txt"));
	}
	
	public Sprite update(){
		if(Gdx.input.isTouched()){
			manager.changeAnimation("Death");
		}
		return manager.update();
	}
	
}
