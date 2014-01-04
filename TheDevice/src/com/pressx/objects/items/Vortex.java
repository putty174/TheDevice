package com.pressx.objects.items;

import java.util.ArrayList;

import com.pressx.control.GameTimer;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;

public class Vortex extends AnimatedObject {
	GameTimer actTimer = new GameTimer(10);
	float scaleBase = 3;

	public Vortex(float posX, float posY) {
		super("vortex",14, posX, posY, 0, 0, 5, 5, 0, 0,
				false, 2.5f, false, 4, 4, Textures.getArtAsset("vortex"),
				400, 400);
		
		this.add_animation("vortex_active", 0, 0, 5, 30, true);
		this.set_animation("vortex_active", true);
		
		Sounds.play("hero.vortex");
	}
	
	@Override
	public void behavior_collision(GameObject obj){
		if(obj.getID() == 2){
			obj.terminate();
		}
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		super.update(dt, objects);
		if(actTimer.isDone()){
			this.terminate();
		}
		else{
			specMove(objects);
			this.sprite.setScale(scaleBase + (float) Math.sin(actTimer.get_time() * 5) / 3);
			scaleBase -= dt/4;
			actTimer.update_timer(dt);
		}
	}
	
	private void specMove(ArrayList<GameObject> objects){
		for(GameObject obj : objects){
			float x = this.get_positionX() - obj.get_positionX();
			float y = this.get_positionY() - obj.get_positionY();
			float dist = (float)Math.sqrt((x * x) + (y * y));
			if(obj.getID() == 2){
				obj.impact(25 / (dist), Math.atan2(y, x));
			}
		}
	}
	
	@Override
	protected float getZ()
	{
		return 10000;
	}
}