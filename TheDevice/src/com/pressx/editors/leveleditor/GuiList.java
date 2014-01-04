package com.pressx.editors.leveleditor;

import java.awt.Color;
import java.util.ArrayList;

import com.pressx.editors.leveleditor.Center;
import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;

public abstract class GuiList<T> extends GuiObject{
    public static final Vector2 BASEOFFSET = new Vector2(20,20);
    public static final Vector2 SIZE = new Vector2(200,400);
    public static final Vector2 ADDITIONALOFFSET = new Vector2(SIZE.x+80,0);
    public static final Vector2 BARSIZE = new Vector2(SIZE.x,20);//size of a single bar thing
    public static final float ARROWSQUARESIZE = 20;
    public static final int MAXBARS = 20;
    public static final float EXTRABUTTONOFFSETY = BASEOFFSET.y+SIZE.y+5;
    
    public static final int SCROLLSPEED = 8;
    
    public int barPosition;
    public int selectedIndex = -1;
    
    public GuiList(Vector2 offset){
		setPosition(offset);//new Vector2(offset.x,offset.y-100));
		setSize(SIZE);//new Vector2(SIZE.x,SIZE.y+200));
    }
    
    /////ArrayList
    public abstract ArrayList<T> getValues();
    
    /////Register
    public void register(){
		super.register();
		new GuiList_Arrow<T>(this,true).register();
		new GuiList_Arrow<T>(this,false).register();
		new GuiList_Scrollbar<T>(this).register();
    }
    
    /////Click
    int screenPositionYToIndex(float y){
		int b = (int)((y-position.y)/SIZE.y*MAXBARS)+barPosition;
		return b;
    }
    
    /////Selection
    T getValueAtIndex(int index){
		if(index < getValues().size() && index >= 0)
		    return getValues().get(index);
		return null;
    }
    
    public void selectValue(T value){
    	selectIndex(getValues().indexOf(value));
    }
    public void selectIndex(int index){
    	if(index < 0 || index >= getValues().size())
    		return;
		if(getValues().size() == 0){
			deselect();
			return;
		}
    	selectedIndex = index;
    	focusOnBar(selectedIndex);
    }
    
    public void deselect(){
    	selectedIndex = -1;
    }
    
    /////Bars
    static final int BARSCROLLACCELERATIONTHRESHOLD = 10;
    int barscrollacceleration;
    int lastbarscrollaccelerationcycle;
    public void setBarPosition(int pos){
		if(pos < getMinBarPosition())
		    pos = getMinBarPosition();
		else if(pos > getMaxBarPosition())
		    pos = getMaxBarPosition();
		barPosition = pos;
    }
    public void changeBarPosition(int val){
		if(lastbarscrollaccelerationcycle < _G.cycle-10){
		    barscrollacceleration = 0;
		}
		setBarPosition(barPosition+val*(1+barscrollacceleration/BARSCROLLACCELERATIONTHRESHOLD));
		lastbarscrollaccelerationcycle = _G.cycle;
		barscrollacceleration++;
    }
    public void incrementBarPosition(){
    	changeBarPosition(1);
    }
    public void decrementBarPosition(){
    	changeBarPosition(-1);
    }
    
    public void useEndBarPosition(){
    	changeBarPosition(getMaxBarPosition());
    }
    
    public int getMaxBarPosition(){
		int s = getValues().size();
		return s < MAXBARS ? 0 : s-MAXBARS;
    }
    public int getMinBarPosition(){
    	return 0;
    }
    
    public void focusOnBar(int index){//Makes the list focus on a certain position if it is not already in view
    	setBarPosition(index < barPosition ? index : index >= barPosition+MAXBARS ? index-MAXBARS+1 : barPosition);
    }
    
    /////Update
    protected void onBarClicked(int index){
	    selectIndex(index);
    }
    
    void update_onmouseover(){
		int rot = Center.getWheelRotation();
		if(rot != 0)
		    setBarPosition(barPosition+rot);
		if(Center.leftMouseClicked()){
		    onBarClicked(screenPositionYToIndex(Center.mousePosition().y));
		}
    }
    
    public void update(){
		super.update();
	
		if(checkMouseOver()){
		    update_onmouseover();
		}
    }
    
    /////Draw
    public abstract String getTextFromValue(T val);
    public abstract String getTitle();
    
    static final int NEWLOCATIONANIMATIONCYCLE = 30;
    protected Color getSelectedUnitDrawColor(int realindex,int relativeindex,T val){
		float a = ((float)Math.sin((float)_G.cycle/4)+1)/8;
		return new Color(1,.6f+a,a);
    }
    protected Color getUnitDrawColor(int realindex,int relativeindex,T val){
		if(realindex == selectedIndex)
			return getSelectedUnitDrawColor(realindex,relativeindex,val);
		return Color.WHITE;
    }
    protected void drawSingleUnit(int realindex,int relativeindex,T val){
		Vector2 pos = new Vector2(getCenter().x-SIZE.x/2+BARSIZE.x/2,getCenter().y-SIZE.y/2+BARSIZE.y*(.5f+relativeindex));
		
		//Draw a bar every five realindex positions
		if(realindex%5 == 4){
		    GraphicsDraw.setColor(Color.BLACK);
		    GraphicsDraw.fillRectangle(new Vector2(pos.x,pos.y+BARSIZE.y/2),new Vector2(BARSIZE.x,4));
		    /*
		    GraphicsDraw.fillRectangle(new Vector2(pos.x+BARSIZE.x/2,pos.y),new Vector2(3,BARSIZE.y));
		    Vector2 p2 = new Vector2(pos.x+BARSIZE.x/2+5,pos.y);
		    GraphicsDraw.fillRectangle(p2,new Vector2(10,3));*/
		}
		
		//Choose the color
		GraphicsDraw.setColor(getUnitDrawColor(realindex,relativeindex,val));
		
		/////Draw bar
		Vector2 siz = new Vector2(BARSIZE.x-4,BARSIZE.y-4);
		GraphicsDraw.fillRectangle(pos,siz);
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.rectangle(pos,siz);
		
		/////Draw text
		GraphicsDraw.centerText(getTextFromValue(val),pos);
    }
    
    public void draw(){
		GraphicsDraw.setColor(new Color(.7f,.7f,.7f));
		GraphicsDraw.fillRectangle(getCenter(),SIZE);
		GraphicsDraw.defaultFont();
		for(int i = barPosition; i < barPosition+MAXBARS && i < getValues().size(); i++){
		    drawSingleUnit(i,i-barPosition,getValues().get(i));
		}
		
		//draw border
		GraphicsDraw.setColor(Color.BLACK);
		GraphicsDraw.rectangle(getCenter(),SIZE);
		//draw title
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(getTitle(),new Vector2(position.x+SIZE.x/2,getCenter().y-SIZE.y/2-10));
    }
}

class GuiList_Arrow<T> extends GuiButton{
    static final float SQUARESIZE = GuiList.ARROWSQUARESIZE;
    static final Vector2 SIZE = new Vector2(SQUARESIZE,SQUARESIZE);
    
    static final Color COLOR_ENABLED = new Color(.9f,.9f,.9f);
    static final Color COLOR_DISABLED = new Color(.5f,.5f,.5f);
    static final Color TEXTCOLOR_ENABLED = Color.BLACK;
    static final Color TEXTCOLOR_DISABLED = new Color(.4f,.4f,.4f);
    
    boolean up;
    boolean enabled;
    
    GuiList<T> parent;
    
    public GuiList_Arrow(GuiList<T> parent,boolean up){
    	this.parent = parent;
		this.up = up;
		setSize(SIZE);
		if(up){
		    setPosition(new Vector2(parent.position.x-SQUARESIZE,parent.position.y));
		}else{
		    setPosition(new Vector2(parent.position.x-SQUARESIZE,parent.position.y+GuiList.SIZE.y-SQUARESIZE));
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
		    parent.decrementBarPosition();
		else
		    parent.incrementBarPosition();
    }
	    
	    /////Update
    static final int SCROLLDELAY = 24;
    static final int SCROLLCYCLE = GuiList.SCROLLSPEED;
    int scroll = -1;
    public void update(){
		super.update();
		if(parent.getValues().isEmpty())
		    enabled = false;
		else if(up)
		    enabled = parent.barPosition != 0;
		else
		    enabled = parent.barPosition < parent.getMaxBarPosition();
		
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

class GuiList_Scrollbar<T> extends GuiObject{
	GuiList<T> parent;
    public GuiList_Scrollbar(GuiList<T> parent){
    	this.parent = parent;
		setSize(new Vector2(GuiList.ARROWSQUARESIZE,GuiList.SIZE.y-GuiList.ARROWSQUARESIZE*2));
		setPosition(new Vector2(parent.position.x-GuiList.ARROWSQUARESIZE,parent.getCenter().y-GuiList.SIZE.y/2+GuiList.ARROWSQUARESIZE));
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
    	return position.y+size.y*(float)barposition/((float)parent.getValues().size()-GuiList.MAXBARS);
    }
    int screenYPositionToBarPosition(float screenposition){
    	return (int)((screenposition-position.y)/size.y*(parent.getValues().size()-GuiList.MAXBARS+2));
    }
    
    /////Update
    boolean enabled;
    boolean beingused;
    public void update(){
		super.update();
		enabled = parent.getValues().size() > GuiList.MAXBARS;
		if(!enabled) return;
		if(Center.leftMouseClicked() && checkMouseOver())
		    beingused = true;
		if(beingused){
		    if(Center.leftMouseDown()){
		    	parent.setBarPosition(screenYPositionToBarPosition(Center.mousePosition().y));
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
		    Vector2 pos = new Vector2(position.x+size.x/2,barPositionToScreenYPosition(parent.barPosition));
		    GraphicsDraw.fillRectangle(pos,new Vector2(size.x,5));
		}
    }
}
