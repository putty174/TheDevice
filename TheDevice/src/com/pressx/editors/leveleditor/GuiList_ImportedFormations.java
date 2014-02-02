package com.pressx.editors.leveleditor;

import java.awt.Color;
import java.util.ArrayList;

import com.pressx.editors.shared.CenteredText;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.SingleFormation;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;

public class GuiList_ImportedFormations extends GuiList<ImportedFormation>{//For imported formations
	public static final Vector2 GUILIST_IMPORTEDFORMATIONS_OFFSET = BASEOFFSET.add(ADDITIONALOFFSET.mul(2));
	/////STATICS
	static ArrayList<ImportedFormation> values = new ArrayList<ImportedFormation>();
	static GuiList_ImportedFormations instance;
	
	static int getNumberFromName(String name){
		int ans = -1;
		for(int i = 0; i < name.length(); i++){
			byte c = (byte)name.charAt(i);
			if(c < 48 || c > 57) break;
			if(ans == -1)
				ans = c;
			else
				ans = ans*10+c;
		}
		return ans;
	}
	public static void tryAddImportedFormation(ImportedFormation form){
		if(checkValueAlreadyImported(form.name)!= null);
		int num = getNumberFromName(form.name);
		if(num == -1)
			values.add(form);
		else{//sort according to Khai's naming system (names start with the number of enemies)
			for(int i = 0; i < values.size(); i++){
				int othernum = getNumberFromName(values.get(i).name);
				if(othernum == -1 || num < othernum){
					values.add(i,form);
					break;
				}
			}
		}
	}
	static ImportedFormation checkValueAlreadyImported(String name){
		for(ImportedFormation value : values)
			if(value.name.equals(name))
				return value;
		return null;
	}
	static boolean valueShouldBeGrayed(ImportedFormation val){
		if(!GuiList_Wave.ready) return true;
		if(!GuiList_Wave.checkWaveIsRandomized()) return false;
		for(SingleFormation f : GuiList_Wave.values)
			if(f.name.equals(val.name))
				return true;
		return false;
	}
	
	static final Color UNITDEFAULTDRAWCOLOR_RANDOMIZED = new Color(.85f,1,.95f);	
	static final Color UNITDEFAULTDRAWCOLOR_UNRANDOMIZED = new Color(1,.95f,.85f);
    protected Color getUnitDefaultDrawColor(){
		return GuiList_Wave.checkWaveIsRandomized() ? UNITDEFAULTDRAWCOLOR_RANDOMIZED : UNITDEFAULTDRAWCOLOR_UNRANDOMIZED;//super.getUnitDefaultDrawColor();
    }
	
	/////NON-STATICS
	public void addSelectedFormationToWave(){
		if(selectedIndex == -1 || !GuiList_Wave.ready) return;
		GuiList_Wave.addFormationFromImportedFormation(values.get(selectedIndex));
		if(GuiList_Wave.checkWaveIsRandomized())
			selectFirstOpenIndexFrom(selectedIndex+1);
	}
	
	public void selectFirstOpenIndexFrom(int index){
		int ans = -1;
		int siz = values.size();
		for(int i = index; i < siz+index; i++){
			if(!valueShouldBeGrayed(values.get(i%siz))){
				ans = i%siz;
				break;
			}
		}
		if(ans == -1)
			GuiList_Wave.instance.selectIndex(0);
		selectIndex(ans);
	}
	
	public GuiList_ImportedFormations(){
		super(GUILIST_IMPORTEDFORMATIONS_OFFSET);
		new ToggleFormationToWaveButton(this).register();
		new FileManager_ReimportFormationsButton(this).register();
		instance = this;
	}

	public ArrayList<ImportedFormation> getValues(){
		return values;
	}
	
	public String getTextFromValue(ImportedFormation value){
		return value.name;
	}
	
	public String getTitle(){
		return "Imported Formations";
	}
	
	public void selectIndex(int i){
		if(i == -1)
			deselect();
		else if(i >= 0 && i < values.size() && valueShouldBeGrayed(values.get(i))){
			GuiList_Wave.selectValueFromImportedFormation(values.get(i));
			selectedIndex = -1;
		}else{
			super.selectIndex(i);
			GuiList_Wave.instance.deselect();
			MiniSpawnmap.load(values.get(i).name);
		}
	}

	static final int DOUBLECLICK_MAXCYCLE = 20;
	int doubleclick_index,doubleclick_firstcycle;
    protected void onBarClicked(int index){
    	super.onBarClicked(index);
    	if(!Center.leftMouseClicked()) return;
    	if(index == doubleclick_index && _G.cycle-doubleclick_firstcycle < DOUBLECLICK_MAXCYCLE)
    		addSelectedFormationToWave();
    	else{
    		doubleclick_index = index;
    		doubleclick_firstcycle = _G.cycle;
    	}
    }
	protected Color getUnitDrawColor(int realindex,int relativeindex,ImportedFormation val){
		return valueShouldBeGrayed(val) ? Color.GRAY : super.getUnitDrawColor(realindex,relativeindex,val);
	}

	public void update(){
		super.update();
		if(Center.getKey(Keys.CONTROL)) return;
		if(Center.keyPressed(Keys.ENTER))
			addSelectedFormationToWave();
		if(selectedIndex == -1) return;
		if(Center.keyPressed(Keys.UP) && selectedIndex > 0){
			selectIndex(selectedIndex-1);
		}else if(Center.keyPressed(Keys.DOWN) && selectedIndex < values.size()){
			selectIndex(selectedIndex+1);
		}else if(Center.keyPressed(Keys.LEFT)){
			GuiList_Wave.instance.selectIndex(0);
		}
	}
	
	public void draw(){
		super.draw();
		if(selectedIndex != -1){
			MiniSpawnmap.draw();
		}
	}
}

class FileManager_ReimportFormationsButton extends GuiButton{
	static final Vector2 SIZE = new Vector2(GuiList.SIZE.x,25);
	static final Vector2 OFFSET = new Vector2(GuiList_ImportedFormations.GUILIST_IMPORTEDFORMATIONS_OFFSET.x,GuiList.EXTRABUTTONOFFSETY);
	static final String TEXT = "Reimport Formations [CTRL+R]";
	CenteredText text;
	GuiList_ImportedFormations parent;
	public FileManager_ReimportFormationsButton(GuiList_ImportedFormations parent){
		this.parent = parent;
		setPosition(OFFSET);
		setSize(SIZE);
		GraphicsDraw.boldFont();
		text = new CenteredText(TEXT,getCenter());
	}
	
	boolean shouldBeActive(){
		return parent.selectedIndex != -1;
	}

	public Color getBaseColor(){
		return _G.COLOR_LEVELEDITORSTANDARDBUTTON;
	}
	
	public void button_OnMouseClick(){
		FileManager.importAllFormations();
	}
	
	public boolean checkMouseClicked(){
		return super.checkMouseClicked() || Center.getKey(Keys.CONTROL) && Center.getKey(Keys.R);
	}
	
	public void draw(){
		super.draw();
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(text);
	}
}

class ToggleFormationToWaveButton extends GuiButton{
	static final float PADDING = 10;
	static final Vector2 SIZE = new Vector2(GuiList.ADDITIONALOFFSET.x-GuiList.ARROWSQUARESIZE-GuiList.SIZE.x-PADDING*2,GuiList.SIZE.y/2);
	static final Vector2 OFFSET = new Vector2(GuiList_ImportedFormations.GUILIST_IMPORTEDFORMATIONS_OFFSET.x-SIZE.x-GuiList.ARROWSQUARESIZE-PADDING,GuiList_ImportedFormations.GUILIST_IMPORTEDFORMATIONS_OFFSET.y+GuiList.SIZE.y/2-SIZE.y/2);
	//CenteredText[] text;
	GuiList_ImportedFormations parent;
	CenteredText centeredtext;
	public ToggleFormationToWaveButton(GuiList_ImportedFormations parent){
		this.parent = parent;
		setPosition(OFFSET);
		setSize(SIZE);
	}
	
	boolean shouldBeActive(){
		return (GuiList_Wave.instance.selectedIndex != -1 || parent.selectedIndex != -1) && GuiList_Wave.ready;
	}
	boolean addNotRemove(){
		return parent.selectedIndex != -1;
	}

	public Color getBaseColor(){
		return _G.COLOR_LEVELEDITORSTANDARDBUTTON;
	}
	
	public boolean shouldBeDepressed(){
		return shouldBeActive() && mouseclicking;
	}

	boolean mouseclicking;
    public boolean checkMouseClicked(){
    	return checkMouseOver() && mouseclicking && !Center.leftMouseDown();//checkMouseOver() && Center.leftMouseClicked();
    }
    
	public void button_OnMouseClick(){
		if(addNotRemove())
			parent.addSelectedFormationToWave();
		else
			GuiList_Wave.instance.removeSelectedFormation();
	}
	
	public void update(){
		if(!shouldBeActive()) return;
		super.update();
		if(checkMouseOver() && Center.leftMouseDown())
			mouseclicking = true;
		else
			mouseclicking = false;
	}
	
	static final int NUMARROWS = 9;
	public void draw(){
		if(!shouldBeActive()) return;
		super.draw();
		float arrowangle = addNotRemove() ? -_G.PI/2 : _G.PI/2;
		for(float f = 1f/NUMARROWS/2; f < 1; f += 1f/NUMARROWS){
			GraphicsDraw.fillArrow(new Vector2(position.x+size.x/2,position.y+size.y*f),size.x/8,arrowangle);
		}
		GraphicsDraw.smallBoldFont();
		GraphicsDraw.centerText(addNotRemove() ? "[enter]" : "[del]",new Vector2(position.x+size.x/2,position.y-10));
	}
}
