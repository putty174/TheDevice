package com.pressx.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.pressx.spawner.LevelLoader.LevelData;

public class Levels {
	public AssetManager l_manager = new AssetManager();
	private HashMap<String, HashMap<String, String>> entries = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, String> currentPackage = new HashMap<String, String>();
	
	public Levels() {
		addPath(
				"1-1", "data/leveldata/levels/level1.devicelevel"
				);
		
		addPath(
				"level1",
				"1_fuzzy", "data/leveldata/formations/1_fuzzy.spawnformation",
				"2_fuzzy_oppositesides", "data/leveldata/formations/2_fuzzy_oppositesides.spawnformation",
				"3_fuzzy_bodyguard_north", "data/leveldata/formations/3_fuzzie/bodyguard_north.spawnformation",
				"4_fuzzy_backandforth", "data/leveldata/formations/4_fuzzy_backandforth.spawnformation",
				"5_fuzzy_semicircle", "data/leveldata/formations/5_fuzzy_semiciricle.spawnformation",
				"8_fuzzie_onetwopunch_eastsouth", "data/leveldata/formation/8_fuzzie_onetwopunch_eastsouth.spawnformation"
				);
	}
	
	public void addPath(String packageName, String... args)
	{
		if(!entries.containsKey(packageName))
			entries.put(packageName, new HashMap<String, String>());
		for(int i = 0; i < args.length; i ++)
			entries.get(packageName).put(args[i], args[++i]);
	}
	
	public boolean loadSoundAssets(String packageName)
	{
		if(entries.containsKey(packageName))
		{
			currentPackage = entries.get(packageName);
			for(String str : entries.get(packageName).values())	
				l_manager.load(str, LevelData.class);
			l_manager.finishLoading();
			return true;
		}
		return false;
	}
}

