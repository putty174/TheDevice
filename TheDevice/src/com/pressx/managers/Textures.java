package com.pressx.managers;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public final class Textures {
	public static AssetManager a_manager = new AssetManager();
	private static HashMap<String, HashMap<String, String>> entries = new HashMap<String, HashMap<String, String>>();
	private static HashMap<String, String> currentPackage = new HashMap<String, String>();
	
	public Textures()
	{
		addPath(
				"Loading",
				"bg","data/art/game/grass.png",
				"exp", "data/art/game/objects/device/exp.png",
				"play", "data/art/main/buttons/play.png"
				);
		
		addPath(
				"Main",
				"main_play","data/art/main/buttons/play.png",
				"main_bg","data/art/main/screen/TitleScreen.png",
				"main_help","data/art/main/buttons/help.png");
		
		addPath(
				"Tut",
				"tut_pg1", "data/art/tutorial/screen/tut1.png",
				"tut_pg2", "data/art/tutorial/screen/tut2.png",
				"tut_nav_left", "data/art/tutorial/buttons/nav_left.png",
				"tut_nav_right","data/art/tutorial/buttons/nav_right.png",
				"tut_nav_exit", "data/art/tutorial/buttons/nav_exit.png");
		
		addPath(
				"Game",
				"game_bg","data/art/game/grass.png",
				"fuzz1", "data/art/game/monsters/fuzzies/1/Fuzzies1.png",
				"fuzz2", "data/art/game/monsters/fuzzies/2/Fuzzies2.png",
				"dust","data/art/game/monsters/fuzzies/2/dust.png",
				"fuzz3", "data/art/game/monsters/fuzzies/3/Fuzzies3.png",
				"shock_wave","data/art/game/monsters/fuzzies/3/shock_wave.png",
				"plant1", "data/art/game/monsters/plants/1/plant_one.png",
				"plant2", "data/art/game/monsters/plants/2/plant_two.png",
				"gas_cloud", "data/art/game/monsters/plants/2/gas_cloud.png",
				"device", "data/art/game/objects/device/device.png",
				"device_hit", "data/art/game/objects/device/device_hit.png",
				"device_spawn", "data/art/game/objects/device/device_spawn.png",
				"exp", "data/art/game/objects/device/exp.png",
				"hero", "data/art/game/objects/hero/hero.png",
				"mine","data/art/game/objects/mine/mine.png",
				"mine_drop","data/art/game/objects/mine/mineDrop.png",
				"deathRing", "data/art/game/objects/nuke/DeathRing.png",
				"vortex","data/art/game/objects/vortex/vortex.png",
				"vortex_drop","data/art/game/objects/vortex/vortIcon.png",
				"indicate", "data/art/game/UI/extra/indication_sheet.png",
				"mlevel", "data/art/game/UI/extra/mLevel.png",
				"plevel", "data/art/game/UI/extra/pLevel.png",
				"ui_warn", "data/art/game/UI/extra/warn.png",
				"hp_bar", "data/art/game/UI/HP/HPBar.png",
				"hp_fill", "data/art/game/UI/HP/HPFill.png",
				"ui_mine", "data/art/game/UI/uibase/buttons/mine/mine.png",
				"ui_bomb", "data/art/game/UI/uibase/buttons/nuke/nuke.png",
				"ui_bombcount", "data/art/game/UI/uibase/buttons/nuke/nukecount.png",
				"ui_vortex", "data/art/game/UI/uibase/buttons/vortex/vortex.png",
				"ui_minecount", "data/art/game/UI/uibase/buttons/count.png",
				"ui_pause", "data/art/game/UI/uibase/Pause.png",
				"ui_base","data/art/game/UI/uibase/UIBase.png"
				);
		
		addPath(
				"Intro",
				"sc0","data/art/cutscene/intro/sketch_scene1-2.png",
				"sc1", "data/art/cutscene/intro/sketch_scene2.png",
				"sc2", "data/art/cutscene/intro/sketch_scene3-2.png",
				"sc3", "data/art/cutscene/intro/sketch_scene4-2.png",
				"sc4", "data/art/cutscene/intro/sketch_scene5.png");
		
		addPath(
				"Outro",
				"sc0", "data/art/cutscene/outro/sketch_scene6-2.png",
				"sc1", "data/art/cutscene/outro/sketch_scene7.png",
				"sc2", "data/art/cutscene/outro/sketch_scene8.png");
		
		addPath(
				"End",
				"end_bg", "data/art/end/gameover.png",
				"end_retry", "data/art/end/buttons/retry.png",
				"end_quit", "data/art/end/buttons/quitter.png");
	}
	
	//Takes variable number of filepaths as strings to add into map for asset reference.
	public static void addPath(String packageName, String... args)
	{
		if(!entries.containsKey(packageName))
			entries.put(packageName, new HashMap<String, String>());
		for(int i = 0; i < args.length; i ++)
			entries.get(packageName).put(args[i], args[++i]);
	}
	
	public static void loadArtAssets(String packName)
	{
		currentPackage = entries.get(packName);
		for(String str : entries.get(packName).values())		
			a_manager.load(str, Texture.class);
		a_manager.finishLoading();
	}
	
	public static Texture getArtAsset(String file)
	{
		Texture tex = null;
		if(currentPackage.containsKey(file))
			tex = a_manager.get(currentPackage.get(file), Texture.class);
		return tex;
	}
	
	public static void unloadArtAssets(){
		a_manager.clear();
	}
}