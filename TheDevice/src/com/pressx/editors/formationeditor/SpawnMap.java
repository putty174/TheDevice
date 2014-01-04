package com.pressx.editors.formationeditor;
import java.awt.Color;
import java.util.ArrayList;

import com.pressx.editors.shared.CenteredText;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.SpawnTypes;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;
import com.pressx.editors.shared.EventLocation;


enum MapSnapType{None,Grid,Ring}

public class SpawnMap extends GuiObject{
    //////////Constants
    public static final int MINSCALE = 2;
    public static final int MAXSCALE = 8;
    public static final int DEFAULTSCALE = 4;
    
    public static final Color COLOR_BACKGROUND = new Color(0,0,0);
    public static final Color COLOR_LINES = new Color(0f,1,0);
    public static final Color COLOR_FIELD = new Color(0.5f,0,1);
    public static final Color COLOR_OUTOFBOUNDS = new Color(.5f,0,0);
    public static final Color COLOR_DRAGSELECTIONBOX = new Color(1,.5f,0);
    
    public static final Vector2 OFFSET = new Vector2(160,139);
    public static final float SIZEFLOAT = 400;
    public static final float RADIUSFLOAT = SIZEFLOAT/2;
    public static final Vector2 SIZE = new Vector2(SIZEFLOAT,SIZEFLOAT);
    public static final Vector2 CENTER = OFFSET.add(SIZE.div(2));
    
    public static final Vector2 FIELDSIZE = new Vector2(996,662);
    public static final Vector2 FIELDUNITSIZE = Vector2.normalize(FIELDSIZE);
    public static final float UNITSIZE = FIELDSIZE.magnitude();
    public static final float FIELDANGLE = Vector2.toAngle(FIELDSIZE);
    
    static final int NUMANGLELINES = 12;
    static final float SCALELERPAMOUNT = .2f;
    
    //////////Statics
    public static SpawnMap currentMap;
    public static void setCurrentSpawnMap(SpawnMap map){
	currentMap = map;
    }
    
    public Vector2 relativeViewOffset = new Vector2(0,0);
    
    public static boolean onlyShowSelected;
    public static boolean showEnemyColors = true;
    public static boolean showRingNumbers;
    public static boolean showMouseDistance;
    
    public static MapSnapType snapType = MapSnapType.Grid;
    public static int snapSizeDivisor = 5;
    
    static float scale = MAXSCALE;
    static float desiredscale = DEFAULTSCALE;
    
    public static void setScale(float sc){
	desiredscale = sc;
    }
    
    //////////Instance
    public ArrayList<EventLocation> spawns;
    public SpawnMap(){
	spawns = new ArrayList<EventLocation>();
	setPosition(OFFSET);
	setSize(SIZE);
	currentMap = this;
	/*
	for(int i = 0; i < 1000; i++){
	    addSpawnLocation(new SpawnLocation(Vector2.zero(),SpawnType.Fuzzy,i));
	}//*/
    }
    
    /////Register (or not)
    public void register(){
    }
    
    /////Position conversions
    Vector2 getCenterOnScreen(){
	return CENTER.sub(relativeViewOffset.mul(RADIUSFLOAT/scale));
    }
    
    public Vector2 originalScreenToMap(Vector2 pos){
	return pos.sub(CENTER).div(RADIUSFLOAT/scale);
    }
    public Vector2 screenToMap(Vector2 pos){
	//return pos.sub(CENTER).div(RADIUSFLOAT/scale);
	return pos.sub(getCenterOnScreen()).div(RADIUSFLOAT/scale);
    }
    
    public Vector2 mapToScreen(Vector2 pos){
	//return pos.mul(RADIUSFLOAT/scale).add(CENTER);
	return pos.mul(RADIUSFLOAT/scale).add(getCenterOnScreen());
    }
    
    boolean checkOutOfMapBounds(Vector2 mappos){//Checks if a map position would be out of the maximum range
	return !Vector2.distanceLessThan(mappos,Vector2.zero(),MAXSCALE/2);
    }
    
    /////Add/Remove locations
    void addSpawnLocationAt(EventLocation spawn,int index){
		spawns.add(index,spawn);
		if(SpawnList.instance != null)
		    SpawnList.instance.focusOnBar(index);
    }
    public void addSpawnLocation(EventLocation spawn){
		EventLocation otherspawn;
	    //int spawnlevel = EventTypes.getLevel(spawn.type);
	    //int otherspawnlevel,otherspawntype;
	    int spawntype = spawn.type.ordinal();
		for(int i = 0; i < spawns.size(); i++){
			if(spawntype < spawns.get(i).type.ordinal()){
		    	addSpawnLocationAt(spawn,i);
		    	return;
		    }
			/*
		    otherspawn = spawns.get(i);
		    otherspawnlevel = EventTypes.getLevel(otherspawn.type);
		    if(spawnlevel > otherspawnlevel){
		    	addSpawnLocationAt(spawn,i);
		    	return;
		    }
		    if(spawnlevel < otherspawnlevel)
		    	continue;
		    otherspawntype = otherspawn.type.ordinal();
		    if(spawntype < otherspawntype){
		    	addSpawnLocationAt(spawn,i);
		    	return;
		    }
		    if(spawntype > otherspawntype)
		    	continue;*/
		}
		addSpawnLocationAt(spawn,spawns.size());
    }
    public void removeSpawnLocation(EventLocation spawn){
    	spawns.remove(spawn);
    }
    
    /////Message (at bottom of screen)
    static String message;
    static final int MESSAGECOUNTDOWN = 600;
    static final int MESSAGECOUNTDOWN_FADESTART = 100;
    static int countdown;
    public static void showMessage(String message){
	currentMap.message = message;
	countdown = MESSAGECOUNTDOWN;
    }
    
    /////Update
    EventLocation currentPoint;
    boolean checkValidSpawnLocation(){
	return currentPoint.position.magnitude() <= MAXSCALE && checkMouseOver();
    }
    
    boolean movingMap;
    Vector2 originalmousescreenpos;
    void update_middleclickmapmovement(){
	if(Center.middleMouseClicked() && checkMouseOver()){
	    movingMap = true;
	    originalmousescreenpos = Center.mousePosition();
	}
	if(movingMap){
	    if(Center.middleMouseDown()){
		relativeViewOffset = relativeViewOffset.sub(originalScreenToMap(Center.mousePosition().sub(originalmousescreenpos).add(CENTER)));
		originalmousescreenpos = Center.mousePosition();
		if(relativeViewOffset.magnitude() > MAXSCALE)
		    relativeViewOffset = Vector2.normalize(relativeViewOffset,MAXSCALE);
	    }else{
		movingMap = false;
	    }
	}
    }
    
    Vector2 rightclickdragstartposition;
    void update_rightclickselection(){
	if(Center.rightMouseDown()){
	    if(!checkMouseOver()) return;
	    if(Center.rightMouseClicked()){
			Vector2 mousepos = screenToMap(Center.mousePosition());
			rightclickdragstartposition = mousepos;
			float dist = 99999;
			ArrayList<EventLocation> selectedlocations = new ArrayList<EventLocation>();
			for(EventLocation location : spawns){
			    float d = Vector2.distance(mousepos,location.position);
			    if(d == dist){
			    	selectedlocations.add(location);
			    }else if(d < dist){
					selectedlocations.clear();
					dist = d;
					selectedlocations.add(location);
			    }
			}
			if(!Center.getKey(Keys.SHIFT) && !Center.getKey(Keys.CONTROL))
			    SpawnList.clearSelections();
			for(EventLocation location : selectedlocations){
			    SpawnList.instance.selectedSpawns.add(location);
			    SpawnList.instance.focusOnBar(spawns.indexOf(location));
			}
	    }
	    if(rightclickdragstartposition == null)
	    	return;
	    Vector2 mousepos = screenToMap(Center.mousePosition());
	    if(!Vector2.distanceLessThan(rightclickdragstartposition,mousepos,.1f)){
			Vector2 pos0 = new Vector2(mousepos.x < rightclickdragstartposition.x ? mousepos.x : rightclickdragstartposition.x,
				mousepos.y < rightclickdragstartposition.y ? mousepos.y : rightclickdragstartposition.y);
			Vector2 pos1 = new Vector2(mousepos.x > rightclickdragstartposition.x ? mousepos.x : rightclickdragstartposition.x,
				mousepos.y > rightclickdragstartposition.y ? mousepos.y : rightclickdragstartposition.y);
			if(!Center.getKey(Keys.SHIFT) && !Center.getKey(Keys.CONTROL))
			    SpawnList.clearSelections();
			for(EventLocation location : spawns){
			    if(location.position.x > pos0.x && location.position.y > pos0.y
				    && location.position.x < pos1.x && location.position.y < pos1.y)
				SpawnList.selectedSpawns.add(location);
			}
	    }
	}else
	    rightclickdragstartposition = null;
    }
    
    public void update(){//sorry for all the megafunctions here
		super.update();
		
		scale = desiredscale*SCALELERPAMOUNT+scale*(1-SCALELERPAMOUNT);
		//drawOffset = viewOffset.add(scale);
		
		update_middleclickmapmovement();
		update_rightclickselection();
		
		if(this.checkMouseOver()){
		    if(Center.leftMouseDown()){
				if(Center.leftMouseClicked()){
				    currentPoint = new EventLocation(Center.mousePosition(),Gui.SpawnLocationCreator_getDesiredSpawnType());
				    SpawnList.clearSelections();
				}
				if(currentPoint != null){
				    currentPoint.position = screenToMap(Center.mousePosition());
				    if(!checkValidSpawnLocation()){
						showMessage("Invalid location.");
						currentPoint.position = new Vector2(9999,0);
				    }else{
				    	switch(snapType){
				    	case None:
				    		break;
				    	case Grid:
				    		currentPoint.useSnap_Cartesian(.2f);
				    		break;
				    	case Ring:
				    		currentPoint.useSnap_Polar(_G.PI/12,.2f);
				    		break;
				    	}
						String mess = "Placing point.";
						for(int i = 0; i < (_G.cycle%150)/50; i++)
						    mess += '.';
						showMessage(mess);
				    }
				}
		    }else if(currentPoint != null){
				if(!checkValidSpawnLocation()){//Out of bounds
				    showMessage("Invalid location.");
				    currentPoint = null;
				}else{
					/*if(currentPoint.type == EventType.PlotHole){
						if(currentPoint.position.x > FIELDUNITSIZE.x*2 || currentPoint.position.x < -FIELDUNITSIZE.x*2 || currentPoint.position.y > FIELDUNITSIZE.y*2 || currentPoint.position.y < -FIELDUNITSIZE.y*2){
							showMessage("WARNING: Plot Hole will be moved.");
						}else{
							showMessage("Plot Hole placed!");
						}
					}else{*/
					    if(currentPoint.position.magnitude() < 2){
					    	showMessage("WARNING: Location is too close!");
					    }else{
					    	showMessage("Point placed!");
					    }
					//}
				    addSpawnLocation(currentPoint);
				    SpawnList.selectSpawn(currentPoint);
				    currentPoint = null;
				}
		    }
		}else if(currentPoint != null){
		    if(Center.leftMouseDown()){
				showMessage("Invalid location.");
				currentPoint.position = new Vector2(9999,0);
		    }else{
				currentPoint = null;
				showMessage("Point placement cancelled.");
		    }
		}else if(SpawnList.selectedSpawns.size() != 0){
		    int siz = SpawnList.selectedSpawns.size();
		    //showMessage(""+siz+(siz == 1 ? " point selected." : " points selected."));
		}
    }
    
    /////Draw
    void drawSingleSpawnLocation(EventLocation s,Vector2 screenpos,boolean selected){
    	SpawnTypes.drawPoint(s,screenpos,SIZEFLOAT/scale,selected);
    }
    final float MESSAGEHEIGHT = 32;
    void draw_message(){
		Vector2 pos = new Vector2(CENTER.x,CENTER.y+RADIUSFLOAT+MESSAGEHEIGHT/2);
		Vector2 siz = new Vector2(SIZEFLOAT,MESSAGEHEIGHT);
		GraphicsDraw.setColor(Color.WHITE);
		GraphicsDraw.fillRectangle(pos,siz);
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.rectangle(pos,siz);
		if(countdown != 0 && message != null){
		    countdown--;
		    if(countdown < MESSAGECOUNTDOWN_FADESTART){
			float a = 1-((float)countdown/MESSAGECOUNTDOWN_FADESTART);
			GraphicsDraw.setColor(new Color(a,a,a));
		    }
		    GraphicsDraw.biggishFont();
		    GraphicsDraw.centerText(message,new Vector2(pos.x,pos.y-3));
		}
    }
    Vector2 drawOffset;
    public void draw(){
		Vector2 centeronscreen = getCenterOnScreen();
		
		/////Draw square/out-of-bounds
		GraphicsDraw.setColor(COLOR_OUTOFBOUNDS);
		GraphicsDraw.fillRectangle(CENTER,SIZE);
		GraphicsDraw.setColor(COLOR_BACKGROUND);
		GraphicsDraw.fillCircle(getCenterOnScreen(),RADIUSFLOAT*MAXSCALE/scale);
		
		/////Draw field
		GraphicsDraw.setColor(COLOR_FIELD);
		Vector2 p1 = FIELDUNITSIZE.mul(SIZEFLOAT/scale);
		Vector2 p0 = centeronscreen.add(p1.mul(-1));
		p1 = p1.mul(2);//CENTER.add(p1);
		GraphicsDraw.screen.fillRect((int)p0.x,(int)p0.y,(int)p1.x,(int)p1.y);
		
		/////Draw lines
		GraphicsDraw.setColor(COLOR_LINES);
		GraphicsDraw.smallBoldFont();
		float ringradius = 0;
		System.out.println(""+2/snapSizeDivisor);
		for(float ring = 2; ring <= MAXSCALE; ring += 2){//(snapType == MapSnapType.None ? 2 : 2/snapSizeDivisor)){
		    ringradius = RADIUSFLOAT*ring/scale;
		    GraphicsDraw.circle(centeronscreen,ringradius);
		    if(showRingNumbers){
			    GraphicsDraw.text(""+(ring/2),centeronscreen.add(new Vector2(ringradius,0)));
			    GraphicsDraw.text(""+(ring/2),centeronscreen.add(new Vector2(-ringradius-8,0)));
		    }
		}
		for(int line = 0; line < NUMANGLELINES; line++){
		    GraphicsDraw.line(getCenterOnScreen(),getCenterOnScreen().add(Vector2.fromAngle(line*_G.TAU/NUMANGLELINES,/*finalring*//*RADIUSFLOAT*1.5f*/RADIUSFLOAT/scale*MAXSCALE)));
		}
		
		/////Draw mouse distance from center
		if(showMouseDistance){
		    GraphicsDraw.setColor(COLOR_LINES);
		    GraphicsDraw.text(""+(float)(int)(screenToMap(Center.mousePosition()).magnitude()/2*100)/100,Center.mousePosition());
		}
		
		/////Draw points
		//normal points
		if(!onlyShowSelected){
		    GraphicsDraw.setColor(Color.RED);
		    for(int i = 0; i < spawns.size(); i++){
			drawSingleSpawnLocation(spawns.get(i),mapToScreen(spawns.get(i).position),false);
		    }
		}
		//selected points
		float blah = ((float)Math.sin((float)_G.cycle/10)+1)/4;
		GraphicsDraw.setColor(new Color(1,.5f+blah,0));
		for(int i = 0; i < spawns.size(); i++){
		    if(SpawnList.selectedSpawns.contains(spawns.get(i)))
			drawSingleSpawnLocation(spawns.get(i),mapToScreen(spawns.get(i).position),true);
		}
		if(currentPoint != null){
		    drawSingleSpawnLocation(currentPoint,mapToScreen(currentPoint.position),true);
		}
		
		/*/////Silly Radar Effect for Eric
		float radarangle = ((float)_G.cycle/50)%(_G.PI*2);
		for(float add = 0; add < _G.PI/16; add += _G.PI/1024){
		    float a = add/(_G.PI/16);
		    GraphicsDraw.setColor(new Color(0,a,0,a));
		    GraphicsDraw.line(CENTER,CENTER.add(Vector2.fromAngle(radarangle+add,RADIUSFLOAT)));
		}//*/
		
		/*
		/////Draw grid lines
		GraphicsDraw.setColor(Color.GREEN);
		Vector2 topleft = screenToMap(OFFSET);
		Vector2 bottomright = screenToMap(Vector2.add(OFFSET,SIZE));
		float scl = snapSize*scale;
		float delta = 1f/scl;
		int num = 0;
		for(float x = (int)((topleft.x+delta)*scl)/scl; x < bottomright.x; x += delta*SIZE.x){
			System.out.println(""+x+":"+OFFSET.y);
			GraphicsDraw.line(new Vector2(OFFSET.x+x,OFFSET.y),new Vector2(OFFSET.x+x,OFFSET.y+SIZE.y));
			num++;
		}
		for(float y = (int)topleft.y+delta; y < bottomright.y; y += delta){
			GraphicsDraw.line(new Vector2(OFFSET.x,y),new Vector2(OFFSET.x+SIZE.x,y));
			num++;
		}
		System.out.println("num: "+num);
		*/
		
		/////Draw the drag-select box
		if(rightclickdragstartposition != null){
		    GraphicsDraw.setColor(COLOR_DRAGSELECTIONBOX);
		    Vector2 pos = mapToScreen(rightclickdragstartposition);
		    Vector2 mousepos = Center.mousePosition();
		    Vector2 siz = mousepos.sub(pos);
		    siz = new Vector2(Math.abs(siz.x),Math.abs(siz.y));
		    pos = new Vector2(pos.x < mousepos.x ? pos.x : mousepos.x,pos.y < mousepos.y ? pos.y : mousepos.y);
		    GraphicsDraw.screen.drawRect((int)pos.x,(int)pos.y,(int)siz.x,(int)siz.y);
		}
		
		/////Draw over any out-of-boundary lines to hide them
		GraphicsDraw.setColor(_G.COLOR_BACKGROUND);
		GraphicsDraw.fillRectangle(CENTER.add(new Vector2(0,-SIZE.y)),new Vector2(SIZE.x*2,SIZE.y));
		GraphicsDraw.fillRectangle(CENTER.add(new Vector2(-SIZE.x,0)),new Vector2(SIZE.x,SIZE.y*2));
		GraphicsDraw.fillRectangle(CENTER.add(new Vector2(0,SIZE.y)),new Vector2(SIZE.x*2,SIZE.y));
		GraphicsDraw.fillRectangle(CENTER.add(new Vector2(SIZE.x,0)),new Vector2(SIZE.x,SIZE.y*2));
		
		/////Draw message
		draw_message();
		
		/////Draw title
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText("MAP",new Vector2(OFFSET.x+SIZE.x/2,OFFSET.y-10));
    }
}

class SpawnMap_RecenterButton extends GuiButton{
    CenteredText centeredText;
    public SpawnMap_RecenterButton(){
	setSize(new Vector2(150,40));
	setPosition(new Vector2(SpawnMap.OFFSET.x-160,SpawnMap.OFFSET.y+40));
	applyfont();
	centeredText = new CenteredText("Recenter",getCenter());
    }
    public Color getBaseColor(){
	return Color.GREEN;
    }
    
    final void applyfont(){
	GraphicsDraw.biggishFont();
    }
    
    public void button_OnMouseClick(){
	SpawnMap.currentMap.relativeViewOffset = Vector2.zero();
    }
    
    public boolean checkMouseClicked(){
	return super.checkMouseClicked() || Center.getKey(Keys.R);
    }
    
    public boolean shouldBeDepressed(){
	return super.shouldBeDepressed() || Center.getKey(Keys.R);
    }
    
    public void draw(){
	super.draw();
	applyfont();
	GraphicsDraw.centerText(centeredText);
	GraphicsDraw.smallBoldFont();
	GraphicsDraw.text("[R]",new Vector2(position.x+size.x-17,position.y+size.y-7));
    }
}
