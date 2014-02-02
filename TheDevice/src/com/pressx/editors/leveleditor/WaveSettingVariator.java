package com.pressx.editors.leveleditor;

import java.awt.Color;

import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.SingleFormation;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;

public abstract class WaveSettingVariator extends GuiVariator{
	public static final Vector2 SIZE = new Vector2(GuiList.SIZE.x,30);
	public static final Vector2 WSV_BASEOFFSET = new Vector2(GuiList_Wave.GUILIST_WAVE_OFFSET.x,GuiList.EXTRABUTTONOFFSETY);
	
	static final Color BASECOLOR = new Color(1f,1,0);
	
    public int getHoldDelay(){
    	return 10;
    }
    public float getHoldIncrementerIncrease(){
    	return .8f;
    }
	
	boolean checkActive(){
		return GuiList_Level.instance.selectedIndex != -1;
	}
	
	public Color getBaseColor(){
		return checkActive() ? BASECOLOR : Color.GRAY;
	}
	
	public Vector2 getInitialSize(){
		return SIZE;
	}

	public abstract int getMinValue();
	public abstract int getMaxValue();
    
    public void onBelowMinValue(){
    	value = getMinValue();
    }
    public void onAboveMaxValue(){
    	value = getMaxValue();
    }
	
	public abstract String getText();
	
	public void draw(){
		super.draw();
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(getText(),getCenter());
	}
}

class NumFormationsUsedVariator extends WaveSettingVariator{
	public NumFormationsUsedVariator(){
		setPosition(WSV_BASEOFFSET);
	}

	public void setValue(int v){
		super.setValue(v);
		GuiList_Wave.setWaveNumFormationsUsed((byte)v);
	}
	
	public boolean checkActive(){
		return super.checkActive() && GuiList_Wave.checkWaveIsRandomized();
	}
	
	public int getMinValue(){
		return 1; 
	}
	public int getMaxValue(){
		return 25;
	}
	
	public String getText(){
		return "Formations used in wave: "+value;
	}
}

class WaveDelayVariator extends WaveSettingVariator{
	static final float VALUEMULTIPLIER = .5f;
	
	public WaveDelayVariator(){
		setPosition(new Vector2(WSV_BASEOFFSET.x,WSV_BASEOFFSET.y+SIZE.y));
	}
	
	public void setValue(int v){
		super.setValue(v);
		GuiList_Wave.setWaveDelayBetweenFormations(VALUEMULTIPLIER*v);
	}
	
	public int getMinValue(){
		return 0; 
	}
	public int getMaxValue(){
		return 50;
	}
	
	public String getText(){
		return "Delay between formations: "+VALUEMULTIPLIER*value+'s';
	}
}

class FormationAngleVariator extends GuiVariator{
	static final float VALUEMULTIPLIER = .5f;
	
	public FormationAngleVariator(){
		setPosition(new Vector2(WaveSettingVariator.WSV_BASEOFFSET.x,WaveSettingVariator.WSV_BASEOFFSET.y+WaveSettingVariator.SIZE.y*2+5));
	}

	static final Color BASECOLOR = new Color(1f,.5f,0);
	boolean checkActive(){
		return GuiList_Wave.ready && GuiList_Wave.instance.selectedIndex != -1;
	}
	public Color getBaseColor(){
		return checkActive() ? BASECOLOR : Color.GRAY;
	}
	
	public Vector2 getInitialSize(){
		return WaveSettingVariator.SIZE;
	}
	
	public void setValue(int v){
		if(v < 0)
			v = 2;
		else if(v > 2)
			v = 0;
		super.setValue(v);
		if(v == 0)
			GuiList_Wave.instance.setFormationSpawnAngle(SingleFormation.SPAWNANGLE_RANDOMRIGHT);
		else if(v == 1)
			GuiList_Wave.instance.setFormationSpawnAngle(SingleFormation.SPAWNANGLE_RANDOM);
		else
			GuiList_Wave.instance.setFormationSpawnAngle((byte)0);
	}
	
	public int getMinValue(){
		return 0;
	}
	public int getMaxValue(){
		return 2;
	}
	
    public int getHoldDelay(){
    	return 10;
    }
    public float getHoldIncrementThreshold(){
    	return 30;//(byte)value >= SingleFormation.SPAWNANGLE_RANDOM ? 24000 : 30;
    }
    public float getHoldIncrementerIncrease(){
    	return .4f;
    }
    
    SingleFormation currentFormation;
    public void setData(SingleFormation f){
    	currentFormation = f;
    	if(f.spawnAngle == SingleFormation.SPAWNANGLE_RANDOMRIGHT)
    		setValue(0);
    	else if(f.spawnAngle == SingleFormation.SPAWNANGLE_RANDOM)
    		setValue(1);
    	else
    		setValue(2);
    	//setValue(f.spawnAngle);
    }
	
	public String getText(){
		if(currentFormation == null) return "--";
		/*if(value == 0)
			return "Spawn at random right angle";
		if(value == 1)
			return "Spawn at random angle";*/
		return SingleFormation.spawnAngleToString((byte)currentFormation.spawnAngle);
	}
	
	public void update(){
		super.update();
	}

	public void draw(){
		super.draw();
		if(!checkActive()) return;
		float radius = size.y;
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(getText(),getCenter());
		float angle = -SingleFormation.spawnAngleToRadians_animation(currentFormation.spawnAngle);//Negative because LibGDX would make angles go counterclockwise, but this system goes clockwise
		GraphicsDraw.setColor(value == 2 ? Color.WHITE : Color.GRAY);
		Vector2 center = new Vector2(position.x+size.x+radius+5,position.y+size.y/2);
		GraphicsDraw.fillCircle(center,radius);//GraphicsDraw.circle(center,size.y/20);
		GraphicsDraw.setColor(value == 2 ? Color.GRAY : Color.BLACK);
		//GraphicsDraw.arrow(center.add(Vector2.fromAngle(angle,size.y/3)),size.y/5,angle-_G.PI/2);
		GraphicsDraw.line(center,center.add(Vector2.fromAngle(angle,radius)));
		GraphicsDraw.setColor(new Color(.5f,1f,1f));
		GraphicsDraw.circle(center,radius/2);//GraphicsDraw.circle(center,size.y/20);
		GraphicsDraw.setColor(Color.BLUE);
		GraphicsDraw.circle(center,1);
		GraphicsDraw.circle(center.add(Vector2.fromAngle(angle,radius)),1);
		GraphicsDraw.circle(center,radius);//GraphicsDraw.circle(center,size.y/20);

		Vector2 mousepos = Center.mousePosition();
		if(Vector2.distanceLessThan(mousepos,center,radius)){
			Vector2 diff = center.sub(mousepos);
			float mouseangle = Vector2.toAngle(new Vector2(-diff.x,diff.y));
			GraphicsDraw.setColor(Color.BLACK);
			GraphicsDraw.smallBoldFont();
			GraphicsDraw.text(""+(15*(int)(mouseangle/3.14159265f*180/15)),mousepos);
			if(Center.leftMouseDown()){
	    		setValue(2);
	    		GuiList_Wave.instance.setFormationSpawnAngle(SingleFormation.radiansToSpawnAngle(mouseangle));
			}
		}
	}
}
