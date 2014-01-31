package com.pressx.editors.formationeditor;

import java.awt.Color;

import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;

public abstract class ToggleButton extends GuiButton{
    public static final Vector2 TOGGLEBUTTONSIZE = new Vector2(30,30);
    public ToggleButton(){
	super();
	setSize(TOGGLEBUTTONSIZE);
    }
    
    static final Color COLOR_OFF = new Color(.9f,.9f,.9f);
    static final Color COLOR_ON = new Color(.3f,1f,.3f);
    public Color getBaseColor(){
	return checkIsToggledOn() ? COLOR_ON : COLOR_OFF;
    }
    
    public abstract String getToggleButtonText();
    public abstract boolean checkIsToggledOn();
    public abstract Keys getShortcutKey();
    
    public void update(){
	super.update();
	if(Center.keyPressed(getShortcutKey()))
	    button_OnMouseClick();
    }
    
    public void draw(){
	super.draw();
	GraphicsDraw.setColor(Color.BLACK);
	GraphicsDraw.defaultFont();
	GraphicsDraw.text(getToggleButtonText(),new Vector2(position.x+size.x+3,position.y+size.y-9));
	if(this.checkIsToggledOn())
	    this.drawToggleSelectedBox();
    }
}
