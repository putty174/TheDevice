package com.pressx.editors.leveleditor;

import java.awt.Color;

import com.pressx.editors.formationeditor.TheDevice_FormationEditor;
import com.pressx.editors.shared.CenteredText;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;

public class OpenInFormationEditorButton extends GuiButton{
	static final Vector2 SIZE = new Vector2(GuiList.SIZE.x,30);
	static final Vector2 OFFSET = new Vector2(GuiList_Wave.GUILIST_WAVE_OFFSET.x,GuiList.EXTRABUTTONOFFSETY+90);
	
	CenteredText centeredText;
	public OpenInFormationEditorButton(){
		setPosition(OFFSET);
		setSize(SIZE);
		centeredText = new CenteredText("Open in Formation Editor",getCenter());
	}
	
	boolean shouldBeActive(){
		return GuiList_ImportedFormations.instance.selectedIndex != -1 || GuiList_Wave.instance.selectedIndex != -1;  
	}
	
	static final Color INACTIVECOLOR = new Color(.5f,.5f,.5f);
	static final Color BASECOLOR = new Color(1,.5f,0);
	public Color getBaseColor(){
		return shouldBeActive() ? BASECOLOR : INACTIVECOLOR;
	}
	
	public void button_OnMouseClick(){
		String name;
		if(GuiList_ImportedFormations.instance.selectedIndex != -1){
			name = GuiList_ImportedFormations.instance.getValues().get(GuiList_ImportedFormations.instance.selectedIndex).name;
		}else if(GuiList_Wave.instance.selectedIndex != -1){
			name = GuiList_Wave.instance.getValues().get(GuiList_Wave.instance.selectedIndex).name;
		}else{
			return;
		}
		TheDevice_FormationEditor.openFile(name);
	}
	
	public void draw(){
		super.draw();
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(centeredText);
	}
}
