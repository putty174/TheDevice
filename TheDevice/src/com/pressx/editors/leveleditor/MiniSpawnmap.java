package com.pressx.editors.leveleditor;

import java.awt.Color;
import java.util.ArrayList;

import com.pressx.editors.shared.EventLocation;
import com.pressx.editors.shared.FormationLoader;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;
import com.pressx.editors.shared.SpawnTypes;

public class MiniSpawnmap{
	static final Vector2 SIZE = new Vector2(200,200);
	static final Vector2 OFFSET = new Vector2(_G.LEVELEDITOR_WIDTH-SIZE.x-25,_G.LEVELEDITOR_HEIGHT-SIZE.y-30);
	static final Vector2 TEXTPOSITION = new Vector2(OFFSET.x,OFFSET.y-5);
	
	static ArrayList<EventLocation> locations;
	public static void load(String name){
		locations = FormationLoader.loadFormationFromFile(FileManager.FOLDER_FORMATION+name+'.'+FileManager.EXTENSION_FORMATION);
	}
	
	public static void draw(){
		Vector2 center = OFFSET.add(SIZE.div(2));
		GraphicsDraw.setColor(new Color(.5f,0,0));
		GraphicsDraw.fillRectangle(center,SIZE);
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.smallBoldFont();
		GraphicsDraw.text("Enemies: "+locations.size(),TEXTPOSITION);
		GraphicsDraw.fillCircle(center,SIZE.x/2);
		GraphicsDraw.setColor(Color.GREEN);
		for(int i = 0; i < 4; i++){
			GraphicsDraw.circle(center,SIZE.x*(i+1)/8);
		}

		for(EventLocation location : locations){
			GraphicsDraw.setColor(SpawnTypes.getColor1(location.type));
			GraphicsDraw.circle(center.add(location.position.mul(SIZE.div(16))),2);
			GraphicsDraw.setColor(SpawnTypes.getColor2(location.type));
			GraphicsDraw.fillCircle(center.add(location.position.mul(SIZE.div(16))),2);
		}
	}
}
