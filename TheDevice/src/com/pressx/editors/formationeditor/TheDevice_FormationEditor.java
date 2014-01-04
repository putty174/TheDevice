package com.pressx.editors.formationeditor;

import javax.swing.JFrame;

import com.pressx.editors.shared._G;

public class TheDevice_FormationEditor{
    public static void changeTitle(String filename){
    	//jframe.setTitle(filename == null ? "The Device: Formation Editor" : "Formation Editor - "+filename);
    }
    static JFrame jframe;
    
    public static String fileToOpen;
    static boolean exitonclose = true;;
    public static void openFile(String name){
    	if(jframe != null){
    		jframe.dispose();
    	}
    	exitonclose = false;
		//main(new String[]{});
    	(new Thread(new NewThreadRunnable())).start();
		fileToOpen = name;
    }
    
    public static void main(String[] args){
    	jframe = new JFrame();
        jframe.setSize(_G.FORMATIONEDITOR_WIDTH,_G.FORMATIONEDITOR_HEIGHT);
        changeTitle(null);
        jframe.setDefaultCloseOperation(exitonclose ? JFrame.EXIT_ON_CLOSE : JFrame.DISPOSE_ON_CLOSE);
        jframe.setResizable(false);
        Center center = new Center();
		center.start();
        jframe.add(center);
        jframe.setVisible(true);
    }
}

class NewThreadRunnable implements Runnable{
	public void run(){
		TheDevice_FormationEditor.main(new String[]{});
	}
}
