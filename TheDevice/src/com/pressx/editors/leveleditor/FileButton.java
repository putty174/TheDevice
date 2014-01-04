package com.pressx.editors.leveleditor;

import java.awt.Color;

import com.pressx.editors.shared.CenteredText;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;

public abstract class FileButton extends GuiButton{
	public static final Vector2 BASEOFFSET_UPPER = new Vector2(20,_G.LEVELEDITOR_HEIGHT-170);//new Vector2(GuiList_ImportedFormations.GUILIST_IMPORTEDFORMATIONS_OFFSET.x,_G.LEVELEDITOR_HEIGHT-170);
	public static final Vector2 BASEOFFSET_LOWER = new Vector2(BASEOFFSET_UPPER.x,BASEOFFSET_UPPER.y+65);
    public static final Vector2 SIZE = new Vector2(GuiList.SIZE.x/2,30);
    static final Color COLOR = new Color(.75f,.5f,1f);
    
    CenteredText centeredText;
    public FileButton(){
		setSize(getBaseSize());
		setPosition(getInitialPosition());
		GraphicsDraw.boldFont();
		centeredText = new CenteredText(getInitialText(),getCenter());
    }
    
    protected abstract String getInitialText();
    protected abstract Vector2 getInitialPosition();
    protected abstract Keys getShortcutKey();
    protected Vector2 getBaseSize(){
    	return SIZE;
    }
    
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
		    Center.resetKey(Keys.CONTROL);
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
    static final Vector2 OFFSET = BASEOFFSET_LOWER;
    
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
		    FileManager.saveLevel();
		    return;
		}
		draw();
		FileManager.saveLevel_Default();
    }
    static Color DISABLEDCOLOR = new Color(.3f,.3f,.3f);
    public Color getBaseColor(){
    	return checkEnabled() ? super.getBaseColor() : DISABLEDCOLOR;
    }
    
    public void draw(){
    	super.draw();
    	GraphicsDraw.setColor(Color.BLACK);
    	//For bottom-let corner
    	Vector2 pos = new Vector2(position.x+size.x*2+20,position.y-20);
    	GraphicsDraw.line(pos,new Vector2(pos.x-size.x*2-100,pos.y));
    	GraphicsDraw.line(pos,new Vector2(pos.x,pos.y+size.y*2+100));
    	/*//For bottom-right corner
    	Vector2 pos = new Vector2(position.x-20,position.y-20);
    	GraphicsDraw.line(pos,new Vector2(pos.x+size.x*2+100,pos.y));
    	GraphicsDraw.line(pos,new Vector2(pos.x,pos.y+size.y*2+100));*/
    }
}

class FileManager_SaveAsButton extends FileButton{
    static final Vector2 OFFSET = new Vector2(BASEOFFSET_LOWER.x+SIZE.x,BASEOFFSET_LOWER.y);
    
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
		FileManager.saveLevel();
    }
}

class FileManager_LoadButton extends FileButton{
    static final Vector2 OFFSET = new Vector2(BASEOFFSET_LOWER.x,BASEOFFSET_LOWER.y+SIZE.y);
    /////FileButton Overrides
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
	FileManager.loadLevel();
    }
    
    public boolean shouldBeDepressed(){
	return FileManager.loading;
    }
}

class FileManager_NewButton extends FileButton{
    static final Vector2 OFFSET = new Vector2(BASEOFFSET_LOWER.x+SIZE.x,BASEOFFSET_LOWER.y+SIZE.y);
    /////FileButton Overrides
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
		FileManager.newLevel();
    }
}
