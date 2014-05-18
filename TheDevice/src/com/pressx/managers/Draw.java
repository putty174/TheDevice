package com.pressx.managers;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class TextSetting{
	public TextSetting(String _text,float _fontscale){text = _text;fontscale = _fontscale;}
	public String text;
	public float fontscale;
}

public class Draw {
	public int screenWidth = Gdx.graphics.getWidth();
	public int screenHeight = Gdx.graphics.getHeight();
	
	private Sprite background = null;
	private TreeMap<Float, Sprite> actors = new TreeMap<Float, Sprite>();
	private TreeMap<Float, Sprite> hpbars = new TreeMap<Float, Sprite>();
	private Sprite ui = null;
	private HashSet<Sprite> buttons = new HashSet<Sprite>();
	private HashSet<Sprite> extras0 = new HashSet<Sprite>();
	private HashSet<Sprite> extras1 = new HashSet<Sprite>();
	private HashSet<Sprite> extras2 = new HashSet<Sprite>();
	private TreeMap<Point, TextSetting> text = new TreeMap<Point, TextSetting>();
	
	public enum TYPES {BACKGROUND, ACTOR, HPBAR, UI, BUTTON, EXTRAS, SUPEREXTRAS, MEGAEXTRAS};
	
	public final BitmapFont font = new BitmapFont(Gdx.files.internal("data/fonts/lilita/lilita.fnt"/*bearz/bearz.fnt"*/),Gdx.files.internal("data/fonts/lilita/lilita.png"/*bearz/bearz.png"*/), false);
	
	public Draw()
	{
		font.setScale((float)screenHeight / 500, (float)screenHeight / 500);
//		font.setScale((float)screenHeight / 1400, (float)screenHeight / 1400);
	}
	
	void addextra(HashSet<Sprite> extras, Sprite sprite, float xPos, float yPos, float width, float height){
		extras.remove(sprite);
		sprite.setOrigin(0, 0);
		sprite.setBounds(xPos*screenWidth, yPos*screenHeight, width*screenWidth, height*screenHeight);
		extras.add(sprite);
	}
	/** Draws sprites to screen. Sprite draw order is automatically done correctly. Valid TYPES are: { BACKGROUND, ACTOR, HPBAR, UI, BUTTON, EXTRAS }
	 * @param TYPE the Type of the asset, background, actor, hpbar, ui, button, extras
	 * @param sprite the sprite to be drawn
	 * @param xPos the X Position to draw the sprite, relative to the screen width
	 * @param yPos the Y Position to draw the sprite, relative to the screen height
	 * @param width the width of the sprite, relative to the screen width
	 * @param height the height of the sprite, relative to the screen height
	 * */
	public void draw(TYPES t, Sprite sprite, float xPos, float yPos, float width, float height)
	{
		switch(t)
		{
			case BACKGROUND:
				sprite.setOrigin(0,0);
				sprite.setBounds(xPos*screenWidth, yPos*screenHeight, width*screenWidth, height*screenHeight);
				background = sprite;
				break;
			
			case ACTOR:
				actors.put(-sprite.getBoundingRectangle().y, sprite);
				break;
				
			case HPBAR:
				hpbars.put(-sprite.getBoundingRectangle().y, sprite);
				break;
				
			case UI:
				sprite.setOrigin(0, 0);
				sprite.setBounds(xPos*screenWidth, yPos*screenHeight, width*screenWidth, height*screenHeight);
				ui = sprite;
				break;
				
			case BUTTON:
				sprite.setOrigin(0, 0);
				sprite.setBounds(xPos*screenWidth, yPos*screenHeight, width*screenWidth, height*screenHeight);
				buttons.add(sprite);
				break;
				
			case EXTRAS:
				addextra(extras0,sprite,xPos,yPos,width,height);
				break;				
			case SUPEREXTRAS:
				addextra(extras1,sprite,xPos,yPos,width,height);
				break;				
			case MEGAEXTRAS:
				addextra(extras2,sprite,xPos,yPos,width,height);
				break;
				
			default:
				break;
		}
	}
	
	/** Write text to screen**/
	public void write(String string,float xPos,float yPos){
		write(string,xPos,yPos,1f/500);
	}
	public void write(String string, float xPos, float yPos,float fontscale)//fontscale is based on the size of the screen
	{
		text.put(new Point(xPos*screenWidth, yPos*screenHeight), new TextSetting(string,fontscale));
	}
	public float getPositionToCenterText(String string,float xcenter,float fontscale){
		return xcenter-font.getBounds(string).width/screenWidth*500*fontscale/2;
	}
	public float getFontScaleToFitText(String string,float desiredwidth){
		return desiredwidth/(font.getBounds(string).width/screenWidth*500);
	}
	public float getFontScaleToFitText(String string,float desiredwidth,float maxscale){
		float scale = getFontScaleToFitText(string,desiredwidth);
		return scale > maxscale ? maxscale : scale;
	}
	
	public void draw(SpriteBatch s)
	{
		if(background != null)
			background.draw(s);
		for(Sprite sprite : actors.values())
			sprite.draw(s);
		for(Sprite sprite : hpbars.values())
			sprite.draw(s);
		if(ui != null)
			ui.draw(s);
		for(Sprite sprite : buttons)
			sprite.draw(s);
		for(Sprite sprite : extras0)
			sprite.draw(s);
		if(!text.isEmpty()){
			//float screenx = (float)screenWidth;
			float screeny = (float)screenHeight;
			float oldscale = -1;
			for(Entry<Point, TextSetting> entry : text.entrySet()){
				float scale = entry.getValue().fontscale;
				if(scale != oldscale){
					font.setScale(screeny*scale, screeny*scale);
					oldscale = scale;
				}
				font.draw(s, entry.getValue().text, entry.getKey().x, entry.getKey().y);
			}
			font.setScale(screeny/500, screeny/500);
		}
		text.clear();
		for(Sprite sprite : extras1)
			sprite.draw(s);
		for(Sprite sprite : extras2)
			sprite.draw(s);
	}
	
	/*public void draw()
	{
		SpriteBatch s = new SpriteBatch();
		s.begin();
		if(background != null)
			background.draw(s);
		for(Sprite sprite : actors.values())
			sprite.draw(s);
		for(Sprite sprite : hpbars.values())
			sprite.draw(s);
		if(ui != null)
			ui.draw(s);
		for(Sprite sprite : buttons)
			sprite.draw(s);
		for(Sprite sprite : extras0)
			sprite.draw(s);
		for(Sprite sprite : extras1)
			sprite.draw(s);
		for(Sprite sprite : extras2)
			sprite.draw(s);
		for(Entry<Point, String> entry : text.entrySet())
			font.draw(s, entry.getValue(), entry.getKey().x, entry.getKey().y);
		s.end();
	}*/
	
	public void clearAll()
	{
		background = null;
		actors.clear();
		hpbars.clear();
		ui = null;
		buttons.clear();
		extras0.clear();
		extras1.clear();
		extras2.clear();
		text.clear();
	}

	public void clear()
	{
		actors.clear();
		hpbars.clear();
		ui = null;
		buttons.clear();
		extras0.clear();
		extras1.clear();
		extras2.clear();
		text.clear();
	}
}