package com.pressx.editors.shared;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

public class FormationLoader{
    public static ArrayList<EventLocation> loadFormationFromFile(String path){
		DataInputStream stream = null;
		boolean success = false;
		ArrayList<EventLocation> locations = new ArrayList<EventLocation>();
		try{
		    stream = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
		    byte version = stream.readByte();
		    SpawnType[] spawntypes = SpawnType.values();
		    for(int i = 0; i < 9999; i++){//The last iteration will throw an EOFException
				SpawnType type = spawntypes[stream.readByte()];
				float xpos = stream.readFloat();
				float ypos = stream.readFloat();
				locations.add(new EventLocation(new Vector2(xpos,ypos),type));
		    }
		}catch(java.io.EOFException _){
		    //Completed successfully
		}catch(Exception e){
		    System.out.println("FORMATION LOADING THREW EXCEPTION: "+e);
		    locations = null;
		}finally{
		    try{
		    	stream.close();
		    }catch(Exception e){
		    	System.out.println("Well this sucks.");
		    	locations = null;
		    }
		}
	    return locations;
    }
}
