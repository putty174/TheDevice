package com.pressx.gadgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.screens.game.Room;
import com.pressx.thedevice.GameStats;

public abstract class Item extends ShopItem{
	public boolean activated;//if the button has been pressed
	
	protected Sprite spr_button,spr_count;//button sprite and number sprite
	protected int ammo;//ammo currently available (maximum ammo found using getMaxAmmo())
	protected Room room;//set with setRoom()
	protected Draw draw;//set with setDraw()
	protected boolean buttonup;//if the button should be drawn as up or pressed
	
	public Item(Textures texture,Sounds sounds,String name,String icon,String largeicon){
		super(texture,sounds,name,icon,largeicon);
	}
	
	public void setGameStats(GameStats stats){
		this.stats = stats;
	}
	public void setDraw(Draw draw){
		this.draw = draw;
	}
	public void setRoom(Room room){
		this.room = room;
	}
	
	/////Implement these
	public abstract String getButtonSpriteName();//should return the button texture name
	public abstract String getCountSpriteName();//should return the count texture name
	public abstract int getMaxAmmo();//should return the maximum charges the item should hold (seems to always be 3)
	public abstract int getMinLevelForDrop();//remove this? This tells the game what "level" the player should be before ammo for this item drops. This was used by the old system, not sure if it has been changed yet. 
	public abstract float getDropChance();//returns the chance (0f to 1f) that the item will be dropped by an enemy on death
	public abstract AnimatedObject dropAmmo(float posx,float posy);//should return the ammo pickup object
	public abstract void fieldPressed(float mouseX,float mouseY);//called when the player presses on the map while this item is active (the button is down)
	
	/////Ammo, UI updating, and interface for UI class or whatever
	public void updateButton(){
		buttonup = ammo > 0 && !activated;
		if(buttonup)
			spr_button.setRegion(0, 0, 128, 110);
		else
			spr_button.setRegion(128, 0, 128, 110);
		spr_count.setRegion(39*(getMaxAmmo()-ammo),0,39,38);
	}
	
	public boolean checkAmmoNotFull(){
		return ammo < getMaxAmmo();
	}
	public boolean changeAmmo(int inc){
		ammo += inc;
		if(ammo > getMaxAmmo())
			ammo = getMaxAmmo();
		else if(ammo < 0)
			ammo = 0;
		else{
			updateButton();
			return true;
		}
		return false;
	}
	
	public boolean checkShouldDropItem(){
		return stats.getLevel() > getMinLevelForDrop() && Math.random() < getDropChance();
	}
	
	//activate/deactivate
	void activate(){
		activated = true;
	}
	public void deactivate(){
		activated = false;
	}
	
	/////
	public void itemButtonPressed(){
		if(activated) deactivate();
		else activate();
		updateButton();
	}
	
	public String getName() {
		return name;
	}
	
	/////update
	public void update(){
		if(spr_button == null) return;
		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();
		if(!stats.pauseState() && Gdx.input.justTouched()){
			if(ammo != 0 && activated &&  x < Gdx.graphics.getWidth()*.8f){//check if it's not int he UI space not in the UI space (right 20% of the screen)
				fieldPressed(x,y);
				stats.pausePlayerTouchToMove();
				deactivate();
				changeAmmo(-1);
			}else if(spr_button.getBoundingRectangle().contains(x,y)){//check if it's on the button
				itemButtonPressed();
			}else{
				activated = false;
			}
		}
	}
	
	/////draw
	public void drawButton(Draw draw,int index){//0 for the first item, 1 for the second item (can be changed if we want more carryable items)
		if(spr_button == null){
			spr_button = getspr("ui");
			spr_count = getspr(getCountSpriteName());
			updateButton();
		}
		draw.draw(Draw.TYPES.BUTTON,spr_button,.823f,.33f+.18f*index,.17f,.17f);
		draw.draw(Draw.TYPES.EXTRAS,spr_count,.933f,(buttonup ? .365f : .345f)+.18f*index,.04f,.06f);
	}
}
