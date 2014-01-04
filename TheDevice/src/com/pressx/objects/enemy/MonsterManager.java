package com.pressx.objects.enemy;

import com.pressx.objects.GameObject;
import com.pressx.screens.game.Room;

public class MonsterManager {
	GameObject d;
	Room r;
	
	public MonsterManager(GameObject d, Room room){
		this.d = d;
		this.r = room;
	}
	
	public Enemy spawnMonster(int ID, float xPos, float yPos){
		if(ID == 1){
			return new FuzzOne(d, xPos, yPos, r);
		}
		if(ID == 2){
			return new FuzzTwo(d, xPos, yPos, r);
		}
		if(ID == 3){
			return new FuzzThree(d, xPos, yPos, r );
		}
		if(ID == 4){
			return new PlantOne(d, xPos, yPos, r);
		}
		if(ID == 5){
			return new PlantTwo(d, xPos, yPos, r);
		}
		return null;
	}
}