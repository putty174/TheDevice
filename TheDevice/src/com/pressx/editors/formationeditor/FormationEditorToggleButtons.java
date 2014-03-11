package com.pressx.editors.formationeditor;

import com.pressx.editors.shared.Vector2;

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
