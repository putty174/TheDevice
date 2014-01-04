package com.pressx.editors.leveleditor;
import java.awt.Color;

import com.pressx.editors.shared.CenteredText;
import com.pressx.editors.shared.SpawnType;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;


public abstract class GuiVariator extends GuiObject{
    public int value = getInitialValue();
    
    GuiVariator_Arrow arrow_Down,arrow_Up;
    
    public GuiVariator(){
		arrow_Down = new GuiVariator_Arrow(this,false);
		arrow_Up = new GuiVariator_Arrow(this,true);
		setSize(getInitialSize());
    }
    
    /////Get/Set
    void updateArrows(){
		arrow_Up.setPosition(new Vector2(position.x-size.y/2,position.y));
		arrow_Down.setPosition(new Vector2(position.x-size.y/2,position.y+size.y/2));
    }
    public void setSize(Vector2 siz){
		super.setSize(siz);
		siz = new Vector2(siz.y/2,siz.y/2);
		arrow_Up.setSize(siz);
		arrow_Down.setSize(siz);
		updateArrows();
    }
    public void setPosition(Vector2 pos){
		super.setPosition(pos);
		updateArrows();
    }
    
    /////Register/Remove
    public void register(){
		super.register();
		arrow_Up.register();
		arrow_Down.register();
    }
    public void remove(){
		super.remove();
		arrow_Up.remove();
		arrow_Down.remove();
    }
    
    /////Overridables
    public abstract Color getBaseColor();
    public Color getArrowDisabledColor(){
    	return Color.GRAY;
    }
    
    public abstract int getMinValue();
    public abstract int getMaxValue();
    
    public Vector2 getInitialSize(){
    	return new Vector2(100,100);
    }
    
    public void applyArrowFont(){
    	GraphicsDraw.biggishFont();
    }
    
    public void setValue(int val){
		value = val;
		if(value > getMaxValue())
		    onAboveMaxValue();
		else if(value < getMinValue())
		    onBelowMinValue();
    }
    
    public int getInitialValue(){
    	return getMinValue();
    }
    
    public void onBelowMinValue(){
    	value = getMaxValue();
    }
    public void onAboveMaxValue(){
    	value = getMinValue();
    }
    
    public int getIncrementAmount(){
    	return 1;
    }
    
    public void increment(float mult){
    	setValue(value+(int)(getIncrementAmount()*mult));
    }
    public void decrement(float mult){
    	increment(-mult);
    }
    
    /////Arrow timings
    public int getHoldDelay(){
    	return 10;
    }
    public float getHoldIncrementThreshold(){
    	return 30;
    }
    public float getHoldIncrementerStart(){
    	return 1;
    }
    public float getHoldIncrementerIncrease(){
    	return .005f;
    }
    
    /////Arrow settings
    public String getArrowText(boolean up){
	return null;
    }
    public Keys getArrowShortcut(boolean up){
	return Keys.NONE;
    }
    
    /////Update
    public void update(){
	super.update();
    }
    
    /////Draw
    public void draw(){
		Color c =  getBaseColor();
		GraphicsDraw.setColor(c);
		GraphicsDraw.fillRectangle(position.add(size.div(2)),size);
		GraphicsDraw.setColor(new Color(1,1,1,.7f));////new Color((c.getRed()+255)/2,(c.getGreen()+255)/2,(c.getBlue()+255)/2));
		int bevelwidth = (int)(size.x < size.y ? size.x : size.y)/3;
		GraphicsDraw.screen.fillRoundRect((int)position.x,(int)position.y,(int)size.x,(int)size.y,bevelwidth,bevelwidth);
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.rectangle(position.add(size.div(2)),size);
    }
}

class GuiVariator_Arrow extends GuiButton{
    final Color BASECOLOR = Color.WHITE;
    
    public boolean up;
    public GuiVariator parent;
    Keys shortcutKey;
    public GuiVariator_Arrow(GuiVariator parent,boolean up){
	this.parent = parent;
	this.up = up;
	shortcutKey = parent.getArrowShortcut(up);
    }
    
    /////Overrides
    public boolean shouldBeDepressed(){
	return checkheld();
    }
    
    float holdincrement;
    float holdincrementer;
    int holding = 0;
    
    public Color getBaseColor(){
	return parent.getBaseColor();//checkDisabled() ? parent.getArrowDisabledColor() : parent.getBaseColor();
    }
    
    /////Methods
    boolean checkDisabled(){
	return up ? parent.value == parent.getMaxValue() : parent.value == parent.getMinValue();
    }
    
    void use(){
	if(up)
	    parent.increment(1);
	else
	    parent.decrement(1);
    }
    
    /*boolean checkpressed(){//Found no need for this
	return Center.leftMouseClicked() || Center.keyPressed(shortcutKey);
    }*/
    boolean checkheld(){
	return this.checkMouseHeld() || (Center.getKey(shortcutKey) && !Center.getKey(Keys.CONTROL));
    }
    
    /////Update
    public void update(){
	super.update();
	if(checkheld()){
	    if(holding == parent.getHoldDelay()){
		holdincrement += holdincrementer;
		if(holdincrement > parent.getHoldIncrementThreshold()){
		    holdincrement -= parent.getHoldIncrementThreshold();
		    use();
		}
		holdincrementer += parent.getHoldIncrementerIncrease();
	    }else{
		if(holding == 0)
		    use();
		holding++;
	    }
	}else{
	    holding = 0;
	    holdincrement = 0;
	    holdincrementer = parent.getHoldIncrementerStart();
	}
    }

    public void draw(){
	super.draw();
	GraphicsDraw.setColor(Color.BLACK);
	GraphicsDraw.rectangle(getCenter(),size);
	parent.applyArrowFont();
	String s = parent.getArrowText(up);
	if(s != null){
	    GraphicsDraw.smallBoldFont();
	    GraphicsDraw.centerText("["+s+']',new Vector2(position.x+size.x-10,position.y+size.y-10));
	}
	GraphicsDraw.fillArrow(getCenter(),Math.min(size.x,size.y)/4,up ? 0 : _G.PI);
    }
}
