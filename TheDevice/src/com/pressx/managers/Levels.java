package com.pressx.managers;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.pressx.spawner.FormationLoader;
import com.pressx.spawner.FormationLoader.FormationData;
import com.pressx.spawner.LevelLoader;
import com.pressx.spawner.LevelLoader.LevelData;

public class Levels {
	public AssetManager levelManager = new AssetManager();
	public AssetManager formationManager = new AssetManager();
	private HashMap<String, HashMap<String, String>> entries_levels = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, HashMap<String, String>> entries_formations = new HashMap<String, HashMap<String, String>>();
	
	private HashMap<String, String> currentPackage = new HashMap<String, String>();
	
	private LevelLoader levelLoader = new LevelLoader(new InternalFileHandleResolver());
	private FormationLoader formationLoader = new FormationLoader(new InternalFileHandleResolver());
	
	public Levels() { 
		levelManager.setLoader(LevelData.class,levelLoader); 
		formationManager.setLoader(FormationData.class,formationLoader);
		addPath_Level(
				"1-1", "data/leveldata/levels/level1.devicelevel"
				);
		
		addPath_Formation(
				"level1",
				"1_fuzzy", "data/leveldata/formations/1_fuzzy.spawnformation",
				"2_fuzzy_oppositesides", "data/leveldata/formations/2_fuzzy_oppositesides.spawnformation",
				"3_fuzzy_bodyguard_north", "data/leveldata/formations/3_fuzzie/bodyguard_north.spawnformation",
				"4_fuzzy_backandforth", "data/leveldata/formations/4_fuzzy_backandforth.spawnformation",
				"5_fuzzy_semicircle", "data/leveldata/formations/5_fuzzy_semiciricle.spawnformation",
				"8_fuzzie_onetwopunch_eastsouth", "data/leveldata/formation/8_fuzzie_onetwopunch_eastsouth.spawnformation"
				);
	}

	public void addPath_Level(String packageName, String... args){
		if(!entries_levels.containsKey(packageName))
			entries_levels.put(packageName, new HashMap<String, String>());
		for(int i = 0; i < args.length; i ++)
			entries_levels.get(packageName).put(args[i], args[++i]);
	}
	public void addPath_Formation(String packageName, String... args){
		if(!entries_formations.containsKey(packageName))
			entries_formations.put(packageName, new HashMap<String, String>());
		for(int i = 0; i < args.length; i ++)
			entries_formations.get(packageName).put(args[i], args[++i]);
	}

	public boolean loadLevelAssets(String packageName){
		if(entries_levels.containsKey(packageName)){
			currentPackage = entries_levels.get(packageName);
			for(String str : entries_levels.get(packageName).values())	
				levelManager.load(str, LevelData.class);
			levelManager.finishLoading();
			return true;
		}
		return false;
	}
	public boolean loadFormationAssets(String packageName){
		if(entries_formations.containsKey(packageName)){
			currentPackage = entries_formations.get(packageName);
			for(String str : entries_formations.get(packageName).values())	
				formationManager.load(str, LevelData.class);
			formationManager.finishLoading();
			return true;
		}
		return false;
	}
}
