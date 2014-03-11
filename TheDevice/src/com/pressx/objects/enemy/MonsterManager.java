package com.pressx.objects.enemy;

import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.screens.game.Room;

public class MonsterManager {
	Draw d;
	Sounds s;
	Textures t;
	GameObject o;
	Room r;
	
	public MonsterManager(Draw d, Sounds s, Textures t, GameObject o, Room room){
		this.d = d;
		this.s = s;
		this.t = t;
		this.o = o;
		this.r = room;
	}
	
	public Enemy spawnMonster(int ID, float xPos, float yPos){
		if(ID == 1){
			return new FuzzOne(d, s, t, o, xPos, yPos, r);
		}
		if(ID == 2){
			return new FuzzTwo(d, s, t, o, xPos, yPos, r);
		}
		if(ID == 3){
			return new FuzzThree(d, s, t, o, xPos, yPos, r );
		}
		if(ID == 4){
			return new PlantOne(d, s, t, o, xPos, yPos, r);
		}
		if(ID == 5){
			return new PlantTwo(d, s, t, o, xPos, yPos, r);
		}
		return null;
	}
}