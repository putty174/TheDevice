package com.pressx.objects.device;

import java.util.ArrayList;

import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;

public class XP extends AnimatedObject {
	
	GameObject noTouch;
	float currentRot;
	
	public XP(Draw d, Sounds s, Textures t, float posX, float posY, float speed, float direction)
	{
		super(d,s,t,
				"XP",
				2, //ID
				posX, posY, //Position
				1, //Mass
				100, //Friction
				3, 3, //Hit Height and Width
				0, 0, //Hit x and y offset
				false, //Solid or not
				0, //Touch Radius
				true, //Touchable
				4, 4, //Draw width and height
				t.getArtAsset("exp"), //Spritesheet
				128, 128 //srcwidth and height
				);
		
		double dir = direction * Math.PI / 180;
		float xComp = (float)(speed * Math.cos(dir));
		float yComp = (float)(speed * Math.sin(dir));
		this.set_velocity(xComp, yComp);
		this.worth = 5;
		
		this.screenBound = true;
		
		this.animationManager = Textures.getAnimManager("ExpOrb").copy();
		this.animationManager.setStdCondition("Bounce");
		this.animationManager.changeAnimation("Bounce", 30, true);
		
		
		//this.animator = null;
//		this.add_animation("xp_float", 0, 0, 3, 6, true);
//		this.set_animation("xp_float", true);
	}//END XP
	
	public XP(Draw draw, Sounds sounds, Textures tex, GameObject noTouch){
		this(draw, sounds, tex, noTouch.get_positionX(), noTouch.get_positionY(), 30, (float)Math.random());
		this.noTouch = noTouch;
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		this.set_position(this.get_positionX(), this.get_positionY() + (float) (0.0625 * Math.sin(currentRot * 0.125)));
		currentRot = currentRot % 360 + 1;
		super.update(dt, objects);
	}
	
	public GameObject cannotTouch(){
		return noTouch;
	}
}