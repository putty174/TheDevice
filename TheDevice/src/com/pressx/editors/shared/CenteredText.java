package com.pressx.editors.shared;

import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public final class CenteredText{
    public String text;
    public Vector2 position;
    public CenteredText(String text,Vector2 center){
	this.text = text;
	setCenter(center);
    }
    
    public void setCenter(Vector2 center){
	FontRenderContext frc = new FontRenderContext(null,true,true);
	TextLayout layout = new TextLayout(text,GraphicsDraw.currentFont,frc);
	Rectangle r = layout.getPixelBounds(null,(int)center.x,(int)center.y);
	position = new Vector2(center.x-r.width/2,center.y+r.height/2);
    }
}
