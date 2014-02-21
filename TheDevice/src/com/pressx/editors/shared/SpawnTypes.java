package com.pressx.editors.shared;

import java.awt.Color;

import com.pressx.editors.formationeditor.SpawnMap;


public class SpawnTypes{
	//static final Color COLOR_PLOTHOLE = new Color(0f,1,1);
	
    public static int getLevel(SpawnType t){
		switch(t){
		    case Fuzzie1:
		    case Plant1:
		    case Phoenix1:
		    	return 0;
		    case Fuzzie2:
		    case Plant2:
		    case Phoenix2:
		    	return 1;
		    case Fuzzie3:
		    case Plant3:
		    case Phoenix3:
		    	return 2;
	    	default:
	    		return 10;
		}
    }
    
    public static String getName(SpawnType t){
    	switch(t){
		    case Fuzzie1:
		    	return "Fuzzie 1";
		    case Fuzzie2:
		    	return "Fuzzie 2";
		    case Fuzzie3:
		    	return "Fuzzie 3";
		    case Plant1:
		    	return "Plant 1";
		    case Plant2:
		    	return "Plant 2";
		    case Plant3:
		    	return "Plant 3";
		    case Phoenix1:
		    	return "Phoenix 1";
		    case Phoenix2:
		    	return "Phoenix 2";
		    case Phoenix3:
		    	return "Phoenix 3";
		}
		return "ERROR";
    }
    
    public static int getApproximateDifficulty(SpawnType t){//right now, just based on the amount of health each one has
    	switch(t){
	    	case Fuzzie1:
	    	case Plant1:
	    	case Phoenix1:
	    		return 1;
	    	case Fuzzie2:
	    	case Plant2:
	    	case Phoenix2:
	    		return 2;
	    	case Fuzzie3:
	    	case Plant3:
	    	case Phoenix3:
	    		return 6;
    	}
    	return 0;
    }
    
    static final int FUZZYGRAY = 174;
    public static Color getColor1(SpawnType t){
	switch(t){
	    case Fuzzie1:
	    case Fuzzie2:
	    case Fuzzie3:
	    	return new Color(FUZZYGRAY,FUZZYGRAY,FUZZYGRAY);
	    case Plant1:
	    case Plant2:
	    case Plant3:
	    	return new Color(.2f,1,.2f);
	    /*case Knight1:
	    case Knight2:
	    case Knight3:
	    	return new Color(.3f,.3f,.3f);*/
	    case Phoenix1:
	    case Phoenix2:
	    case Phoenix3:
	    	return new Color(.85f,0,0);
	    /*case PlotHole:
	    	return COLOR_PLOTHOLE;*/
	}
	return Color.BLUE;
    }
    public static Color getColor2(SpawnType t){
	switch(t){
	    case Fuzzie1:
	    	return new Color(222,165,198);
	    case Fuzzie2:
	    	return new Color(222,100,125);
	    case Fuzzie3:
	    	return new Color(222,20,25);
	    case Plant1:
	    	return new Color(.2f,.6f,.2f);
	    case Plant2:
	    	return new Color(.6f,.4f,.2f);
	    case Plant3:
	    	return new Color(1,.2f,.2f);
	    /*case Knight1:
	    	return new Color(1,1,.3f);
	    case Knight2:
	    	return new Color(1,.7f,.21f);
	    case Knight3:
	    	return new Color(1,.4f,.12f);*/
	    case Phoenix1:
	    	return new Color(.9f,.5f,0);
	    case Phoenix2:
	    	return new Color(.9f,.25f,.25f);
	    case Phoenix3:
	    	return new Color(.9f,0,.5f);
	    /*case PlotHole:
	    	return COLOR_PLOTHOLE;*/
	}
	return Color.RED;
    }
    
    public static void drawPoint(EventLocation s,Vector2 screenpos,float mapsingleunitscreenradius,boolean selected){
	    Color c = GraphicsDraw.screen.getColor();
    	/*if(s.type == EventType.PlotHole){
    		//two rings
    		float radius = mapsingleunitscreenradius*.2f/1.4f;
    		if(!selected)
    				GraphicsDraw.setColor(COLOR_PLOTHOLE);
        	GraphicsDraw.circle(screenpos,radius);
        	if(selected)
				GraphicsDraw.setColor(COLOR_PLOTHOLE);
        	GraphicsDraw.circle(screenpos,radius-2);
    	}else{*/
	    	//large X with a small lined circle
	    	float siz = 5+SpawnTypes.getLevel(s.type)*3;
	    	GraphicsDraw.circle(screenpos,siz/2);
	    	GraphicsDraw.line(new Vector2(screenpos.x-siz,screenpos.y-siz),new Vector2(screenpos.x+siz,screenpos.y+siz));
	    	GraphicsDraw.line(new Vector2(screenpos.x+siz,screenpos.y-siz),new Vector2(screenpos.x-siz,screenpos.y+siz));
	    	if(SpawnMap.showEnemyColors){
	    	    GraphicsDraw.setColor(SpawnTypes.getColor1(s.type));
	    	    GraphicsDraw.fillCircle(screenpos,siz/2);
	    	}
    	//}
	    GraphicsDraw.setColor(c);
    }
    
    public static void drawSymbol(EventLocation spawn,Vector2 sqpos,Vector2 sqsiz){
    	/*if(spawn.type == EventType.PlotHole){
			GraphicsDraw.setColor(EventTypes.getColor1(spawn.type));
			GraphicsDraw.fillCircle(sqpos,sqsiz.x/2-2);
			GraphicsDraw.circle(sqpos,sqsiz.x/2);
    	}else{*/
			GraphicsDraw.setColor(SpawnTypes.getColor1(spawn.type));
			GraphicsDraw.fillRectangle(sqpos,sqsiz);
			GraphicsDraw.setColor(SpawnTypes.getColor2(spawn.type));
			GraphicsDraw.fillRectangle(sqpos,sqsiz.div(2));
    	//}
    }
}
