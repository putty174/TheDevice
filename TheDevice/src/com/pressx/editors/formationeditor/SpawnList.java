//TODO: Differentiate CTRL+CLICK and SHIFT+CLICK

package com.pressx.editors.formationeditor;

import java.awt.Color;
import java.util.ArrayList;

import com.pressx.editors.leveleditor.GuiList;
import com.pressx.editors.shared.EventLocation;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.SpawnTypes;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;

public class SpawnList extends GuiObject{
	public static final Vector2 COUNTTEXTPOS = new Vector2(500,40);
	public static final Vector2 DIFFTEXTPOS = new Vector2(500,80);
	
    public static final Vector2 OFFSET = new Vector2(600,139);
    public static final Vector2 SIZE = new Vector2(120,400);
    public static final Vector2 BARSIZE = new Vector2(120,20);//size of a single bar thing
    public static final float ARROWSQUARESIZE = 20;
    public static final int MAXBARS = 20;
    
    public static final int SCROLLSPEED = 8;
    
    public static ArrayList<EventLocation> selectedSpawns;
    
    public static SpawnList instance;
    
    public int barPosition;
    
    public boolean showTimeBars;
    
    public SpawnList(){
	instance = this;
	setPosition(new Vector2(OFFSET.x,OFFSET.y-100));
	setSize(new Vector2(SIZE.x,SIZE.y+200));
	selectedSpawns = new ArrayList<EventLocation>();
    }
    
    /////Register
    public void register(){
		super.register();
		new SpawnList_Arrow(true).register();
		new SpawnList_Arrow(false).register();
		new SpawnList_Scrollbar().register();
    }
    
    /////Click
    int screenPositionYToIndex(float y){
	int b = (int)((y-OFFSET.y)/SIZE.y*MAXBARS)+barPosition;
	return b;
    }
    
    /////Selection
    static EventLocation getSpawnAtIndex(int index){
	if(index < SpawnMap.currentMap.spawns.size() && index >= 0)
	    return SpawnMap.currentMap.spawns.get(index);
	return null;
    }
    public static void clearSelections(){
	selectedSpawns.clear();
    }
    public static void selectSpawn(int index){
	EventLocation spawn = getSpawnAtIndex(index);
	if(spawn != null){
	    selectSpawn(spawn);
	    if(firstSelection == -1)
		firstSelection = index;
	}
    }
    public static void selectSpawn(EventLocation spawn){
	if(!selectedSpawns.contains(spawn)){
	    selectedSpawns.add(spawn);
	}
    }
    
    public static void toggleSelection(int index){
	EventLocation spawn = getSpawnAtIndex(index);
	if(spawn != null){
	    if(selectedSpawns.contains(spawn))
		selectedSpawns.remove(spawn);
	    else
		selectedSpawns.add(spawn);
	}
    }
    
    /////Modifying selected points
    public static void deleteSelectedPoints(){
	int siz = selectedSpawns.size();
	for(int i = 0; i < siz; i++){
	    SpawnMap.currentMap.spawns.remove(selectedSpawns.get(i));
	}
	clearSelections();
	SpawnMap.showMessage(""+siz+" point"+(siz == 1 ? "" : "s")+" removed.");
    }
    
    /////Bars
    static final int BARSCROLLACCELERATIONTHRESHOLD = 10;
    int barscrollacceleration;
    int lastbarscrollaccelerationcycle;
    public static void setBarPosition(int pos){
	if(pos < instance.getMinBarPosition())
	    pos = instance.getMinBarPosition();
	else if(pos > instance.getMaxBarPosition())
	    pos = instance.getMaxBarPosition();
	instance.barPosition = pos;
    }
    public void changeBarPosition(int val){
	if(lastbarscrollaccelerationcycle < _G.cycle-10){
	    barscrollacceleration = 0;
	}
	setBarPosition(barPosition+val*(1+barscrollacceleration/BARSCROLLACCELERATIONTHRESHOLD));
	lastbarscrollaccelerationcycle = _G.cycle;
	barscrollacceleration++;
    }
    public static void incrementBarPosition(){
	instance.changeBarPosition(1);
    }
    public static void decrementBarPosition(){
	instance.changeBarPosition(-1);
    }
    
    public int getMaxBarPosition(){
	int s = SpawnMap.currentMap.spawns.size();
	return s < MAXBARS ? 0 : s-MAXBARS;
    }
    public int getMinBarPosition(){
	return 0;
    }
    
    public void focusOnBar(int index){//Makes the list focus on a certain position if it is not already in view
    	setBarPosition(index < barPosition ? index : index >= barPosition+MAXBARS ? index-MAXBARS+1 : barPosition);
    	firstSelection = index;
    }
    
    /////Update
    int dragSelectIndex0 = -1;
    int dragSelectIndex1 = -1;
    void dragSelect_Recalculate(){
	clearSelections();
	int a,b;
	if(dragSelectIndex0 < dragSelectIndex1){
	    a = dragSelectIndex0;
	    b = dragSelectIndex1;
	}else{
	    b = dragSelectIndex0;
	    a = dragSelectIndex1;
	}
	for(int i = a; i <= b; i++){
	    selectSpawn(i);
	}
    }
    
    void update_scrollwheel(){
	int rot = Center.getWheelRotation();
	if(rot != 0)
	    setBarPosition(barPosition+rot);
    }
    
    static int firstSelection = -1;
    
    void update_onmouseover(){
	update_scrollwheel();
	if(Center.leftMouseDown()){
	    int index = screenPositionYToIndex(Center.mousePosition().y);
	    if(Center.leftMouseClicked()){
		if(Center.getKey(Keys.SHIFT)){
		    if(firstSelection != -1){
			dragSelectIndex0 = firstSelection;
			dragSelectIndex1 = index;
			dragSelect_Recalculate();
		    }
		}else if(Center.getKey(Keys.CONTROL)){
		    firstSelection = index;
		    toggleSelection(index);
		}else{
		    firstSelection = index;
		    clearSelections();
		    selectSpawn(index);
		    dragSelectIndex0 = index;
		    dragSelectIndex1 = dragSelectIndex0;
		}
	    }
	    if(dragSelectIndex0 != -1){
		float mousey = Center.mousePosition().y;
		int newindex = index;
		if(_G.cycle%10 == 0){
		    if(mousey < OFFSET.y){
			decrementBarPosition();
			newindex = barPosition;
		    }else if(mousey > OFFSET.y+SIZE.y){
			incrementBarPosition();
			newindex = barPosition+MAXBARS-1;
		    }
		}
		if(newindex != dragSelectIndex1){
		    dragSelectIndex1 = newindex;
		    dragSelect_Recalculate();
		}
	    }
	}
    }
    
    public void update(){
	super.update();
	
	if(Center.keyPressed(Keys.DELETE) || Center.keyPressed(Keys.X))
	    deleteSelectedPoints();
	
	if(dragSelectIndex0 != -1 && !Center.leftMouseDown()){
	    dragSelectIndex0 = -1;
	    dragSelectIndex1 = -1;
	}
	
	if(checkMouseOver()){
	    update_onmouseover();
	}
	
	//clear selections with Escape or CTRL+D
	if(Center.keyPressed(Keys.ESCAPE) || (Center.getKey(Keys.CONTROL) && Center.keyPressed(Keys.D))){
	    clearSelections();
	    dragSelectIndex0 = -1;
	    if(Center.keyPressed(Keys.D))
		Center.resetKey(Keys.D);
	}
	
	//Go up and down with arrow keys
	boolean goup = Center.keyPressed(Keys.UP);
	if(goup || Center.keyPressed(Keys.DOWN)){
	    /*int index = dragSelectIndex0 != -1 ? dragSelectIndex0 : firstSelection;//This commented-out code would instead make the up/down keys change the selection up/down, though shift would be buggy
	    if(index != -1){
		index = goup ? index-1 : index+1;
		if(index >= 0 && index < SpawnMap.currentMap.spawns.size()){
			if(Center.getKey(Keys.SHIFT)){
				if(dragSelectIndex0 == -1){
					dragSelectIndex0 = firstSelection;
					dragSelectIndex1 = index;
				}
			}else
				clearSelections();
			//firstSelection = index;//focusOnBar does this anyway
		    selectSpawn(index);
		    focusOnBar(index);
		}
	    }*/
		setBarPosition(goup ? barPosition-1 : barPosition+1);
	}
	
	//select all with CTRL+A
	if(Center.getKey(Keys.CONTROL) && Center.keyPressed(Keys.A)){
	    for(int i = 0; i < SpawnMap.currentMap.spawns.size(); i++){
		selectSpawn(i);
	    }
	    Center.resetKey(Keys.A);
	}
    }
    
    /////Draw
    String getNameTextFromSpawnLocation(EventLocation spawn){
		String s = spawn.type.name();
		return s;
    }
    static final int NEWLOCATIONANIMATIONCYCLE = 30;
    void drawSingleUnit(int realindex,int relativeindex,EventLocation spawn){
		Vector2 pos = new Vector2(OFFSET.x+BARSIZE.x/2,OFFSET.y+BARSIZE.y*(.5f+relativeindex));
		
		//Draw a bar every five realindex positions
		if(realindex%5 == 4){
		    GraphicsDraw.setColor(Color.BLACK);
		    GraphicsDraw.fillRectangle(new Vector2(pos.x+BARSIZE.x/2,pos.y),new Vector2(3,BARSIZE.y));
		    Vector2 p2 = new Vector2(pos.x+BARSIZE.x/2+5,pos.y);
		    GraphicsDraw.fillRectangle(p2,new Vector2(10,3));
		    GraphicsDraw.centerText(""+(realindex+1),new Vector2(p2.x+15,p2.y));
		}
		
		//Choose the color
		Color drawcolor = Color.WHITE;
		/*int cycledifference = _G.cycle-spawn.cyclePlaced;
		if(cycledifference < NEWLOCATIONANIMATIONCYCLE){//Make new ones flash green
		    float a = (float)cycledifference/NEWLOCATIONANIMATIONCYCLE;
		    drawcolor = new Color(a,1,a);
		}else */
		if(selectedSpawns.contains(spawn)){//Highlight selected ones
		    float a = ((float)Math.sin((float)(_G.cycle+realindex*5)/10)+1)/8;
		    if(realindex == dragSelectIndex0 || realindex == firstSelection){
			drawcolor = new Color(1f,.8f,a+.15f);
		    }else{
			drawcolor = new Color(1,1,a+.15f);
		    }
		}
		
		GraphicsDraw.setColor(drawcolor);
		
		/////Draw actual bar
		Vector2 siz = new Vector2(BARSIZE.x-4,BARSIZE.y-4);
		GraphicsDraw.fillRectangle(pos,siz);
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.rectangle(pos,siz);
		
		/////Draw text
		GraphicsDraw.centerText(getNameTextFromSpawnLocation(spawn),new Vector2(pos.x+BARSIZE.x/4,pos.y));
		
		/////Draw color-coded square
		Vector2 sqpos = new Vector2(pos.x-BARSIZE.x/2+BARSIZE.y/2,pos.y);
		Vector2 sqsiz = new Vector2(BARSIZE.y-8,BARSIZE.y-8);
		SpawnTypes.drawSymbol(spawn,sqpos,sqsiz);
    }
    
    public void draw(){
		if(SpawnMap.currentMap == null || SpawnMap.currentMap.spawns == null)
		    return;
		GraphicsDraw.setColor(new Color(.7f,.7f,.7f));
		GraphicsDraw.fillRectangle(getCenter(),SIZE);
		GraphicsDraw.defaultFont();
		for(int i = barPosition; i < barPosition+MAXBARS && i < SpawnMap.currentMap.spawns.size(); i++){
		    drawSingleUnit(i,i-barPosition,SpawnMap.currentMap.spawns.get(i));
		}
		
		//draw border
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.rectangle(getCenter(),SIZE);
		//draw title
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText("LIST",new Vector2(OFFSET.x+SIZE.x/2,OFFSET.y-10));
		//draw number of selected locations
		if(!selectedSpawns.isEmpty()){
		    int num = selectedSpawns.size();
		    GraphicsDraw.text((num != 1 && num == SpawnMap.currentMap.spawns.size() ? "All " : "")+num+" location"+(num == 1 ? "" : "s")+" selected.",new Vector2(OFFSET.x+5,OFFSET.y+SIZE.y+20));
		}
		GraphicsDraw.biggishFont();
		GraphicsDraw.text("Total Enemies: "+SpawnMap.currentMap.spawns.size(),COUNTTEXTPOS);
		int diff = 0;
		for(EventLocation location : SpawnMap.currentMap.spawns)
			diff += SpawnTypes.getApproximateDifficulty(location.type);
		GraphicsDraw.text("Approx. Difficulty: "+diff,DIFFTEXTPOS);
    }
}

class SpawnList_Arrow extends GuiButton{
    static final float SQUARESIZE = SpawnList.ARROWSQUARESIZE;
    static final Vector2 SIZE = new Vector2(SQUARESIZE,SQUARESIZE);
    
    static final Color COLOR_ENABLED = new Color(.9f,.9f,.9f);
    static final Color COLOR_DISABLED = new Color(.5f,.5f,.5f);
    static final Color TEXTCOLOR_ENABLED = Color.BLACK;
    static final Color TEXTCOLOR_DISABLED = new Color(.4f,.4f,.4f);
    
    boolean up;
    boolean enabled;
    
    public SpawnList_Arrow(boolean up){
	this.up = up;
	setSize(SIZE);
	if(up){
	    setPosition(new Vector2(SpawnList.OFFSET.x-SQUARESIZE,SpawnList.OFFSET.y));
	}else{
	    setPosition(new Vector2(SpawnList.OFFSET.x-SQUARESIZE,SpawnList.OFFSET.y+SpawnList.SIZE.y-SQUARESIZE));
	}
    }
    
    /////Overrides
    public Color getBaseColor(){
	return enabled ? COLOR_ENABLED : COLOR_DISABLED;
    }
    
    public boolean shouldBeDepressed(){
	return enabled && super.shouldBeDepressed();
    }
    
    /////Other
    void use(){
	if(up)
	    SpawnList.decrementBarPosition();
	else
	    SpawnList.incrementBarPosition();
    }
    
    /////Update
    static final int SCROLLDELAY = 24;
    static final int SCROLLCYCLE = SpawnList.SCROLLSPEED;
    int scroll = -1;
    public void update(){
	super.update();
	if(SpawnMap.currentMap.spawns.isEmpty())
	    enabled = false;
	else if(up)
	    enabled = SpawnList.instance.barPosition != 0;
	else
	    enabled = SpawnList.instance.barPosition < SpawnList.instance.getMaxBarPosition();
	if(enabled){
	    if(checkMouseOver()){
		if(Center.leftMouseDown()){
		    if(Center.leftMouseClicked()){
			scroll = SCROLLDELAY;
			use();
		    }
		    if(scroll-- == 0){
			scroll = SCROLLCYCLE;
			use();
		    }
		}
	    }else if(scroll != -1)
		scroll = -1;
	}
    }
    
    /////Draw
    public void draw(){
		if(!enabled) return;
		super.draw();
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.rectangle(getCenter(),size);
		GraphicsDraw.setColor(enabled ? TEXTCOLOR_ENABLED : TEXTCOLOR_DISABLED);
		GraphicsDraw.defaultFont();
		GraphicsDraw.centerText(up ? "/\\" : "\\/",getCenter());
    }
}

class SpawnList_Scrollbar extends GuiObject{
    public SpawnList_Scrollbar(){
	setSize(new Vector2(SpawnList.ARROWSQUARESIZE,SpawnList.SIZE.y-SpawnList.ARROWSQUARESIZE*2));
	setPosition(new Vector2(SpawnList.OFFSET.x-SpawnList.ARROWSQUARESIZE,SpawnList.OFFSET.y+SpawnList.ARROWSQUARESIZE));
    }
    
    /////Properties
    static final Color COLOR_ENABLED = new Color(.9f,.9f,.9f);
    static final Color COLOR_DISABLED = new Color(.5f,.5f,.5f);
    static final Color COLOR_SCROLLLINE = new Color(.25f,0,.5f);
    public Color getBaseColor(){
	return enabled ? COLOR_ENABLED : COLOR_DISABLED;
    }
    
    /////Conversions
    float barPositionToScreenYPosition(int barposition){
    	return position.y+size.y*barposition/(SpawnMap.currentMap.spawns.size()-SpawnList.MAXBARS);
    }
    int screenYPositionToBarPosition(float screenposition){
    	return (int)((screenposition-position.y)/size.y*(SpawnMap.currentMap.spawns.size()-GuiList.MAXBARS+2));
    }
    
    /////Update
    boolean enabled;
    boolean beingused;
    public void update(){
		super.update();
		enabled = SpawnMap.currentMap.spawns.size() > SpawnList.MAXBARS;
		if(!enabled) return;
		if(Center.leftMouseClicked() && checkMouseOver())
		    beingused = true;
		if(beingused){
		    if(Center.leftMouseDown()){
		    	SpawnList.setBarPosition(screenYPositionToBarPosition(Center.mousePosition().y));
		    }else{
		    	beingused = false;
		    }
		}
    }
    
    /////Draw
    public void draw(){
		GraphicsDraw.setColor(getBaseColor());
		GraphicsDraw.fillRectangle(getCenter(),size);
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.rectangle(getCenter(),size);
		if(enabled){
		    GraphicsDraw.setColor(COLOR_SCROLLLINE);
		    Vector2 pos = new Vector2(position.x+size.x/2,barPositionToScreenYPosition(SpawnList.instance.barPosition));
		    GraphicsDraw.fillRectangle(pos,new Vector2(size.x,5));
		}
    }
}
