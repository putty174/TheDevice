package com.pressx.editors.formationeditor;

/////Useful imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared._G;

public class Center extends GameDriver{/////The class that handles everything
    public static Center instance;//The instance of this class
    
    public static boolean getKey(Keys n){//If a key is being held down
	return instance.keys[n.ordinal()] || keyPressed(n);
    }
    public static boolean keyPressed(Keys n){//If a key was pressed this cycle
	return instance.keypress[n.ordinal()];
    }
    public static void resetKey(Keys n){//Forces a key to not be down (to make shortcuts like CTRL+A not also proc A)
	instance.keys[n.ordinal()] = false;
	instance.keypress[n.ordinal()] = false;
    }
    
    public void start(){
	super.start();
    	instance = this;
    }
    
    boolean first = true;
    public void draw(Graphics2D screen){
    	GraphicsDraw.initializeDraw(screen);
    	if(first){
	    GraphicsDraw.initialize(screen.getFont());
	    Gui.initialize();
	    if(TheDevice_FormationEditor.fileToOpen != null){
	    	FileManager.loadFormationFromFileName(TheDevice_FormationEditor.fileToOpen);
	    	TheDevice_FormationEditor.fileToOpen = null;
	    }
	    first = false;
    }
	GraphicsDraw.setColor(_G.COLOR_BACKGROUND);
	GraphicsDraw.fillRectangle(_G.FORMATIONEDITOR_SCREENSIZE.div(2),_G.FORMATIONEDITOR_SCREENSIZE);
    	
    	/////Update stuff
	update_mouse();
	Gui.update();
	
	/////Draw stuff
	Gui.draw();
    	
	/////Final stuff (must be last)
    	for(int i = 0;i < keypress.length;i++)
    		keypress[i] = false;
	wheelRotation = 0;
	_G.cycle++;
    }
}
