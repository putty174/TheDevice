package com.pressx.objects.enemy.projectiles;

import java.util.ArrayList;

import com.pressx.control.GameTimer;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;

public class Shockwave extends AnimatedObject {
	GameTimer displayTime = new GameTimer(1);
	float currentScale = 1;
	
	public Shockwave(Draw d, Sounds s, Textures t, float posX, float posY) {
		super(d,s,t,"shockwave", 18, posX, posY, 1, 1, 0, 0, 0, 0,
				false, 0, false, 10, 10, t.getArtAsset("shock_wave"),
				512, 512);
		
		this.add_animation("shock", 0, 0, 3, 3, false);
		this.set_animation("shock", true);
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		displayTime.update_timer(dt);
		if(!displayTime.isDone())
		{
			this.drawWidth += dt * 100;
			this.drawHeight += dt * 100;
			this.sprite.setColor(1, 1, 1, this.currentScale -= dt);
		}
		else
			this.terminate();
	}
	
	@Override
	protected float getZ()
	{
		return 10000;
	}
}