package com.pressx.objects.items;

import java.util.ArrayList;
import java.util.HashSet;

import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;

public class Gear extends AnimatedObject {
	private int lvl;
	private int bounce;
	private float travel;
	private HashSet<GameObject> hit = new HashSet<GameObject>();
	public Gear(Draw d, Sounds s, Textures t, float posX, float posY){
		super(d, s, t, "gear", 1004, posX, posY, 1, 0, 8, 8, 0, 0, 
				false, 0, false, 15, 15, t.getArtAsset("gear"), 
				200, 200);
		lvl = 1;
		travel = 2;
		bounce = 3;
	}
	@Override
	public void behavior_collision(GameObject obj){
		if(obj.getID() == 3 && !hit.contains(obj)) {
			obj.setHp(obj.getHp() - lvl);
			hit.add(obj);
			bounce--;
		}
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		travel -= dt;
		if(travel < 0 || bounce > 0)
			this.terminate();
	}
}