package com.pressx.editors.leveleditor;

/////Useful imports
import java.awt.Graphics2D;

import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared._G;

public class Center extends GameDriver{/////The class that handles everything
    static Center instance;//The instance of this class
    
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
	    	FileManager.initialize();
		    first = false;
    	}
		GraphicsDraw.setColor(_G.COLOR_BACKGROUND);
		GraphicsDraw.fillRectangle(_G.LEVELEDITOR_SCREENSIZE.div(2),_G.LEVELEDITOR_SCREENSIZE);
    	
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
