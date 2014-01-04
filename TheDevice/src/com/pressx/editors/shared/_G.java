package com.pressx.editors.shared;

import java.awt.Color;

/*
Definitions:
-Match: A series of rounds
-Round: One wave of enemies, selected from a list of possible waves (or just one if randomization is not needed)
-Wave: A single wave taken from a .spawnmap file

Masana's To-Do/Idea List:
-List of imported wave files (as file names); up to 256
-List of possible waves to create for each round (each round will choose one wave from the list so the match is randomized); up to 256
-Button to set which round is being edited
-Difficul

Match Editor File Format:
-Filepaths for waves (String); up to 256
-Number of possible wave versions (byte) + Wave IDs (byte)
 */

//Global stuff
public class _G {
	public static final float PI = (float)Math.PI;
    public static final float TAU = PI*2;
    
    public static final String DATAFOLDER = "./data/leveldata/";
    
    public static long timeElapsed;
    public static int cycle = 0;

    public static final Color COLOR_BACKGROUND = new Color(.85f,.8f,.99f);
    public static final Color COLOR_FOREGROUND = Color.BLACK;
    
    public static final Color COLOR_ENEMYVARIATOR = new Color(1,.6f,.6f);
    public static final Color COLOR_SECONDSVARIATOR = new Color(.5f,.75f,1);
    public static final Color COLOR_MILLISECONDSVARIATOR = new Color(.6f,.85f,1);
    
    public static final Color COLOR_MAPZOOMVARIATOR = new Color(0f,.9f,0);
    public static final Color COLOR_MAPSNAPVARIATOR = new Color(.1f,.7f,0);//unused
    
    public static final Color COLOR_LEVELEDITORSTANDARDBUTTON = new Color(.5f,.75f,1);

    /////Formation Editor
    public static final int FORMATIONEDITOR_WIDTH = 820,FORMATIONEDITOR_HEIGHT = 600;
    public static final Vector2 FORMATIONEDITOR_SCREENSIZE = new Vector2(FORMATIONEDITOR_WIDTH,FORMATIONEDITOR_HEIGHT);
    
    /////Match Editor
    public static int LEVELEDITOR_WIDTH = 800,LEVELEDITOR_HEIGHT = 700;
    public static final Vector2 LEVELEDITOR_SCREENSIZE = new Vector2(LEVELEDITOR_WIDTH,LEVELEDITOR_HEIGHT);
}