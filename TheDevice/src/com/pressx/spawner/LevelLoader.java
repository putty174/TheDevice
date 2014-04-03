package com.pressx.spawner;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import com.pressx.editors.shared.LevelWave;
import com.pressx.editors.shared.SingleFormation;
import com.pressx.editors.shared._G;

public class LevelLoader extends SynchronousAssetLoader<LevelLoader.LevelData,LevelLoader.LevelInput>{//SpawnLoaderParameter>{
	/////LevelInput class (input)
	public class LevelInput extends AssetLoaderParameters<LevelLoader.LevelData>{
		public String name;
		public LevelInput(String name){
			this.name = name;
		}
		public void finishedLoading(AssetManager assetManager, java.lang.String fileName, java.lang.Class type){}
	}
	/////LevelData class (output)
	public class LevelData{
		//public ArrayList<String> list;
		public ArrayList<LevelWave> waves = new ArrayList<LevelWave>();
	}
	/////LevelLoader
	static final String LEVELFILEPATH = _G.DATAFOLDER+"levels/";
	static final String LEVELFILENAMEEXTENSION = ".devicelevel";
	
	private ArrayList<String> formationNames;	
	private AssetManager formationManager;
	public LevelLoader(FileHandleResolver resolver){
		super(resolver);
		formationNames = new ArrayList<String>();
	}
	
	public Array<AssetDescriptor> getDependencies(String s,FileHandle f,LevelInput param){
		return new Array<AssetDescriptor>();
	}
	
	public AssetManager getFormationManager(){
		return formationManager;
	}
	
	int convbyte(byte b){return b & 0xff;}
	public LevelLoader.LevelData load(AssetManager assetManager,String filename,FileHandle cheese,LevelInput uselessparam){
		formationManager = new AssetManager();
    	formationManager.setLoader(FormationLoader.FormationData.class,new FormationLoader(new InternalFileHandleResolver()));
    	
		FileHandle file = resolve(LEVELFILEPATH+filename+LEVELFILENAMEEXTENSION);
		
		LevelData data = new LevelData();
		
		DataInputStream stream = null;
		try{
		    stream = new DataInputStream(new BufferedInputStream(file.read()));
		    byte version = stream.readByte();
		    //System.out.println("Level version: "+version);
		    
		    int numFormationTypes = convbyte(stream.readByte());
		    for(int b = 0; b < numFormationTypes; b++){
		    	String formationname = stream.readUTF(); 
		    	formationNames.add(formationname);
		    	formationManager.load(formationname,FormationLoader.FormationData.class);
		    }
			formationManager.finishLoading();
		    
		    int numWaves = convbyte(stream.readByte());
		    //System.out.println(""+numWaves);
		    for(int i = 0; i < numWaves; i++){
	    		LevelWave wave = new LevelWave();
		    	int numFormationsInWave = convbyte(stream.readByte());
		    	for(int j = 0; j < numFormationsInWave; j++){
		    		SingleFormation form = new SingleFormation();
		    		int formationnameindex = convbyte(stream.readByte());
		    		form.name = formationNames.get(formationnameindex);
		    		form.spawnAngle = stream.readByte();
		    		wave.formations.add(form);
		    	}
		    	if(version == 1){//version 1, which added wave name (editor only) and randomized waves
		    		stream.readUTF();//The wave's name, which is only needed by the designers
		    		wave.isRandomized = stream.readBoolean();
		    	}else{//for backward compatibility with version 0, which only had randomized waves
		    		wave.isRandomized = true;
		    	}
	    		if(wave.isRandomized)
			    	wave.numFormationsUsed = convbyte(stream.readByte());
	    		else
	    			wave.numFormationsUsed = wave.formations.size();
		    	wave.delayBetweenFormations = stream.readFloat();
		    	data.waves.add(wave);
		    }
		}catch(java.io.EOFException _){
		    //Completed sucessfully
		}catch(Exception e){
			System.out.println("Map loading threw exception: "+e);
			System.out.println("Check if the file "+filename+" is missing. If it isn't, please kindly inform Masana that his level-loading system sucks.");
		}finally{
		    try{
		    	stream.close();
		    }catch(Exception e){
		    	System.out.println("):");
		    }
		}
		return data;
	}
	
	public float getProgress(){
		return formationManager.getProgress(); 
	}
	
	public boolean getUpdate(){
		return formationManager.update();
	}
}
