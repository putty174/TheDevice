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
	
	public LevelLoader.LevelData load(AssetManager assetManager,String filename,FileHandle cheese,LevelInput uselessparam){
		formationManager = new AssetManager();
    	formationManager.setLoader(FormationLoader.FormationData.class,new FormationLoader(new InternalFileHandleResolver()));
    	
		FileHandle file = resolve(LEVELFILEPATH+filename+LEVELFILENAMEEXTENSION);
		
		LevelData data = new LevelData();
		
		DataInputStream stream = null;
		try{
		    stream = new DataInputStream(new BufferedInputStream(file.read()));
		    byte version = stream.readByte();
		    
		    byte numFormationTypes = stream.readByte();
		    System.out.println("numFormationTypes: "+numFormationTypes);
		    for(byte b = 0; b < numFormationTypes; b++){
		    	String formationname = stream.readUTF(); 
		    	formationNames.add(formationname);
		    	formationManager.load(formationname,FormationLoader.FormationData.class);
		    }
			formationManager.finishLoading();
		    
		    byte numWaves = stream.readByte();
		    for(byte i = 0; i < numWaves; i++){
		    	System.out.println("i: "+i);
	    		LevelWave wave = new LevelWave();
		    	byte numFormationsInWave = stream.readByte();
		    	for(byte j = 0; j < numFormationsInWave; j++){
			    	System.out.println("j: "+j);
		    		SingleFormation form = new SingleFormation();
		    		byte formationnameindex = stream.readByte();
		    		form.name = formationNames.get(formationnameindex);
		    		form.spawnAngle = stream.readByte();
		    		wave.formations.add(form);
		    	}
		    	wave.numFormationsUsed = stream.readByte();
		    	wave.delayBetweenFormations = stream.readFloat();
		    	data.waves.add(wave);
		    }
		}catch(java.io.EOFException _){
		    //Completed sucessfully
		}catch(Exception e){
			System.out.println("Map loading threw exception: "+e);
			System.out.println("Check if the file "+filename+" is missing. If it isn't, please inform Masana that his level-loading system sucks.");
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
