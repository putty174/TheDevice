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
				"play", "data/art/main/buttons/play.png",
				"game_bg","data/art/game/grass.png",
				"fuzz1", "data/art/game/monsters/fuzzies/1/fuzzies1.png",
				"fuzz2", "data/art/game/monsters/fuzzies/2/fuzzies2.png",
				"dust","data/art/game/monsters/fuzzies/2/dust.png",
				"fuzz3", "data/art/game/monsters/fuzzies/3/fuzzies3.png",
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
				"indicate", "data/art/game/ui/extra/indication_sheet.png",
				"mlevel", "data/art/game/ui/extra/mLevel.png",
				"plevel", "data/art/game/ui/extra/pLevel.png",
				"ui_warn", "data/art/game/ui/extra/warn.png",
				"hp_bar", "data/art/game/ui/hp/hpbar.png",
				"hp_fill", "data/art/game/ui/hp/hpfill.png",
				"ui_mine", "data/art/game/ui/uibase/buttons/mine/mine.png",
				"ui_bomb", "data/art/game/ui/uibase/buttons/nuke/nuke.png",
				"ui_bombcount", "data/art/game/ui/uibase/buttons/nuke/nukecount.png",
				"ui_vortex", "data/art/game/ui/uibase/buttons/vortex/vortex.png",
				"ui_minecount", "data/art/game/ui/uibase/buttons/count.png",
				"ui_pause", "data/art/game/ui/uibase/Pause.png",
				"ui_base","data/art/game/ui/uibase/UIBase.png"
				);
		
		addPath(
				"Main",
				"main_play","data/art/main/buttons/play.png",
				"main_bg","data/art/main/screen/titlescreen.png",
				"main_help","data/art/main/buttons/help.png",
				"exp", "data/art/game/objects/device/exp.png",
				"shopbutton", "data/art/shop/shop_button.png"
				);
		
		addPath(
				"Tut",
				"tut_pg1", "data/art/tutorial/screen/tut1.png",
				"tut_pg2", "data/art/tutorial/screen/tut2.png",
				"tut_nav_left", "data/art/tutorial/buttons/nav_left.png",
				"tut_nav_right","data/art/tutorial/buttons/nav_right.png",
				"tut_nav_exit", "data/art/tutorial/buttons/nav_exit.png");
		
//		addPath(
//				"Game",
//				
//				);
		
		addPath(
				"Intro",
				"sc0","data/art/cutscene/Intro/sketch_scene1-2.png",
				"sc1", "data/art/cutscene/Intro/sketch_scene2.png",
				"sc2", "data/art/cutscene/Intro/sketch_scene3-2.png",
				"sc3", "data/art/cutscene/Intro/sketch_scene4-2.png",
				"sc4", "data/art/cutscene/Intro/sketch_scene5.png");
		
		addPath(
				"Outro",
				"sc0", "data/art/cutscene/Outro/sketch_scene6-2.png",
				"sc1", "data/art/cutscene/Outro/sketch_scene7.png",
				"sc2", "data/art/cutscene/Outro/sketch_scene8.png");
		
		addPath(
				"End",
				"end_bg", "data/art/end/gameover.png",
				"end_retry", "data/art/end/buttons/retry.png",
				"end_quit", "data/art/end/buttons/quitter.png");
		
		final String path = "data/art/shop/";
		addPath(
				"Shop",
				"shop_background",path+"shop_background.png",
				"shop_backbutton",path+"backbutton.png",
				"itembackground",path+"item_background.png",
				"itembackground_selected",path+"item_background_selected.png",
				"itemdescbackground",path+"itemdescbackground.png",
				"itembutton_buy",path+"itembutton_buy.png",
				"itembutton_equip",path+"itembutton_equip.png",
				"itembutton_unequip",path+"itembutton_unequip.png",
				"itembutton_buy_big",path+"itembutton_buy_big.png",
				"itembutton_equip_big",path+"itembutton_equip_big.png",
				"itembutton_unequip_big",path+"itembutton_unequip_big.png",
				"exp", "data/art/game/objects/device/exp.png",
				"uparrow",path+"uparrow.png",
				"item0",path+"item0.png",
				"item1",path+"item1.png",
				"item2",path+"item2.png",
				"item3",path+"item3.png",
				"item4",path+"item4.png",
				"item5",path+"item5.png");
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