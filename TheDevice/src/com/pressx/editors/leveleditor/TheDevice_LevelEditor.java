package com.pressx.editors.leveleditor;

import javax.swing.JFrame;

import com.pressx.editors.formationeditor.TheDevice_FormationEditor;
import com.pressx.editors.shared._G;

public class TheDevice_LevelEditor{
    public static void changeTitle(String filename){
    	jframe.setTitle(filename == null ? "The Device: Level Editor" : "Level Editor - "+filename);
    }
    static JFrame jframe;
    public static void main(String[] args){
    	jframe = new JFrame();
        jframe.setSize(_G.LEVELEDITOR_WIDTH,_G.LEVELEDITOR_HEIGHT);
        changeTitle(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);
        Center center = new Center();
		center.start();
        jframe.add(center);
        jframe.setVisible(true);
    }
}
