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

/*class SpawnMap_ToggleSnap extends ToggleButton{
    public SpawnMap_ToggleSnap(){
	super();
	setPosition(new Vector2(1,420));
	setSize(ToggleButton.TOGGLEBUTTONSIZE);
    }
    
    /////Overrides
    public void button_OnMouseClick(){
	SpawnMap.showRingNumbers = !SpawnMap.showRingNumbers;
    }
    
    public String getToggleButtonText(){
	return "Snap to Grid [G]";
    }
    public boolean checkIsToggledOn(){
	return SpawnMap.currentMap.showRingNumbers;
    }
    public Keys getShortcutKey(){
    	return Keys.G;
    }
}*/

class SpawnMap_ToggleShowColors extends ToggleButton{
    public SpawnMap_ToggleShowColors(){
	super();
	setPosition(new Vector2(1,300));
    }
    
    /////Overrides
    public void button_OnMouseClick(){
	SpawnMap.showEnemyColors = !SpawnMap.showEnemyColors;
    }
    
    public String getToggleButtonText(){
	return "Show colors [T]";
    }
    public boolean checkIsToggledOn(){
	return SpawnMap.showEnemyColors;
    }
    public Keys getShortcutKey(){
	return Keys.T;
    }
}
class SpawnMap_ToggleOnlySelected extends ToggleButton{
    public SpawnMap_ToggleOnlySelected(){
	super();
	setPosition(new Vector2(1,330));
    }
    
    /////Overrides
    public void button_OnMouseClick(){
	SpawnMap.onlyShowSelected = !SpawnMap.onlyShowSelected;
    }
    
    public String getToggleButtonText(){
	return "Hide deselected [G]";
    }
    public boolean checkIsToggledOn(){
	return SpawnMap.currentMap.onlyShowSelected;
    }
    public Keys getShortcutKey(){
	return Keys.G;
    }
}
class SpawnMap_ToggleShowMouseDistance extends ToggleButton{
    public SpawnMap_ToggleShowMouseDistance(){
	super();
	setPosition(new Vector2(1,360));
	setSize(ToggleButton.TOGGLEBUTTONSIZE);
    }
    
    /////Overrides
    public void button_OnMouseClick(){
	SpawnMap.showMouseDistance = !SpawnMap.showMouseDistance;
    }
    
    public String getToggleButtonText(){
	return "Show mouse dist [B]";
    }
    public boolean checkIsToggledOn(){
	return SpawnMap.showMouseDistance;
    }
    public Keys getShortcutKey(){
	return Keys.B;
    }
}
class SpawnMap_ToggleShowNumbers extends ToggleButton{
    public SpawnMap_ToggleShowNumbers(){
	super();
	setPosition(new Vector2(1,390));
	setSize(ToggleButton.TOGGLEBUTTONSIZE);
    }
    
    /////Overrides
    public void button_OnMouseClick(){
	SpawnMap.showRingNumbers = !SpawnMap.showRingNumbers;
    }
    
    public String getToggleButtonText(){
	return "Show ring numbers [H]";
    }
    public boolean checkIsToggledOn(){
	return SpawnMap.currentMap.showRingNumbers;
    }
    public Keys getShortcutKey(){
	return Keys.H;
    }
}
