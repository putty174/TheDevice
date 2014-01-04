package com.pressx.editors.leveleditor;

import java.awt.Color;

import com.pressx.editors.shared.GraphicsDraw;
import com.pressx.editors.shared.SingleFormation;
import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;

public abstract class WaveSettingVariator extends GuiVariator{
	public static final Vector2 SIZE = new Vector2(GuiList.SIZE.x,30);
	public static final Vector2 WSV_BASEOFFSET = new Vector2(GuiList_Wave.GUILIST_WAVE_OFFSET.x,GuiList.BASEOFFSET.y+GuiList.SIZE.y);
	
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
		super.setValue(v);
		GuiList_Wave.instance.setFormationSpawnAngle((byte)v);
	}
	
	public int getMinValue(){
		return 0;
	}
	public int getMaxValue(){
		return SingleFormation.SPAWNANGLE_MAX;
	}
	
    public int getHoldDelay(){
    	return 10;
    }
    public float getHoldIncrementThreshold(){
    	return (byte)value >= SingleFormation.SPAWNANGLE_RANDOM ? 24000 : 30;
    }
    public float getHoldIncrementerIncrease(){
    	return .4f;
    }
    
    public void setData(SingleFormation f){
    	setValue(f.spawnAngle);
    }
	
	public String getText(){
		return SingleFormation.spawnAngleToString((byte)value);
	}

	public void draw(){
		super.draw();
		if(!checkActive()) return;
		GraphicsDraw.boldFont();
		GraphicsDraw.centerText(getText(),getCenter());
		float angle;
		byte val = (byte)value;
		if(val == SingleFormation.SPAWNANGLE_RANDOM)
			angle = (float)_G.cycle/5;
		else if(val == SingleFormation.SPAWNANGLE_RANDOMRIGHT)
			angle = (_G.cycle/8)*_G.PI/2;
		else
			angle = SingleFormation.spawnAngleToRadians(val,null);
		angle = -angle;//Negative because LibGDX would make angles go counterclockwise, but this system goes clockwise
		Vector2 center = new Vector2(position.x+size.x+size.y*2/3,position.y+size.y/2);
		GraphicsDraw.circle(center,size.y/20);
		GraphicsDraw.arrow(center.add(Vector2.fromAngle(angle,size.y/3)),size.y/5,angle-_G.PI/2);
	}
}
