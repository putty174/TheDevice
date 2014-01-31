package com.pressx.editors.leveleditor;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pressx.editors.shared.CenteredText;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.LevelWave;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;

public class WaveRenameBox{
	public WaveRenameBox(){
	}
	
	public String activate(String defaulttext){
		JPanel panel = new JPanel(new GridLayout(0,1));
		JTextField field = new JTextField(defaulttext);
		panel.add(field);
		//panel.requestFocusInWindow(field);
        int result = JOptionPane.showConfirmDialog(null, panel, "Rename \""+defaulttext+'"', JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
        	return field.getText();
        }else{
        	return defaulttext;
        }
	}
}

class RenameWaveButton extends GuiButton{
	static final Vector2 SIZE = NewWaveButton.SIZE;
	static final Vector2 OFFSET = new Vector2(GuiList_Level.GUILIST_LEVEL_EXTRABUTTON_OFFSET_X,GuiList_Level.GUILIST_LEVEL_EXTRABUTTON_OFFSET_Y+SIZE.y*5);
	static final String TEXT = "Rename Wave [R]";
	CenteredText text;
	GuiList_Level parent;
	public RenameWaveButton(GuiList_Level parent){
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
		return super.checkMouseClicked() || Center.keyPressed(Keys.R);
	}
	
	boolean debounce;
	public void button_OnMouseClick(){
		if(debounce || !shouldBeActive()) return;
		debounce = true;
		WaveRenameBox blah = new WaveRenameBox();
		LevelWave wave = parent.getSelectedValue();
		wave.name = blah.activate(wave.name);
		debounce = false;
	}
	
	public void draw(){
		super.draw();
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(text);
	}
}