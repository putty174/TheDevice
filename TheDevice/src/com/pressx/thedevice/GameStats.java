package com.pressx.thedevice;
import com.badlogic.gdx.Gdx;
import com.pressx.items.*;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.player.Player;

public class GameStats {
	private Sounds sound;
	private int monsterCount = 0;
	private int score = 0;
	private int xpCount = 0;
	private int xpMax=20;
	private int level=1;
	private int vortCount = 3;
	private int nukeCount = 3;
	private float nukeCD = 0;
	private int maxItemCount;
	private float boxHP;
	private float totalTimeElapsed;
	private Player player;
	private boolean pause = false;
	private int placeItem = 0;
	private boolean nukeState = false;
	
	public Item item0;
	public Item item1;
	
	public GameStats(Draw d, Textures t, Sounds s, Player p){
		player = p;
		sound = s;
		
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
		
		item0 = new Item_Vortex(d, s, t, this);
		item1 = new Item_Mine(d, s, t, this);
	}
	
	public boolean addAmmo(String itemname){//adds ammo for the specified item; the itemname is the "name" parameter for Item's constructor
		if(item0.name.equals(itemname))
			if(item0.changeAmmo(1))//So that if we have two of the same item, it will be able to add ammo to both
				return true;
		if(item1.name.equals(itemname))
			return item1.changeAmmo(1);
		return false;
	}
	
	public void addMonsterKill()
	{
		monsterCount++;
	}
	public int getMonsterCount()
	{
		return monsterCount;
	}
	
	public void addScore(int scoreAdd){
		score += scoreAdd;
	}
	
	public int getScore(){
		return score;
	}
	
	public int getXP(){
		return xpCount;
	}
	
	public void addXP(int XP){
		xpCount += XP;
		if (xpCount > xpMax)
		{
			xpCount -= xpMax;
			setXPMax((int)Math.floor(getXPMax() * 1.2));
			level++;
			sound.play("hero.level");
			player.levelUp = 3;
		}
	}
	
	public int getXPMax(){
		return xpMax;
	}
	
	public void setXPMax(int XP){
		xpMax = XP;
	}
	
	public float getXPpercent(){
		return (float)xpCount/xpMax;
	}
	
	public int getLevel(){
		return level;
	}
	
	public void levelUp(){
		level++;
	}
	
	public float getboxHP(){
		return boxHP;
	}
	
	public void setBoxHP(float f){
		boxHP = f;
	}
	
	public int minutesElapsed(){
		return (int)totalTimeElapsed / 60;
	}
	
	public int secondsElapsed(){
		return (int)totalTimeElapsed % 60;
	}
	
	public int timeElapsed(){
		return (int)totalTimeElapsed;
	}
	
	public void updateTimeElapsed(){
		float dt = Gdx.graphics.getDeltaTime();
		totalTimeElapsed += dt;
		nukeCD -= dt;
	}
	
	public boolean useNuke(){
		if (nukeCount > 0)
		{
			nukeCount--;
			nukeState = true;
			nukeCD = 3;
			return true;
		}
		return false;
	}
	
	public void nukeStateOff(){
		nukeState = false;
	}
	
	public boolean nukeState(){
		return nukeState;
	}
	
	public boolean nukeReady(){
		return nukeCount > 0 && nukeCD <= 0;
	}
	
	public int nukeCount(){
		return nukeCount;
	}
	
	public void pauseToggle(){
		pause = !pause;
	}
	
	public boolean pauseState(){
		return pause;
	}
	
	public int placeItem(){
		return placeItem;
	}
	
	public void pausePlayerTouchToMove(){
		player.pause_touch();
	}
}