package com.pressx.thedevice;
import com.badlogic.gdx.Gdx;
import com.pressx.managers.Sounds;
import com.pressx.objects.player.Player;

public final class GameStats {
	private static int monsterCount = 0;
	private static int score = 0;
	private static int xpCount = 0;
	private static int xpMax=20;
	private static int level = 1;
	private static int mineCount = 3;
	private static int vortCount = 3;
	private static int nukeCount = 3;
	private static float nukeCD = 0;
	private static int maxItemCount;
	private static float boxHP;
	private static float totalTimeElapsed;
	private static Player player;
	private static boolean pause = false;
	private static int placeItem = 0;
	private static boolean nukeState = false;
	
	public GameStats(Player p){
		player = p;
		
		monsterCount = 0;
		score = 0;
		xpCount = 0;
		xpMax = 20;
		level = 1;
		mineCount = 3;
		vortCount = 3;
		nukeCount = 3;
		nukeCD = 0;
		maxItemCount = 3;
		totalTimeElapsed = 0;
		pause = false;
		placeItem = 0;
		nukeState = false;
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
	
	public static int getMineCount(){
		return mineCount;
	}
	
	public static boolean useMine(){
		if(mineCount > 0 && placeItem == 0){
			mineCount--;
			placeItem = 1;
			return true;
		}
		return false;
	}
	
	public static boolean placeMine()
	{
		placeItem = 0;
		return true;
	}
	
	public static boolean mineReady()
	{
		return (mineCount > 0 && placeItem != 1);
	}
	
	public static boolean addMine(){
		if(mineCount + 1 > maxItemCount){
			return false;
		}
		mineCount ++;
		return true;
	}
	
	public static int getVortCount(){
		return vortCount;
	}
	
	public static boolean useVort(){
		if(vortCount > 0 && placeItem == 0){
			vortCount--;
			placeItem = 2;
			return true;
		}
		return false;
	}
	
	public static void placeVort(){
		placeItem = 0;
	}
	
	public static boolean vortexReady(){
		return (vortCount > 0 && placeItem != 2);
	}
	
	public static boolean addVortex() {
		if(vortCount + 1 > maxItemCount){
			return false;
		}
		vortCount ++;
		return true;
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
}