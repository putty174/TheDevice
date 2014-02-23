package com.pressx.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.screens.game.Room;
import com.pressx.thedevice.GameStats;

public abstract class Item extends ShopItem{
	protected boolean activated;//if the button has been pressed
	
	protected Sprite spr_button,spr_count;
	protected int ammo;
	protected Room room;
	protected Draw draw;
	protected boolean buttonup;
	
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
	
	/////Overridables
	public abstract String getButtonSpriteName();//should return the button texture name
	public abstract String getCountSpriteName();//should return the count texture name
	public abstract int getMaxAmmo();
	public abstract int getMinLevelForDrop();
	public abstract float getDropChance();
	public abstract AnimatedObject dropAmmo(float posx,float posy);
	
	/////Ammo, UI updating, and interface for UI class or whatever
	public void updateButton(){
		buttonup = ammo > 0 && !activated;
		if(buttonup)
			spr_button.setRegion(0,0,124,95);
		else
			spr_button.setRegion(124,0,124,95);
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
	void deactivate(){
		activated = false;
	}
	
	protected abstract void onActivated();
	protected abstract void onDeactivated();
	public abstract void fieldPressed(float mouseX,float mouseY);
	
	/////"events"
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
			if(ammo != 0 && activated &&  x < Gdx.graphics.getWidth()*.8f){//not in the UI space (right 20% of the screen)
				fieldPressed(x,y);
				stats.pausePlayerTouchToMove();
				deactivate();
				changeAmmo(-1);
			}else if(spr_button.getBoundingRectangle().contains(x,y)){
				itemButtonPressed();
			}else{
				activated = false;
			}
		}
	}
	
	/////draw
<<<<<<< HEAD
	public void drawButton(Draw draw,int index){//0 for the first item, 1 for the second item (can be changed if we want more carryable items)
		if(spr_button == null){
			spr_button = getspr(getButtonSpriteName());
			spr_count = getspr(getCountSpriteName());
			updateButton();
		}
		draw.draw(Draw.TYPES.BUTTON,spr_button,.83f,.35f+.22f*index,.17f,.165f);
		draw.draw(Draw.TYPES.EXTRAS,spr_count,.935f,(buttonup ? .40f : .38f)+.22f*index,.04f,.06f);
=======
	public void drawButton(Draw d, int index){//0 for the first item, 1 for the second item (can be changed if we want more carryable items)
		d.draw(Draw.TYPES.BUTTON,spr_button,.83f,.35f+.22f*index,.17f,.165f);
		d.draw(Draw.TYPES.EXTRAS,spr_count,.935f,(buttonup ? .40f : .38f)+.22f*index,.04f,.06f);
>>>>>>> 619ef0f56ca11613393556499301f564a86045df
	}
}
