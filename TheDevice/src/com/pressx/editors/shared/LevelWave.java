package com.pressx.editors.shared;

import java.util.ArrayList;


public class LevelWave{
	public ArrayList<SingleFormation> formations;
	public int numFormationsUsed = 5;
	public float delayBetweenFormations = 15;
	public String name;
	public boolean isRandomized = true;
	
	public LevelWave(){
		formations = new ArrayList<SingleFormation>();
	}
	
	public LevelWave(ArrayList<SingleFormation> formations){
		this.formations = formations;
	}
	
	public LevelWave(LevelWave copyfrom){
		formations = new ArrayList<SingleFormation>();
		for(SingleFormation form : copyfrom.formations){
			formations.add(form);
		}
		numFormationsUsed = copyfrom.numFormationsUsed;
		delayBetweenFormations = copyfrom.delayBetweenFormations;
		name = copyfrom.name;
		isRandomized = copyfrom.isRandomized;
	}
}
