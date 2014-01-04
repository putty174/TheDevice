package com.pressx.editors.formationeditor;

import java.awt.Color;

import com.pressx.editors.shared.CenteredText;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;

public abstract class FileButton extends GuiButton{
    static final Color COLOR = new Color(.75f,.5f,1f);
    static final Vector2 SIZE = new Vector2(60,40);
    
    CenteredText centeredText;
    public FileButton(){
	setSize(SIZE);
	setPosition(getInitialPosition());
	GraphicsDraw.boldFont();
	centeredText = new CenteredText(getInitialText(),getCenter());
    }
    
    protected abstract String getInitialText();
    protected abstract Vector2 getInitialPosition();
    protected abstract Keys getShortcutKey();
    
    public Color getBaseColor(){
	return COLOR;
    }
    
    public abstract void fileButton_Activate();
    
    public boolean checkShouldBeActivated(){
	return Center.getKey(Keys.CONTROL) && Center.keyPressed(getShortcutKey());
    }
    
    public void update(){
	super.update();
	if(checkMouseClicked() || checkShouldBeActivated()){
	    fileButton_Activate();
	    Center.resetKey(getShortcutKey());
	}
    }
    
    public void draw(){
	super.draw();
	GraphicsDraw.setColor(Color.BLACK);
	GraphicsDraw.boldFont();
	GraphicsDraw.centerText(centeredText);
    }
}


class FileManager_SaveButton extends FileButton{
    static final Vector2 OFFSET = new Vector2(1,450);
    
    /////FileButton Overrides
    boolean checkEnabled(){return FileManager.fileName != null;}
    public String getInitialText(){
	return "Save";
    }
    public Keys getShortcutKey(){
	return Keys.S;
    }
    public boolean checkShouldBeActivated(){
	return super.checkShouldBeActivated() && !Center.getKey(Keys.SHIFT);
    }
    public Vector2 getInitialPosition(){
	return OFFSET;
    }
    int lastSaveAttemptCycle;
    public void fileButton_Activate(){
	if(lastSaveAttemptCycle > _G.cycle-10) return;
	lastSaveAttemptCycle = _G.cycle;
	if(!checkEnabled()){
	    FileManager.saveFormation();
	    return;
	}
	draw();
	FileManager.saveFormation_Default();
    }
    static Color DISABLEDCOLOR = new Color(.3f,.3f,.3f);
    public Color getBaseColor(){
	return checkEnabled() ? super.getBaseColor() : DISABLEDCOLOR;
    }
}

class FileManager_SaveAsButton extends FileButton{
    static final Vector2 OFFSET = new Vector2(61,450);
    public FileManager_SaveAsButton(){
    }
    
    public boolean shouldBeDepressed(){
	return FileManager.saving;
    }
    
    public Vector2 getInitialPosition(){
	return OFFSET;
    }
    
    public String getInitialText(){
	return "Save As";
    }
    
    public Keys getShortcutKey(){
	return Keys.S;
    }
    
    public boolean checkShouldBeActivated(){
	return (super.checkShouldBeActivated() && Center.getKey(Keys.SHIFT));// || FileManager.fileName == null && ;
    }
    
    int lastSaveAttemptCycle;
    public void fileButton_Activate(){
		if(lastSaveAttemptCycle > _G.cycle-10) return;
		draw();
		lastSaveAttemptCycle = _G.cycle;
		FileManager.saveFormation();
    }
}

class FileManager_LoadButton extends FileButton{
    static final Vector2 OFFSET = new Vector2(1,490);
    /////FileButton Overrides
    public Keys getShortcutKeys(){
	return Keys.S;
    }
    public Vector2 getInitialPosition(){
	return OFFSET;
    }
    public String getInitialText(){
	return "Open";
    }
    public Keys getShortcutKey(){
	return Keys.O;
    }
    
    int lastLoadAttemptCycle;
    public void fileButton_Activate(){
	if(lastLoadAttemptCycle > _G.cycle-10) return;
	draw();
	lastLoadAttemptCycle = _G.cycle;
	FileManager.loadFormation();
    }
    
    public boolean shouldBeDepressed(){
	return FileManager.loading;
    }
}

class FileManager_NewButton extends FileButton{
    static final Vector2 OFFSET = new Vector2(61,490);
    /////FileButton Overrides
    public Keys getShortcutKeys(){
    	return Keys.S;
    }
    public Vector2 getInitialPosition(){
    	return OFFSET;
    }
    public String getInitialText(){
    	return "New";
    }
    public Keys getShortcutKey(){
    	return Keys.N;
    }
    
    int lastNewAttemptCycle;
    public void fileButton_Activate(){
		if(lastNewAttemptCycle > _G.cycle-10) return;
		draw();
		lastNewAttemptCycle = _G.cycle;
		FileManager.newFormation();
		SpawnMap.currentMap.showMessage("New map created.");
    }
}
