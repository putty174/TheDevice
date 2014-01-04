package com.pressx.editors.leveleditor;

import java.awt.Color;

import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;

public abstract class GuiButton extends GuiObject{//Standard fancy gui button
    boolean depressed;// ):
    
    public GuiButton(){
		position = new Vector2(50,50);
		size = new Vector2(50,50);
    }
    
    /////Overridable methods
    public Color getBaseColor(){
	return new Color(.5f,0,1);
    }
    
    final int DEFAULTBEVELWIDTH = 4;
    public int getBevelWidth(){
	return DEFAULTBEVELWIDTH;
    }
    
    public void button_OnMouseClick(){
    }

	public boolean shouldBeDepressed(){//):
		return checkMouseClicked();
	}
    
    /////Update
    public void update(){
	super.update();
	if(checkMouseClicked())
	    button_OnMouseClick();
	depressed = shouldBeDepressed();
    }
    
    /////Draw
    //final int DRAW_SIDELENGTH = 7;
    
    protected void drawToggleSelectedBox(){
	GraphicsDraw.fillRectangle(getCenter().add(2),size.div(3).add(0));
    }
    
    public void draw(){
	int bevelwidth = getBevelWidth();
	GraphicsDraw.setColor(getBaseColor());
	
	GraphicsDraw.fillRectangle(getCenter(),size);
	
	for(int i = 0; i < bevelwidth; i++){
	    GraphicsDraw.screen.draw3DRect((int)position.x+i,(int)position.y+i,(int)size.x-i*2,(int)size.y-i*2,!depressed);
	}
	GraphicsDraw.setColor(Color.BLACK);
	GraphicsDraw.rectangle(getCenter(),size);
    }
}
