package com.pressx.spawner;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.pressx.editors.shared._G;

public class FormationLoader extends SynchronousAssetLoader<FormationLoader.FormationData,FormationLoader.FormationLoaderInput>{//SpawnLoaderParameter>{
	/////FormationLoaderInput class (input)
	public class FormationLoaderInput extends AssetLoaderParameters<FormationLoader.FormationData>{
		public String name;
		public FormationLoaderInput(String name){
			this.name = name;
		}
		public void finishedLoading(AssetManager assetManager, java.lang.String fileName, java.lang.Class type){}
	}
	/////FormationData class (output)
	public class FormationData{
		public ArrayList<SpawnData> list = new ArrayList<SpawnData>();
	}
	public class SpawnData{
		public byte type;
		public Vector2 position;
		public SpawnData(byte t,Vector2 p){
			type = t;
			position = p;
		}
	}
	/////FormationLoader
	static final String FORMATIONFILEPATH = _G.DATAFOLDER+"formations/";
	static final String FORMATIONFILENAMEEXTENSION = ".spawnformation";
	
	public FormationLoader(FileHandleResolver resolver){
		super(resolver);
	}
	
	public Array<AssetDescriptor> getDependencies(String s,FileHandle f,FormationLoaderInput param){
		return new Array<AssetDescriptor>();
	}
	
	public FormationLoader.FormationData load(AssetManager assetmanager,String filename,FileHandle fakefile,FormationLoaderInput whateverparam){
    	String filepath = FORMATIONFILEPATH+filename+FORMATIONFILENAMEEXTENSION;
    	
		FileHandle file = resolve(filepath);
		
		DataInputStream stream = null;
	    FormationData ans = new FormationData();
		try{
		    stream = new DataInputStream(new BufferedInputStream(file.read()));
		    byte version = stream.readByte();
		    for(int i = 0; i < 9999; i++){
			    byte type = stream.readByte();
			    float x = stream.readFloat();
			    Vector2 pos = new Vector2(x,stream.readFloat());
			    ans.list.add(new SpawnData(type,pos));
		    }
		}catch(java.io.EOFException _){
		    //Completed sucessfully
		}catch(Exception e){
			System.out.println("Formation loading threw exception: "+e);
			System.out.println("Check if the file "+filename+" is missing. If it isn't, please inform Masana that his formation-loading system sucks.");
		}finally{
		    try{
		    	stream.close();
		    }catch(Exception e){
		    	System.out.println("Well this sucks.");
		    }
		}
		return ans;
	}
}
