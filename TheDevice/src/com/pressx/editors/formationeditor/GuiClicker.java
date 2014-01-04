//It turns out I never needed this

package com.pressx.editors.formationeditor;

import java.awt.Color;

import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;

public abstract class GuiClicker extends GuiObject{//Standard fancy gui button
    public GuiClicker(){
	position = new Vector2(50,50);
	size = new Vector2(50,50);
    }
    
    /////Overridable methods
    public Color getBaseColor(){
	return new Color(.5f,0,1);
    }
    public Color getSideColor(){
	Color c = getBaseColor();
	Color c2 = new Color(c.getRed()/2,c.getGreen()/2,c.getBlue()/2,c.getAlpha()/2);
	return c2;
    }
    
    final int DEFAULTBEVELWIDTH = 4;
    public int getBevelWidth(){
	return DEFAULTBEVELWIDTH;
    }
    
    public void button_OnMouseClick(){
    }
    
    /////Update
    boolean clicked;
    public void update(){
	super.update();
	if(checkMouseOver()){
	    if(Center.leftMouseDown()){
		clicked = true;
	    }else if(clicked){
		button_OnMouseClick();
		clicked = false;
	    }
	}else{
	    clicked = false;
	}
    }
    
    /////Draw
    final int DRAW_SIDELENGTH = 7;
    
    public void draw(){
	int bevelsizex = (int)size.x/4;
	int bevelsizey = (int)size.y/4;
	int offset = clicked ? DRAW_SIDELENGTH : 0;
	GraphicsDraw.setColor(getSideColor());
	if(!clicked){
	    for(int i = 0; i < DRAW_SIDELENGTH; i++)
		GraphicsDraw.screen.fillRoundRect((int)position.x+i,(int)position.y+i,(int)size.x,(int)size.y,bevelsizex,bevelsizey);
	}else{
	    for(int i = 0; i < 4; i++)
		GraphicsDraw.screen.fillRoundRect((int)position.x-i+offset,(int)position.y-i+offset,(int)size.x+i*2,(int)size.y+i*2,bevelsizex,bevelsizey);
	}
	GraphicsDraw.setColor(getBaseColor());
	GraphicsDraw.screen.fillRoundRect((int)position.x+offset,(int)position.y+offset,(int)size.x,(int)size.y,bevelsizex,bevelsizey);
    }
}

/////////////////////////GuiClickerGroup
class GuiClickerGroup{
    static final float positionoffsetx = 150;
    
    Vector2 position;
    public GuiClickerGroup(Vector2 pos){
	position = pos;
    }
    
    GuiClicker[] clickers;
    public void setClickers(GuiClicker[] clickers){
	this.clickers = clickers;
	for(int i = 0; i < clickers.length; i++){
	    clickers[i].setPosition(new Vector2(position.x+positionoffsetx*i,position.y));
	}
    }
}

/////////////////////////PlaceClicker
class PlaceClicker extends GuiClicker{
}
