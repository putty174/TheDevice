package com.pressx.editors.leveleditor;

import java.awt.Color;

import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;

public abstract class ToggleButton extends GuiButton{
    public static final Vector2 TOGGLEBUTTONSIZE = new Vector2(20,20);
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
    
    public void checkForShortcutKey(){
		if(Center.keyPressed(getShortcutKey()))
		    button_OnMouseClick();
    }
    
    public void update(){
		super.update();
		checkForShortcutKey();
    }
    
    public void draw(){
		super.draw();
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.boldFont();
		GraphicsDraw.text(getToggleButtonText(),new Vector2(position.x+size.x+5,position.y+size.y-5));
		this.drawToggleSelectedBox(checkIsToggledOn());
    }
}

class WaveModeToggler extends ToggleButton{
	GuiList_Wave parent;
	public WaveModeToggler(GuiList_Wave parent){
		this.parent = parent;
		setPosition(new Vector2(WaveSettingVariator.WSV_BASEOFFSET.x-10,WaveSettingVariator.WSV_BASEOFFSET.y+WaveSettingVariator.SIZE.y*3+15));
	}

	public Keys getShortcutKey(){return null;}	
	public void checkForShortcutKey(){}//Override
	
	public void button_OnMouseClick(){
		parent.toggleWaveRandomization();
	}
	
	public boolean checkIsToggledOn(){
		return GuiList_Wave.checkWaveIsRandomized();
	}
	
    public String getToggleButtonText(){
    	return "Randomize Order of Formations";
    }
    
    public void draw(){
    	super.draw();
    	if(!GuiList_Wave.checkWaveIsRandomized() && GuiList_Wave.ready){
			GraphicsDraw.smallBoldFont();
	    	GraphicsDraw.text("(will remove repeated formations)",new Vector2(position.x+size.x+5,position.y+size.y+12));
    	}
    }
}
