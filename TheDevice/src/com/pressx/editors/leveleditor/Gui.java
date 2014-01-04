package com.pressx.editors.leveleditor;

import java.util.ArrayList;
import java.awt.Color;

import com.pressx.editors.shared.CenteredText;
import com.pressx.editors.shared.SpawnType;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;


public class Gui{
    static ArrayList<GuiObject> guiObjects;
    
    public static void initialize(){
    	guiObjects = new ArrayList<GuiObject>();
    	new GuiList_Level().register();
    	new GuiList_Wave().register();
    	new GuiList_ImportedFormations().register();
    }
    
    /////Register/Remove
    public static void registerGuiObject_First(GuiObject obj){
		if(guiObjects.contains(obj))
		    guiObjects.remove(obj);
		guiObjects.add(0,obj);
    }
    public static void registerGuiObject(GuiObject obj){
		if(!guiObjects.contains(obj))
		    guiObjects.add(obj);
    }
    public static void removeGuiObject(GuiObject obj){
		if(guiObjects.contains(obj))
		    guiObjects.remove(obj);
    }
    
    /////Update/Draw
    public static void update(){
		for(Object obj : guiObjects.toArray())
		    ((GuiObject)obj).update();
    }
    
    public static void draw(){
		for(GuiObject obj : guiObjects)
		    obj.draw();
    }
}
