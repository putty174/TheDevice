package com.pressx.managers;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public final class Textures {
	public AssetManager a_manager = new AssetManager();
	private HashMap<String, HashMap<String, String>> entries = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, String> currentPackage = new HashMap<String, String>();
	private static HashMap<String, HashMap<String, AnimationManager>> animManagers = new HashMap<String, HashMap<String, AnimationManager>>();

	
	public Textures()
	{
		addPath(
				"Loading",
				"bg","data/art/game/grass.png",
				"gear", "data/art/loading/gear.png",
				"play", "data/art/main/buttons/play.png"
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
				"Tutorial",
				"tut_pg1", "data/art/tutorial/screen/tut1.png",
				"tut_pg2", "data/art/tutorial/screen/tut2.png",
				"tut_nav_left", "data/art/tutorial/buttons/nav_left.png",
				"tut_nav_right","data/art/tutorial/buttons/nav_right.png",
				"tut_nav_exit", "data/art/tutorial/buttons/nav_exit.png");
		
		addPath(
				"Game",
				"game_bg","data/art/game/grass.png",
				"exp", "data/art/game/objects/device/exp.png",
				"fuzz1", "data/art/game/monsters/fuzzies/1/fuzzies1.png",
				"fuzz2", "data/art/game/monsters/fuzzies/2/fuzzies2.png",
				"dust","data/art/game/monsters/fuzzies/2/dust.png",
				"fuzz3", "data/art/game/monsters/fuzzies/3/fuzzies3.png",
				"shock_wave","data/art/game/monsters/fuzzies/3/shock_wave.png",
				"plant1", "data/art/game/monsters/plants/1/plant_one.png",
				"plant2", "data/art/game/monsters/plants/2/plant_two.png",
				"plant3", "data/art/game/monsters/plants/3/plant_three.png",
				//"gas_cloud", "data/art/game/monsters/Plants/2/gas_cloud.png",
				"gasEject", "data/art/game/monsters/plants/2/gas_eject.png",
				"attackBlob", "data/art/game/monsters/plants/2/gas_atk.png",
				"device", "data/art/game/objects/device/device.png",
				"device_hit", "data/art/game/objects/device/device_hit.png",
				"device_spawn", "data/art/game/objects/device/device_spawn.png",
				//"device", "data/art/game/objects/device/device_full.png",
				"exp", "data/art/game/objects/device/exp.png",
				"hero", "data/art/game/objects/hero/hero.png",
				"mine","data/art/game/objects/mine/mine.png",
				"mine_drop","data/art/game/objects/mine/mine_pickup.png",
				"deathRing", "data/art/game/objects/nuke/death_ring.png",
				"vortex","data/art/game/objects/vortex/vortex.png",
				"vortex_drop","data/art/game/objects/vortex/vortex_pickup.png",
				"gear_drop","data/art/game/objects/gear/gear_pickup.png",
				"hammer_drop","data/art/game/objects/hammer/hammer_pickup.png",
				"wrench_drop","data/art/game/objects/wrench/wrench_pickup.png",
				"gear", "data/art/game/objects/gear/gear.png",
				"wrench", "data/art/game/objects/wrench/wrench.png",
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
				"ui_pause", "data/art/game/ui/uibase/pause.png",
				"ui_base","data/art/game/ui/uibase/ui_base.png",
				"ui_wavebar","data/art/game/ui/uibase/wavebar/top_bar.png",
				"ui", "data/art/game/ui/sidebar_full.png"
				);
		
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
		
		animManagers = AnimationManagerLoader.placeHolderShit();
		
		final String shop = "data/art/shop/";
		addPath(
				"Shop",
				"shop_background",shop+"shop_background.png",
				"shop_backbutton",shop+"backbutton.png",
				"itembackground",shop+"item_background.png",
				"itembackground_selected",shop+"item_background_selected.png",
				"itemdescbackground",shop+"itemdescbackground.png",
				"itembutton_buy",shop+"itembutton_buy.png",
				"itembutton_equip",shop+"itembutton_equip.png",
				"itembutton_unequip",shop+"itembutton_unequip.png",
				"itembutton_loadoutfull_big",shop+"itembutton_equip_gray_big.png",
				"itembutton_buy_big",shop+"itembutton_buy_big.png",
				"itembutton_equip_big",shop+"itembutton_equip_big.png",
				"itembutton_unequip_big",shop+"itembutton_unequip_big.png",
				"itembutton_upgrade_big",shop+"itembutton_upgrade_big.png",
				"exp", "data/art/game/objects/device/exp.png",
				"uparrow",shop+"uparrow.png",
				"ui_mine", "data/art/game/ui/uibase/buttons/mine/mine.png",
				"ui_vortex", "data/art/game/ui/uibase/buttons/vortex/vortex.png",
				"largeicon_mine",shop+"mine_large.png",
				"largeicon_vortex",shop+"vortex_large.png",
				"largeicon_control",shop+"control_large.png",
				"largeicon_gear",shop+"gear_large.png",
				"mine_drop","data/art/game/objects/mine/mine_pickup.png",
				"vortex_drop","data/art/game/objects/vortex/vortex_pickup.png",
				"gear_drop","data/art/game/objects/gear/gear_pickup.png",
				"controller_drop","data/art/game/objects/controller/controller_pickup.png",
				"hammer_drop","data/art/game/objects/hammer/hammer_pickup.png",
				"loadoutbar",shop+"loadoutbar.png",
				"shop1",shop+"workshop_1.png",
				"shop2",shop+"workshop_2.png"
				);
		final String lselect = "data/art/levelselect/";
		addPath(
				"LevelSelect",
				"levelselect_background",lselect+"background.png",
				"levelselect_button",lselect+"button.png"
				);
	}
	
	//Takes variable number of filepaths as strings to add into map for asset reference.
	public void addPath(String packageName, String... args)
	{
		if(!entries.containsKey(packageName))
			entries.put(packageName, new HashMap<String, String>());
		for(int i = 0; i < args.length; i ++)
			entries.get(packageName).put(args[i], args[++i]);
	}
	
	public void loadArtAssets(String packName)
	{
		currentPackage = entries.get(packName);
		for(String str : currentPackage.values()){
			//System.out.println(str);
			a_manager.load(str, Texture.class);
		}
		a_manager.finishLoading();
		while(!a_manager.update()){};
		if(packName.equals("Game")){
			for(AnimationManager m : animManagers.get("Loading").values()){
				m.setSprite(new Sprite(getArtAsset(m.texturesAccessor)));
			}
		}	
	}
	
	public static AnimationManager getAnimManager(String str){
		HashMap<String, AnimationManager> eh = animManagers.get("Loading");
		if(eh.containsKey(str)){
			return eh.get(str);
		}
		return null;
	}
	
	public Texture getArtAsset(String file)
	{
		Texture tex = null;
		if(currentPackage.containsKey(file))
			tex = a_manager.get(currentPackage.get(file), Texture.class);
		return tex;
	}
	
	public void unloadArtAssets(){
		a_manager.clear();
		for(HashMap<String, AnimationManager> m : animManagers.values()){
			for(AnimationManager em : m.values()){
				em.clearSprite();
			}
		}
	}
}
