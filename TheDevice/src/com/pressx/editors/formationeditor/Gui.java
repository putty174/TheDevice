package com.pressx.editors.formationeditor;

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
    	initialize_SpawnLocationCreator();
    	initialize_SpawnLocationPlacer();
    	initialize_SpawnFileIO();
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
	draw_SpawnLocationPlacer();
	draw_SpawnLocationCreator();
	draw_FileIO();
	for(GuiObject obj : guiObjects)
	    obj.draw();
    }
    
    //////////SpawnLocation Creator
    static final Vector2 SLC_OFFSET = new Vector2(000,000);
    static final Vector2 SLC_SIZE = new Vector2(300,100);
    
    static CenteredText Text_Spawn;
    
    static EnemyVariator ev;
    public static void initialize_SpawnLocationCreator(){
		ev = new EnemyVariator();
		ev.setPosition(new Vector2(230,0));
		ev.register();
		
		GraphicsDraw.biggishFont();
		Text_Spawn = new CenteredText("Enemy Type:",new Vector2(SLC_OFFSET.x+88,SLC_OFFSET.y+SLC_SIZE.y/2));
    }
    public static void draw_SpawnLocationCreator(){
		GraphicsDraw.biggishFont();
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.rectangle(SLC_OFFSET.add(SLC_SIZE.div(2)),SLC_SIZE);
		GraphicsDraw.centerText(Text_Spawn);
    }
    
    public static SpawnType SpawnLocationCreator_getDesiredSpawnType(){
    	return SpawnType.values()[ev.value];
    }
    
    /////////////////////////SpawnLocation Placer
    static SpawnList spawnList;
    public static void initialize_SpawnLocationPlacer(){
		new MapScaleVariator().register();
		FileManager.newFormation();
		new SpawnList().register();
		new SpawnMap_ToggleShowColors().register();
		new SpawnMap_ToggleOnlySelected().register();
		new SpawnMap_ToggleShowMouseDistance().register();
		//new SpawnMap_ToggleShowNumbers().register();
		new SpawnMap_RecenterButton().register();
		new SnapTypeVariator().register();
    }
    
    public static void draw_SpawnLocationPlacer(){
		if(SpawnMap.currentMap != null){
		    SpawnMap.currentMap.update();
		    SpawnMap.currentMap.draw();
		}
    }
    
    /////////////////////////File I/O
    public static void initialize_SpawnFileIO(){
	FileManager.initialize();
    }
    public static void draw_FileIO(){
	FileManager.draw();
    }
}
