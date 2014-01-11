package com.pressx.editors.leveleditor;

import java.awt.Color;
import java.util.ArrayList;

import com.pressx.editors.shared.CenteredText;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.SingleFormation;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared.LevelWave;
import com.pressx.editors.shared._G;

public class GuiList_Wave extends GuiList<SingleFormation>{
	public static final Vector2 GUILIST_WAVE_OFFSET = BASEOFFSET.add(ADDITIONALOFFSET);
	public static final Vector2 TOPPOINT = new Vector2(GUILIST_WAVE_OFFSET.x-20,GUILIST_WAVE_OFFSET.y+20);
	public static final Vector2 BOTTOMPOINT = new Vector2(GUILIST_WAVE_OFFSET.x-20,GUILIST_WAVE_OFFSET.y+SIZE.y-20);

	public static boolean ready;
	static LevelWave currentWave;
	static String title;
	public static ArrayList<SingleFormation> values;
	static GuiList_Wave instance;
	
	static NumFormationsUsedVariator numFormationsVariator;
	static WaveDelayVariator waveDelayVariator;
	static FormationAngleVariator formationAngleVariator;
	
	public static void useWave(String name,LevelWave w){
		ready = true;
		title = name;
		currentWave = w;
		values = currentWave.formations;
		numFormationsVariator.setValue(currentWave.numFormationsUsed);
		waveDelayVariator.setValue((int)(currentWave.delayBetweenFormations/WaveDelayVariator.VALUEMULTIPLIER));
		instance.selectIndex(instance.selectedIndex);
	}
	public static void setWaveNumFormationsUsed(byte b){
		currentWave.numFormationsUsed = b;
	}
	public static void setWaveDelayBetweenFormations(float f){
		currentWave.delayBetweenFormations = f;
	}
	public static void clearWave(){
		values = new ArrayList<SingleFormation>();
		ready = false;
		title = "(Wave will appear here)";
	}
	public static void addFormationFromImportedFormation(ImportedFormation form){
		for(SingleFormation f : values)
			if(f.name == form.name)
				return;
		SingleFormation newformation = new SingleFormation(form);
		values.add(newformation);
		//instance.selectValue(newformation);
	}
	
	public static void selectValueFromImportedFormation(ImportedFormation form){
		for(int i = 0; i < values.size(); i++){
			if(form.name.equals(values.get(i).name)){
				instance.selectIndex(i);
				return;
			}
		}
	}
	
	public void removeSelectedFormation(){
		if(selectedIndex == -1 || values.size() == 0) return;
		values.remove(selectedIndex);
		selectIndex(selectedIndex == 0 ? 0 : selectedIndex-1);
		if(values.size() == 0)
			selectedIndex = -1;
	}
	
	public GuiList_Wave(){
		super(GUILIST_WAVE_OFFSET);
		clearWave();
		instance = this;
		
		numFormationsVariator = new NumFormationsUsedVariator();
		numFormationsVariator.register();
		waveDelayVariator = new WaveDelayVariator();
		waveDelayVariator.register();
		
		formationAngleVariator = new FormationAngleVariator();
		formationAngleVariator.register();
		
		new OpenInFormationEditorButton().register();
	}
	
	public void selectIndex(int i){
		super.selectIndex(i);
		GuiList_ImportedFormations.instance.deselect();
		if(selectedIndex != -1 && values.size() != 0){
			formationAngleVariator.setData(values.get(selectedIndex));
			MiniSpawnmap.load(values.get(i).name);
		}
	}
	public void setFormationSpawnAngle(byte val){
		values.get(selectedIndex).spawnAngle = val;
	}

	public ArrayList<SingleFormation> getValues(){
		return values;
	}
	
	public String getTextFromValue(SingleFormation value){
		return value.name;
	}
	
	public String getTitle(){
		return title;
	}
    
    public void update(){
    	super.update();
		if(Center.getKey(Keys.CONTROL) || selectedIndex == -1) return;
		
		if(Center.keyPressed(Keys.UP) && selectedIndex > 0){
			selectIndex(selectedIndex-1);
		}else if(Center.keyPressed(Keys.DOWN) && selectedIndex < values.size()){
			selectIndex(selectedIndex+1);
		}else if(Center.keyPressed(Keys.RIGHT)){
			GuiList_ImportedFormations.instance.selectFirstOpenIndexFrom(0);
		}
    	if(Center.keyPressed(Keys.DELETE))
    		removeSelectedFormation();
    }
    
	public void draw(){
		super.draw();
		if(selectedIndex != -1){
			MiniSpawnmap.draw();
		}
	}
}

/*
class FormationDelayVariator extends GuiVariator{
	public FormationDelayVariator(){
	}
}
*/
