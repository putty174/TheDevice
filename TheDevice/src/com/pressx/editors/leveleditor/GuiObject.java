package com.pressx.editors.leveleditor;

import com.pressx.editors.shared.Vector2;

public abstract class GuiObject{
	public Vector2 position = Vector2.zero();
    public Vector2 center;
    public Vector2 getPosition(){return position;}
    public void setPosition(Vector2 pos){position = pos;calculateCenter();}
    public Vector2 size = Vector2.zero();
    public Vector2 getSize(){return size;}
    public void setSize(Vector2 siz){size = siz;calculateCenter();}
    
    /////Register/Remove
    public final void register_first(){
	Gui.registerGuiObject_First(this);
    }
    public void register(){
	Gui.registerGuiObject(this);
    }
    public void remove(){
	Gui.removeGuiObject(this);
    }
    
    /////Mouse
    public boolean checkMouseOver(){
	Vector2 pos = Center.mousePosition();
	return pos.x > position.x && pos.y > position.y && pos.x < position.x+size.x && pos.y < position.y+size.y;
    }
    public boolean checkMouseClicked(){
	return checkMouseOver() && Center.leftMouseClicked();
    }
    public boolean checkMouseHeld(){
	return checkMouseOver() && Center.leftMouseDown();
    }
    
    /////Extra
    public void calculateCenter(){
	this.center = position.add(size.div(2));
    }
    public Vector2 getCenter(){
	return this.center;
    }
    
    /////Update/Draw
    public void update(){
    }
    
    public abstract void draw();
}
