package com.pressx.editors.leveleditor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.pressx.editors.formationeditor.Center;
import com.pressx.editors.shared.SingleFormation;
import com.pressx.editors.shared.LevelWave;
import com.pressx.editors.shared._G;

public class FileManager{
	static final byte FILEVERSION = 0;
	static final String FOLDER_LEVEL = _G.DATAFOLDER+"levels/";
    static final String EXTENSION_LEVEL = "devicelevel";
    static final String EXTENSION_LEVEL_WITHDOT = ".devicelevel";
	static final String FOLDER_FORMATION = _G.DATAFOLDER+"formations/";
    static final String EXTENSION_FORMATION = "spawnformation";
    static boolean saving;
    static boolean loading;
    static String fileName;

    public static void initialize(){
		new FileManager_SaveButton().register();
		new FileManager_SaveAsButton().register();
		new FileManager_LoadButton().register();
		new FileManager_NewButton().register();
		newLevel();
		importAllFormations();
    }
    public static void draw(){
    }
    
    static String chooseFileName(boolean savingnotloading,boolean formationnotlevel){
    	String extension = formationnotlevel ? EXTENSION_FORMATION : EXTENSION_LEVEL;
    	String text = (formationnotlevel ? "Spawn Formation File": "The Device Level File")+" (*."+extension+')';
    	
		Center.resetLeftMouse();
		
		JFileChooser chooser = new JFileChooser(formationnotlevel ? FOLDER_FORMATION : FOLDER_LEVEL);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(text,extension);
		chooser.setFileFilter(filter);
		int returnval;
		if(savingnotloading)
			returnval = chooser.showSaveDialog(Center.instance);
		else
			returnval = chooser.showOpenDialog(Center.instance);
		/*chooser.setDialogTitle(savingnotloading ? "Save As" : "Open");
		chooser.setDialogType(savingnotloading ? JFileChooser.SAVE_DIALOG : JFileChooser.OPEN_DIALOG);
		int returnval = chooser.showOpenDialog(Center.instance);*/
		if(returnval == 0){
		    return chooser.getSelectedFile().getName();
		}
		return null;
    }
    
    //////////Formation importing
    public static void importSingleFormation(String path,String name){
    	name = name.substring(0,name.length()-EXTENSION_FORMATION.length()-1);
    	
    	DataInputStream stream = null;
		try{
		    stream = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
		    stream.readByte();//version
		    GuiList_ImportedFormations.tryAddImportedFormation(new ImportedFormation(name));
		}catch(Exception e){
			//If it errors, then don't do anything
			System.out.println("ERROR: "+e);
		}finally{
			try{
				stream.close();
			}catch(Exception _){}
		}
    }
    public static void importFormation(){
    	String name = chooseFileName(false,true);
    	if(name == null) return;//cancelled
    	String path = FOLDER_FORMATION+name;
    	importSingleFormation(path,name);
    }
    
    public static void importAllFormations(){
    	File[] files = new File(FOLDER_FORMATION).listFiles();
    	for(File f : files)
    		importSingleFormation(FOLDER_FORMATION+f.getName(),f.getName());
    }
    
    //////////Level management
    public static void newLevel(){
    	GuiList_Level.newLevel();
		fileName = null;
		TheDevice_LevelEditor.changeTitle(fileName);
    }
    
    public static void saveLevel(){
		saving = true;
		String s = chooseFileName(true,false);
		if(s != null){
		    int i2 = s.length()-EXTENSION_LEVEL_WITHDOT.length();
		    if(i2 <= 0 || !s.substring(i2).equals(EXTENSION_LEVEL_WITHDOT))
		    	s += EXTENSION_LEVEL_WITHDOT;
		    saveLevelToFile(s);
		}
		saving = false;
    }
    
    public static void saveLevel_Default(){
		if(fileName == null)
		    saveLevel();
		else
		    saveLevelToFile(fileName);
    }
    
    static void saveLevelToFile(String filename){
		DataOutputStream stream = null;
		try{
		    stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(FOLDER_LEVEL+filename)));
		    stream.write(FILEVERSION);
		    
		    ArrayList<String> formationnames = new ArrayList<String>();
	    	boolean found;
		    for(ImportedFormation f : GuiList_ImportedFormations.instance.getValues()){
		    	found = false;
		    	for(LevelWave w : GuiList_Level.instance.getValues()){
		    		for(SingleFormation sf : w.formations)
		    			if(sf.name.equals(f.name)){
		    				found = true;
				    		formationnames.add(f.name);
		    				break;
		    			}
		    		if(found) break;
		    	}
		    }
		    stream.writeByte((byte)formationnames.size());
		    for(String name : formationnames)
	    		stream.writeUTF(name);

		    System.out.println(5);
		    ArrayList<LevelWave> waves = GuiList_Level.instance.getValues();
		    stream.write((int)waves.size());
		    for(int i = 0; i < waves.size(); i++){
		    	LevelWave wave = waves.get(i);
		    	stream.writeByte((byte)wave.formations.size());
		    	for(int j = 0; j < wave.formations.size(); j++){
		    		SingleFormation formation = wave.formations.get(j);
		    		stream.writeByte((byte)formationnames.indexOf(formation.name));
		    		stream.writeByte((byte)formation.spawnAngle);
		    	}
		    	stream.writeByte((byte)wave.numFormationsUsed);
		    	stream.writeFloat((float)wave.delayBetweenFormations);
		    }
		    
		    System.out.println(filename);
		    
		    stream.flush();
		    fileName = filename;
		    TheDevice_LevelEditor.changeTitle(fileName);
		}catch(Exception e){
		    System.out.println("LEVEL SAVING THREW EXCEPTION: "+e);
		}finally{
		    try{
		    	stream.close();
		    }catch(Exception _){
		    	System.out.println("This doesn't even make sense.");
		    }
		}
    }
    
    static void loadLevel(){
		loading = true;
		String s = chooseFileName(false,false);
		if(s != null)
		    loadLevelFromFile(s);
		loading = false;
    }
    
    static void loadLevelFromFile(String filename){
		newLevel();
		DataInputStream stream = null;
		boolean success = true;
		try{
		    stream = new DataInputStream(new BufferedInputStream(new FileInputStream(FOLDER_LEVEL+filename)));
		    byte version = stream.readByte();
		    
		    ArrayList<String> formationnames = new ArrayList<String>();
		    
		    byte num = stream.readByte();//number of formation types
		    for(int i = 0; i < num; i++)
		    	formationnames.add(stream.readUTF());
		    
		    num = stream.readByte();//number of waves
		    for(byte i = 0; i < num; i++){
		    	byte numformations = stream.readByte();//number of formations in the current wave
		    	ArrayList<SingleFormation> formations = new ArrayList<SingleFormation>();
		    	for(byte j = 0; j < numformations; j++){
		    		SingleFormation s = new SingleFormation();
		    		s.name = formationnames.get(stream.readByte());
		    		s.spawnAngle = stream.readByte();
		    		formations.add(s);
		    	}
		    	LevelWave wave = new LevelWave(formations);
		    	wave.numFormationsUsed = stream.readByte();
		    	wave.delayBetweenFormations = stream.readFloat();
		    	GuiList_Level.addWave(wave);
		    }
		}catch(java.io.EOFException _){
		    //Completed sucessfully
		    success = true;
		}catch(Exception e){
		    System.out.println("LEVEL LOADING THREW EXCEPTION: "+e);
		    success = false;
		}finally{
		    if(success){
				fileName = filename;
				TheDevice_LevelEditor.changeTitle(fileName);
		    }
		    try{
		    	stream.close();
		    }catch(Exception e){
		    	System.out.println("Well this sucks.");
		    }
		}
    }
}
