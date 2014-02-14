package com.pressx.thedevice;
import com.badlogic.gdx.Gdx;
import com.pressx.items.*;
import com.pressx.managers.Sounds;
import com.pressx.objects.player.Player;

public final class GameStats {
	private static int monsterCount = 0;
	private static int score = 0;
	private static int xpCount = 0;
	private static int xpMax=20;
	private static int level = 1;
	private static int nukeCount = 3;
	private static float nukeCD = 0;
	private static int maxItemCount;
	private static float boxHP;
	private static float totalTimeElapsed;
	private static Player player;
	private static boolean pause = false;
	private static int placeItem = 0;
	private static boolean nukeState = false;
	
	public static Item item0,item1;
	
	public GameStats(Player p){
		player = p;
		
		monsterCount = 0;
		score = 0;
		xpCount = 0;
		xpMax = 20;
		level = 1;
		nukeCount = 3;
		nukeCD = 0;
		maxItemCount = 3;
		totalTimeElapsed = 0;
		pause = false;
		placeItem = 0;
		nukeState = false;
		
		item0 = new Item_Vortex();
		item1 = new Item_Mine();
	}
	
	public static boolean addAmmo(String itemname){//adds ammo for the specified item; the itemname is the "name" parameter for Item's constructor
		if(item0.name.equals(itemname))
			if(item0.changeAmmo(1))//So that if we have two of the same item, it will be able to add ammo to both
				return true;
		if(item1.name.equals(itemname))
			return item1.changeAmmo(1);
		return false;
	}
	
	public static void addMonsterKill()
	{
		monsterCount++;
	}
	public static int getMonsterCount()
	{
		return monsterCount;
	}
	
	public static void addScore(int scoreAdd){
		score += scoreAdd;
	}
	
	public static int getScore(){
		return score;
	}
	
	public int getXP(){
		return xpCount;
	}
	
	public static void addXP(int XP){
		xpCount += XP;
		if (xpCount > xpMax)
		{
			xpCount -= xpMax;
			setXPMax((int)Math.floor(getXPMax() * 1.2));
			level++;
			Sounds.play("hero.level");
			player.levelUp = 3;
		}
	}
	
	public static int getXPMax(){
		return xpMax;
	}
	
	public static void setXPMax(int XP){
		xpMax = XP;
	}
	
	public float getXPpercent(){
		return (float)xpCount/xpMax;
	}
	
	public static int getLevel(){
		return level;
	}
	
	public void levelUp(){
		level++;
	}
	
	public float getboxHP(){
		return boxHP;
	}
	
	public static void setBoxHP(float f){
		boxHP = f;
	}
	
	public int minutesElapsed(){
		return (int)totalTimeElapsed / 60;
	}
	
	public int secondsElapsed(){
		return (int)totalTimeElapsed % 60;
	}
	
	public static int timeElapsed(){
		return (int)totalTimeElapsed;
	}
	
	public static void updateTimeElapsed(){
		float dt = Gdx.graphics.getDeltaTime();
		totalTimeElapsed += dt;
		nukeCD -= dt;
	}
	
	public static boolean useNuke(){
		if (nukeCount > 0)
		{
			nukeCount--;
			nukeState = true;
			nukeCD = 3;
			return true;
		}
		return false;
	}
	
	public static void nukeStateOff(){
		nukeState = false;
	}
	
	public static boolean nukeState(){
		return nukeState;
	}
	
	public static boolean nukeReady(){
		return nukeCount > 0 && nukeCD <= 0;
	}
	
	public static int nukeCount(){
		return nukeCount;
	}
	
	public static void pauseToggle(){
		pause = !pause;
	}
	
	public static boolean pauseState(){
		return pause;
	}
	
	public static int placeItem(){
		return placeItem;
	}
	
	public static void pausePlayerTouchToMove(){
		player.pause_touch();
	}
}