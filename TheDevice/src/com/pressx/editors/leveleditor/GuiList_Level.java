package com.pressx.editors.leveleditor;

import java.awt.Color;
import java.util.ArrayList;

import com.pressx.editors.shared.CenteredText;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared.LevelWave;
import com.pressx.editors.shared._G;

public class GuiList_Level extends GuiList<LevelWave>{
	public static final Vector2 GUILIST_LEVEL_OFFSET = BASEOFFSET;

	public static GuiList_Level instance;	
	static ArrayList<LevelWave> values = new ArrayList<LevelWave>();
	
	public static void newLevel(){
		values.clear();
		instance.deselect();
	}
	
	public static void addWave(LevelWave wave){
		values.add(wave);
		if(values.size() == 1)
			GuiList_ImportedFormations.instance.selectFirstOpenIndexFrom(0);
	}
	
	public void newWave(){
		values.add(new LevelWave());
		selectIndex(values.size()-1);
		if(values.size() == 1)
			GuiList_ImportedFormations.instance.selectFirstOpenIndexFrom(0);
	}
	
	public void removeSelectedWave(){
		if(selectedIndex != -1){
			if(values.size() != 0){
				values.remove(selectedIndex);
				selectIndex(selectedIndex-1);
			}
			if(values.size() == 0)
				deselect();
		}
	}
	
	public void copySelectedWave(){
		if(selectedIndex == -1) return;
		values.add(selectedIndex+1,new LevelWave(values.get(selectedIndex)));
		selectIndex(selectedIndex+1);
	}
	
	public void moveSelectedWave(boolean up){
		if(up){
			values.add(selectedIndex-1,values.get(selectedIndex));
			values.remove(selectedIndex+1);
			selectIndex(selectedIndex-1);
		}else{
			values.add(selectedIndex+2,values.get(selectedIndex));
			values.remove(selectedIndex);
			selectIndex(selectedIndex+1);
		}
	}
	
	public GuiList_Level(){
		super(GUILIST_LEVEL_OFFSET);
		instance = this;
		new NewWaveButton(this).register();
		new RemoveWaveButton(this).register();
		new CopyWaveButton(this).register();
		new MoveWaveButton(this,true).register();
		new MoveWaveButton(this,false).register();
	}
	
	public void selectIndex(int index){
		super.selectIndex(index);
		if(index != -1 && index < values.size())
			GuiList_Wave.useWave("Wave "+(index+1)+"'s possible formations",values.get(index));
	}
	
	public void deselect(){
		super.deselect();
		GuiList_Wave.clearWave();
	}

	public ArrayList<LevelWave> getValues(){
		return values;
	}
	
	public String getTextFromValue(LevelWave value){
		return "Wave "+(values.indexOf(value)+1)+"    ("+(value.formations.isEmpty() ? "empty" : value.formations.size())+')';
	}
	
	public String getTitle(){
		return values.size() > 20 ? "A bit too many waves, don't you think?" : "Level Waves";//FileManager.fileName == null ? "(Untitled Level)" : FileManager.fileName;
	}
	
	protected Color getSelectedUnitDrawColor(int realindex,int relativeindex,LevelWave val){
		return Color.YELLOW;
	}
	protected void drawSingleUnit(int realindex,int relativeindex,LevelWave val){
		super.drawSingleUnit(realindex,relativeindex,val);
		if(realindex != selectedIndex) return;
		Vector2 pos = new Vector2(getCenter().x-SIZE.x/2+BARSIZE.x,getCenter().y-SIZE.y/2+BARSIZE.y*(.5f+relativeindex));
		for(float f = 0; f <= 1; f += 1f/(GuiList_Wave.TOPPOINT.x-pos.x)){
			float a = ((float)Math.sin((float)-_G.cycle/5+f*5)+15)/16;
			GraphicsDraw.setColor(new Color(a,a,a*.8f));
			GraphicsDraw.line(Vector2.lerp(pos,GuiList_Wave.TOPPOINT,f),Vector2.lerp(pos,GuiList_Wave.BOTTOMPOINT,f));
		}
		/*
		GraphicsDraw.setColor(Color.BLUE);
		GraphicsDraw.line(pos,GuiList_Wave.TOPPOINT);
		GraphicsDraw.line(pos,GuiList_Wave.BOTTOMPOINT);
		GraphicsDraw.line(GuiList_Wave.TOPPOINT,GuiList_Wave.BOTTOMPOINT);*/
	}
}

class NewWaveButton extends GuiButton{
	static final Vector2 SIZE = new Vector2(GuiList.SIZE.x,25);
	static final Vector2 OFFSET = new Vector2(GuiList_Level.GUILIST_LEVEL_OFFSET.x,GuiList.EXTRABUTTONOFFSETY);
	static final String TEXT = "New Wave [N]";
	CenteredText text;
	GuiList_Level parent;
	public NewWaveButton(GuiList_Level parent){
		this.parent = parent;
		setPosition(OFFSET);
		setSize(SIZE);
		GraphicsDraw.boldFont();
		text = new CenteredText(TEXT,getCenter());
	}
 
	public Color getBaseColor(){
		return _G.COLOR_LEVELEDITORSTANDARDBUTTON;
	}
	
	public void button_OnMouseClick(){
		parent.newWave();
	}
	
	public boolean checkMouseClicked(){
		return super.checkMouseClicked() || (!Center.getKey(Keys.CONTROL) && Center.keyPressed(Keys.N));
	}
	
	public void draw(){
		super.draw();
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(text);
	}
}

class RemoveWaveButton extends GuiButton{
	static final Vector2 SIZE = NewWaveButton.SIZE;
	static final Vector2 OFFSET = new Vector2(GuiList_Level.GUILIST_LEVEL_OFFSET.x,GuiList.EXTRABUTTONOFFSETY+SIZE.y);
	static final String TEXT = "Delete Wave [CTRL+DEL]";
	CenteredText text;
	GuiList_Level parent;
	public RemoveWaveButton(GuiList_Level parent){
		this.parent = parent;
		setPosition(OFFSET);
		setSize(SIZE);
		GraphicsDraw.boldFont();
		text = new CenteredText(TEXT,getCenter());
	}
	
	boolean shouldBeActive(){
		return parent.selectedIndex != -1;
	}

	static final Color INACTIVECOLOR = new Color(.5f,.5f,.5f);
	public Color getBaseColor(){
		return shouldBeActive() ? _G.COLOR_LEVELEDITORSTANDARDBUTTON : INACTIVECOLOR;
	}
	
	public boolean shouldBeDepressed(){//):
		return shouldBeActive() && super.shouldBeDepressed();
	}

	public boolean checkMouseClicked(){
		return super.checkMouseClicked() || Center.getKey(Keys.CONTROL) && Center.keyPressed(Keys.DELETE);
	}
	
	public void button_OnMouseClick(){
		parent.removeSelectedWave();
	}
	
	public void draw(){
		super.draw();
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(text);
	}
}
class CopyWaveButton extends GuiButton{
	static final Vector2 SIZE = NewWaveButton.SIZE;
	static final Vector2 OFFSET = new Vector2(GuiList_Level.GUILIST_LEVEL_OFFSET.x,GuiList.EXTRABUTTONOFFSETY+SIZE.y*2);
	static final String TEXT = "Duplicate Wave [CTRL+D]";
	CenteredText text;
	GuiList_Level parent;
	public CopyWaveButton(GuiList_Level parent){
		this.parent = parent;
		setPosition(OFFSET);
		setSize(SIZE);
		GraphicsDraw.boldFont();
		text = new CenteredText(TEXT,getCenter());
	}
	
	boolean shouldBeActive(){
		return parent.selectedIndex != -1;
	}

	static final Color INACTIVECOLOR = new Color(.5f,.5f,.5f);
	public Color getBaseColor(){
		return shouldBeActive() ? _G.COLOR_LEVELEDITORSTANDARDBUTTON : INACTIVECOLOR;
	}
	
	public boolean shouldBeDepressed(){//):
		return shouldBeActive() && super.shouldBeDepressed();
	}

	public boolean checkMouseClicked(){
		return super.checkMouseClicked() || Center.getKey(Keys.CONTROL) && Center.keyPressed(Keys.D);
	}
	
	public void button_OnMouseClick(){
		parent.copySelectedWave();
	}
	
	public void draw(){
		super.draw();
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(text);
	}
}

class MoveWaveButton extends GuiButton{
	static final Vector2 SIZE = NewWaveButton.SIZE;
	static final Vector2 OFFSET_UP = new Vector2(GuiList_Level.GUILIST_LEVEL_OFFSET.x,GuiList.EXTRABUTTONOFFSETY+SIZE.y*3);
	static final Vector2 OFFSET_DOWN = new Vector2(GuiList_Level.GUILIST_LEVEL_OFFSET.x,GuiList.EXTRABUTTONOFFSETY+SIZE.y*4);
	static final String TEXT_UP = "Raise Wave [CTRL+UP]";
	static final String TEXT_DOWN = "Lower Wave [CTRL+DOWN]";
	CenteredText text;
	GuiList_Level parent;
	boolean up;
	public MoveWaveButton(GuiList_Level parent,boolean up){
		this.parent = parent;
		this.up = up;
		setPosition(up ? OFFSET_UP : OFFSET_DOWN);
		setSize(SIZE);
		GraphicsDraw.boldFont();
		text = new CenteredText(up ? TEXT_UP : TEXT_DOWN,getCenter());
	}
	
	boolean shouldBeActive(){
		return parent.selectedIndex != -1 && (up ? parent.selectedIndex != 0 : parent.selectedIndex != parent.getValues().size()-1);
	}

	static final Color INACTIVECOLOR = new Color(.5f,.5f,.5f);
	public Color getBaseColor(){
		return shouldBeActive() ? _G.COLOR_LEVELEDITORSTANDARDBUTTON : INACTIVECOLOR;
	}
	
	public boolean shouldBeDepressed(){//):
		return shouldBeActive() && super.shouldBeDepressed();
	}

	public boolean checkMouseClicked(){
		return shouldBeActive() && (super.checkMouseClicked() || Center.getKey(Keys.CONTROL) && Center.keyPressed(up ? Keys.UP : Keys.DOWN));
	}
	
	public void button_OnMouseClick(){
		parent.moveSelectedWave(up);
	}
	
	public void draw(){
		super.draw();
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(text);
	}
}
