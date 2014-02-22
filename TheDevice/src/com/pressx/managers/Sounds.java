package com.pressx.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
	public AssetManager s_manager = new AssetManager();
	private HashMap<String, HashMap<String, String>> entries = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, String> currentPackage = new HashMap<String, String>();
	public Music music;
	private boolean playBGM;
	public Sound sound;
	
	
	public enum PACKS { MAIN, TUTORIAL, SHOP, GAME, INTRO, OUTRO, END };
	
	public Sounds()
	{
		playBGM = false;
		addPath(
				"Main", 
				"bgm", "data/sounds/main/title.mp3",
				"buttonl", "data/sounds/buttons/buttonl.mp3",
				"buttonh", "data/sounds/buttons/buttonh.mp3");
		
		addPath(
				"Tutorial",
				"bgm", "data/sounds/main/title.mp3",
				"buttonl", "data/sounds/buttons/buttonl.mp3",
				"buttonh", "data/sounds/buttons/buttonh.mp3");
		
		addPath(
				"Game",
				"bgm", "data/sounds/game/game.mp3",
				"device.hit", "data/sounds/game/device/hit.mp3",
				"device.spawn", "data/sounds/game/device/spawn.mp3",
				"fuzzie1.bite", "data/sounds/game/monster/fuzzie/1/m1bite.mp3",
				"fuzzie1.death", "data/sounds/game/monster/fuzzie/1/m1death.mp3",
				"fuzzie2.bite", "data/sounds/game/monster/fuzzie/2/m2bite.mp3",
				"fuzzie2.charge", "data/sounds/game/monster/fuzzie/2/m2charge.mp3",
				"fuzzie2.collide", "data/sounds/game/monster/fuzzie/2/m2collide.mp3",
				"fuzzie2.damage", "data/sounds/game/monster/fuzzie/2/m2damage.mp3",
				"fuzzie2.death", "data/sounds/game/monster/fuzzie/2/m2death.mp3",
				"fuzzie3.attack", "data/sounds/game/monster/fuzzie/3/m3attack.mp3",
				"fuzzie3.damage", "data/sounds/game/monster/fuzzie/3/m3damage.mp3",
				"fuzzie3.death", "data/sounds/game/monster/fuzzie/3/m3death.mp3",
				"fuzzie3.roar", "data/sounds/game/monster/fuzzie/3/m3roar.mp3",
				"fuzzie3.slam", "data/sounds/game/monster/fuzzie/3/m3slam.mp3",
				"plant1.death", "data/sounds/game/monster/plant/1/p1death.mp3",
				"plant2.death", "data/sounds/game/monster/plant/2/p2death.mp3",
				"plant3.death", "data/sounds/game/monster/plant/3/p3death.mp3",
				"monster.level", "data/sounds/game/monster/level.mp3",
				"hero.level", "data/sounds/game/pickup/level.mp3",
				"hero.xp", "data/sounds/game/pickup/xp.mp3",
				"hero.mine", "data/sounds/game/skills/mine.mp3",
				"hero.nuke", "data/sounds/game/skills/nuke.mp3",
				"hero.vortex", "data/sounds/game/skills/vortex.mp3",
				"hero.smack0", "data/sounds/game/smack/smack0.mp3",
				"hero.smack1", "data/sounds/game/smack/smack1.mp3",
				"hero.smack2", "data/sounds/game/smack/smack2.mp3",
				"hero.smack3", "data/sounds/game/smack/smack3.mp3",
				"hero.smack4", "data/sounds/game/smack/smack4.mp3",
				"hero.smack5", "data/sounds/game/smack/smack5.mp3",
				"hero.smack6", "data/sounds/game/smack/smack6.mp3",
				"hero.smack7", "data/sounds/game/smack/smack7.mp3");
		
		addPath(
				"Intro",
				"bgm", "data/sounds/menu/title.mp3");
		
		addPath(
				"Outro",
				"bgm", "data/sounds/menu/title.mp3");
		
		addPath(
				"End",
				"laugh", "data/sounds/gameover/laugh.mp3",
				"buttonl", "data/sounds/buttons/buttonl.mp3",
				"buttonh", "data/sounds/buttons/buttonh.mp3");
	}
	
	public void addPath(String packageName, String... args)
	{
		if(!entries.containsKey(packageName))
			entries.put(packageName, new HashMap<String, String>());
		for(int i = 0; i < args.length; i ++)
			entries.get(packageName).put(args[i], args[++i]);
	}
	
	public void loadSoundAssets(PACKS packs)
	{
		switch (packs)
		{
			case MAIN:
				currentPackage = entries.get("Main");
				break;
			case TUTORIAL:
				currentPackage = entries.get("Tutorial");
				break;
			case GAME:
				currentPackage = entries.get("Game");
				break;
			case INTRO:
				currentPackage = entries.get("Intro");
				break;
			case OUTRO:
				currentPackage = entries.get("Outro");
				break;
			case END:
				currentPackage = entries.get("End");
				break;
			case SHOP:
				break;
		default:
			break;
		}
		if(currentPackage.containsKey("bgm"))
			music = Gdx.audio.newMusic(Gdx.files.internal(currentPackage.get("bgm")));
		for(String str : currentPackage.values())
			s_manager.load(str, Sound.class);
		s_manager.finishLoading();
	}
	
	public boolean loadSoundAssets(String packageName)
	{
		if(entries.containsKey(packageName))
		{
			currentPackage = entries.get(packageName);
			if(currentPackage.containsKey("bgm"))
				music = Gdx.audio.newMusic(Gdx.files.internal(currentPackage.get("bgm")));
			for(String str : entries.get(packageName).values())	
				s_manager.load(str, Sound.class);
			s_manager.finishLoading();
			return true;
		}
		return false;
	}
	
	public boolean playBGM()
	{
		if(!playBGM)
		{
			music.setLooping(true);
			music.play();
			playBGM = true;
		}
		return playBGM;
	}
	
	public boolean stopBGM()
	{
		if(playBGM)
		{
			music.stop();
			playBGM = false;
		}
		return playBGM;
	}
	
	public boolean stopSound()
	{
		if(sound != null)
			sound.stop();
		return true;
	}
	
	public boolean play(String file)
	{
		if(currentPackage.containsKey(file))
		{
			sound = s_manager.get(currentPackage.get(file), Sound.class);
			sound.play();
			return true;
		}
		return false;
	}
	
	public boolean unloadSoundAssets()
	{
		if(music != null && !playBGM)
			music.dispose();
		if(sound != null)
			sound.dispose();
		s_manager.clear();
		return currentPackage.isEmpty();
	}
}