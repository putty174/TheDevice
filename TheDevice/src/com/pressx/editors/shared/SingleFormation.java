package com.pressx.editors.shared;

import java.util.Random;

import com.pressx.editors.leveleditor.ImportedFormation;

public class SingleFormation{
	public static float spawnAngleToRadians(byte spawnangle,Random random){
		if(spawnangle == SPAWNANGLE_RANDOM)
			return random.nextFloat()*(_G.PI*2);
		if(spawnangle == SPAWNANGLE_RANDOMRIGHT)
			return _G.PI*random.nextInt(4)/2;
		return _G.PI*2/SPAWNANGLE_NUMSEGMENTS*spawnangle;
	}
	public static String spawnAngleToString(byte spawnangle){
		if(spawnangle == SPAWNANGLE_RANDOM)
			return "Spawn at a random angle";
		if(spawnangle == SPAWNANGLE_RANDOMRIGHT)
			return "Spawn at a random right angle";
		return "Spawn at "+(int)(spawnAngleToRadians(spawnangle,null)/_G.PI/2*360)+" degrees";
	}
	public static byte SPAWNANGLE_NUMSEGMENTS = 24;
	public static byte SPAWNANGLE_RANDOM = (byte)(SPAWNANGLE_NUMSEGMENTS);
	public static byte SPAWNANGLE_RANDOMRIGHT = (byte)(SPAWNANGLE_NUMSEGMENTS+1);
	public static byte SPAWNANGLE_MAX = SPAWNANGLE_RANDOMRIGHT;
	
	public String name;
	public byte spawnAngle = SPAWNANGLE_RANDOMRIGHT;//< -15 means a random angle; < -30 means a random right angle
	
	public SingleFormation(){}
	public SingleFormation(ImportedFormation from){
		name = from.name;
	}
	public SingleFormation(SingleFormation copyfrom){
		name = copyfrom.name;
		spawnAngle = copyfrom.spawnAngle;
	}
}
